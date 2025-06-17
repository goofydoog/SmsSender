package com.example.smsSender.ui.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun combineDateAndTime(dateMillis: Long, hour: Int, minute: Int): Long {
    // 1. Zamieniamy epoch na LocalDate w naszej strefie
    val localDate = Instant.ofEpochMilli(dateMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    // 2. Tworzymy LocalDateTime
    val ldt = LocalDateTime.of(localDate, LocalTime.of(hour, minute))

    // 3. Konwertujemy z powrotem na epoch millis
    return ldt
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun formatMillisToDateTime(millisUtc: Long?): String {
    if (millisUtc == null) return ""
    val localDateTime = Instant
        .ofEpochMilli(millisUtc)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    // pattern: day.month.year hours:minutes (24-hour clock)
    val fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return localDateTime.format(fmt)
}

