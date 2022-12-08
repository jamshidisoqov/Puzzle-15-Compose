package uz.gita.puzzle15_compose.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


fun Int.getTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    val min = if (minutes >= 10) "$minutes" else "0$minutes"
    val sec = if (seconds >= 10) "$seconds" else "0$seconds"
    return "$min : $sec"
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDate(): String {
    return SimpleDateFormat("MMM dd,yyyy").format(Date())
}
