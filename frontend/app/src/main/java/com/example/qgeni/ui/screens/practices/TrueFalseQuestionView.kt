package com.example.qgeni.ui.screens.practices

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.collectAsState
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
import com.example.qgeni.data.model.McqQuestion
import com.example.qgeni.data.preferences.ThemeMode
import com.example.qgeni.ui.theme.QGenITheme
import com.example.qgeni.utils.ContextConstants

/*
    Phần hiển thị câu hỏi và trả lời cho ReadingPracticeScreen
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrueFalseQuestionView(
    questions: List<McqQuestion>,
    modifier: Modifier = Modifier,
    viewModel: ReadingPracticeViewModel,
) {

    var showExplanation by remember {
        mutableStateOf(false)
    }


    val uiState by viewModel.uiState.collectAsState()
    var color: Color = Color.Black
    if(uiState.isComplete) {
        color = if (uiState.correctAnswerList[uiState.currentQuestionIndex]) {
            MaterialTheme.colorScheme.onError
        } else {
            MaterialTheme.colorScheme.error
        }
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
                text = "Question ${uiState.currentQuestionIndex + 1}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
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
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = questions[uiState.currentQuestionIndex].question,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (!uiState.isComplete)
                                MaterialTheme.colorScheme.onBackground
                            else color,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (uiState.isComplete) {
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
                                    tint = MaterialTheme.colorScheme.primary
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
                                    text = questions[uiState.currentQuestionIndex].explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                        }
                    }

                    if(uiState.isComplete) {
//                if (true) {
//                    Column {
                        Icon(
                            imageVector = if (uiState.correctAnswerList[uiState.currentQuestionIndex]) {
                                Icons.Default.Done
                            } else {
                                Icons.Default.Close
                            },
                            contentDescription = null,
                            tint = color,
                        )

//                    }

                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // LazyColumn hiển thị câu hỏi hiện tại
            item {
                Column(
                    modifier = Modifier
                        .background(color = Color.Transparent)
                        .fillMaxWidth()
                ) {
                        // Hiển thị các lựa chọn True/False/Not Given
                    questions[uiState.currentQuestionIndex].answerList.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
//                                selectedAnswer = option
//                                answeredQuestions[currentQuestionIndex] = option
                                    viewModel.updateSelectAnswer(option)
                                    viewModel.updateAnsweredQuestions(
                                        uiState.currentQuestionIndex,
                                        option
                                    )
                                }
                                .height(30.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                enabled = !uiState.isComplete,
                                selected = uiState.selectAnswer == option,
                                onClick = {
                                    val newSelected =
                                        if (uiState.selectAnswer == option) null else option
                                    viewModel.updateSelectAnswer(newSelected)
                                    viewModel.updateAnsweredQuestions(
                                        uiState.currentQuestionIndex,
                                        newSelected
                                    )
                                },
                                colors = RadioButtonDefaults.colors(
                                    unselectedColor = MaterialTheme.colorScheme.tertiary,
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    disabledSelectedColor = MaterialTheme.colorScheme.primary,
                                    disabledUnselectedColor = if (uiState.isComplete &&
                                        uiState.readingPracticeItem?.questionList?.get(uiState.currentQuestionIndex)?.answer.toString() == option
                                    )
                                        Color.Red else MaterialTheme.colorScheme.tertiary,
                                )
                            )
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
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
            itemsIndexed(questions) { index, _ ->
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
                                uiState.currentQuestionIndex == index -> MaterialTheme.colorScheme.surfaceContainerHigh // Style cho câu hỏi đang chọn
                                uiState.answeredQuestions.containsKey(index) -> MaterialTheme.colorScheme.primary
                                else -> Color.Transparent
                            }
                        )
                        .clickable {
//                            currentQuestionIndex = index
//                            selectedAnswer = answeredQuestions[index]
                            viewModel.updateCurrentQuestionIndex(index)
                            viewModel.updateSelectAnswer(uiState.answeredQuestions[index])

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = when {
                            uiState.currentQuestionIndex == index -> MaterialTheme.colorScheme.onBackground // Style cho câu hỏi đang chọn
                            uiState.answeredQuestions.containsKey(index) -> MaterialTheme.colorScheme.onPrimary
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TrueFalseQuestionLightViewPreview() {
    QGenITheme(dynamicColor = false) {
        TrueFalseQuestionView(
            questions = listOf(
                McqQuestion(
                    question = "Choose the correct picture for the most correct answer, if it is no correct one, just randomly pick",
                    answerList = listOf(
                        "True",
                        "False",
                        "Not Given"
                    ),
                    explanation = "This must be a very long explanation to show if it works well or not"
                )
            ),
            viewModel = ReadingPracticeViewModel(1)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TrueFalseQuestionDarkViewPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        TrueFalseQuestionView(
            questions = listOf(
                McqQuestion(
                    question = "Choose the correct picture",
                    answerList = listOf(
                        "True",
                        "False",
                        "Not Given"
                    ),
                    explanation = "haha"
                )
            ),
            viewModel = ReadingPracticeViewModel(1)
        )
    }
}