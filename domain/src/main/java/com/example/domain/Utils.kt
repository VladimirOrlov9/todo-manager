package com.example.domain

import android.icu.text.SimpleDateFormat
import java.util.*

fun getDateFromMillis(millis: Long): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(millis)
}