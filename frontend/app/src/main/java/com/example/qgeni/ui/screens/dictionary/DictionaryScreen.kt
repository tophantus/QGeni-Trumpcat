package com.example.qgeni.ui.screens.dictionary

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.data.model.word.Example
import com.example.qgeni.data.model.word.Word
import com.example.qgeni.data.model.word.WordMeaning
import com.example.qgeni.data.model.word.WordMockData
import com.example.qgeni.data.model.word.WordType
import com.example.qgeni.ui.theme.QGenITheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DictionaryScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DictionaryViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier.fillMaxSize()
    )   {
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
                    text = "Dictionary",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExposedDropdownMenuBox(
                    expanded = uiState.expandDropdown,
                    onExpandedChange = {expanded ->
                        viewModel.setDropdownExpanded(expanded)}
                ) {
                    OutlinedTextField(
                        value = uiState.text,
                        onValueChange = {
                            viewModel.onTextChanged(it)
                        },
                        label = {
                            Text(
                                text = "Nhập từ cần tra ...",
                            )
                        },
                        maxLines = 1,
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Search,
                                "search",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        shape = RoundedCornerShape(size = 10.dp),
                        colors = OutlinedTextFieldDefaults
                            .colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                cursorColor = MaterialTheme.colorScheme.onBackground
                            ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.expandDropdown,
                        onDismissRequest = {
                            viewModel.setDropdownExpanded(false)
                        },
                        modifier = Modifier
                            .heightIn(max = 500.dp)
                    ) {
                        uiState.recommendedWord.forEach { word ->
                            DropdownMenuItem(
                                text = { Text(word) },
                                onClick = {
                                    viewModel.selectWord(word)
                                    viewModel.setDropdownExpanded(false)
                                    keyboardController?.hide()
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                            .background(color = MaterialTheme.colorScheme.onPrimary)
                    )
                } else if (uiState.showWord) {
                    WordInfoCard(
                        word = uiState.curWord ?: WordMockData.words[0],
                        onTextClicked = {
                            viewModel.selectWord(it)
                        },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Box (
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "QGENI",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 16.dp)
                .fillMaxWidth(0.2f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(4.dp)
                    .clickable(
                        onClick = {
                            viewModel.speak(Locale.UK)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.VolumeUp,
                    contentDescription = "speaker"
                )
                Text(
                    text = "UK",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(4.dp)
                    .clickable(
                        onClick = {
                            viewModel.speak(Locale.US)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.VolumeUp,
                    contentDescription = "speaker"
                )
                Text(
                    text = "US",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }
        }
    }
}

@Composable
fun WordInfoCard(
    word: Word = WordMockData.words[0],
    onTextClicked: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp),
    ) {
        item {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.titleLarge.toSpanStyle().copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(word.text)
                    }
                    withStyle(
                        style = MaterialTheme.typography.labelLarge.toSpanStyle().copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        append(" " + word.pronunciation)
                    }
                }
            )
            for (type in word.types) {
                WordTypeCard(
                    wordType = type,
                    onTextClicked = onTextClicked
                )
            }
        }
    }
}

@Composable
fun WordTypeCard(
    wordType: WordType = WordMockData.words[0].types[0],
    onTextClicked: (String) -> Unit = {}
) {
    Text(
        text = wordType.text,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleLarge,
        textDecoration = TextDecoration.Underline
    )
    for (wm in wordType.meanings) {
        WordMeaningCard(
            wordMeaning = wm,
            onTextClicked = onTextClicked
        )
    }
}

@Composable
fun WordMeaningCard(
    wordMeaning: WordMeaning = WordMockData.words[0].types[0].meanings[0],
    onTextClicked: (String) -> Unit = {}
) {
    Row {
        Icon(
            Icons.Outlined.PlayArrow,
            "meaning",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = wordMeaning.text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
    for (ex in wordMeaning.examples) {
        ExampleCard(
            example = ex,
            onTextClicked = onTextClicked
        )
    }
}

@Composable
fun ExampleCard(
    example: Example = WordMockData.words[0].types[0].meanings[0].examples[0],
    onTextClicked: (String) -> Unit = {}
) {
    UnderlinedClickableSentence(
        sentence = example.src,
        underlineColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
    ) { clickedText ->
        onTextClicked(clickedText)
    }

    Text(text = example.tgt)
}

@Composable
fun UnderlinedClickableSentence(
    sentence: String,
    underlineColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onWordClick: (String) -> Unit
) {
    val words = sentence.split(" ")

    val annotatedText = buildAnnotatedString {
        words.forEachIndexed {index, word ->
            val tag = "WORD_$index"
            val cleanedWord = cleanWord(word)
            pushStringAnnotation(tag, cleanedWord)
            withStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    color = textColor,
                    shadow = Shadow(underlineColor, offset = Offset(0f, 2f), blurRadius = 0f)
                )
            ) {
                append(word)
            }
            pop()

            if (index < words.size - 1) append(" ")
        }
    }
    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.bodyLarge,
        onClick = { offset ->
            words.forEachIndexed { index, word ->
                val tag = "WORD_$index"
                annotatedText.getStringAnnotations(tag, offset, offset)
                    .firstOrNull()?.let {
                        val cleanedWord = cleanWord(it.item)
                        onWordClick(cleanedWord)
                        return@ClickableText
                    }
            }
        }
    )
}

private fun cleanWord(word: String): String {
    return word.trim().trimEnd('.', ',', '?', '!', '"', '|', '\\', '/',':', ';')
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ExampleLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        ExampleCard(
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun WordMeaningLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        WordMeaningCard(

        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun WordTypeLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        WordTypeCard(
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun WordInfoLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        WordInfoCard(
        )
    }
}






@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = false)
@Composable
fun DictionaryLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        DictionaryScreen(
            onBackClick = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DictionaryDarkScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        DictionaryScreen(
            onBackClick = {}
        )
    }
}

