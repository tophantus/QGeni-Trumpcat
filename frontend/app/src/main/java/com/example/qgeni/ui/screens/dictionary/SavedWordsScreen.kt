package com.example.qgeni.ui.screens.dictionary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.R
import com.example.qgeni.ui.screens.components.CustomOutlinedButton
import com.example.qgeni.ui.screens.components.CustomSolidButton
import com.example.qgeni.ui.screens.components.ExpandableImageWithHandle
import com.example.qgeni.ui.screens.uploads.ErrorScreen
import com.example.qgeni.ui.screens.uploads.LoadingScreen
import com.example.qgeni.ui.theme.QGenITheme

/*
    Màn hình hiển thị danh sách đề đọc
 */
@OptIn(ExperimentalStdlibApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SavedWordsScreen(
    onBackClick: () -> Unit,
    onWordItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavedWordsViewModel = viewModel()
) {

    val uiState by viewModel.savedWordListUIState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                    text = "Từ đã tra",
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
                            .padding(8.dp)
                            .background(color = MaterialTheme.colorScheme.onPrimary)
                    ) {
                        if (uiState.savedWordList.isEmpty()) {
                            item {
                                Text(
                                    text = "Chưa có từ nào!",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(16.dp)
                                )
                            }
                        } else {
                            items(uiState.savedWordList.size) { idx ->
                                WordCard(
                                    word = uiState.savedWordList[idx],
                                    onFavoriteClick = {
                                        print("hahaha")
                                        viewModel.onFavoriteWordsChange(uiState.savedWordList[idx].id)

                                        println("favorite clicked")
                                    },
                                    onCheckBoxClick = { check ->
                                        viewModel.onSelectedWordsChange(uiState.savedWordList[idx].id, check)
                                        println("checkbox clicked")
                                    },
                                    checked = viewModel.isSelected(uiState.savedWordList[idx].id),
                                    isFavorite = uiState.savedWordList[idx].isFavorite,
                                    onItemCLick = {
                                        onWordItemClick(it)
                                    },
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
        if (uiState.showDialog) {
            ActionDialog(
                onDismissRequest = {
                    viewModel.removeAllSelectedWordId()
                },
                onDeleteClick = {
                    viewModel.deleteAllSelectedWords()
                },
                onFavoriteClick = {
                    viewModel.addAllSelectedWordToFavorite()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f)
            )
        }
    }
    if (uiState.loading) {
        LoadingScreen(message = "Vui lòng đợi")
    }
    if (uiState.error != null) {
        ErrorScreen(
            onDismissRequest = { viewModel.removeError() },
            onLeaveButtonClick = { viewModel.reload() },
            message = "Vui lòng thử lại",
        )

    }

}

@Composable
fun ActionDialog(
    onDeleteClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 50.dp
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(10.dp)
                )
            ,
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                CustomOutlinedButton(
                    onClick = onDismissRequest,
                    text = "HỦY",
                    color = MaterialTheme.colorScheme.tertiary
                )
                CustomSolidButton(
                    onClick = onDeleteClick,
                    text = "XÓA",
                    textColor = MaterialTheme.colorScheme.primary,
                    bgColor = MaterialTheme.colorScheme.onPrimary
                )
                CustomSolidButton(
                    onClick = onFavoriteClick,
                    text = "YÊU THÍCH",
                    textColor = MaterialTheme.colorScheme.primary,
                    bgColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ActionDialogLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        Surface (
            modifier = Modifier.fillMaxSize()
        ) {
            ActionDialog(
                onFavoriteClick = {},
                onDeleteClick = {},
                onDismissRequest = {}
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ReadingPracticeListLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        SavedWordsScreen(
            onBackClick = {},
            {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReadingPracticeListDarkScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        SavedWordsScreen(
            onBackClick = {},
            {}
        )
    }
}

