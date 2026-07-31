package org.map_bd.surveycalculator

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class InvoiceActivity : AppCompatActivity() {

    private val REQUESTCODE = 100
    private lateinit var rootLayout: View // Made global to access inside permission callback

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable edge-to-edge if utilizing the latest androidx libraries
        // enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invoice_activity)

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDateAndTime = sdf.format(Date()).toString()

        val location = findViewById<TextView>(R.id.txt_invoice)
        val date = findViewById<TextView>(R.id.today_Id)
        val title = findViewById<TextView>(R.id.txt_cust_name)
        val mouza = findViewById<TextView>(R.id.txt_date)
        val plot = findViewById<TextView>(R.id.txt_dates)
        val srname = findViewById<TextView>(R.id.txt_datess)
        val upazila = findViewById<TextView>(R.id.txt_datesss)
        val details = findViewById<TextView>(R.id.txt_datessss)
        val ivResultImage = findViewById<ImageView>(R.id.img)
        val pdf = findViewById<Button>(R.id.printId)
        val back = findViewById<Button>(R.id.backId)

        rootLayout = findViewById<View>(R.id.rootLayout)
        date.text = currentDateAndTime

        // Receive and apply text to TextView
        location.text = intent.getStringExtra("LOCATION") ?: " Location"
        title.text = intent.getStringExtra("TITLE") ?: " Title"
        mouza.text = intent.getStringExtra("MOUZA") ?: " Mouza"
        plot.text = intent.getStringExtra("PLOT") ?: " Plot"
        srname.text = intent.getStringExtra("SURVEY") ?: " Survey or Record"
        upazila.text = intent.getStringExtra("UPAZILA") ?: " Upazila"
        details.text = intent.getStringExtra("DETAILS") ?: " Details"

        // Retrieve the String URI from the Intent
        val uriString = intent.getStringExtra("IMAGE_URI_EXTRA")
        if (!uriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(uriString)
            ivResultImage.setImageURI(imageUri)
        }

        pdf.setOnClickListener {
            checkStoragePermissionAndSave()
        }

        back.setOnClickListener {
            val form = Intent(this, FormActivity::class.java)
            startActivity(form)
            finish()
        }
    }

    private fun checkStoragePermissionAndSave() {
        // Android 10 (API 29) & above does NOT require storage permission for MediaStore.Downloads
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            captureAndSavePdf()
        } else {
            // Android 9 and below requires legacy permission check
            if (ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                captureAndSavePdf()
            } else {
                requestAllPermission()
            }
        }
    }

    private fun captureAndSavePdf() {
        val pdfButton = findViewById<Button>(R.id.printId)
        val backButton = findViewById<Button>(R.id.backId)

        // Hide print button before screenshot
        pdfButton.isVisible = false

        // Use post to ensure UI layouts finish hiding the print button
        rootLayout.post {
            val bitmap = createBitmapFromView(rootLayout)
            saveBitmapAsPdf(bitmap)

            // Show buttons back
//            pdfButton.isVisible = true
            backButton.visibility = View.VISIBLE
        }
    }

    private fun createBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveBitmapAsPdf(bitmap: Bitmap) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        val sdf = SimpleDateFormat("dd-MM-yyyy_HH-mm-ss")
        val currentDateAndTime = sdf.format(Date())
        val fileName = "Form_$currentDateAndTime.pdf"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/Survey Calculator/Form")
            }

            val resolver = contentResolver
            val collectionUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

            try {
                val uri = resolver.insert(collectionUri, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri).use { outputStream ->
                        if (outputStream != null) {
                            pdfDocument.writeTo(outputStream)
                            Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show()
                        } else {
                            throw IOException("Failed to open output stream.")
                        }
                    }
                } else {
                    throw IOException("Failed to create MediaStore entry.")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show()
            }
        } else {
            val folder = File(Environment.getExternalStorageDirectory(), "Survey Calculator/Form")
            if (!folder.exists()) {
                folder.mkdirs()
            }

            val file = File(folder, fileName)
            try {
                FileOutputStream(file).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                Toast.makeText(this, "PDF saved to: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show()
            }
        }
        pdfDocument.close()
    }

    private fun requestAllPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE),
            REQUESTCODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUESTCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                captureAndSavePdf() // Proceed to capture and save now that permission is allowed
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}