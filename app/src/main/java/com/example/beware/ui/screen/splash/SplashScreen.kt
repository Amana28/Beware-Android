package com.example.beware.ui.screen.splash

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.beware.R
import com.example.beware.navigation.Screen
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController){


    LaunchedEffect(true) {
        delay(5000) // Delay for 5 seconds
        navController.navigate(Screen.Login.route)
    }
    Column(modifier = Modifier.fillMaxHeight().background(colorResource(id = R.color.dark_grey))) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ){
            VideoPlayer()
        }
    }


}

@Composable
fun VideoPlayer(){
    Column() {
        val context = LocalContext.current
        val url = "https://firebasestorage.googleapis.com/v0/b/beware-f953e.appspot.com/o/files%2Fanimations%2Fbewarelogoanimation.mp4?alt=media&token=3567f8cc-9fa3-4c63-a228-8b0ad6b8a7eb"
        val exoPlayer = ExoPlayer.Builder(context).build()
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        exoPlayer.setMediaItem(mediaItem)


        val playerView = StyledPlayerView(context)
        playerView.useController = false
        playerView.player = exoPlayer


        DisposableEffect(AndroidView(factory = {playerView})){

            exoPlayer.prepare()
            exoPlayer.playWhenReady= true
            exoPlayer.setPlaybackSpeed(0.8F)

            onDispose {
                exoPlayer.release()
            }
        }
    }
}

