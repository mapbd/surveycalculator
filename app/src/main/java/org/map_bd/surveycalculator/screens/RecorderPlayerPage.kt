package org.map_bd.surveycalculator.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.map_bd.surveycalculator.AudioActivity
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.ui.themes.DarkGrey
import org.map_bd.surveycalculator.ui.themes.DarkText
import org.map_bd.surveycalculator.ui.themes.LightText
import org.map_bd.surveycalculator.ui.themes.LightYellow
import java.io.File
import java.io.FileInputStream
import java.util.Locale

import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RecorderPlayerPage(navController: NavHostController) {
    val context = LocalContext.current
    val speechContext = context as AudioActivity
    val coroutineScope = rememberCoroutineScope()

    var recordingTime by remember { mutableStateOf(0L) }
    var lockedRecordingTime by remember { mutableStateOf(0L) }
    var isRecording by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

    var currentFile by remember { mutableStateOf<File?>(null) }
    var currentFileName by remember { mutableStateOf("") }

    // Coordinates state
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }

    // Proactively fetch location on component launch
    LaunchedEffect(Unit) {
        val location = getCurrentLocation(context)
        latitude = location?.latitude
        longitude = location?.longitude
    }

    LaunchedEffect(key1 = isRecording, key2 = isPaused) {
        if (isRecording && !isPaused) {
            while (true) {
                delay(1000L)
                recordingTime += 1000L
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Recorder Player",
                        color = if (isSystemInDarkTheme()) DarkText else LightText
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isSystemInDarkTheme()) DarkText else LightText
                        )
                    }
                },
                backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                shape = RoundedCornerShape(25.dp),
                backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                elevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Text(
                            text = "NEW RECORD",
                            color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Image(
                            painter = painterResource(R.drawable.equalizer),
                            contentDescription = "Equalizer image",
                            colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) DarkGrey else LightYellow)
                        )
                    }

                    Text(
                        text = formatTime(recordingTime),
                        color = if (isSystemInDarkTheme()) DarkText else LightText,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // ... Inside your RecorderPlayerPage Composable function ...
                        val coroutineScope = rememberCoroutineScope()
                        var currentFileName by remember { mutableStateOf("") }
                        var currentFile by remember { mutableStateOf<File?>(null) }
                        var isRecording by remember { mutableStateOf(false) }
                        var isPaused by remember { mutableStateOf(false) }

// Inside your Play/Record FloatingActionButton onClick block:
                        FloatingActionButton(
                            onClick = {
                                if (!isRecording) {
                                    // Launch coroutine to get location before starting recording
                                    coroutineScope.launch {
                                        val location = getCurrentLocation(context)
                                        val latitude = location?.latitude
                                        val longitude = location?.longitude

                                        val timestamp = System.currentTimeMillis()

                                        // Format location coordinates safely for filename system strings
                                        val latStr = latitude?.let { String.format(Locale.US, "%.6f", it) } ?: "UnknownLat"
                                        val lonStr = longitude?.let { String.format(Locale.US, "%.6f", it) } ?: "UnknownLng"

                                        // Final customized tracking name structure
                                        currentFileName = "Survey_Calculator_${timestamp}_Lat${latStr}_Lng${lonStr}.mp3"

                                        val targetFile: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            File(context.cacheDir, currentFileName)
                                        } else {
                                            val recordsFolder = File(Environment.getExternalStorageDirectory(), "Survey Calculator/Records")
                                            if (!recordsFolder.exists()) { recordsFolder.mkdirs() }
                                            File(recordsFolder, currentFileName)
                                        }

                                        currentFile = targetFile
                                        speechContext.recorderPlayer.start(targetFile)
                                        isRecording = true
                                        isPaused = false
                                    }
                                } else if (isPaused) {
                                    speechContext.recorderPlayer.resume()
                                    isPaused = false
                                } else {
                                    speechContext.recorderPlayer.pause()
                                    isPaused = true
                                }
                            },
                            backgroundColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                        ) {
                            Icon(
                                imageVector = if (isRecording && !isPaused) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = "Start recording",
                                tint = if (isSystemInDarkTheme()) LightYellow else DarkGrey
                            )
                        }




                        FloatingActionButton(
                            onClick = {
                                if (isRecording) {
                                    speechContext.recorderPlayer.stop()
                                    lockedRecordingTime = recordingTime
                                    recordingTime = 0L
                                    isRecording = false
                                    isPaused = false

                                    currentFile?.let { file ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            val savedUri = saveAudioToMediaStore(context, file, currentFileName)
                                            if (savedUri != null) {
                                                speechContext.audioFileList.add(file)
                                                speechContext.saveFilePath(savedUri.toString())
                                                file.delete()
                                            }
                                        } else {
                                            speechContext.audioFileList.add(file)
                                            speechContext.saveFilePath(file.absolutePath)
                                        }
                                    }

                                    android.widget.Toast.makeText(context, "Saved", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            },
                            backgroundColor = if (isSystemInDarkTheme()) DarkGrey else LightYellow
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Stop,
                                contentDescription = "Stop recording",
                                tint = if (isSystemInDarkTheme()) LightYellow else DarkGrey
                            )
                        }
                    }
                }
            }
        }
    }
}



fun formatTime(milliseconds: Long): String {
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

fun saveAudioToMediaStore(context: Context, cacheFile: File, fileName: String): Uri? {
    val resolver = context.contentResolver
    val audioCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    val contentValues = ContentValues().apply {
        put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Saves into Music/Survey Calculator Records/
            put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Survey Calculator/Records")
            put(MediaStore.Audio.Media.IS_PENDING, 1)
        }
    }

    val uri = resolver.insert(audioCollection, contentValues) ?: return null

    return try {
        resolver.openOutputStream(uri)?.use { outputStream ->
            FileInputStream(cacheFile).use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.Audio.Media.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }
        uri
    } catch (e: Exception) {
        e.printStackTrace()
        resolver.delete(uri, null, null)
        null
    }
}

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(context: Context): Location? = withContext(Dispatchers.IO) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    return@withContext try {
        // Blocks safely within the IO thread pool to resolve the task synchronously
        Tasks.await(fusedLocationClient.lastLocation)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}