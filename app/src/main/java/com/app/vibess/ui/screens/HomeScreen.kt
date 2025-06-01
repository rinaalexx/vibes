package com.app.vibess.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.app.vibess.R
import com.app.vibess.data.model.Product
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter


@Composable
fun HomeScreen(
    onShopNowClick: () -> Unit,
    onCreateNowClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
) {
    // Категории
    val categories = listOf(
        CategoryItem("tShirt", R.drawable.tshirt),
        CategoryItem("hoodie", R.drawable.hoodie)
    )
    val scrollState = rememberScrollState()
    var isLoading by remember { mutableStateOf(true) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            scale.animateTo(
                targetValue = 1.5f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1000
                        1.5f at 500
                        1f at 1000
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {


        Spacer(Modifier.height(24.dp))

        // Баннер New Collection
        Image(
            painter = painterResource( R.drawable.new_collection_banner),
            contentDescription = "New Collection Banner",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        // Кнопка SHOP NOW
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = onShopNowClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(0xff443F3F),       // цвет текста
                    containerColor = Color.White),
                border = BorderStroke(1.dp, Color.DarkGray),
                shape = RoundedCornerShape(5.dp),
            ) {
                Text("SHOP NOW")
            }
        }

        Spacer(Modifier.height(24.dp))
//custom
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.3f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.custom_banner),
                    contentDescription = "Custom background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,

                )
                Button(
                    onClick = onCreateNowClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,       // цвет текста
                        containerColor = Color(0xff7C0000))){
                    Text("CREATE NOW")
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Горизонтальный список категорий
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // Вызов items с правильным типом - список категорий
            items(categories) { category ->
                // Передаём объект CategoryItem, а не Int
                CategoryCard(category = category, onClick = { onCategoryClick(category.name) })
            }
        }

        Spacer(Modifier.height(24.dp))

        // Сетка товаров по 2 в строке
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp) // подгоните под свой дизайн
        ) {

            }
        }

        Spacer(Modifier.height(1000.dp))

        // Блок кастомизации с кнопкой CREATE NOW

    }


// Исправленный параметр category теперь правильного типа CategoryItem
@Composable
fun CategoryCard(category: CategoryItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .fillMaxHeight()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)

    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(category.imageRes),
                contentDescription = category.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = category.name,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ProductCardSimple(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text("${product.price} ₽", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

data class CategoryItem(
    val name: String,
    val imageRes: Int
)
