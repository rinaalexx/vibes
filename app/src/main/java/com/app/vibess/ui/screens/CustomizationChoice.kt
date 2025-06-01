package com.app.vibess.ui.screens

import android.service.carrier.MessagePdu
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.vibess.R

@Composable
fun CustomizationChoice(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFE9E9E9)) // Убедитесь, что указано значение цвета
    ) {

        Spacer(Modifier.height(24.dp))

        // Баннер New Collection
        Image(
            painter = painterResource(R.drawable.custom_banner),
            contentDescription = "New Collection Banner",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))
        Text(
            text = "WHAT YOU WANT TO CUSTOM?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

      /*  // Первая кнопка с изображением
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Высота кнопки
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB0B0B0)
            ),
            content = {
                Box(
                    modifier = Modifier.fillMaxSize() // Контейнер для изображения
                ) {
                    Image(
                        painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxdspcnk6/image/upload/v1748699818/hoodie_ls0sao.png"),
                        contentDescription = "hoodie",
                        modifier = Modifier
                            .fillMaxWidth() // Изображение на всю ширину
                            .height(200.dp) // Высота изображения должна быть равна высоте кнопки
                    )
                }
            }
        )

        // Вторая кнопка с изображением
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), // Высота кнопки
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB0B0B0)
            ),
            content = {
                Box(
                    modifier = Modifier.fillMaxSize() // Контейнер для изображения
                ) {
                    Image(
                        painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxdspcnk6/image/upload/v1748125075/custom_thirt_cqlrbc.png"),
                        contentDescription = "tshirt",
                        modifier = Modifier
                            .fillMaxWidth() // Изображение на всю ширину
                            .height(200.dp) // Высота изображения
                    )
                }
            }
        )
*/

        // Отображаем категории с картинками
        CustomItem(
            categoryName = "Футболки",
            routeName = "tshirt",
            imageUrl = "https://res.cloudinary.com/dxdspcnk6/image/upload/v1748700951/tshirtcustom_nrveie.png", // Ваш URL для футболок
            navController = navController
        )
        Spacer(modifier = Modifier.height(20.dp))

        CustomItem(
            categoryName = "Худи",
            routeName = "hoodie",
            imageUrl = "https://res.cloudinary.com/dxdspcnk6/image/upload/v1748700950/hoodiecustom_bzioiz.png", // Ваш URL для худи
            navController = navController
        )
    }
}
@Composable
fun CustomItem(
    categoryName: String,
    routeName: String,
    imageUrl: String,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { navController.navigate("custom/$routeName") },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF)),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Вставляем изображение с Cloudinary
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = categoryName,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )


        }
    }
}



