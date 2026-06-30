package org.map_bd.surveycalculator

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log.d
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.font.FontVariation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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

    private lateinit var locationid: TextView



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



    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)







        var title =findViewById<EditText>(R.id.titleId)
        var mouza =findViewById<EditText>(R.id.mouzaId)
        var plot =findViewById<EditText>(R.id.plotId)
        var survey =findViewById<EditText>(R.id.surveyId)
        var upazila =findViewById<EditText>(R.id.upazilaId)
        var details =findViewById<EditText>(R.id.detailsId)
        var printId =findViewById<Button>(R.id.printId)


        locationid = findViewById(R.id.locationid)




//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        val locationListener = object : LocationListener {
//            override fun onLocationChanged(location: Location) {
//                val lat = location.latitude
//                val lng = location.longitude
//                // Convert coordinates to String
//                val locationText = "Lat: $lat, Long: $lng"
//                println(locationText) // Or update your TextView
//            }
//        }
//
//        // Request updates specifically from GPS_PROVIDER
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER, // Forces offline, raw GPS
//                5000L, // 5 seconds
//                10f,   // 10 meters
//                locationListener
//            )
//        }


        imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.header_image)
        scaledImageBitmap = imageBitmap?.let { Bitmap.createScaledBitmap(it, 720, 257, false) }



        imgV = findViewById(R.id.imageId)



//        val drawable = imgV.drawable as BitmapDrawable
//        val imgBitmap = drawable.bitmap


        val pickmedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri ->
                if (uri!=null){
                    imgV.setImageURI(uri)

            }
        }

        binding.photobtnId.setOnClickListener {
            pickmedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }




//        imageUri = createImageUri()
//        binding.capbtnId.setOnClickListener {
//            tackPhoto.launch(imageUri)
//        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()








        printId.setOnClickListener {

            if (title.text.isEmpty() && mouza.text.isEmpty() && plot.text.isEmpty() && survey.text.isEmpty()) {
                Toast.makeText(this, "Please Fill the impotent field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            var title = title.text.toString()
            var mouza = mouza.text.toString()
            var plot = plot.text.toString()
            var survey = survey.text.toString()
            var upazila = upazila.text.toString()
            var dtls = details.text.toString()

            var locs = locationid.text.toString()





            val sdf = SimpleDateFormat("dd-MM-yyyy HH-mm-ss")
            val currentDateAndTime = sdf.format(Date()).toString()



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    createPDF(currentDateAndTime, title, mouza, plot, survey,upazila, dtls,locs)
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
                    createPDF(currentDateAndTime,title, mouza, plot, survey,upazila, dtls,locs)
                    printId.visibility = View.GONE

                } else {
                    requestAllPermission()
                }
            }
        }













    }

//    fun createImageUri(): Uri{
//
//        val image = File(applicationContext.filesDir,
//            "camera_photos.png")
//        return FileProvider.getUriForFile(applicationContext,
//            "org.map_bd.surveycalculator.FileProvider",
//            image)
//    }












    private fun getCurrentLocation(){

        if(checkPermissions())
        {
            if (isLocationEnnabled())
            {
                // final lot and long code here
                if (ActivityCompat.checkSelfPermission(
                        this,
                        permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task ->
                    val location : Location? = task.result
                    if (location==null){
                        Toast.makeText(this, "Null Location", Toast.LENGTH_SHORT).show()
                    }else
                    {
                        Toast.makeText(this, "Get Success", Toast.LENGTH_SHORT).show()
                        val latitude = location.latitude
                        val longitude = location.longitude

                        locationid.setText("$latitude, $longitude")
                    }

                }
            }else
            {
        //      Settings Open Here
                Toast.makeText(this, "Turn on Location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else
        {
            // request permission here
            requestPermission()
        }


    }

    private fun isLocationEnnabled(): Boolean{
        val locationManager: LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUESTCODE

//            PERMISSION_REQUEST_ACCESS_LOCATION
        )

    }

//    companion object{
//        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
//    }

    private fun checkPermissions() : Boolean
    {
        if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
        {
           return true
        }

        return false
    }












    // hide keyboard
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }



    private fun createPDF(currentDateAndTime: String, title: String, mouza: String, plot: String, survey: String, upazila: String, dtls: String, locs: String) {
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
        canvas.drawLine(150f, 600f, 550f, 600f, line)


        // vertical lines
        canvas.drawLine(150f, 350f, 150f, 600f, line)
        canvas.drawLine(350f, 350f, 350f, 600f, line)
        canvas.drawLine(550f, 350f, 550f, 600f, line)

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
        canvas.drawText("Upazila: ",180f,580f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.black,null)
        paint.textSize = 20f
        canvas.drawText("$upazila ",360f,580f,paint)


        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        paint.color = resources.getColor(R.color.hollyGreenColor, null)
        canvas.drawText("Location : $locs ",150f, 630f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        paint.color = resources.getColor(R.color.hollyGreenColor, null)
        canvas.drawText("Details  : $dtls ",10f, 680f, paint)

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
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }








}