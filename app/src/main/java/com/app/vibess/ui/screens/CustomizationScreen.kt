package com.app.vibess.ui.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.vibess.ui.components.custom.ImagePicker
import com.app.vibess.ui.components.custom.TShirtCustomizerWithGestures
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.callback.ErrorInfo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.app.vibess.data.model.Customization
import com.app.vibess.functions.AuthViewModelCart
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CustomTshirtScreen(navController: NavController) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var scale by remember { mutableStateOf(1f) }
    var textToAdd by remember { mutableStateOf(TextFieldValue("")) }
    var textColor by remember { mutableStateOf(Color.Black) }
    var textPosition by remember { mutableStateOf("bottom") }
    var textFont by remember { mutableStateOf("Inter") }
    var isImageLoaded by remember { mutableStateOf(false) } // Отслеживаем, загружено ли изображение
    var isTextAdd by remember { mutableStateOf(false) } // Отслеживаем, активен ли режим добавления текста
    var isColorPickerEnabled by remember { mutableStateOf(false) } // Отслеживаем, активен ли выбор цвета
    var isPositionPickerEnabled by remember { mutableStateOf(false) } // Отслеживаем, активен ли выбор положения
    var isFontPickerEnabled by remember { mutableStateOf(false) } // Отслеживаем, активен ли выбор шрифта
    var isTextEntered by remember { mutableStateOf(false) } // Отслеживаем, введен ли текст
    var isTextEditing by remember { mutableStateOf(false) } // Отслеживаем, введен ли текст

    // Используем launcher для выбора изображения из галереи
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = uri
            isImageLoaded = true // Устанавливаем флаг, что изображение загружено
        }
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid?: "" // Получаем UID текущего пользователя
    val currentUser = FirebaseAuth.getInstance().currentUser


    var imageUrlForsave: String? = null;
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween // Это прижмет контент к верхней и нижней части экрана
    ) {

        // Кнопка для загрузки изображения
        if (!isImageLoaded) {
            ImagePicker { uri ->
                selectedImageUri = uri
                isImageLoaded = true // Устанавливаем флаг, что изображение загружено
            }
        }

        // Иконка для текста, которая появляется после загрузки изображения
        if (isImageLoaded && !isTextEntered) {
            Button(onClick = { isTextAdd = true; isTextEntered =true}) {
                Text("Добавить текст", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Если добавление текста включено, показываем кнопки
        if (isTextAdd && isTextEntered) {
            // Кнопка для ввода текста
            Button(onClick = {
                isTextEntered = true
                isTextEditing = true
                isTextAdd = false
            }) {
                Text("Готово", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Поле для ввода текста
            OutlinedTextField(
                value = textToAdd,
                onValueChange = { textToAdd = it },
                label = { Text("Введите текст") }, // Подсказка для ввода текста
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF)) // Цвет фона для поля
                    .padding(16.dp),
                textStyle = TextStyle(color = textColor),
                singleLine = true // Сделаем текстовое поле однострочным
            )
        }

        // Когда текст введен, показываем кнопки для выбора цвета, положения и шрифта
        if (isTextEntered && isTextEditing) {
            Spacer(modifier = Modifier.height(16.dp))

            // Кнопки для выбора цвета, положения и шрифта в одном ряду
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { isColorPickerEnabled = true; isTextEditing = false  }) {
                    Text("Цвет", color = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { isPositionPickerEnabled = true; isTextEditing = false  }) {
                    Text("Положение", color = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { isFontPickerEnabled = true; isTextEditing = false  }) {
                    Text("Шрифт", color = Color.White)
                }
            }
        }

// Цвет текста
        if ((isColorPickerEnabled) and (!isPositionPickerEnabled) and (!isFontPickerEnabled)) {
            Row {
                Button(onClick = {
                    textColor = Color.Black; isTextEditing = true; isColorPickerEnabled = false
                }) {
                    Text("Черный", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    textColor = Color(0xFF4B3221); isTextEditing = true; isColorPickerEnabled =
                    false
                }) {
                    Text("Коричневый", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    textColor = Color(0xFF6B0202); isTextEditing = true; isColorPickerEnabled =
                    false
                }) {
                    Text("Красный", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                isColorPickerEnabled = false; isTextEditing = true; isColorPickerEnabled = false
            }) {
                Text("Назад", color = Color.White)
            }
        }

        // Положение текста
        if (isPositionPickerEnabled) {
            Row {
                Button(onClick = {
                    textPosition = "top"; isTextEditing = true; isPositionPickerEnabled = false
                }) {
                    Text("Сверху", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    textPosition = "bottom"; isTextEditing = true; isPositionPickerEnabled = false
                }) {
                    Text("Снизу", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { isPositionPickerEnabled = false; isTextEditing = true }) {
                Text("Назад", color = Color.White)
            }
        }

        // Шрифт текста
        if (isFontPickerEnabled) {
            Row {
                Button(onClick = {
                    textFont = "Inter"; isFontPickerEnabled = false; isTextEditing = true
                }) {
                    Text("Inter", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    textFont = "Italianno"; isFontPickerEnabled = false; isTextEditing = true
                }) {
                    Text("Italianno", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { isFontPickerEnabled = false; isTextEditing = true }) {
                Text("Назад", color = Color.White)
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

        // Кнопка для сохранения данных и кнопка мусорной корзины
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (isImageLoaded) {
                Button(
                    onClick = {
                        // Сохраняем данные (ссылка на изображение, масштаб, позиция, текст) в Firebase
                        selectedImageUri?.let { imageUri ->
                            uploadImageToCloudinary(
                                imageUri,
                                context,
                                userId
                            ) { imageUrl ->
                                if (imageUrl != null) {
                                    imageUrlForsave = imageUrl
                                    // Получаем ссылку на изображение и используем ее
                                    Log.d("ImageUpload", "Image uploaded successfully: $imageUrl")
                                    val customization = Customization(
                                        category = "tshirt", // или hoodie
                                        imageUrl = imageUrl,
                                        text = textToAdd.text,
                                        textColor = textColor.toString(),
                                        textPosition = textPosition,
                                        font = textFont
                                    )
                                    val authViewModel = AuthViewModelCart()
                                    authViewModel.addToCartWithCustomization(
                                       currentUser!!.uid.hashCode(),
                                        imageUrlForsave,
                                        1,
                                        customization,
                                        "tshirt"
                                    )
                                } else {
                                    // Обработка ошибки
                                    Log.e("ImageUpload", "Image upload failed")
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black), // Черная кнопка для сохранения
                    modifier = Modifier
                        .padding(16.dp)
                        .height(56.dp) // Увеличиваем размер кнопки
                ) {
                    Text("Сохранить и добавить в корзину", color = Color.White) // Белый текст
                }


                // Кнопка мусорной корзины
                IconButton(
                    onClick = {
                        // Сбрасываем все состояния
                        selectedImageUri = null
                        textToAdd = TextFieldValue("")
                        textColor = Color.Black
                        textPosition = "bottom"
                        textFont = "Inter"
                        isImageLoaded = false
                        isTextAdd = false
                        isColorPickerEnabled = false
                        isPositionPickerEnabled = false
                        isFontPickerEnabled = false
                        isTextEntered = false
                        isTextEditing = false
                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Удалить",
                        tint = Color(0xFFA61B1B)
                    ) // Черный цвет мусорной корзины
                }
            }
        }
    }
}




fun uploadImageToCloudinary(uri: Uri, context: Context, userId: String, callback: (String?) -> Unit) {
    // Формируем имя файла как uid пользователя
    val fileName = "$userId.jpg"  // Название файла, будет использовать uid пользователя

    // Загружаем изображение в Cloudinary
    MediaManager.get().upload(uri)
        .unsigned("kks8dyht") // Ваш unsigned upload preset
        .option("public_id", fileName) // Устанавливаем имя файла (public_id) как uid пользователя
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

                // Возвращаем ссылку на изображение через callback
                callback(imageUrl)
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                // Ошибка загрузки
                Toast.makeText(context, "Ошибка загрузки: $error", Toast.LENGTH_SHORT).show()
                callback(null)
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                // Повторная попытка загрузки
                Toast.makeText(context, "Загрузка отложена: $error", Toast.LENGTH_SHORT).show()
                callback(null)
            }
        }).dispatch() // Запускаем запрос
}
