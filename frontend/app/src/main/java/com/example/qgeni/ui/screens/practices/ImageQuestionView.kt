package com.example.qgeni.ui.screens.practices

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.qgeni.R
import com.example.qgeni.data.repositories.DefaultFileRepository
import com.example.qgeni.ui.screens.components.CustomAsyncImage
import com.example.qgeni.ui.screens.components.CustomSolidButton
import com.example.qgeni.utils.formatTime
import com.example.qgeni.ui.theme.QGenITheme


@Composable
fun ImageQuestionView(
    modifier: Modifier = Modifier,
    isCompleted : Boolean,
    currentQuestion: Int,
    timeString: String,
    imageList: List<String>,
    imageLabelList: List<String>,
    onSubmitClick: () -> Unit,
    onPlayClick: () -> Unit,
    playbackState: PlaybackState,
    sliderPosition: Float,
    duration: Float,
    onSliderPositionChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    var popupImages by remember {
        mutableStateOf(false)
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
                text = timeString,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomSolidButton(
                onClick = onSubmitClick,
                text = if (isCompleted) "TIẾP TỤC" else "NỘP BÀI",
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

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
                .clickable { popupImages = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Record ${currentQuestion + 1}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        AudioPlayer(
            playbackState = playbackState,
            sliderPosition = sliderPosition,
            duration = duration,
            onSliderPositionChange = onSliderPositionChange,
            onPlayClick = onPlayClick,
            onValueChangeFinished = onValueChangeFinished
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (imageList.isEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(16.dp)
            )
        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(imageList.size) { index ->
                    ImageBox(
                        image = imageList[index],
                        label = imageLabelList[index],
                    )
                }
            }

        }

        if (popupImages) {
            ContentDialog(onDismissRequest = { popupImages = false}) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(imageList.size) { index ->
                        ImageBox(
                            image = imageList[index],
                            label = imageLabelList[index],
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ImageBox(
    image: String,
    label: String,
    modifier: Modifier = Modifier
) {
    println("Image for AsyncImage $image")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .wrapContentSize()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp),
            )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(8.dp)
        )
        CustomAsyncImage(
            url = image,
            placeholder = painterResource(id = R.drawable.listening),
            error = painterResource(id = R.drawable.error_load),
            contentDescription = label,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp
                    )
                )

        )
    }
}

/*
    Phần hiển thị AudioPlayer, lựa chọn ảnh,
    và thời gian cho ListeningPracticeScren
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayer(
    playbackState: PlaybackState,
    sliderPosition: Float,
    duration: Float,
    onSliderPositionChange: (Float) -> Unit,
    onPlayClick: () -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp),
            )
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPlayClick
        ) {
            Icon(
                imageVector = when (playbackState) {
                    PlaybackState.PAUSED -> Icons.Outlined.PlayArrow
                    PlaybackState.PLAYING -> Icons.Outlined.Pause
                    PlaybackState.FINISHED -> Icons.Outlined.Replay
                },
                contentDescription = "Play/Pause",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = formatTime(sliderPosition.toInt()),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = " / " + formatTime(duration.toInt()),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(2.dp))
        Slider(
            value = sliderPosition,
            onValueChange = onSliderPositionChange,
            valueRange = 0f..duration,
            onValueChangeFinished = onValueChangeFinished,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.tertiary
            ),
            thumb = {},
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {}) {
            Icon(
                Icons.AutoMirrored.Outlined.VolumeUp,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = {},
        ) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ImageQuestionLightViewPreview() {
    QGenITheme(dynamicColor = false) {
        ImageQuestionView(
            currentQuestion = 0,
            isCompleted = false,
            timeString = "00:00",
            imageList = emptyList(),
            imageLabelList = listOf("Pic. A", "Pic. B", "Pic. C"),
            onPlayClick = {},
            onSubmitClick = {},
            playbackState = PlaybackState.PAUSED,
            sliderPosition = 0f,
            duration = 100f,
            onSliderPositionChange = {},
            onValueChangeFinished = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ImageQuestionDarkViewPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        ImageQuestionView(
            currentQuestion = 0,
            isCompleted = true,
            timeString = "00:00",
            imageList = emptyList(),
            imageLabelList = listOf("Pic. A", "Pic. B", "Pic. C"),
            onPlayClick = {},
            onSubmitClick = {},
            playbackState = PlaybackState.PAUSED,
            sliderPosition = 0f,
            duration = 100f,
            onSliderPositionChange = {},
            onValueChangeFinished = {}
        )

    }
}





