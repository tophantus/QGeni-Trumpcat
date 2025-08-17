package com.example.qgeni.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qgeni.ui.screens.components.NextButton
import com.example.qgeni.ui.theme.QGenITheme

@Composable
fun WelcomeScreen(
    onNextButtonClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        PageChanger(
            time = 4000L,
            modifier = Modifier.weight(1f)
        )
        Row {
            Spacer(modifier = Modifier.weight(2f))
            NextButton(
                onPrimary = true,
                onClick = {
                    onNextButtonClick()
                }
            )
            Spacer(modifier = Modifier.weight(0.25f))
        }
        Spacer(modifier = Modifier.height(56.dp))
    }
}



@Preview(showSystemUi = true)
@Composable
fun WelcomeLightPreview() {
    QGenITheme(dynamicColor = false) {
        WelcomeScreen({})
    }
}

@Preview(showSystemUi = true)
@Composable
fun WelcomeDarkPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        WelcomeScreen({})
    }
}

