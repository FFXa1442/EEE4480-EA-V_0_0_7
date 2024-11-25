package com.sample.application.ea.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Format to "yyyyMMdd"
 */
@SuppressLint("SimpleDateFormat")
fun Calendar.toCompactDate(): String =
    SimpleDateFormat("yyyyMMdd", Locale.US).format(this.time)

/**
 * Format to "yyyyMMddHHmm"
 */
@SuppressLint("SimpleDateFormat")
fun Calendar.toCompactDateTime(): String =
    SimpleDateFormat("yyyyMMddHHmm", Locale.US).format(this.time)

/**
 * Format to "HH:mm"
 */
@SuppressLint("SimpleDateFormat")
fun Calendar.to24HourTime(): String =
    SimpleDateFormat("HH:mm", Locale.US).format(this.time)

/**
 * Format to "dd MMM, yyyy"
 */
@SuppressLint("SimpleDateFormat")
fun Calendar.toReadableDate(): String =
    SimpleDateFormat("dd MMM, yyyy", Locale.US).format(this.time)

/**
 * Format to "yyyy/MM/dd"
 */
@SuppressLint("SimpleDateFormat")
fun Calendar.toSlashSeparatedDate(): String =
    SimpleDateFormat("yyyy/MM/dd", Locale.US).format(this.time)