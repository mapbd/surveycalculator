package org.map_bd.surveycalculator


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.databinding.ActivityPaintBinding
import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.map_bd.surveycalculator.databinding.DialogBrushSizeBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Suppress("DEPRECATION")
class PaintActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPaintBinding
    private var mImageButtonCurrentPaint:ImageButton?=null
    var customProgressDialog: Dialog? = null

    // Launcher for picking images from gallery
    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                binding.ivBackground.setImageURI(result.data?.data)
            }
        }

    // Launcher for checking and requesting runtime permissions
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isGranted = false
            permissions.entries.forEach { entry ->
                if (entry.value) {
                    isGranted = true
                }
            }

            if (isGranted) {
                Toast.makeText(this, "Permission granted for storage", Toast.LENGTH_LONG).show()
                openGalleryIntent()
            } else {
                Toast.makeText(this, "Permission denied for storage", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityPaintBinding.inflate(layoutInflater)

        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val paintColors = binding.paintColor
        mImageButtonCurrentPaint = paintColors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pallet_pressed))

        binding.drawingView.setSizeForBrush(20f)

        binding.brush.setOnClickListener {
            showBrushSizeChooserDialog()
        }


        binding.ibGallery.setOnClickListener {
            handleGalleryPermission()
        }


        binding.ibUndo.setOnClickListener {
            binding.drawingView.onClickUndo()
        }

        binding.ibSave.setOnClickListener {
            // MediaStore doesn't require run-time permissions to save images on API 29+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || isStoragePermissionGranted()) {
                showProgressDialog()
                lifecycleScope.launch {
                    saveBitmapFile(getBitmapFromView(binding.flDrawingViewContainer))
                }
            } else {
                // Request legacy permission for API 28 and down
                requestLegacyWritePermission()
            }
        }
    }



    private fun openGalleryIntent() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        openGalleryLauncher.launch(pickIntent)
    }

    private fun getStoragePermissionString(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, getStoragePermissionString()
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun handleGalleryPermission() {
        val permission = getStoragePermissionString()
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showRationaleDialog(
                "Storage Permission Needed",
                "This app needs access to your gallery to load background images."
            ) {
                requestPermissionLauncher.launch(arrayOf(permission))
            }
        } else {
            requestPermissionLauncher.launch(arrayOf(permission))
        }
    }

    private fun requestLegacyWritePermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showRationaleDialog(
                "Storage Permission Needed",
                "This app needs storage permission to save your drawings."
            ) {
                requestPermissionLauncher.launch(arrayOf(permission))
            }
        } else {
            requestPermissionLauncher.launch(arrayOf(permission))
        }
    }

    private fun showRationaleDialog(title: String, message: String, onPositiveClick: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Grant") { dialog, _ ->
                onPositiveClick()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showBrushSizeChooserDialog() {
        val brushbinding = DialogBrushSizeBinding.inflate(layoutInflater)
        val brushDialog = Dialog(this)
        brushDialog.setContentView(brushbinding.root)
        brushDialog.setTitle("Brush Size:")

        brushbinding.ibSmallBrush.setOnClickListener {
            binding.drawingView.setSizeForBrush(10f)
            brushDialog.dismiss()
        }
        brushbinding.ibMediumBrush.setOnClickListener {
            binding.drawingView.setSizeForBrush(20f)
            brushDialog.dismiss()
        }
        brushbinding.ibLargeBrush.setOnClickListener {
            binding.drawingView.setSizeForBrush(30f)
            brushDialog.dismiss()
        }
        brushDialog.show()
    }

    fun paintClicked(view: View) {
        if (view !== mImageButtonCurrentPaint) {
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            binding.drawingView.setColor(colorTag)
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet_pressed))
            mImageButtonCurrentPaint!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet_normal))
            mImageButtonCurrentPaint = view
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }



    private suspend fun saveBitmapFile(mBitmap: Bitmap?): String {
        var result = ""
        withContext(Dispatchers.IO) {
            if (mBitmap != null) {
                try {
                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

                    val sdf = SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault())
                    val currentDateAndTime = sdf.format(Date())
                    val fileName = "surveycalculator_$currentDateAndTime.png"

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val contentValues = ContentValues().apply {
                            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Survey Calculator/Paint")
                            put(MediaStore.Images.Media.IS_PENDING, 1)
                        }

                        val resolver = contentResolver
                        val collectionUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        val imageUri = resolver.insert(collectionUri, contentValues)

                        if (imageUri != null) {
                            resolver.openOutputStream(imageUri).use { outputStream ->
                                outputStream?.write(bytes.toByteArray())
                            }
                            contentValues.clear()
                            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                            resolver.update(imageUri, contentValues, null, null)
                            result = imageUri.toString()
                        }
                    } else {
                        val folder = File(Environment.getExternalStorageDirectory(), "Survey Calculator/Paint")
                        if (!folder.exists()) {
                            folder.mkdirs()
                        }
                        val file = File(folder, fileName)
                        FileOutputStream(file).use { outputStream ->
                            outputStream.write(bytes.toByteArray())
                        }
                        result = file.absolutePath
                    }

                    withContext(Dispatchers.Main) {
                        cancelProgressDialog()
                        if (result.isNotEmpty()) {
                            Toast.makeText(this@PaintActivity, "File saved successfully", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@PaintActivity, "Something went wrong saving the file", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    result = ""
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        cancelProgressDialog()
                    }
                }
            }
        }
        return result
    }

    private fun showProgressDialog() {
        customProgressDialog = Dialog(this@PaintActivity)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        customProgressDialog?.setContentView(R.layout.dialog_custom_progress)

        //Start the dialog and display it on screen.
        customProgressDialog?.show()
    }
    private fun cancelProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }




}