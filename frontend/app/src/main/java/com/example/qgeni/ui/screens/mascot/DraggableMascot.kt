package com.example.qgeni.ui.screens.mascot

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlin.math.roundToInt


fun getDetailContent(message: String): String {
    return when {
        "abandon" in message -> "Abandon (v): bỏ rơi, từ bỏ\nVí dụ: He abandoned his car."
        "Run" in message -> "'Run' là động từ: chạy, vận hành.\nVí dụ: I run every morning."
        "benevolent" in message -> "Benevolent (adj): nhân hậu, tốt bụng"
        else -> "Thông tin bổ sung cho: $message"
    }
}


@Composable
fun DraggableMascot(
    modifier: Modifier = Modifier,
    uiState: MascotUiState,
    onDrag: (dx: Float, dy: Float) -> Unit,
    onClickBubble: () -> Unit,
    onClickMascot: () -> Unit,
    onDismiss: () -> Unit,
    onHold: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(uiState.animationState)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )

    val screenWidthPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    val haptic = LocalHapticFeedback.current


    Box(modifier = modifier.fillMaxSize()) {

        if (uiState.showBubble && uiState.bubbleText.isNotBlank()) {
            BubbleWithDynamicOffset(
                uiState = uiState,
                onClick = onClickBubble,
                screenWidthPx = screenWidthPx
            )
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(uiState.offsetX.roundToInt(), uiState.offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount.x, dragAmount.y)
                    }
                }
        ) {
            LottieAnimation(
                composition = composition,
                progress = {progress},
                modifier = Modifier
                    .size(100.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onClickMascot() },
                            onLongPress = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onHold()
                            }
                        )
                    }
            )
        }
    }

    if (uiState.showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Đóng")
                }
            },
            title = { Text("Thông tin thú vị") },
            text = { Text(getDetailContent(uiState.bubbleText)) }
        )
    }
}


@Composable
fun BubbleWithDynamicOffset(
    uiState: MascotUiState,
    onClick: () -> Unit,
    screenWidthPx: Float
) {
    var bubbleSize by remember { mutableStateOf(IntSize.Zero) }

    if (uiState.showBubble && uiState.bubbleText.isNotBlank()) {
        val mascotWidth = with(LocalDensity.current) { 100.dp.toPx() }
        val isRight = uiState.offsetX + mascotWidth / 2f > screenWidthPx / 2f


        val offsetX = if (isRight) {
            (uiState.offsetX - bubbleSize.width + mascotWidth)
        } else {
            (uiState.offsetX)
        }

        val offsetY = uiState.offsetY - bubbleSize.height

        Box(
            modifier = Modifier.offset {
                IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
            }
        ) {
            Surface(
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .shadow(4.dp)
                    .clickable { onClick() }
                    .onGloballyPositioned {
                        bubbleSize = it.size
                    }
            ) {
                Text(
                    text = uiState.bubbleText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

