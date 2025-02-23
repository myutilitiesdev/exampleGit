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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foryou.examplegit.R

@Composable
fun ErrorLayout(onClicked: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.error_info),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle.Default
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                onClicked()
            }) {
                Text(text = stringResource(R.string.error_refresh))
            }
        }
    }
}