package com.app.vibess.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.app.vibess.R

@Composable
fun SplashScreen() {
    val imageUrl = "https://res.cloudinary.com/dxdspcnk6/image/upload/v1748096212/vibes_ylg4jr.png"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // вот здесь фон белый
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Splash Logo"

        )
    }
}
