package com.example.qgeni.ui.screens.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qgeni.data.repositories.DefaultHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class StatsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(StatsUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val calendar = Calendar.getInstance().apply { add(Calendar.DATE, -1) }
            val yesterday = calendar.time
            val startDate = limitDate(yesterday, -6)


            try {
                val start = startDate.toInstant().atZone(ZoneId.systemDefault()).format(
                    DateTimeFormatter.ISO_DATE_TIME)
                val end = yesterday.toInstant().atZone(ZoneId.systemDefault()).format(
                    DateTimeFormatter.ISO_DATE_TIME)
                val newStats = withContext(Dispatchers.IO) {
                    DefaultHistoryRepository.getStats(start, end)
                }
                println(newStats.toString())
                _uiState.update {
                    it.copy(
                        startDate = startDate,
                        endDate = yesterday,
                        stats = newStats
                    )
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }
    fun getLimitDate(startDate: Date): Date? {
        val limit = _uiState.value.startDate?.let { limitDate(it, 6) }
        return limit
    }

    fun removeError() {
        _uiState.update {
            it.copy(
                error = null
            )
        }
    }

    private suspend fun getStats(startDate: Date, days: Int): List<DayStat> {
        ///
        return getFixedMockStats(startDate, days)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEndDateSelected(date: Date) {
        if (_uiState.value.startDate == null) {
            _uiState.update {
                it.copy(
                    error = "Vui lòng chọn ngày bắt đầu",
                    stats = emptyList()
                )
            }
            return
        }
        val days = _uiState.value.startDate?.let {
            daysBetween(
                it,
                date
            )
        }
        if (days != null) {
            if (days in 0..6) {
                val start = _uiState.value.startDate!!.toInstant().atZone(ZoneId.systemDefault()).format(
                    DateTimeFormatter.ISO_DATE_TIME)
                println(start)
                val end = date.toInstant().atZone(ZoneId.systemDefault()).format(
                    DateTimeFormatter.ISO_DATE_TIME)
                println(end)
                viewModelScope.launch {
                    try {
                        val newStats = withContext(Dispatchers.IO) {
                            DefaultHistoryRepository.getStats(start, end)
                        }
                        println(newStats.toString())
                        _uiState.update {
                            it.copy(
                                endDate = date,
                                stats = newStats
                            )
                        }
                    } catch (e: Exception) {
                        println("Error: ${e.message}")
                    }
                }


            } else {
                _uiState.update {
                    it.copy(
                        error = "Chỉ chọn tối đa 7 ngày!",
                        stats = emptyList()
                    )
                }
            }
        }

        /*endDate = selected

        if (days in 0..6) {
            if (days != null) {
                stats = startDate?.let { getFixedMockStats(it, days + 1) }
            }
        } else {
            Toast.makeText(context, "Chỉ chọn tối đa 7 ngày!", Toast.LENGTH_SHORT).show()
            stats = emptyList()
        }*/
    }

    fun onStartDateSelected(date: Date) {
        _uiState.update {
            it.copy(
                startDate = date,
                endDate = null,
                stats = emptyList()
            )
        }

        /*if (endDate != null) {
            endDate = null
            stats = emptyList()
        } else {
            val days = endDate?.let {
                com.example.qgeni.ui.screens.components.daysBetween(
                    selected,
                    it
                )
            }
            if (days in 0..6) if (days != null) {
                stats = getFixedMockStats(selected, days + 1)
            }
        }*/
    }
}

data class StatsUIState(
    var startDate: Date? = null,
    var endDate: Date? = null,
    var error: String? = null,
    var stats: List<DayStat> = emptyList(),
    val loading: Boolean = false
)

data class DayStat(
    val label: String,
    val wordCount: Int,
    val practiceCount: Int
)

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