package com.example.qgeni.ui.screens.practices

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.R
import com.example.qgeni.data.model.McqQuestion
import com.example.qgeni.ui.screens.uploads.ErrorScreen
import com.example.qgeni.ui.screens.uploads.LoadingScreen
import com.example.qgeni.ui.theme.QGenITheme

/*
    Màn hình thực hiện đề đọc
    gồm PassageView và TrueFalseQuestionView
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReadingPracticeScreen(
    id: Int,
    onNextButtonClick: () -> Unit,
    onBackClick: () -> Unit,
    readingPracticeViewModel: ReadingPracticeViewModel =
        viewModel(factory = ReadingPracticeViewModel.factory(id))
) {

    val uiState by readingPracticeViewModel.uiState.collectAsState()
    var shrinkPassage by remember {
        mutableStateOf(false)
    }

    val passageWeight by animateFloatAsState(
        if (shrinkPassage) 0.2f else 1f, label = "",
        animationSpec = spring()
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
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
                text = "Reading",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        val passage = uiState.readingPracticeItem?.passage ?: ""
        val questionList = uiState.readingPracticeItem?.questionList ?: emptyList()

        val passageContent = @Composable {
            PassageView(
                text = passage,
                modifier = Modifier
                    .weight(passageWeight)
                    .padding(bottom = 4.dp)
                    .animateContentSize(),
                onBackButton = onNextButtonClick,
                viewModel = readingPracticeViewModel
            )
        }

        passageContent()


        Box(
            modifier = Modifier
                .height(20.dp)
                .width(30.dp)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { shrinkPassage = !shrinkPassage },
            ) {
                Icon(
                    imageVector = if (shrinkPassage)
                        Icons.Default.KeyboardArrowDown
                    else
                        Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }


        if (questionList.isEmpty()) {
            Box( modifier = Modifier
                .padding(
                    top = 4.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .background(color = Color.Transparent)
                .fillMaxWidth()
                .weight(0.7f)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
            )
            return
        }

        TrueFalseQuestionView(
            questions = questionList.map {
                McqQuestion(
                    question = it.statement,
                    answerList = listOf(
                        "TRUE",
                        "FALSE",
                        "NOT GIVEN"
                    ),
                    explanation = it.explanation
                )
            },
            modifier = Modifier
                .weight(0.7f)
                .padding(
                    top = 4.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            viewModel = readingPracticeViewModel
        )
    }

    if (uiState.error != null) {
        ErrorScreen(
            onDismissRequest = { readingPracticeViewModel.removeError() },
            onLeaveButtonClick = { readingPracticeViewModel.reload() },
            message = "Vui lòng thử lại",
        )
    }

    if (uiState.loading) {
        LoadingScreen(
            message = "Đang tải đề...",
        )
    }


    if (uiState.showSubmitConfirmDialog) {
        SubmitConfirm(
            onDismissRequest = {
                readingPracticeViewModel.toggleSubmitConfirmDialog(false)
            },
            onSubmitClick = {
                readingPracticeViewModel.toggleSubmitConfirmDialog(false)
                readingPracticeViewModel.submit()
            },
            imageResourceId = R.drawable.reading_submit_confirm
        )
    }

    if (uiState.showScoreDialog) {
        DisplayScore(
            message = uiState.scoreMessage,
            onNextButtonClick = {
                readingPracticeViewModel.toggleScoreDialog(false)
                onNextButtonClick()
            },
            onDismissRequest = {
                readingPracticeViewModel.toggleScoreDialog(false)
                readingPracticeViewModel.updateIsComplete(true)
            },
            imageResourceId = R.drawable.reading_open_delete_confirm
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ReadingPracticeScreenPreview() {
    QGenITheme(dynamicColor = false) {
         ReadingPracticeScreen(
            id = 1,
             {},
            onBackClick = { }, readingPracticeViewModel = ReadingPracticeViewModel(1)
        )
    }
}

