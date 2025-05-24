package com.app.vibess.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun LogoHeader() {
    // Замените на реальный URL вашей картинки из Cloudinary
    val imageUrl = "https://res.cloudinary.com/dxdspcnk6/image/upload/v1748096212/vibes_ylg4jr.png"
    Spacer(Modifier.height(24.dp))

    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = "Логотип Vibes",
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),
        contentScale = ContentScale.Fit
    )
}
