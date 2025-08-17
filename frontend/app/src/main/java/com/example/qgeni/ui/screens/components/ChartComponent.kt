/*
package com.example.qgeni.ui.screens.components

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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
import com.example.qgeni.ui.theme.QGenITheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

data class DayStat(val label: String, val wordCount: Int, val practiceCount: Int)

@Composable
fun WordStatsRangeChartScreen() {
    val context = LocalContext.current
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val labelSdf = remember { SimpleDateFormat("dd/MM", Locale.getDefault()) }

    val calendar = Calendar.getInstance().apply { add(Calendar.DATE, -1) }
    val yesterday = calendar.time
    val defaultStart = limitDate(yesterday, -6)
    val defaultEnd = yesterday

    var startDate by remember { mutableStateOf<Date?>(defaultStart) }
    var endDate by remember { mutableStateOf<Date?>(defaultEnd) }
    var stats by remember { mutableStateOf(startDate?.let { getFixedMockStats(it, 7) }) }

    val maxDays = 7

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = {
                showDatePicker(
                    context = context,
                    initialDate = startDate,
                    maxDate = Date(),
                    onDateSelected = { selected ->
                        startDate = selected
                        if (endDate != null) {
                            endDate = null
                            stats = emptyList()
                        } else {
                            val days = endDate?.let { daysBetween(selected, it) }
                            if (days in 0..6) if (days != null) {
                                stats = getFixedMockStats(selected, days + 1)
                            }
                        }
                    }
                )
            }) {
                Text(
                    text = "Start: ${startDate?.let { sdf.format(it) } ?: "____/__/__"}",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            OutlinedButton(enabled = startDate != null, onClick = {
                showDatePicker(
                    context = context,
                    initialDate = endDate,
                    minDate = startDate,
                    maxDate = startDate?.let { limitDate(it, 6) },
                    onDateSelected = { selected ->
                        endDate = selected
                        val days = startDate?.let { daysBetween(it, selected) }
                        if (days in 0..6) {
                            if (days != null) {
                                stats = startDate?.let { getFixedMockStats(it, days + 1) }
                            }
                        } else {
                            Toast.makeText(context, "Chỉ chọn tối đa 7 ngày!", Toast.LENGTH_SHORT).show()
                            stats = emptyList()
                        }
                    }
                )
            }) {
                Text(
                    text = "End: ${endDate?.let { sdf.format(it) } ?: "____/__/__"}",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        if (stats?.isNotEmpty() == true) {
            DoubleBarChart(
                data = stats!!,
                maxValue = stats!!.maxOf { maxOf(it.wordCount, it.practiceCount) }.coerceAtLeast(10),
                onColumnClick = { label, word, practice ->
                    Toast.makeText(context, "$label: $word từ, $practice đề", Toast.LENGTH_SHORT).show()
                }
            )
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
    val density = LocalDensity.current
    val axisLineColor = Color.Gray
    val axisLineWidth = 1.dp

    var tooltipText by remember { mutableStateOf<String?>(null) }
    var tooltipOffset by remember { mutableStateOf<Offset?>(null) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(axisLineWidth)
                        .background(axisLineColor)
                        .align(Alignment.BottomCenter)
                )
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = -2.dp),
                        )
                        Text(
                            "${maxValue / 2}", textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()

                        )
                        Text(
                            "0", textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium,
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
                                            .background(Color.Red)
                                            .onGloballyPositioned { coords = it }
                                            .clickable {
                                                coords?.let {
                                                    val offset = it.localToWindow(Offset.Zero)
                                                    tooltipOffset = offset
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
                                            .background(Color.Blue)
                                            .onGloballyPositioned { coords = it }
                                            .clickable {
                                                coords?.let {
                                                    val offset = it.localToWindow(Offset.Zero)
                                                    tooltipOffset = offset
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
                    Text(it.label, modifier = Modifier.width(barWidth * 2 + 4.dp), fontSize = 10.sp, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LegendDot(
                    Color.Red,
                    "Từ vựng",
                )
                LegendDot(
                    Color.Blue,
                    "Đề"
                )
            }
        }

        tooltipText?.let { text ->
            tooltipOffset?.let { offset ->
                Popup(offset = IntOffset(offset.x.toInt(), offset.y.toInt() - 60)) {
                    Text(
                        text = text,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(Color.Black, shape = RoundedCornerShape(8.dp))
                            .padding(6.dp)
                    )
                }
                LaunchedEffect(text) {
                    delay(2000)
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
        WordStatsRangeChartScreen()
    }
}

@Composable
fun DoubleBarChart2(
    data: List<DayStat>,
    maxValue: Int,
    onColumnClick: (label: String, wordCount: Int, practiceCount: Int) -> Unit
) {
    val barWidth = 12.dp
    val gap = 12.dp
    val spacingBetweenBars = 4.dp
    val yAxisLabelWidth = 40.dp
    val chartHeight = 200.dp
    val axisLineColor = Color.Gray
    val axisLineWidth = 1.dp

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {
            // 1. Trục X: các đường kẻ ngang
            val lines = listOf(0f, 0.5f, 1f)
            lines.forEach { ratio ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(axisLineWidth)
                        .background(axisLineColor)
                        .align(Alignment.BottomStart)
                        .offset(y = -chartHeight * ratio)
                )
            }

            // 2. Trục Y chính (tạo một Box cao 100% nằm sát trái)
            Box(
                modifier = Modifier
                    .width(axisLineWidth)
                    .height(chartHeight)
                    .background(axisLineColor)
                    .align(Alignment.BottomStart)
                    .offset(x = yAxisLabelWidth + gap - axisLineWidth)
            )

            // 3. Nội dung biểu đồ
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = yAxisLabelWidth),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .width(yAxisLabelWidth)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    Text("$maxValue", fontSize = 12.sp)
                    Text("${maxValue / 2}", fontSize = 12.sp)
                    Text("0", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(gap))

                Row(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(gap),
                    verticalAlignment = Alignment.Bottom
                ) {
                    data.forEach { stat ->
                        val wordRatio = stat.wordCount.toFloat() / maxValue
                        val practiceRatio = stat.practiceCount.toFloat() / maxValue

                        Row(
                            modifier = Modifier.height(chartHeight),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(spacingBetweenBars)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(barWidth)
                                    .fillMaxHeight(wordRatio)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.Red)
                                    .clickable {
                                        onColumnClick(
                                            stat.label,
                                            stat.wordCount,
                                            stat.practiceCount
                                        )
                                    }
                            )
                            Box(
                                modifier = Modifier
                                    .width(barWidth)
                                    .fillMaxHeight(practiceRatio)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.Blue)
                                    .clickable {
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

        Spacer(modifier = Modifier.height(8.dp))

        // Label trục X
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = yAxisLabelWidth + gap),
            horizontalArrangement = Arrangement.spacedBy(gap)
        ) {
            data.forEach {
                Text(
                    text = it.label,
                    modifier = Modifier.width(barWidth * 2 + spacingBetweenBars),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Chú giải
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            LegendDot(Color.Red, "Từ vựng")
            LegendDot(Color.Blue, "Đề")
        }
    }
}

//@Composable
//fun DoubleBarChart(
//    data: List<DayStat>,
//    maxValue: Int,
//    onColumnClick: (label: String, wordCount: Int, practiceCount: Int) -> Unit
//) {
//    val barWidth = 12.dp
//    val gap = 8.dp
//    val yAxisLabelWidth = 40.dp
//    val chartHeight = 200.dp
//
//    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Row(modifier = Modifier.fillMaxWidth().height(chartHeight), verticalAlignment = Alignment.Bottom) {
//            Column(
//                modifier = Modifier.width(yAxisLabelWidth).fillMaxHeight(),
//                verticalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text("$maxValue", textAlign = TextAlign.End)
//                Text("${maxValue / 2}", textAlign = TextAlign.End)
//                Text("0", textAlign = TextAlign.End)
//            }
//
//            Row(
//                modifier = Modifier.fillMaxHeight().padding(start = gap),
//                horizontalArrangement = Arrangement.spacedBy(gap),
//                verticalAlignment = Alignment.Bottom
//            ) {
//                data.forEach { stat ->
//                    val wordRatio = stat.wordCount.toFloat() / maxValue
//                    val practiceRatio = stat.practiceCount.toFloat() / maxValue
//
//                    Row(
//                        modifier = Modifier.height(chartHeight),
//                        verticalAlignment = Alignment.Bottom
//                    ) {
//                        Box(
//                            modifier = Modifier.width(barWidth).fillMaxHeight(wordRatio)
//                                .clip(RoundedCornerShape(4.dp)).background(Color.Red)
//                                .clickable { onColumnClick(stat.label, stat.wordCount, stat.practiceCount) }
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Box(
//                            modifier = Modifier.width(barWidth).fillMaxHeight(practiceRatio)
//                                .clip(RoundedCornerShape(4.dp)).background(Color.Blue)
//                                .clickable { onColumnClick(stat.label, stat.wordCount, stat.practiceCount) }
//                        )
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//        Row(modifier = Modifier.fillMaxWidth().padding(start = yAxisLabelWidth + gap),
//            horizontalArrangement = Arrangement.spacedBy(gap)) {
//            data.forEach {
//                Text(it.label, modifier = Modifier.width(barWidth * 2), textAlign = TextAlign.Center)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(4.dp))
//        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//            LegendDot(Color.Red, "Từ vựng")
//            LegendDot(Color.Blue, "Đề")
//        }
//    }
//}

@Composable
fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier
            .size(12.dp)
            .background(color, CircleShape))
        Spacer(modifier = Modifier.width(4.dp))
        Text(label)
    }
}

val fixedMockData = mapOf(
    "10/06" to 12, "11/06" to 8, "12/06" to 14, "13/06" to 7, "14/06" to 11,
    "15/06" to 6, "16/06" to 10, "17/06" to 9, "18/06" to 13, "19/06" to 5,
    "20/06" to 8, "21/06" to 6, "22/06" to 7, "23/06" to 10, "24/06" to 11,
    "25/06" to 13, "26/06" to 6
)

val mockPracticeData = mapOf(
    "10/06" to 5, "11/06" to 4, "12/06" to 7, "13/06" to 3, "14/06" to 6,
    "15/06" to 2, "16/06" to 5, "17/06" to 4, "18/06" to 6, "19/06" to 3,
    "20/06" to 4, "21/06" to 3, "22/06" to 2, "23/06" to 4, "24/06" to 6,
    "25/06" to 8, "26/06" to 6
)

fun getFixedMockStats(startDate: Date, days: Int): List<DayStat> {
    val calendar = Calendar.getInstance().apply { time = startDate }
    val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())

    return List(days) {
        val label = sdf.format(calendar.time)
        val word = fixedMockData[label] ?: 0
        val practice = mockPracticeData[label] ?: 0
        calendar.add(Calendar.DATE, 1)
        DayStat(label, word, practice)
    }
}

fun daysBetween(start: Date, end: Date): Int {
    val diff = end.time - start.time
    return (diff / (1000 * 60 * 60 * 24)).toInt()
}

fun limitDate(base: Date, days: Int): Date {
    return Calendar.getInstance().apply {
        time = base
        add(Calendar.DATE, days)
    }.time
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
*/
