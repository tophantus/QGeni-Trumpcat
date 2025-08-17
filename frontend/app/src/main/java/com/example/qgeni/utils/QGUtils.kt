package com.example.qgeni.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

// Hàm format thời gian
fun formatTime(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
    return dateTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateTime: LocalDateTime?): String {
    if (dateTime == null) {
        return ""
    }

    val now = LocalDateTime.now()
    val daysBetween = ChronoUnit.DAYS.between(dateTime.toLocalDate(), now.toLocalDate())

    val dayPart = when (daysBetween) {
        0L -> "Hôm nay"
        1L -> "Hôm qua"
        in 2..6 -> "$daysBetween ngày trước"
        else -> {
            val weeksAgo = daysBetween / 7
            "$weeksAgo trước"
        }
    }

    val timePart = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    return "$dayPart, $timePart"
}

fun formatFloat2Decimal(number: Float?): String {
    if (number == null) {
        return "0.00"
    }
    print("Formatted number: $number")
    return String.format("%.2f", number).also { print("Formatted $it") }
}

sealed class ErrorMessages(val message: String) {
    data object EmptyField : ErrorMessages("Trường này không được để trống.")
    data object Failure: ErrorMessages("Sai tài khoản hoặc mật khẩu.")
}
