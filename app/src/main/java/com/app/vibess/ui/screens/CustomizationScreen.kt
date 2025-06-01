package com.app.vibess.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.vibess.R
import com.app.vibess.ui.components.custom.ImagePicker
import com.app.vibess.ui.components.custom.TShirtCustomizerWithGestures
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.callback.ErrorInfo
import com.google.firebase.database.FirebaseDatabase

@Composable
fun CustomTshirtScreen(navController: NavController) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var scale by remember { mutableStateOf(1f) }
    var textToAdd by remember { mutableStateOf(TextFieldValue("")) }
    var textColor by remember { mutableStateOf(Color.Black) }
    var textPosition by remember { mutableStateOf("bottom") }
    var isImageLoaded by remember { mutableStateOf(false) } // Отслеживаем, загружено ли изображение
    var isTextEditingEnabled by remember { mutableStateOf(false) } // Отслеживаем, активен ли режим редактирования текста

    // Используем launcher для выбора изображения из галереи
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = uri
            isImageLoaded = true // Устанавливаем флаг, что изображение загружено
        }
    }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Показываем футболку сразу на экране
        Image(
            painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxdspcnk6/image/upload/v1748125075/custom_thirt_cqlrbc.png"),
            contentDescription = "Футболка",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Fit
        )

        // Кнопка для загрузки изображения
        if (!isImageLoaded) {
            ImagePicker { uri ->
                selectedImageUri = uri
                isImageLoaded = true // Устанавливаем флаг, что изображение загружено
            }
        }

        // Иконка для текста, которая появляется после загрузки изображения
        if (isImageLoaded) {
            IconButton(onClick = { isTextEditingEnabled = !isTextEditingEnabled }) {
                Icon(painter = painterResource(id = R.drawable.ic_text), contentDescription = "Текст")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Если текстовое редактирование включено, показываем кнопки
        if (isTextEditingEnabled) {
            Row {
                Button(onClick = { /* Логика для ввода текста */ }) {
                    Text("Ввести текст", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { textColor = Color.Red }) {
                    Text("Цвет", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { textPosition = "bottom" }) {
                    Text("Положение", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Основная область с кастомизацией футболки
        TShirtCustomizerWithGestures(
            userImageUri = selectedImageUri,
            scale = scale,
            rotationState = 0f,
            offsetState = Offset.Zero,
            textOverlay = textToAdd.text, // Передаем текст
            textColor = textColor,
            textPosition = textPosition,
            onScaleChange = { scale = it },
            onRotationChange = {},
            onOffsetChange = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка для сохранения данных
        if (isImageLoaded) {
            Button(
                onClick = {
                    // Сохраняем данные (ссылка на изображение, масштаб, позиция, текст) в Firebase
                    selectedImageUri?.let { imageUri ->
                        uploadImageToCloudinary(imageUri, context, scale, textPosition, textToAdd.text)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green) // Зеленая кнопка для сохранения
            ) {
                Text("Сохранить", color = Color.White) // Белый текст
            }
        }
    }
}

// Функция для загрузки изображения в Cloudinary
fun uploadImageToCloudinary(uri: Uri, context: Context, scale: Float, position: String, text: String) {
    // Загружаем изображение в Cloudinary
    MediaManager.get().upload(uri)
        .unsigned("kks8dyht") // Ваш unsigned upload preset
        .callback(object : UploadCallback {
            override fun onStart(requestId: String?) {
                // Загрузка началась
                Toast.makeText(context, "Загрузка началась", Toast.LENGTH_SHORT).show()
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                // Прогресс загрузки
            }

            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                // Загружено успешно, получаем URL изображения
                val imageUrl = resultData?.get("url") as String
                Toast.makeText(context, "Загрузка успешна", Toast.LENGTH_SHORT).show()

                // Сохраняем URL изображения и дополнительные данные в Firebase
                saveImageDataToDatabase(imageUrl, scale, position, text)
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                // Ошибка загрузки
                Toast.makeText(context, "Ошибка загрузки: $error", Toast.LENGTH_SHORT).show()
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                // Повторная попытка загрузки
                Toast.makeText(context, "Загрузка отложена: $error", Toast.LENGTH_SHORT).show()
            }
        }).dispatch() // Запускаем запрос
}

// Функция для сохранения данных в Firebase
fun saveImageDataToDatabase(imageUrl: String, scale: Float, position: String, text: String) {
    val database = FirebaseDatabase.getInstance() // Получаем ссылку на базу данных Firebase
    val myRef = database.getReference("userImages") // Ссылка на таблицу с изображениями

    // Создаем новый уникальный ключ и сохраняем данные
    val newImageRef = myRef.push() // Создаем уникальный ключ для нового изображения

    // Данные, которые мы хотим сохранить: URL, масштаб, позиция, текст
    val imageData = mapOf(
        "imageUrl" to imageUrl,
        "scale" to scale,
        "position" to position,
        "text" to text,
        "timestamp" to System.currentTimeMillis() // Время загрузки
    )

    newImageRef.setValue(imageData) // Сохраняем данные в Firebase
}