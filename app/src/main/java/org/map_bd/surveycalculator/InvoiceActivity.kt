package org.map_bd.surveycalculator

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


@Suppress("DEPRECATION")
class InvoiceActivity : AppCompatActivity() {

    private val REQUESTCODE = 100




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Enable full-screen drawing
        enableEdgeToEdge()
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

        val pdf  = findViewById<Button>(R.id.printId)

        val back  = findViewById<Button>(R.id.backId)

        val rootLayout = findViewById<View>(R.id.rootLayout)


        date.text = currentDateAndTime


        // 1. Receive and apply text to TextView
        val locations = intent.getStringExtra("LOCATION")
        location.text = locations ?: " Location"

        val titles = intent.getStringExtra("TITLE")
        title.text =  titles ?: " Title"

        val mouzas = intent.getStringExtra("MOUZA")
        mouza.text = mouzas ?: " Mouza"

        val plots = intent.getStringExtra("PLOT")
        plot.text = plots ?: " Plot"

        val srnames = intent.getStringExtra("SURVEY")
        srname.text = srnames ?: " Survey or Record"

        val upazilas = intent.getStringExtra("UPAZILA")
        upazila.text = upazilas ?: " Upazila"

        val detailss = intent.getStringExtra("DETAILS")
        details.text = detailss ?: " Details"

        // Retrieve the String URI from the Intent
        val uriString = intent.getStringExtra("IMAGE_URI_EXTRA")

        if (!uriString.isNullOrEmpty()) {
            // Convert back to Uri object and set to target ImageView
            val imageUri = Uri.parse(uriString)
            ivResultImage.setImageURI(imageUri)
        }

        pdf.setOnClickListener {

            it.isVisible = false
            val bitmap = createBitmapFromView(rootLayout)
//            saveBitmapAsPdf(bitmap)



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    saveBitmapAsPdf(bitmap)
                } else {
                    requestAllPermission()
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        applicationContext,
                        permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    saveBitmapAsPdf(bitmap)
                } else {
                    requestAllPermission()
                }
            }

            back.visibility = View.VISIBLE


        }

        back.setOnClickListener {
            val form = Intent(this,FormActivity::class.java);
            startActivity(form)
            finish()
        }

    }




    // Convert the XML layout view into a Bitmap image
    private fun createBitmapFromView(view: View): Bitmap {
        // Define the bounds of the view
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }



    private fun saveBitmapAsPdf(bitmap: Bitmap) {
        val pdfDocument = PdfDocument()

        // Create page info matching the bitmap dimensions
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        // Draw the bitmap onto the PDF page canvas
        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        val sdf = SimpleDateFormat("dd-MM-yyyy HH-mm-ss")
        val currentDateAndTime = sdf.format(Date())
        val fileName = "Form_$currentDateAndTime.pdf"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 (API 29) and above: MediaStore Implementation
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                // Saves to /Documents/Survey Calculator/Form
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOCUMENTS}/Survey Calculator/Form")
            }

            val resolver = contentResolver
            // Use MediaStore.Downloads.EXTERNAL_CONTENT_URI or MediaStore.Files.getContentUri("external")
            val collectionUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

            try {
                val uri = resolver.insert(collectionUri, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri).use { outputStream ->
                        if (outputStream != null) {
                            pdfDocument.writeTo(outputStream)
                            Toast.makeText(this, "PDF saved successfully via MediaStore", Toast.LENGTH_SHORT).show()
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
            // Android 9 and below: Legacy File API Implementation
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this@InvoiceActivity,
                arrayOf<String>(permission.READ_MEDIA_IMAGES),
                REQUESTCODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this@InvoiceActivity, arrayOf(
                    permission.READ_EXTERNAL_STORAGE,
                    permission.WRITE_EXTERNAL_STORAGE
                ), REQUESTCODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUESTCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@InvoiceActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}