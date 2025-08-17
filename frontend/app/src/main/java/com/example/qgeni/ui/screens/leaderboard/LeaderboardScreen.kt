package com.example.qgeni.ui.screens.leaderboard

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.data.model.achievement.Achievement
import com.example.qgeni.ui.screens.dictionary.DictionaryScreen
import com.example.qgeni.ui.screens.uploads.ErrorScreen
import com.example.qgeni.ui.theme.OceanEyes
import com.example.qgeni.ui.theme.OldWomen
import com.example.qgeni.ui.theme.PastelGreen
import com.example.qgeni.ui.theme.QGenITheme
import com.example.qgeni.ui.theme.Soul
import com.example.qgeni.ui.theme.YoungPuppy

@Composable
fun LeaderboardScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LeaderboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val top3 = uiState.achievements.take(3).sortedBy { it.top }
    val others = uiState.achievements.drop(3)


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "BackIcon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.weight(0.7f))
            Text(
                text = "Leaderboard",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp)
        ) {
            Text(
                "🏆 Leaderboard",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Top 3 Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                top3.getOrNull(1)?.let {
                    TopUserItem(it, uiState.currentUserId)
                }
                top3.getOrNull(0)?.let {
                    TopUserItem(it, uiState.currentUserId, isCenter = true)
                }
                top3.getOrNull(2)?.let {
                    TopUserItem(it, uiState.currentUserId)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Others
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(others) {user ->
                    UserItem(user, uiState.currentUserId)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun TopUserItem(
    user: Achievement,
    currentUserId: Int,
    isCenter: Boolean = false
) {
    val df = DecimalFormat("#.#")
    val bgColor = when (user.top) {
        1 -> MaterialTheme.colorScheme.primary//Color(0xFFFFD700) // Vàng
        2 -> YoungPuppy//Color(0xFFC0C0C0) // Bạc
        3 -> OldWomen//Color(0xFFCD7F32) // Đồng
        else -> Color.LightGray
    }

    val width = if (user.top == 1) 120.dp else 80.dp
    val height = if (user.top == 1) 140.dp else 100.dp

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .drawBehind {
                val notchHeight = 30f

                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    lineTo(size.width * 0.5f, size.height - notchHeight)
                    lineTo(0f, size.height)
                    close()
                }

                drawPath(path = path, color = bgColor)
            }
            .drawBehind {
                val notchHeight = 30f
                val strokeWidth = 4.dp.toPx()
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    lineTo(size.width * 0.5f, size.height - notchHeight)
                    lineTo(0f, size.height)
                    close()
                }

                if (user.userId == currentUserId) {
                    drawPath(
                        path = path,
                        color = PastelGreen,
                        style = Stroke(width = strokeWidth)
                    )
                }
            }
        ,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            if (user.top == 1) {
                Text("👑",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            if (user.top != 1) {
                Text(
                    text = when (user.top) {
                        2 -> "🥈"
                        3 -> "🥉"
                        else -> "${user.top}"
                    },
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text(
                user.name,
                style = if (user.top == 1 ) MaterialTheme.typography.headlineMedium
                else MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Text(
                "${df.format(user.score)} pts",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
            Text(
                "${user.time}s",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }
    }
}

@Composable
fun UserItem(user: Achievement, currentUserId: Int) {
    val borderModifier = if (user.userId == currentUserId) {
        Modifier.border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp))
    } else Modifier

    val df = DecimalFormat("#.#")
    val bgColor = when (user.top) {
        in 4..10 -> MaterialTheme.colorScheme.tertiaryContainer
        in 11..20 -> MaterialTheme.colorScheme.tertiary
        else -> Soul
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(8.dp))
            .then(borderModifier)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${user.top}.",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.width(30.dp)
        )
        Column {
            Text(
                user.name,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Black
            )
            Text(
                "Score: ${df.format(user.score)} - Time: ${user.time}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun LeaderboardLightScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = false) {
        LeaderboardScreen(
            {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun LeaderboardDarkScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        LeaderboardScreen(
            {}
        )
    }
}


