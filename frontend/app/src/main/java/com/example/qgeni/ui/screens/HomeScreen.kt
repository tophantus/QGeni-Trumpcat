package com.example.qgeni.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qgeni.R
import com.example.qgeni.ui.theme.QGenITheme

@Composable
fun HomeScreen(
    onAvatarClick: () -> Unit,
    onDictionaryClick: () -> Unit,
    onBackpackClick: () -> Unit,
    onGameClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primary
            ),
    ) {
        Image(
            painter = painterResource(R.drawable.fairy_with_a_girl),
            contentDescription = "fairy_and_a_girl"
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(shape = CircleShape)
                    .clickable(
                        onClick = onAvatarClick
                    )
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = CircleShape
                    )
            ) {
                Image(
                    painter = painterResource(R.drawable.avatar_2),
                    contentDescription = "avatar"
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column() {
            ItemCard(
                imageId = R.drawable.reading,
                title = "Từ điển",
                onClick = onDictionaryClick
            )
            Spacer(modifier = Modifier.height(32.dp))
            ItemCard(
                imageId = R.drawable.backpack1,
                title = "Balo đề",
                onClick = onBackpackClick
            )
            Spacer(modifier = Modifier.height(32.dp))
            ItemCard(
                imageId = R.drawable.avatar_3,
                title = "Trò chơi",
                onClick = onGameClick
            )
        }
        Spacer(modifier = Modifier.weight(0.4f))

    }
}

@Composable
fun ItemCard(
    @DrawableRes
    imageId: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                onClick = onClick
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(10.dp)
            )
            .width(300.dp)
            .clip(
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer
            )
            .padding(8.dp)

    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(shape = RoundedCornerShape(size = 10.dp))
        ) {
            Image(
                painter = painterResource(imageId),
                contentDescription = "avatar"
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun ItemCardPreview() {
    QGenITheme(dynamicColor = false) {
        ItemCard(
            imageId = R.drawable.reading,
            title = "Bài đọc",
            {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(
                onAvatarClick = {},
                {},
                onBackpackClick = {},
                {}
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeDarkScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(
                onAvatarClick = {},
                {},
                onBackpackClick = {},
                {}
            )
        }
    }
}