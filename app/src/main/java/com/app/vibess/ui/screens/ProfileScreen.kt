package com.app.vibess.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    // Получаем текущего пользователя
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Проверяем, если пользователь не найден, перенаправляем на экран входа
    if (currentUser == null) {
        LaunchedEffect(Unit) {
            navController.navigate("login") // Перенаправление на экран входа
        }
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            // Отображаем почту пользователя
            Text("Email: ${currentUser.email ?: "No email available"}")

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка выхода
            Button(onClick = {
                // Выполняем выход
                authViewModel.signout()
                navController.navigate("login") // Переход на экран входа после выхода
            }) {
                Text("Sign Out")
            }
        }
    }
}
