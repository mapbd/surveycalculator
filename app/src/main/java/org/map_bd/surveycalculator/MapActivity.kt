package org.map_bd.surveycalculator


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.map_bd.surveycalculator.databinding.ActivityGenarelBinding
import org.map_bd.surveycalculator.databinding.ActivityMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import java.io.OutputStream

@Suppress("DEPRECATION")
class MapActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMapBinding

    private lateinit var mapView: MapView
    private lateinit var fabSwitchLayer: FloatingActionButton

    private lateinit var btnZoomIn: FloatingActionButton

    private lateinit var btnZoomOut: FloatingActionButton
    private lateinit var btnToggleTracker: Button
    private lateinit var btnExportGpxFile: Button

    private var trackingStatusActive = false
    private var isSatelliteLayerActive = false
    private val trackingVisualPolyline = Polyline()

    // Define a custom, free ArcGIS Global Imagery Tile Source Engine
    private val arcGisSatelliteSource = XYTileSource(
        "ArcGIS_Satellite",
        0, 19, 256, ".png",
        arrayOf("https://{switch:services,server}.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/tile/{zoom}/{y}/{x}")


//                https://{switch:services,server}.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/tile/{zoom}/{y}/{x}
//                https://mt1.google.com/vt/lyrs=y&x={x}&y={y}&z={z}
//                https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Load osmdroid internal configuration safely for storage tile caching
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_map)


        mapView = findViewById(R.id.mapView)
        fabSwitchLayer = findViewById(R.id.fabSwitchLayer)
        btnToggleTracker = findViewById(R.id.btnToggleTracker)
        btnExportGpxFile = findViewById(R.id.btnExportGpxFile)

        initializeMapFramework()

        btnZoomIn = findViewById(R.id.btnZoomIn)
        btnZoomOut = findViewById(R.id.btnZoomOut)

        // Custom Zoom In action
        btnZoomIn.setOnClickListener {
            mapView.controller.zoomIn()
        }

        // Custom Zoom Out action
        btnZoomOut.setOnClickListener {
            mapView.controller.zoomOut()
        }

        fabSwitchLayer.setOnClickListener { toggleMapLayers() }
        btnToggleTracker.setOnClickListener { switchTrackingStates() }
        btnExportGpxFile.setOnClickListener { triggerGpxDocumentCreationWizard() }

        TrackingService.onRoutePointsUpdated = { spatialVectors ->
            runOnUiThread {
                renderUpdatedPathVectors(spatialVectors)
            }
        }

        verifyRuntimePermissions()
    }

    private fun initializeMapFramework() {
        mapView.setBuiltInZoomControls(false)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)

        val dm : DisplayMetrics = this.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(mapView)
        scaleBarOverlay.setCentred(true)
//play around with these values to get the location on screen in the right place for your application
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mapView.overlays.add(scaleBarOverlay)
        val compassOverlay = CompassOverlay(this, InternalCompassOrientationProvider(this), mapView)
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)



        // Set Default Layer to Standard OpenStreetMap Street Vector Style
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        // Configure a universal neon tracking path line style
        trackingVisualPolyline.outlinePaint.color = Color.parseColor("#FF0055") // High-contrast Hot Pink
        trackingVisualPolyline.outlinePaint.strokeWidth = 10f
        mapView.overlays.add(trackingVisualPolyline)
    }

    private fun toggleMapLayers() {
        if (isSatelliteLayerActive) {
            mapView.setTileSource(TileSourceFactory.MAPNIK) // Switch to Standard Vector Map
            Toast.makeText(this, "Switched to OpenStreetMap Street view", Toast.LENGTH_SHORT).show()
        } else {
            mapView.setTileSource(arcGisSatelliteSource) // Switch to Raster Satellite
            Toast.makeText(this, "Switched to Satellite Imagery view", Toast.LENGTH_SHORT).show()
        }
        isSatelliteLayerActive = !isSatelliteLayerActive
        mapView.invalidate() // Force map to redraw immediately
    }

    private fun switchTrackingStates() {
        val serviceIntent = Intent(this, TrackingService::class.java)
        if (trackingStatusActive) {
            stopService(serviceIntent)
            btnToggleTracker.text = "Start Tracking"
            trackingStatusActive = false
        } else {
            ContextCompat.startForegroundService(this, serviceIntent)
            btnToggleTracker.text = "Stop Tracking"
            trackingStatusActive = true
        }
    }

    private fun renderUpdatedPathVectors(points: List<GeoPoint>) {
        if (points.isNotEmpty()) {
            trackingVisualPolyline.setPoints(points)
            mapView.controller.animateTo(points.last())
            mapView.invalidate()
        }
    }

    // --- SECURE STORAGE ACCESS FRAMEWORK DOCUMENT EXPORTER (Android 14 Compliant) ---
    private val saveDocumentFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { systemUri ->
                contentResolver.openOutputStream(systemUri)?.use { documentStream ->
                    compileAndWriteGpxPayload(documentStream, TrackingService.trackingPathPoints)
                    Toast.makeText(this, "Track Exported successfully!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun triggerGpxDocumentCreationWizard() {
        if (TrackingService.trackingPathPoints.isEmpty()) {
            Toast.makeText(this, "No vector track data available to export.", Toast.LENGTH_SHORT).show()
            return
        }
        val documentIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/gpx+xml"
            putExtra(Intent.EXTRA_TITLE, "surveycalculator_${System.currentTimeMillis()}.gpx")
        }
        saveDocumentFileLauncher.launch(documentIntent)
    }

    private fun compileAndWriteGpxPayload(outputStream: OutputStream, vectors: List<GeoPoint>) {
        val xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<gpx version=\"1.1\" creator=\"SurveyCalculator\" xmlns=\"http://topografix.com\">\n" +
                "  <trk>\n    <name>Dual Layer Track Logs</name>\n    <trkseg>\n"
        val xmlFooter = "    </trkseg>\n  </trk>\n</gpx>"

        val bodyContent = StringBuilder()
        for (item in vectors) {
            bodyContent.append("      <trkpt lat=\"${item.latitude}\" lon=\"${item.longitude}\"></trkpt>\n")
        }

        outputStream.write((xmlHeader + bodyContent.toString() + xmlFooter).toByteArray())
    }

    // --- RUNTIME PERMISSIONS WRAPPER FOR TARGET SDK 34 ---
    private fun verifyRuntimePermissions() {
        val requestPool = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPool.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        permissionRequestLauncher.launch(requestPool.toTypedArray())
    }

    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { assessmentMap ->
        if (assessmentMap[Manifest.permission.ACCESS_FINE_LOCATION] != true) {
            Toast.makeText(this, "Coordinate tracking strictly requires GPS permissions.", Toast.LENGTH_LONG).show()
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