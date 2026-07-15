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
import android.util.DisplayMetrics
import android.util.Log.d
import android.view.LayoutInflater
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
import kotlin.jvm.java


class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    private lateinit var imgV: ImageView

    private lateinit var locationid: TextView


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



        imgV = findViewById(R.id.imageId)





        val pickmedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri ->
                if (uri!=null){
                    imgV.setImageURI(uri)

            }
        }

        binding.photobtnId.setOnClickListener {
            pickmedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }




        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()








        printId.setOnClickListener {

            val title = title.text.toString().trim()
            val mouza =mouza.text.toString().trim()
            val plot = plot.text.toString().trim()
            val survey = survey.text.toString().trim()
            val upazila = upazila.text.toString().trim()
            val details = details.text.toString().trim()
            val locationid = locationid.text.toString().trim()


            val intent = Intent(this, InvoiceActivity::class.java).apply {
                putExtra("TITLE", title)
                putExtra("MOUZA", mouza)
                putExtra("PLOT", plot)
                putExtra("SURVEY", survey)
                putExtra("UPAZILA", upazila)
                putExtra("DETAILS", details)
                putExtra("LOCATION", locationid)

            putExtra("EXTRA_IMAGE_URI", imgV?.toString())

            // CRITICAL FOR SDK 34: Grants temporary read access if using content:// URIs
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)


            }
            startActivity(intent)

            createViewPDF()
        }















    }














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



    private fun createViewPDF() {
        val screenWidth : Int
        val screenHeight : Int

        val sdf = SimpleDateFormat("dd-MM-yyyy HH-mm-ss")
        val currentDateAndTime = sdf.format(Date()).toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            screenWidth = windowManager.currentWindowMetrics.bounds.width()
            screenHeight = windowManager.currentWindowMetrics.bounds.height()
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            screenWidth = displayMetrics.widthPixels
            screenHeight = displayMetrics.heightPixels
        }

        val view = LayoutInflater.from(this).inflate(R.layout.invoice_activity, null)

        view.measure(
            View.MeasureSpec.makeMeasureSpec(screenWidth, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(screenHeight, View.MeasureSpec.EXACTLY)
        )

        view.layout(0, 0, screenWidth, screenHeight)

        val pdfDocument = PdfDocument()
        val pageInfo =  PageInfo.Builder(screenWidth, screenHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        view.draw(page.canvas)
        pdfDocument.finishPage(page)

//        val file = File(Environment..getExternalStorageDirectory(), "MyViewPDF.pdf")
//        pdfDocument.writeTo(FileOutputStream(file))

        val folder = File(Environment.getExternalStorageDirectory(), "Survey Calculator/Form")
        if (folder.exists()) {
            d("folder", "exists")
        } else {
            d("folder", "not exists")
            folder.mkdirs()
        }


        val file = File(folder,
            "/Form_$currentDateAndTime.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "PDF saved to " + file.absolutePath, Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }






}