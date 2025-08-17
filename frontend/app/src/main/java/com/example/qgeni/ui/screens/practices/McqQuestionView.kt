package com.example.qgeni.ui.screens.practices

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qgeni.R
import com.example.qgeni.ui.theme.QGenITheme

/*
    Phần hiển thị câu hỏi và trả lời cho ListeningPracticeScreen
 */

@Composable
fun McqQuestionView(
    modifier: Modifier = Modifier,
    currentQuestionIdx: Int,
    numQuestion: Int,
    questionContent: String,
    answerList: List<String>,
    caption: String = "",
    answeredQuestions: Map<Int, Int?>,
    correctAnswerIdx: Int?,
    onQuestionChange: (Int) -> Unit,
    onAnswerSelected: (Int) -> Unit,
) {
    val selectedAnswerIdx = answeredQuestions[currentQuestionIdx]

    val correct = selectedAnswerIdx == correctAnswerIdx

    var showExplanation by remember {
        mutableStateOf(false)
    }

    val completedColor: Color = if (correct) {
            MaterialTheme.colorScheme.onError
        } else {
            MaterialTheme.colorScheme.error
        }


    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.onPrimary)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(
                    top = 4.dp,
                    bottom = 4.dp,
                    start = 6.dp,
                    end = 6.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Question ${currentQuestionIdx + 1}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // LazyColumn hiển thị câu hỏi hiện tại
        Column(
            modifier = Modifier
                .background(color = Color.Transparent)
                .fillMaxWidth()
                .weight(0.7f)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp))

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = questionContent,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (correctAnswerIdx == null)
                            MaterialTheme.colorScheme.onBackground
                        else completedColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (correctAnswerIdx != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Giải thích",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    showExplanation = !showExplanation
                                }
                                    .padding(end = 4.dp)
                            )

                            Icon(
                                imageVector = if (showExplanation) {
                                    Icons.Default.KeyboardArrowUp
                                } else {
                                    Icons.Default.KeyboardArrowDown
                                },
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    showExplanation = !showExplanation
                                },
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        AnimatedVisibility(
                            visible = showExplanation,
                            enter = expandVertically(),
                            exit = shrinkVertically(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Text(
                                text = caption,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier.padding(4.dp)
                            )
                        }

                    }
                }

                if (correctAnswerIdx != null) {
                    Icon(
                        imageVector = if (correct) {
                            Icons.Default.Done
                        } else {
                            Icons.Default.Close
                        },
                        contentDescription = null,
                        tint = completedColor,
                    )
                }
            }


            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .fillMaxWidth()
            ) {
                item {
                    // Hiển thị các lựa chọn
                    answerList.forEachIndexed { answerIdx,  option ->
                        val selected = answerIdx == selectedAnswerIdx
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onAnswerSelected(answerIdx)
                                }
                                .height(30.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                selected = selected,
                                enabled = correctAnswerIdx == null,
                                onClick = {
                                    onAnswerSelected(answerIdx)
                                },
                                colors = RadioButtonDefaults.colors(
                                    unselectedColor = MaterialTheme.colorScheme.tertiary,
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    disabledSelectedColor = if (correctAnswerIdx != null && selected) {
                                        completedColor
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    },
                                    disabledUnselectedColor = MaterialTheme.colorScheme.tertiary,
                                )
                            )

                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (correctAnswerIdx != null && selected) {
                                    completedColor
                                } else {
                                    MaterialTheme.colorScheme.onBackground
                                },
                            )

                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị các ô câu hỏi bên dưới bằng LazyRow
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp)),
        ) {
            items(numQuestion) { index ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            when {
                                currentQuestionIdx == index -> MaterialTheme.colorScheme.surfaceContainerHigh   //Style cho câu hỏi đang chọn
                                answeredQuestions[index] != null -> MaterialTheme.colorScheme.primary
                                else -> Color.Transparent
                            }
                        )
                        .clickable {
                            onQuestionChange(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = when {
                            currentQuestionIdx == index -> MaterialTheme.colorScheme.onBackground   //Style cho câu hỏi đang chọn
                            answeredQuestions[index] != null -> MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.tertiary
                        }
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MsqQuestionLightViewPreview() {
    QGenITheme(dynamicColor = false) {
        McqQuestionView(
            questionContent = "What is the capital of France?",
            answerList = listOf("Paris", "London", "Berlin", "Madrid"),
            numQuestion = 2,
            currentQuestionIdx = 0,
            onQuestionChange = {},
            onAnswerSelected = {},
            answeredQuestions = mapOf(0 to 1),
            correctAnswerIdx = 1
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MsqQuestionDarkViewPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {

        McqQuestionView(
            questionContent = "What is the capital of France?",
            answerList = listOf("Paris", "London", "Berlin", "Madrid"),
            numQuestion = 3,
            currentQuestionIdx = 0,
            onQuestionChange = {},
            onAnswerSelected = {},
            answeredQuestions = mapOf(0 to 0),
            correctAnswerIdx = null
        )
    }
}