package org.map_bd.surveycalculator


import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.text.SimpleDateFormat
import java.util.Locale

class CameraActivity : AppCompatActivity() {

    private lateinit var viewFinder: PreviewView
    private lateinit var btnCapture: Button

    private var imageCapture: ImageCapture? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        viewFinder = findViewById(R.id.viewFinder)
        btnCapture = findViewById(R.id.btnCapture)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnCapture.setOnClickListener {
            // Update physical coordinates at the exact split-second of clicking
            fetchOfflineLocation { takePhoto() }
        }

        requestRequiredPermissions()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this, "Camera initialization failed", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun fetchOfflineLocation(onReady: () -> Unit) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            onReady() // Proceed even without coords if permission is missing
            return
        }

        // Target hardware GPS engines directly without reliance on Google Network Location Providers
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                lastKnownLocation = location
                onReady()
            }
            .addOnFailureListener {
                onReady()
            }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Survey Calculator/GeotagCamera")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(baseContext, "Photo capture failed: ${exc.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    output.savedUri?.let { uri ->
                        // Intercept image output stream to inject EXIF properties offline
                        contentResolver.openFileDescriptor(uri, "rw")?.use { pfd ->
                            val fd = pfd.fileDescriptor
                            lastKnownLocation?.let { location ->
                                injectOfflineGeotag(fd, location)
                            }
                        }
                        Toast.makeText(baseContext, "Photo Saved & Geotagged!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    // --- Core EXIF Manipulation Engine ---
    private fun injectOfflineGeotag(fileDescriptor: java.io.FileDescriptor, location: Location) {
        try {
            val exif = ExifInterface(fileDescriptor)

            // Format standard numeric degrees to EXIF Rational Format (D/1,M/1,S/1000)
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, convertToExifRational(location.latitude))
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, if (location.latitude > 0) "N" else "S")

            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, convertToExifRational(location.longitude))
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, if (location.longitude > 0) "E" else "W")

            // Inject Altitude metrics if present
            if (location.hasAltitude()) {
                exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, "${Math.abs(location.altitude)}/1")
                exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, if (location.altitude >= 0) "0" else "1")
            }

            exif.saveAttributes()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun convertToExifRational(coordinate: Double): String {
        val absCoord = Math.abs(coordinate)
        val degrees = absCoord.toInt()
        val minutesNotTruncated = (absCoord - degrees) * 60
        val minutes = minutesNotTruncated.toInt()
        val seconds = ((minutesNotTruncated - minutes) * 60 * 1000).toInt()

        return "$degrees/1,$minutes/1,$seconds/1000"
    }

    // --- Runtime Permissions ---
    private fun requestRequiredPermissions() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        permissionLauncher.launch(permissions)
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val cameraGranted = perms[Manifest.permission.CAMERA] ?: false
        val fineLocationGranted = perms[Manifest.permission.ACCESS_FINE_LOCATION] ?: false

        if (cameraGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission is strictly required.", Toast.LENGTH_LONG).show()
        }
    }
}