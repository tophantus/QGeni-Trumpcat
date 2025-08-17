package com.example.qgeni.ui.screens.profile


import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.ui.screens.components.CustomOutlinedButton
import com.example.qgeni.ui.theme.QGenITheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WordStatsRangeChartScreen(
    onBackClick: () -> Unit,

    statsViewModel: StatsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }


    val uiState by statsViewModel.uiState.collectAsState()


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
                text = "Stats",
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CustomOutlinedButton(
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Start: ${uiState.startDate?.let { sdf.format(it) } ?: "____/__/__"}",
                    onClick = {
                    showDatePicker(
                        context = context,
                        initialDate = uiState.startDate,
                        maxDate = Date(),
                        onDateSelected = { selected ->
                            statsViewModel.onStartDateSelected(selected)
                        }
                    )
                })

                CustomOutlinedButton(
                    enabled = uiState.startDate != null,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    text = "End: ${uiState.endDate?.let { sdf.format(it) } ?: "____/__/__"}",
                    onClick = {
                    showDatePicker(
                        context = context,
                        initialDate = uiState.endDate,
                        minDate = uiState.startDate,
                        maxDate = uiState.startDate?.let { statsViewModel.getLimitDate(it) },
                        onDateSelected = { selected ->
                            statsViewModel.onEndDateSelected(selected)
                        }
                    )
                })
            }

            if (uiState.stats.isNotEmpty()) {
                DoubleBarChart(
                    data = uiState.stats,
                    maxValue = uiState.stats.maxOf { maxOf(it.wordCount, it.practiceCount) }.coerceAtLeast(10),
                    onColumnClick = { label, word, practice ->
                        Toast.makeText(context, "$label: $word từ, $practice đề", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            if (uiState.error != null) {
                Toast.makeText(context, "Chỉ chọn tối đa 7 ngày!", Toast.LENGTH_SHORT).show()
                statsViewModel.removeError()
            }
        }

    }

}


@Composable
fun DoubleBarChart(
    data: List<DayStat>,
    maxValue: Int,
    onColumnClick: (label: String, wordCount: Int, practiceCount: Int) -> Unit
) {
    val barWidth = 12.dp
    val gap = 8.dp
    val yAxisLabelWidth = 40.dp
    val chartHeight = 200.dp
    val axisLineColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val axisLineWidth = 1.dp

    val density = LocalDensity.current

    var tooltipText by remember { mutableStateOf<String?>(null) }
    var tooltipOffset by remember { mutableStateOf<Offset?>(null) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(axisLineWidth)
                        .padding(start = 16.dp, end = 16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Box(
                        modifier = Modifier
                        .fillMaxWidth()
                        .height(axisLineWidth)
                        .background(axisLineColor)
                        .align(Alignment.BottomCenter)
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier
                            .width(yAxisLabelWidth)
                            .fillMaxHeight()
                            .padding(
                                end = 4.dp
                            ),
                        verticalArrangement = Arrangement.SpaceBetween,

                        ) {
                        Text(
                            "$maxValue", textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (-2).dp),
                        )
                        Text(
                            "${maxValue / 2}", textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()

                        )
                        Text(
                            "0", textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column (
                        modifier = Modifier
                            .width(4.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ){
                        val lines = listOf(0f, 0.5f, 1f)
                        lines.forEach { ratio ->
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(axisLineWidth)
                                    .background(axisLineColor)
                                    .offset(
                                        y = -chartHeight * ratio,
                                    )
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .width(axisLineWidth)
                            .height(chartHeight)
                            .background(axisLineColor)
                            .offset(x = yAxisLabelWidth + gap - axisLineWidth)
                    )
                    Column (
                        modifier = Modifier
                            .width(4.dp)
                            .fillMaxHeight()
                        ,
                        verticalArrangement = Arrangement.SpaceBetween,
                    ){
                        val lines = listOf(0f, 0.5f, 1f)
                        lines.forEach { ratio ->
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(axisLineWidth)
                                    .background(axisLineColor)
                                    .offset(
                                        y = -chartHeight * ratio,
                                    )
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = gap),
                        horizontalArrangement = Arrangement.spacedBy(gap),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        data.forEach { stat ->
                            val wordRatio = stat.wordCount.toFloat() / maxValue
                            val practiceRatio = stat.practiceCount.toFloat() / maxValue

                            var coords by remember { mutableStateOf<LayoutCoordinates?>(null) }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(end = 2.dp)
                            ) {
                                Row(modifier = Modifier.height(chartHeight), verticalAlignment = Alignment.Bottom) {
                                    Box(
                                        modifier = Modifier
                                            .width(barWidth)
                                            .fillMaxHeight(wordRatio)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MaterialTheme.colorScheme.primary)
                                            .onGloballyPositioned { coords = it }
                                            .clickable {
                                                coords?.let {
                                                    val offset = it.localToWindow(Offset.Zero)
                                                    val ox = with(density) {
                                                        offset.x - 16.dp.toPx()
                                                    }
                                                    val oy = with(density) {
                                                        offset.y - wordRatio*chartHeight.toPx()
                                                    }
                                                    tooltipOffset = Offset( ox, oy)
                                                    tooltipText = "${stat.wordCount} từ"
                                                }
                                                onColumnClick(
                                                    stat.label,
                                                    stat.wordCount,
                                                    stat.practiceCount
                                                )
                                            }
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(barWidth)
                                            .fillMaxHeight(practiceRatio)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(MaterialTheme.colorScheme.onBackground)
                                            .onGloballyPositioned { coords = it }
                                            .clickable {
                                                coords?.let {
                                                    val offset = it.localToWindow(Offset.Zero)
                                                    val ox = with(density) {
                                                        offset.x
                                                    }
                                                    val oy = with(density) {
                                                        offset.y - wordRatio*chartHeight.toPx()
                                                    }
                                                    tooltipOffset = Offset( ox, oy)
                                                    tooltipText = "${stat.practiceCount} đề"
                                                }
                                                onColumnClick(
                                                    stat.label,
                                                    stat.wordCount,
                                                    stat.practiceCount
                                                )
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = yAxisLabelWidth + gap + 8.dp),
                horizontalArrangement = Arrangement.spacedBy(gap + 2.dp)
            ) {
                data.forEach {
                    val strings = it.label.split("-")
                    val d = strings[2] + "/" + strings[1] + strings[0]
                    Text(
                        d,
                        modifier = Modifier.width(barWidth * 2 + 4.dp),
                        fontSize = 10.sp, textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LegendDot(
                    MaterialTheme.colorScheme.primary,
                    "Từ vựng",
                )
                LegendDot(
                    MaterialTheme.colorScheme.onBackground,
                    "Đề"
                )
            }
        }

        tooltipText?.let { text ->
            tooltipOffset?.let { offset ->
                Popup(offset = IntOffset(offset.x.toInt(), offset.y.toInt())) {
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(4.dp))
                            .padding(6.dp)
                    )
                }
                LaunchedEffect(text) {
                    delay(1500)
                    tooltipText = null
                    tooltipOffset = null
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StatsPreview() {
    QGenITheme {
        WordStatsRangeChartScreen(
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StatsDarkPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        WordStatsRangeChartScreen(
            onBackClick = {}
        )
    }
}

@Composable
fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier
            .size(12.dp)
            .background(color, CircleShape))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

fun showDatePicker(
    context: android.content.Context,
    initialDate: Date?,
    minDate: Date? = null,
    maxDate: Date? = null,
    onDateSelected: (Date) -> Unit
) {
    val calendar = Calendar.getInstance().apply {
        time = initialDate ?: Date()
    }

    val dialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            Calendar.getInstance().apply {
                set(y, m, d)
                onDateSelected(time)
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    minDate?.let { dialog.datePicker.minDate = it.time }
    maxDate?.let { dialog.datePicker.maxDate = it.time }
    dialog.show()
}
