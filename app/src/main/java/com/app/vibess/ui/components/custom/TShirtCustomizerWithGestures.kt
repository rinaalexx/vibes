package com.app.vibess.ui.components.custom

import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.vibess.ui.components.custom.ImagePicker
import com.app.vibess.ui.components.custom.TShirtCustomizerWithGestures
// Обновленный метод TShirtCustomizerWithGestures
@Composable
fun TShirtCustomizerWithGestures(
    userImageUri: Uri?,
    scale: Float,
    onScaleChange: (Float) -> Unit,
    rotationState: Float,
    onRotationChange: (Float) -> Unit,
    offsetState: Offset,
    onOffsetChange: (Offset) -> Unit,
    textOverlay: String,
    textPosition: String,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp), // Размер контейнера для футболки
        contentAlignment = Alignment.Center
    ) {
        // Футболка
        Image(
            painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxdspcnk6/image/upload/v1748125075/custom_thirt_cqlrbc.png"),
            contentDescription = "Футболка",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Изображение пользователя (сдвигаем на 20dp вниз)
        userImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Пользовательское изображение",
                modifier = Modifier
                    .size(140.dp) // Ограничиваем размер изображения
                    .offset(y = 16.dp) // Сдвигаем изображение на 20dp вниз
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
                "top" -> Modifier
                    .align(Alignment.TopCenter) // Текст над изображением
                    .offset(y = 115.dp) // Сдвигаем текст на 20dp вниз от верхней части
                "bottom" -> Modifier
                    .align(Alignment.BottomCenter) // Текст под изображением
                    .offset(y = -85.dp) // Сдвигаем текст на 20dp вверх от нижней части
                else -> Modifier.align(Alignment.Center) // По умолчанию, если не задано
            }

            Text(
                text = textOverlay,
                modifier = textModifier,
                color = textColor, // Цвет текста
                fontSize = 20.sp, // Размер шрифта
                fontWeight = FontWeight.Bold
            )
        }
    }
}
