package com.steven.imui

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Steven Duan
 * @since 2018/4/12
 * @version 1.0
 */
private val TIME_FORMAT by lazy { SimpleDateFormat("HH:mm:ss", Locale.CHINA) }
private val DATE_FORMAT by lazy { SimpleDateFormat("MM/dd", Locale.CHINA) }
private val YEAR_FORMAT by lazy { SimpleDateFormat("yyyy/MM/dd", Locale.CHINA) }

fun formatTimestamp(time: Long): String {
  return when {
    time > getTodayZero() -> TIME_FORMAT.format(time)
    time > (getTodayZero() - 24 * 3600 * 1000) -> "昨天"
    isSameYear(time) -> DATE_FORMAT.format(time)
    else -> YEAR_FORMAT.format(time)
  }
}

private fun isSameYear(time: Long): Boolean {
  val calendar = Calendar.getInstance()
  calendar.set(Calendar.DAY_OF_YEAR, 1)
  calendar.set(Calendar.HOUR_OF_DAY, 0)
  calendar.set(Calendar.MINUTE, 0)
  calendar.set(Calendar.SECOND, 0)
  calendar.set(Calendar.MILLISECOND, 0)
  return time >= calendar.timeInMillis
}

private fun getTodayZero(): Long {
  val calendar = Calendar.getInstance()
  calendar.set(Calendar.HOUR_OF_DAY, 0)
  calendar.set(Calendar.MINUTE, 0)
  calendar.set(Calendar.SECOND, 0)
  calendar.set(Calendar.MILLISECOND, 0)
  return calendar.timeInMillis
}