package com.app.vibess.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.vibess.ui.components.custom.ImagePicker
import com.app.vibess.ui.components.custom.TShirtCustomizer
import com.app.vibess.ui.components.custom.TShirtCustomizerWithGestures

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CustomizationScreen(navController: NavController) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImagePicker { uri ->
            selectedImageUri = uri
        }

        Spacer(modifier = Modifier.height(24.dp))

        TShirtCustomizerWithGestures(userImageUri = selectedImageUri)
    }
}
