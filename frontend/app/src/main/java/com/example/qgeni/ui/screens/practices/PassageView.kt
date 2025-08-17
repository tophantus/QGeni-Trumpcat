package com.example.qgeni.ui.screens.practices

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qgeni.R
import com.example.qgeni.ui.screens.components.CustomSolidButton
import com.example.qgeni.utils.formatTime
import com.example.qgeni.ui.theme.Nunito
import com.example.qgeni.ui.theme.QGenITheme
import kotlinx.coroutines.delay

/*
    Phần hiển thị bài đọc, thời gian
    và các chức năng highlight cho ReadingPracticeScreen
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PassageView(
    modifier: Modifier = Modifier,
    text: String,
    onBackButton: () -> Unit,
    viewModel: ReadingPracticeViewModel
) {
    val passageUIState by viewModel.uiState.collectAsState()
    val words = text.split(Regex("(?<=\\s)|(?=\\s)|(?<=\\n)|(?=\\n)|(?<=\\t)|(?=\\t)")).filter { it.isNotEmpty() }

    val lazyListState = rememberLazyListState()

    var popupPassage by remember {
        mutableStateOf(false)
    }

    val annotatedText = buildAnnotatedString {
        words.forEachIndexed { index, word ->
            if(passageUIState.highlightedIndices.contains(index) && passageUIState.isHighlightEnabled) {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        background = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    append(word)
                }
            } else {
                append(word)
            }
            addStringAnnotation(
                tag = "WORD",
                annotation = index.toString(),
                start = this.length - word.length - 1,
                end = this.length - 1
            )
        }
    }

    // Tính năng đếm thời gian
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            viewModel.updateTime()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onPrimary
            )
            .padding(
                start = 16.dp,
                end = 16.dp,
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Thời gian làm bài: ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = formatTime(passageUIState.time),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomSolidButton(
                onClick = {
                    if(!passageUIState.isComplete)
                        viewModel.toggleSubmitConfirmDialog(true)
                    else
                        onBackButton()
                },
                text = if(!passageUIState.isComplete) "NỘP BÀI" else "XONG",
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HighlightSwitch(
                isHighlightEnabled = passageUIState.isHighlightEnabled,
                onIsHighlightChange = {viewModel.toggleHighlightEnabled()}
            )
            Spacer(modifier = Modifier.width(16.dp))
            ModeSelectionSwitch(
                isHighlightMode = passageUIState.isHighlightMode,
                onIsHighlightModeChange = {viewModel.toggleHighlightMode()},
                enabledIconResId = R.drawable.highlighter,
                enabledText = "Highlighter",
                disabledText = "Eraser",
                disabledIconResId = R.drawable.eraser
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Text Passage
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(
                    top = 4.dp,
                    bottom = 4.dp,
                    start = 6.dp,
                    end = 6.dp
                )
                .clickable { popupPassage = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Passage",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val touchPosition = change.position

//                        textLayoutResult.value?.let { layoutResult ->
                        passageUIState.textLayoutResult?.let { layoutResult ->
                            // Lấy offset từ vị trí vuốt và vị trí cuộn
                            val clickedOffset = layoutResult.getOffsetForPosition(
                                Offset(
                                    x = touchPosition.x,
                                    y = touchPosition.y + lazyListState.firstVisibleItemScrollOffset
                                )
                            )

                            annotatedText
                                .getStringAnnotations("WORD", clickedOffset, clickedOffset)
                                .firstOrNull()
                                ?.let { annotation ->
                                    val index = annotation.item.toInt()
                                    if (passageUIState.isHighlightEnabled) {
                                        viewModel.updateHighlightedIndices(
                                            index,
                                            passageUIState.isHighlightMode
                                        )
                                    }
                                }
                        }
                    }
                },
            state = lazyListState
        ) {
            item {
                ClickableText(
                    text = annotatedText,
                    onClick = { offset ->
                        annotatedText.getStringAnnotations("WORD", offset, offset)
                            .firstOrNull()?.let { annotation ->
                                val index = annotation.item.toInt()
                                if(passageUIState.isHighlightEnabled) {
                                    viewModel.updateHighlightedIndices(index, passageUIState.isHighlightMode)
                                }
                            }
                    },
                    onTextLayout = { result ->
                        viewModel.updateTextLayoutResult(result)
                    },
                    style = TextStyle(
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                )
            }
        }
    }

    if (popupPassage) {
        ContentDialog(onDismissRequest = { popupPassage = false }) {
            LazyColumn {
                item {
                    Text(
                        text = annotatedText,
                        style = TextStyle(
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CustomIconSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    useIcon: Boolean,
    enabledIconResId: Int,   // Icon khi bật (on)
    disabledIconResId: Int, // Icon khi tắt (off)
    testTag: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(0.dp)
            .wrapContentSize()
    ) {
        // Custom Switch với icon
        Box(
            modifier = Modifier
                .size(60.dp, 30.dp)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.primary
                )
                .padding(4.dp)
                .clickable { onCheckedChange(!checked) }
                .testTag(testTag)
        ) {
            // Vòng tròn bên trong switch
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(
                        if (checked) Alignment.CenterEnd
                        else Alignment.CenterStart
                    )
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.secondary
                    )
            ) {
                if (useIcon) {
                    Icon(
                        painter = painterResource(id = if (checked) enabledIconResId else disabledIconResId),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@Composable
fun HighlightSwitch(
    isHighlightEnabled: Boolean,
    onIsHighlightChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(100.dp),

                )
            .padding(
                end = 8.dp
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        CustomIconSwitch(
            checked = isHighlightEnabled,
            onCheckedChange = {onIsHighlightChange(!isHighlightEnabled)},
            useIcon = false,
            enabledIconResId = R.drawable.eraser,
            disabledIconResId = R.drawable.highlighter,
            testTag = "test highlight"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "HighLight",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ModeSelectionSwitch(
    isHighlightMode: Boolean,
    onIsHighlightModeChange: (Boolean) -> Unit,
    enabledIconResId: Int = R.drawable.eraser,
    disabledIconResId: Int = R.drawable.highlighter,
    enabledText: String,
    disabledText: String
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(100.dp),
            )
            .padding(
                end = 8.dp
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        CustomIconSwitch(
            checked = isHighlightMode,
            onCheckedChange = {onIsHighlightModeChange(!isHighlightMode)},
            useIcon = true,
            enabledIconResId = enabledIconResId,
            disabledIconResId = disabledIconResId,
            testTag = "highlight mode"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isHighlightMode) enabledText
            else disabledText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}

@Preview
@Composable
fun HighlightSwitchOnPreview() {
    QGenITheme(dynamicColor = false) {
        HighlightSwitch(
            true,
            {}
        )
    }
}

@Preview
@Composable
fun ModeSelectionOffSwitchPreview() {
    QGenITheme(dynamicColor = false) {
        HighlightSwitch(
            false,
            {}
        )
    }
}

@Preview
@Composable
fun HighlightsModeSelectionSwitchPreview() {
    QGenITheme(dynamicColor = false) {
        ModeSelectionSwitch(
            true,
            {},
            enabledIconResId = R.drawable.highlighter,
            enabledText = "Highlighter",
            disabledText = "Eraser",
            disabledIconResId = R.drawable.eraser
        )
    }
}

@Preview
@Composable
fun EraserModeSelectionSwitchPreview() {
    QGenITheme(dynamicColor = false) {
        ModeSelectionSwitch(
            false,
            {},
            enabledIconResId = R.drawable.highlighter,
            enabledText = "Highlighter",
            disabledText = "Eraser",
            disabledIconResId = R.drawable.eraser
        )
    }
}

@Preview
@Composable
fun CustomSwitchOffPreview() {
    QGenITheme(dynamicColor = false) {
        CustomIconSwitch(
            checked = false,
            onCheckedChange = {},
            useIcon = true,
            enabledIconResId = R.drawable.eraser,
            disabledIconResId = R.drawable.highlighter,
            "a"
        )
    }
}

@Preview
@Composable
fun CustomSwitchOnPreview() {
    QGenITheme(dynamicColor = false) {
        CustomIconSwitch(
            checked = true,
            onCheckedChange = {},
            useIcon = true,
            enabledIconResId = R.drawable.eraser,
            disabledIconResId = R.drawable.highlighter,
            "a"
        )
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PassageLightViewPreview() {
//    QGenITheme(dynamicColor = false) {
//        PassageView(text = MockReadingPracticeItem.readingPracticeItem.passage, viewModel = viewModel())
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PassageDarkViewPreview() {
//    QGenITheme(dynamicColor = false, darkTheme = true) {
//        PassageView(text = MockReadingPracticeItem.readingPracticeItem.passage, viewModel = viewModel())
//    }
//}


