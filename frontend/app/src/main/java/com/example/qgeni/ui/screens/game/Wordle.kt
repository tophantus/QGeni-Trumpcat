package com.example.qgeni.ui.screens.game

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qgeni.AppContextHolder
import com.example.qgeni.BuildConfig
import com.example.qgeni.data.model.achievement.Achievement
import com.example.qgeni.data.preferences.JwtPreferenceManager
import com.example.qgeni.ui.screens.components.CustomSolidButton
import com.example.qgeni.ui.screens.uploads.ErrorScreen
import com.example.qgeni.ui.theme.QGenITheme
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun WordleGame(
    onBackClick: () -> Unit,
    onLeaderBoardClick: (List<Achievement>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wordLengthOptions = listOf(5, 6, 7)
    var wordLength by remember { mutableIntStateOf(5) }
    var targetWord by remember { mutableStateOf("") }
    var currentGuess by remember { mutableStateOf("") }
    var guesses by remember { mutableStateOf(mutableListOf<String>()) }
    var gameOver by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var invalidWordDialog by remember { mutableStateOf(false) }
    val rows = 6
    val scope = rememberCoroutineScope()
    var error by remember { mutableStateOf("no") }
    var loading by remember {
        mutableStateOf(false)
    }
    var elapsedTime by remember { mutableStateOf(0L) }
    var timerJob by remember { mutableStateOf<Job?>(null) }

    var leaderboard by remember { mutableStateOf(listOf<Achievement>()) }

    fun restartGame() {
        scope.launch {
            try {
                loading = true
                error = "no"
                targetWord = fetchRandomWord(wordLength)
                Log.e("NOT ERROR", "THIS IS ANSWER:$targetWord" )
                guesses.clear()
                gameOver = false
                resultMessage = ""
                currentGuess = ""
                showDialog = false
                invalidWordDialog = false
                loading = false
                elapsedTime = 0L
                timerJob?.cancel()
                timerJob = launch {
                    while (isActive && !gameOver) {
                        delay(1000L)
                        elapsedTime++
                    }
                }
            } catch (e: Exception) {
                println("E: ${e.message}")
                error = "le"
            }

        }
    }

    fun submitWordleResult(wordLength: Int, guessesCount: Int, timeSeconds: Long) {
        scope.launch(Dispatchers.IO) {
            try {
                val urlStr = "${BuildConfig.BASE_URL}/api/wordle/submit"
                val token = JwtPreferenceManager.getJwtFlow(AppContextHolder.appContext)

                val connection = (URL(urlStr).openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Authorization", "Bearer $token")
                    doOutput = true
                }
                //
                val point = 7 - guessesCount
                val jsonBody = """
                    {
                        "word_length": $wordLength,
                        "guesses": $point,
                        "time_taken": $timeSeconds
                    }
                """.trimIndent()
                connection.outputStream.write(jsonBody.toByteArray())
                connection.outputStream.flush()

                if (connection.responseCode == 201) {
                    Log.d("Wordle", "Result submitted")
                } else {
                    Log.e("Wordle", "Submit failed: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                Log.e("Wordle", "Error: ${e.message}")
                error = "sm"
            }
        }
    }

    fun fetchLeaderboard() {
        scope.launch(Dispatchers.IO) {
            try {
                val urlStr = "${BuildConfig.BASE_URL}/api/wordle/leaderboard/$wordLength"
                val token = JwtPreferenceManager.getJwtFlow(AppContextHolder.appContext)

                val connection = (URL(urlStr).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    setRequestProperty("Authorization", "Bearer $token")
                }
                connection.connect()
                if (connection.responseCode == 200) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val json = JSONObject(response)
                    val arr = json.getJSONArray("leaderboard")
                    val list = mutableListOf<Achievement>()
                    for (i in 0 until arr.length()) {
                        val item = arr.getJSONObject(i)
                        list.add(
                            Achievement(
                                userId = item.getInt("user_id"),
                                name = item.getString("user_name"),
                                score = item.getInt("guesses").toFloat(),
                                time = item.getDouble("time_taken").toString(),
                                top = i + 1
                            )
                        )
                    }
                    withContext(Dispatchers.Main) {
                        leaderboard = list
                    }
                    println("lb: " + leaderboard)
                }
            } catch (e: Exception) {
                println("E: ${e.message}")
                error = "leaderBoard"
            }
        }
    }

    LaunchedEffect(wordLength) {
        restartGame()
    }

    LaunchedEffect(Unit) {
        fetchLeaderboard()
    }

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
                    text = "Game",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "WORDLE",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(100.dp)
                            .height(40.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(
                                start = 8.dp,
                                end = 4.dp
                            )

                    ) {
                        Icon(
                            Icons.Outlined.Timer,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "${elapsedTime}s",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,

                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(
                                start = 8.dp,
                                end = 8.dp
                            )
                            .clickable(
                                onClick = {
                                    onLeaderBoardClick(leaderboard)
                                }
                            )

                    ) {
                        Icon(
                            Icons.Outlined.Leaderboard,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Leaderboard",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))


                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Word Length: ",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.background
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    DropdownMenuWordLength(wordLength) {
                        wordLength = it
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (i in 0 until rows) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            val guess = guesses.getOrNull(i) ?: "".padEnd(wordLength)
                            for (j in 0 until wordLength) {
                                val c = guess.getOrNull(j)?.toString() ?: ""
                                val color = when {
                                    i >= guesses.size -> Color(0xFFE0E0E0)
                                    c.lowercase() == targetWord.getOrNull(j)?.toString()?.lowercase() -> Color(0xFF2E7D32)
                                    c.lowercase() in targetWord.lowercase() -> Color(0xFFF9A825)
                                    else -> Color(0xFF616161)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(color, RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        c.uppercase(),
                                        fontSize = 22.sp,
                                        color = Color.White,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = currentGuess,
                    onValueChange = {
                        if (it.length <= wordLength && !gameOver) {
                            currentGuess = it.uppercase()
                        }
                    },
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
                            if (currentGuess.length == wordLength && !gameOver) {
                                scope.launch {
                                    try {
                                        val isValid = checkWordValid(currentGuess.lowercase())
                                        if (isValid) {
                                            guesses.add(currentGuess)
                                            if (currentGuess == targetWord) {
                                                resultMessage = "\uD83C\uDF89 You win!"
                                                gameOver = true
                                                showDialog = true
                                                timerJob?.cancel()
                                                submitWordleResult(wordLength, guesses.size, elapsedTime)
                                                fetchLeaderboard()
                                            } else if (guesses.size == rows) {
                                                resultMessage = "\u274C You lose! Word: $targetWord"
                                                gameOver = true
                                                showDialog = true
                                                timerJob?.cancel()
                                            }
                                            currentGuess = ""

                                        } else {
                                            invalidWordDialog = true
                                            resultMessage = "❌ Invalid word!"
                                        }
                                    } catch (e: Exception) {
                                        println("E: ${e.message}")
                                        error = "se"
                                    }

                                }
                            }
                        }
                    ),
                    placeholder = { Text("Enter your guess") },
                    singleLine = true,
                    modifier = Modifier.width((wordLength * 36).dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomSolidButton(
                        onClick = {
                            if (currentGuess.length == wordLength && !gameOver) {
                                scope.launch {
                                    try {
                                        val isValid = checkWordValid(currentGuess.lowercase())
                                        if (isValid) {
                                            guesses.add(currentGuess)
                                            if (currentGuess == targetWord) {
                                                resultMessage = "\uD83C\uDF89 You win!"
                                                gameOver = true
                                                showDialog = true
                                                timerJob?.cancel()
                                                submitWordleResult(wordLength, guesses.size, elapsedTime)
                                                fetchLeaderboard()
                                            } else if (guesses.size == rows) {
                                                resultMessage = "\u274C You lose! Word: $targetWord"
                                                gameOver = true
                                                showDialog = true
                                                timerJob?.cancel()
                                            }
                                            currentGuess = ""
                                        } else {
                                            invalidWordDialog = true
                                            resultMessage = "❌ Invalid word!"
                                        }
                                    } catch (e: Exception) {
                                        println("E: ${e.message}")
                                        error = "se"
                                    }
                                }
                            }
                        },
                        text = "SUBMIT"
                    )

                    CustomSolidButton(
                        onClick = {
                            restartGame()
                        },
                        text = "RESTART"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))


            }

        }

        if (!error.equals("no")) {
            ErrorScreen(
                onDismissRequest = { error = "no" },
                onLeaveButtonClick = { restartGame() },
                message = "Vui lòng thử lại"
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text(if (resultMessage.contains("win")) "🎉 Congratulations!" else "💀 Game Over")
                },
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                textContentColor = MaterialTheme.colorScheme.onPrimary,
                text = {
                    Text(resultMessage)
                },
                confirmButton = {
                    Button(onClick = { restartGame() }) {
                        Text("Restart")
                    }
                }
            )
        }
        if (invalidWordDialog) {
            AlertDialog(
                onDismissRequest = { invalidWordDialog = false },
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                textContentColor = MaterialTheme.colorScheme.onPrimary,
                title = { Text("❌ Invalid Word") },
                text = { Text("The word you entered is not valid. Please try again!") },
                confirmButton = {
                    Button(onClick = { invalidWordDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }

}

@Preview
@Composable
fun WordleLightPreview() {
    QGenITheme(dynamicColor = false, darkTheme = false){
        WordleGame({}, {})
    }
}

@Preview
@Composable
fun WordleDarkPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true){
        WordleGame({}, {})
    }
}

@Composable
fun DropdownMenuWordLength(current: Int, onLengthChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(5, 6, 7)
    Box {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(60.dp)
                .height(30.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(
                    start = 2.dp,
                    end = 4.dp
                )
                .clickable(
                    onClick = { expanded = true }
                )
        ) {
            Text(
                "$current",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,

                modifier = Modifier.weight(1f)
            )
            if (expanded) {
                Icon(
                    Icons.Outlined.ArrowDropUp,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    Icons.Outlined.ArrowDropDown,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }


        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { length ->
                DropdownMenuItem(
                    text = { Text("$length") },
                    onClick = {
                        onLengthChange(length)
                        expanded = false
                    }
                )
            }
        }
    }
}


data class LeaderboardEntry(val userId: Int, val userName: String, val guesses: Int, val timeTaken: Double, val top: Int)

// === NETWORK ===

suspend fun fetchRandomWord(length: Int): String = withContext(Dispatchers.IO) {
    val urlStr = "${BuildConfig.BASE_URL}/api/words/random/$length"
    val url = URL(urlStr)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connectTimeout = 5000
    connection.readTimeout = 5000
    connection.connect()
    if (connection.responseCode == 200) {
        val text = connection.inputStream.bufferedReader().readText()
        val key = "\"word\": \""
        val start = text.indexOf(key) + key.length
        val end = text.indexOf("\"", start)
        if (start >= key.length && end > start) {
            text.substring(start, end).uppercase()
        } else ""
    } else ""
}

suspend fun checkWordValid(word: String): Boolean = withContext(Dispatchers.IO) {
    val urlStr = "${BuildConfig.BASE_URL}/api/words/check/$word"
    val url = URL(urlStr)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connectTimeout = 5000
    connection.readTimeout = 5000
    connection.connect()
    connection.responseCode == 200
}