package com.example.qgeni.ui.screens.leaderboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.R
import com.example.qgeni.data.model.achievement.Achievement
import com.example.qgeni.ui.screens.components.ExpandableImageWithHandle
import com.example.qgeni.ui.screens.practices.ListeningPracticeListViewModel
import com.example.qgeni.ui.screens.practices.OpenConfirmDialog
import com.example.qgeni.ui.screens.practices.PracticeItemCard
import com.example.qgeni.ui.screens.uploads.ErrorScreen
import com.example.qgeni.ui.theme.QGenITheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
    Hiển thị danh sách đề nghe
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeaderboardListScreen(
    onBackClick: () -> Unit,
    onItemClick: (List<Achievement>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LeaderboardListViewModel = viewModel()
) {

    val scope = rememberCoroutineScope()
    val uiState by viewModel.leaderboardListUIState.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onPrimary
            )
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .fillMaxWidth()
                .background(
                    color = Color.Transparent
                ),
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
            modifier = modifier
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExpandableImageWithHandle(
                imageResourceId = R.drawable.backpack2,
                maxHeight = 400.dp
            )

            if (uiState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .background(color = MaterialTheme.colorScheme.onPrimary)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .background(color = MaterialTheme.colorScheme.onPrimary),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (uiState.practiceItemList.isEmpty()) {
                        item {
                            Text(
                                text = "Chưa có đề nghe nào!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(16.dp)
                            )
                        }
                    } else {
                        items(uiState.practiceItemList.size) { idx ->
                            PracticeItemCard(
                                practiceItem = uiState.practiceItemList[idx],
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            scope.launch {
                                                try {
                                                    val list = viewModel.getLeaderboard(uiState.practiceItemList[idx].id)
                                                    onItemClick(list)
                                                } catch (e: Exception) {
                                                    println(e.message)
                                                    viewModel.setError("error")
                                                }
                                            }
                                        }
                                    )
                                    .testTag("open dialog")
                            )
                        }
                    }
                }
            }
        }
    }


    if (uiState.error != null) {
        ErrorScreen(
            onDismissRequest = { viewModel.removeError() },
            onLeaveButtonClick = { viewModel.reload() },
            message = "Vui lòng thử lại",
        )
    }
}