package org.map_bd.surveycalculator


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import java.io.OutputStream

@Suppress("DEPRECATION")
class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var btnToggleTracking: Button
    private lateinit var btnExport: Button
    private var isTracking = false
    private val polyline = Polyline()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load osmdroid configuration safely
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)
        btnToggleTracking = findViewById(R.id.btnToggleTracking)
        btnExport = findViewById(R.id.btnExport)

        // Initialize Map
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)
        mapView.overlays.add(polyline)


//        after added

        mapView.setMultiTouchControls(true)
        val dm : DisplayMetrics = this.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(mapView)
        scaleBarOverlay.setCentred(true)
//play around with these values to get the location on screen in the right place for your application
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mapView.overlays.add(scaleBarOverlay)
        val compassOverlay = CompassOverlay(this, InternalCompassOrientationProvider(this), mapView)
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)





        btnToggleTracking.setOnClickListener { toggleTracking() }
        btnExport.setOnClickListener { selectExportFileLocation() }

        TrackingService.onLocationUpdated = { points ->
            runOnUiThread {
                updateMapPath(points)
            }
        }

        requestRequiredPermissions()
    }

    private fun toggleTracking() {
        val intent = Intent(this, TrackingService::class.java)
        if (isTracking) {
            stopService(intent)
            btnToggleTracking.text = "Start Tracking"
            isTracking = false
        } else {
            ContextCompat.startForegroundService(this, intent)
            btnToggleTracking.text = "Stop Tracking"
            isTracking = true
        }
    }

    private fun updateMapPath(points: List<GeoPoint>) {
        if (points.isNotEmpty()) {
            polyline.setPoints(points)
            mapView.controller.animateTo(points.last())
            mapView.invalidate()
        }
    }

    // --- GPX EXPORTER (No write permissions needed for SDK 34 via Storage Access Framework) ---
    private val createGpxFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    writeGpxData(outputStream, TrackingService.pathPoints)
                    Toast.makeText(this, "GPX Track Exported!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun selectExportFileLocation() {
        if (TrackingService.pathPoints.isEmpty()) {
            Toast.makeText(this, "No track data available to export.", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/gpx+xml"
            putExtra(Intent.EXTRA_TITLE, "surveycalculator_${System.currentTimeMillis()}.gpx")
        }
        createGpxFileLauncher.launch(intent)
    }

    private fun writeGpxData(outputStream: OutputStream, points: List<GeoPoint>) {
        val header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<gpx version=\"1.1\" creator=\"SurveyCalculator\" xmlns=\"http://topografix.com\">\n" +
                "  <trk>\n    <name>Logged Track</name>\n    <trkseg>\n"
        val footer = "    </trkseg>\n  </trk>\n</gpx>"

        val segments = StringBuilder()
        for (point in points) {
            segments.append("      <trkpt lat=\"${point.latitude}\" lon=\"${point.longitude}\"></trkpt>\n")
        }

        outputStream.write((header + segments.toString() + footer).toByteArray())
    }

    // --- RUNTIME PERMISSIONS (SDK 34 Compliant) ---
    private fun requestRequiredPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        permissionLauncher.launch(permissions.toTypedArray())
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val fineGranted = perms[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        if (!fineGranted) {
            Toast.makeText(this, "Location permission is required for tracking.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}