package org.map_bd.surveycalculator


import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import org.osmdroid.util.GeoPoint

class TrackingService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    companion object {
        const val CHANNEL_ID = "GPS_Tracking_Channel"
        const val NOTIFICATION_ID = 707
        val trackingPathPoints = mutableListOf<GeoPoint>()
        var onRoutePointsUpdated: ((List<GeoPoint>) -> Unit)? = null
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (loc in locationResult.locations) {
                    val point = GeoPoint(loc.latitude, loc.longitude)
                    trackingPathPoints.add(point)
                    onRoutePointsUpdated?.invoke(trackingPathPoints)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createTrackingNotificationChannel()
        val systemNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("GPS Tracker Running")
            .setContentText("Recording track coordinates...")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, systemNotification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(NOTIFICATION_ID, systemNotification)
        }

        initializeHardwareLocationLoops()
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun initializeHardwareLocationLoops() {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 4000)
            .setMinUpdateIntervalMillis(2000)
            .build()

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createTrackingNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelItem = NotificationChannel(
                CHANNEL_ID, "GPS Tracker Logs", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channelItem)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}