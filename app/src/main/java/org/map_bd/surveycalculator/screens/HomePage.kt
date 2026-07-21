package org.map_bd.surveycalculatorr.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import org.map_bd.surveycalculator.GenarelActivity
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.navigation.NavScreens
// import org.map_bd.surveycalculator.ui.composables.BackPress
import org.map_bd.surveycalculator.ui.themes.DarkGrey
import org.map_bd.surveycalculator.ui.themes.DarkText
import org.map_bd.surveycalculator.ui.themes.DarkYellow
import org.map_bd.surveycalculator.ui.themes.LightText
import org.map_bd.surveycalculator.ui.themes.LightYellow
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomePage(navController: NavHostController) {
    var showInfoDialog by remember { mutableStateOf(false) }

//    BackPress(onBackPressed = {})

    val context = LocalContext.current as? Activity

    // 2. Intercept the system back button
    BackHandler {
        // 3. Create the intent for the target activity
        val intent = Intent(context, GenarelActivity::class.java)
        context?.startActivity(intent)

        // 4. Close the current activity
        context?.finish()
    }


    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = if (isSystemInDarkTheme()) DarkText else LightText
                    )
                },

                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {

                            val intent = Intent(context, GenarelActivity::class.java)

                            // 3. Start the target activity
//                            context.startActivity(intent)
                            context?.startActivity(intent)

                            // finish  current activity
                            (context as? ComponentActivity)?.finish()

//                            // Close the current activity
//                                    context?.finish()

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isSystemInDarkTheme()) DarkText else LightText
                        )
                    }
                },
                backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey
//                actions = {
//                    IconButton(
//                        onClick = { showInfoDialog = true }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Info,
//                            contentDescription = "Info icon",
//                            tint = if (isSystemInDarkTheme()) DarkText else LightText
//                        )
//                    }
//                },
//                backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .clickable { navController.navigate(NavScreens.RecorderPlayerPage.route) }
                        .width(280.dp)
                        .height(120.dp),
                    shape = RoundedCornerShape(25.dp),
                    backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                    elevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 30.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.recorder),
                            contentDescription = "Recorder image",
                            modifier = Modifier.size(70.dp),
                            colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) DarkGrey else DarkYellow)
                        )
                        Text(
                            text = "Recorder\nplayer",
                            color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Card(
                    modifier = Modifier
                        .clickable { navController.navigate(NavScreens.AudioPlayerPage.route) }
                        .width(280.dp)
                        .height(120.dp),
                    shape = RoundedCornerShape(25.dp),
                    backgroundColor = if (isSystemInDarkTheme()) LightYellow else DarkGrey,
                    elevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 30.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.audio_player),
                            contentDescription = "Audio player image",
                            modifier = Modifier.size(70.dp),
                            colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) DarkGrey else DarkYellow)
                        )
                        Text(
                            text = "Records\nlist",
                            color = if (isSystemInDarkTheme()) DarkGrey else LightYellow,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
    if (showInfoDialog) {
        val uriHandler = LocalUriHandler.current

        Dialog(
            onDismissRequest = { showInfoDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .height(470.dp)
                    .width(450.dp),
                shape = RoundedCornerShape(size = 8.dp),
                backgroundColor = MaterialTheme.colors.background
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { showInfoDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close dialog",
                            tint = if (isSystemInDarkTheme()) DarkText else LightText
                        )
                    }
                    Card(
                        modifier = Modifier
                            .height(400.dp)
                            .width(450.dp)
                            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = MaterialTheme.colors.onBackground,
                        elevation = 10.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSystemInDarkTheme()) LightText else DarkText,
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                thickness = 1.dp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Android application built with Kotlin and Jetpack Compose that shows how to " +
                                        "record the input voice and save it in .mp3 files.",
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                thickness = 1.dp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "My GitHub",
                                color = if (isSystemInDarkTheme()) LightText else DarkText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            IconButton(
                                onClick = { uriHandler.openUri("https://github.com/ndenicolais") },
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.github_logo),
                                    contentDescription = "Github image",
                                    colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) LightText else DarkText)
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Developed by MapBD",
                        color = if (isSystemInDarkTheme()) DarkText else LightText,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
