package com.foryou.examplegit.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorLayout(errorInfo: String, errorBtn: String, onClicked: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = errorInfo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle.Default
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                onClicked()
            }) {
                Text(text = errorBtn)
            }
        }
    }
}