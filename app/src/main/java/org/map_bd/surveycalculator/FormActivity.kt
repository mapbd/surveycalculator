package org.map_bd.surveycalculator

import android.Manifest.permission
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log.d
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import org.map_bd.surveycalculator.databinding.ActivityFormBinding
import org.osmdroid.util.GeoPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    private lateinit var imgV: ImageView

//    lateinit var imageUri: Uri
//
//    private val tackPhoto = registerForActivityResult(ActivityResultContracts.TakePicture()){
//        imgV.setImageURI(null)
//        imgV.setImageURI(imageUri)
//    }



    private val REQUESTCODE = 100
    private var pageWidth = 720
    private var pageHeight = 1200
    private var imageBitmap: Bitmap? =  null
    private var scaledImageBitmap: Bitmap? = null






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.header_image)
        scaledImageBitmap = imageBitmap?.let { Bitmap.createScaledBitmap(it, 720, 257, false) }


        imgV = findViewById(R.id.imageId)

        var title =findViewById<EditText>(R.id.titleId)
        var mouza =findViewById<EditText>(R.id.mouzaId)
        var plot =findViewById<EditText>(R.id.plotId)
        var survey =findViewById<EditText>(R.id.surveyId)
        var details =findViewById<EditText>(R.id.detailsId)




        val pickmedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri ->
                if (uri!=null){
                    imgV.setImageURI(uri)
            }
        }

        binding.photobtnId.setOnClickListener {
            pickmedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }




//        imageUri = createImageUri()
//
//
//        binding.capbtnId.setOnClickListener {
//            tackPhoto.launch(imageUri)
//        }








        binding.printId.setOnClickListener {


            var title = title.text.toString()
            var mouza = mouza.text.toString()
            var plot = plot.text.toString()
            var survey = survey.text.toString()
            var dtls = details.text.toString()





            val sdf = SimpleDateFormat("dd-MM-yyyy HH-mm-ss")
            val currentDateAndTime = sdf.format(Date()).toString()



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    createPDF(currentDateAndTime, title, mouza, plot, survey, dtls)
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
                    createPDF(currentDateAndTime,title, mouza, plot, survey, dtls)
                } else {
                    requestAllPermission()
                }
            }
        }










    }

    fun createImageUri(): Uri{

        val image = File(filesDir,"survey_calculator.png")

        return FileProvider.getUriForFile(this,"org.map_bd.surveycalculator.fileprovider",image)
    }










    private fun updateTextView(geoPoint: GeoPoint) {
        val latitude = geoPoint.latitude
        val longitude = geoPoint.longitude
        binding.locationid.text = "$latitude,$longitude"
    }



    private fun createPDF(currentDateAndTime: String, title: String, mouza: String, plot: String, survey: String, dtls: String) {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val pageInfo = PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        canvas.drawBitmap(scaledImageBitmap!!, 0f, 0f, paint)
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 40f
        //paint.color = resources.getColor(android.R.color.holo_blue_bright, null)
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        canvas.drawText("Survey Form" , (pageWidth / 2).toFloat(),  295f, paint)

        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 20f
        canvas.drawText("Client Information",(pageWidth / 2).toFloat(),335f,paint)

        val line = Paint()
        line.strokeWidth = 2f

        canvas.drawLine(150f,310f,550f,310f,line)

        canvas.drawLine(150f,310f,150f,350f,line)
        canvas.drawLine(550f,310f,550f,350f,line)

        // horizontal lines
        canvas.drawLine(150f, 350f, 550f, 350f, line)
        canvas.drawLine(150f, 400f, 550f, 400f, line)
        canvas.drawLine(150f, 450f, 550f, 450f, line)
        canvas.drawLine(150f, 500f, 550f, 500f, line)
        canvas.drawLine(150f, 550f, 550f, 550f, line)


        // vertical lines
        canvas.drawLine(150f, 350f, 150f, 550f, line)
        canvas.drawLine(350f, 350f, 350f, 550f, line)
        canvas.drawLine(550f, 350f, 550f, 550f, line)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.grapeColor,null)
        paint.textSize = 20f
        canvas.drawText("Title: ",180f,380f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.black,null)
        paint.textSize = 20f
        canvas.drawText("$title ",360f,380f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        canvas.drawText("Mouza: ",180f,430f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.black,null)
        paint.textSize = 20f
        canvas.drawText("$mouza ",360f,430f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.grapeColor,null)
        paint.textSize = 20f
        canvas.drawText("Plot: ",180f,480f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.black,null)
        paint.textSize = 20f
        canvas.drawText("$plot ",360f,480f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        canvas.drawText("Survey: ",180f,530f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.black,null)
        paint.textSize = 20f
        canvas.drawText("$survey ",360f,530f,paint)


        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        paint.color = resources.getColor(R.color.hollyGreenColor, null)
        canvas.drawText("Total : ",150f, 565f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 30f
        paint.color = resources.getColor(R.color.hollyGreenColor, null)
        canvas.drawText("Details  : $dtls ",150f, 590f, paint)

//        paint.textAlign = Paint.Align.LEFT
//        paint.textSize = 30f
//        paint.color = resources.getColor(R.color.hollyGreenColor, null)
//        canvas.drawText("Decimal = $dece ",150f, 620f, paint)

//        paint.textAlign = Paint.Align.LEFT
//        paint.textSize = 30f
//        paint.color = resources.getColor(R.color.hollyGreenColor, null)
//        canvas.drawText("Katha = $kata ",150f, 650f, paint)
//
//        paint.textAlign = Paint.Align.LEFT
//        paint.textSize = 30f
//        paint.color = resources.getColor(R.color.hollyGreenColor, null)
//        canvas.drawText("Square Link = $sqrlin ",150f, 680f, paint)


        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 15f
        paint.color = resources.getColor(R.color.brickRedColor, null)
        canvas.drawText("Note: This is automatically generated from Survey Calculator.",(pageWidth / 2).toFloat(), 1000f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 10f
        paint.color = resources.getColor(R.color.black, null)
        canvas.drawText("Pdf Generating Time: $currentDateAndTime",40f,1150f,paint)

        pdfDocument.finishPage(page)


        val folder = File(Environment.getExternalStorageDirectory(), "Survey Calculator/Form")
        if (folder.exists()) {
            d("folder", "exists")
        } else {
            d("folder", "not exists")
            folder.mkdirs()
        }


        val file = File(folder,
            "/Survey_form_$currentDateAndTime.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "PDF saved to " + file.absolutePath, Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }


    private fun requestAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this@FormActivity,
                arrayOf<String>(permission.READ_MEDIA_IMAGES),
                REQUESTCODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this@FormActivity, arrayOf(
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
                Toast.makeText(this@FormActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }





}