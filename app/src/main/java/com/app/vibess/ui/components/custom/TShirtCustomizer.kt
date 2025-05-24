package com.app.vibess.ui.components.custom

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.app.vibess.R

@Composable
fun TShirtCustomizer(
    userImageUri: Uri?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        contentAlignment = Alignment.Center
    ) {
        // Фон — футболка (замени на свой ресурс)
        Image(
            painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxdspcnk6/image/upload/v1748125075/custom_thirt_cqlrbc.png"),
            contentDescription = "Футболка",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Наложенное изображение пользователя
        userImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Пользовательское изображение",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit
            )
        }
    }
}
