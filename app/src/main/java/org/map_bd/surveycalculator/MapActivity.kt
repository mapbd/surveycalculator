package org.map_bd.surveycalculator



import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.map_bd.surveycalculator.databinding.ActivityMapBinding
import org.map_bd.surveycalculator.ui.MainMapViewModel
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.File


import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import android.os.Environment
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


@Suppress("DEPRECATION")
class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding

    private lateinit var mMap: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var mapController: IMapController

    private lateinit var viewModel: MainMapViewModel






    private var currentPoint: GeoPoint? = null
    private var permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            requestLocationUpdate()
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Denied!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mMap = binding.streetMapView

        initializeOSM()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainMapViewModel::class.java)


        val rotationGestureOverlay = RotationGestureOverlay(mMap)
        rotationGestureOverlay.isEnabled

        mMap.setTileSource(TileSourceFactory.MAPNIK)


     /*   tring custom tileSource */

//        mMap.setTileSource(
//            /* aTileSource = */
//            XYTileSource(
//                /* aName = */ "mapbd",
//                /* aZoomMinLevel = */
//                0, /* aZoomMaxLevel = */
//                19, /* aTileSizePixels = */
//                256, /* aImageFilenameEnding = */
//                " ",
//                /* aBaseUrl = */
//                 String[]{"https://mapbd.github.io/boimela/image/{z}/{x}/{y}.png"}, "© OpenStreetMap contributors"
//            )
//        )





        mMap.setMultiTouchControls(true)
        mMap.overlays.add(rotationGestureOverlay)


        val dm : DisplayMetrics = this.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(mMap)
        scaleBarOverlay.setCentred(true)
//play around with these values to get the location on screen in the right place for your application
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mMap.overlays.add(scaleBarOverlay)


        val compassOverlay = CompassOverlay(this, InternalCompassOrientationProvider(this), mMap)
        compassOverlay.enableCompass()
        mMap.overlays.add(compassOverlay)

        mMap.invalidate()


        binding.pdf.setOnClickListener {

            binding.toolbar.isVisible = false
            it.isVisible = false

            createViewPDF()
        }




        viewModel.getStatus().observe(this,{
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })



    }


    private fun initializeOSM() {
        Configuration.getInstance()
            .load(this, getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE))
        mMap.setMultiTouchControls(true)
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mMap)
        mapController = mMap.controller


        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            requestLocationUpdate()
        }
    }


    private fun requestLocationUpdate() {
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()
        myLocationOverlay.isDrawAccuracyEnabled = true

        myLocationOverlay.runOnFirstFix {
            GlobalScope.launch(Dispatchers.Main) {
                currentPoint = myLocationOverlay.myLocation
                mapController.setCenter(currentPoint)
                mapController.animateTo(currentPoint)

                mMap.overlays.add(myLocationOverlay)
                mapController.setZoom(19.0)

                updateTextView(currentPoint!!)
            }
        }
    }

    private fun updateTextView(geoPoint: GeoPoint) {
        val latitude = geoPoint.latitude
        val longitude = geoPoint.longitude
        binding.tvLatLng.text = "$latitude,$longitude"
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }

        }
        return true
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

        val view = LayoutInflater.from(this).inflate(R.layout.activity_map, null)

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

        val folder = File(Environment.getExternalStorageDirectory(), "Survey Calculator/Map")
        if (folder.exists()) {
            d("folder", "exists")
        } else {
            d("folder", "not exists")
            folder.mkdirs()
        }


        val file = File(folder,
            "/Map_pdf_$currentDateAndTime.pdf"
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