package com.app.vibess.ui.components.custom

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


@Composable
fun TShirtCustomizerWithGestures(userImageUri: Uri?) {
    var scale by remember { mutableStateOf(1f) }
    var rotationState by remember { mutableStateOf(0f) }
    var offsetState by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxdspcnk6/image/upload/v1748125075/custom_thirt_cqlrbc.png"),
            contentDescription = "Футболка",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        userImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Пользовательское изображение",
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        rotationZ = rotationState,
                        translationX = offsetState.x,
                        translationY = offsetState.y
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, rotation ->
                            scale = (scale * zoom).coerceIn(0.5f, 5f)
                            rotationState += rotation
                            offsetState += pan
                        }
                    }
            )
        }
    }
}

