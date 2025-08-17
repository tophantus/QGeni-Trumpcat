package com.example.qgeni.ui.screens.practices

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.R
import com.example.qgeni.ui.screens.uploads.ErrorScreen
import com.example.qgeni.ui.screens.uploads.LoadingScreen
import com.example.qgeni.utils.formatTime

/*
    Màn hình thực hiện đề nghe, gồm ImageQuestionView và McQuestionView
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListeningPracticeScreen(
    id: Int,
    onBackClick: () -> Unit,
    onNavigatingToPracticeRepo: () -> Unit,
    viewModel: ListeningPracticeViewModel =
        viewModel(factory = ListeningPracticeViewModel.factory(id))
) {

    val uiState by viewModel.uiState.collectAsState()

    var showPassage by remember {
        mutableStateOf(true)
    }

    val imageViewWeight by animateFloatAsState(
        if (showPassage) 1f else 0.2f, label = "",
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
                text = "Listening",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        ImageQuestionView(
            isCompleted = uiState.isCompleted,
            currentQuestion = uiState.currentQuestionIndex,
            timeString = formatTime(uiState.time),
            imageList = uiState.imageList,
            imageLabelList = List(uiState.imageList.size) { index ->
                "Pic. " + ('A' + index)
            },
            modifier = Modifier.weight(imageViewWeight).padding(bottom = 4.dp),
            onPlayClick = {
                viewModel.play()
            },
            onSubmitClick = {
                if (uiState.isCompleted) {
                    onNavigatingToPracticeRepo()
                } else {
                    viewModel.toggleSubmitConfirmDialog(true)
                }
            },
            playbackState = uiState.playbackState,
            sliderPosition = uiState.audioSliderPos,
            duration = uiState.audioDuration,
            onSliderPositionChange = viewModel::updateAudioSliderPos,
            onValueChangeFinished = {
                viewModel.seekTo()
            }
        )

        Box(
            modifier = Modifier
                .height(20.dp)
                .width(30.dp)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { showPassage = !showPassage },
            ) {
                Icon(
                    imageVector = if (showPassage)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        if (uiState.questionList.isEmpty()) {
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
        } else {
            McqQuestionView(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(
                        top = 4.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                questionContent = uiState.questionList[uiState.currentQuestionIndex].question,
                answerList = uiState.questionList[uiState.currentQuestionIndex].answerList,
                caption = uiState.questionList[uiState.currentQuestionIndex].explanation,
                numQuestion = uiState.questionList.size,
                currentQuestionIdx = uiState.currentQuestionIndex,
                answeredQuestions = uiState.answeredQuestions,
                correctAnswerIdx = if (uiState.correctAnswerIds.isEmpty()) {
                    null
                } else {
                    uiState.correctAnswerIds[uiState.currentQuestionIndex]
                },
                onQuestionChange = { index ->
                    viewModel.updateCurrentQuestionIndex(index)
                },
                onAnswerSelected = { answer ->
                    viewModel.updateAnsweredQuestions(uiState.currentQuestionIndex, answer)
                },
            )
        }
    }

    if (uiState.showSubmitConfirmDialog) {
        SubmitConfirm(
            onDismissRequest = {
                viewModel.toggleSubmitConfirmDialog(false)
            },
            onSubmitClick = {
                viewModel.toggleScoreDialog(true)
            },
            imageResourceId = R.drawable.listening_submit_confirm
        )
    }

    if (uiState.showScoreDialog) {
        DisplayScore(
            message = uiState.scoreMessage,
            onNextButtonClick = onNavigatingToPracticeRepo,
            onDismissRequest = {
                viewModel.toggleScoreDialog(false)
            },
            imageResourceId = R.drawable.listening_open_delete_confirm
        )
    }

    if (uiState.showLoadingDialog) {
        LoadingScreen(
            message = "Đang tải đề...",
        )
    }

    if (uiState.error != null) {
        ErrorScreen(
            onDismissRequest = { viewModel.removeError() },
            onLeaveButtonClick = { viewModel.reload() },
            message = "Vui lòng thử lại",
        )

    }

}
