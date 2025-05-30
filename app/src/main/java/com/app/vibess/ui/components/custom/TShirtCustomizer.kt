package com.app.vibess.ui.components.custom

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter


@Composable
fun TShirtCustomizerWithGestures(
    userImageUri: Uri?,
    scale: Float,
    onScaleChange: (Float) -> Unit,
    rotationState: Float,
    onRotationChange: (Float) -> Unit,
    offsetState: Offset,
    onOffsetChange: (Offset) -> Unit,
    textOverlay: String, // Новый параметр для отображения текста
    textPosition: String // Новый параметр для положения текста
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        contentAlignment = Alignment.Center
    ) {
        // Футболка
        Image(
            painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxdspcnk6/image/upload/v1748125075/custom_thirt_cqlrbc.png"),
            contentDescription = "Футболка",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Изображение пользователя
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
                            val newScale = (scale * zoom).coerceIn(0.5f, 5f)
                            onScaleChange(newScale)
                            onRotationChange(rotationState + rotation)
                            onOffsetChange(offsetState + pan)
                        }
                    }
            )
        }

        // Отображение текста
        if (textOverlay.isNotEmpty()) {
            val textModifier = when (textPosition) {
                "top" -> Modifier.align(Alignment.TopCenter) // Текст над изображением
                "bottom" -> Modifier.align(Alignment.BottomCenter) // Текст под изображением
                else -> Modifier.align(Alignment.Center) // По умолчанию, если не задано
            }

            Text(
                text = textOverlay,
                modifier = textModifier
                    .graphicsLayer(
                        scaleX = scale,  // Масштаб
                        scaleY = scale,  // Масштаб
                        rotationZ = rotationState,  // Поворот
                        translationX = offsetState.x,  // Перемещение по оси X
                        translationY = offsetState.y   // Перемещение по оси Y
                    ),
                color = Color.Black, // Цвет текста
                fontSize = 24.sp, // Размер шрифта
                fontWeight = FontWeight.Bold
            )
        }
    }
}