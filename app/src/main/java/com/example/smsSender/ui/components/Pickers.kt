package com.example.smsSender.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogModal(
    selectedHour: Int?,
    selectedMinute: Int?,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {

    val now = LocalDateTime.now()
    val initHour = selectedHour ?: now.hour
    val initMinute = selectedMinute ?: now.minute

    val state = rememberTimePickerState(
        initialHour = initHour,
        initialMinute = initMinute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Select time",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            TimePicker(
                state = state,
                modifier = Modifier.wrapContentHeight()
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(state.hour, state.minute)
                onDismiss()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    selectedDateUtc: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val todayUtcMidnight = LocalDate
        .now(ZoneOffset.UTC)
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()

    val state = rememberDatePickerState(
        initialDisplayedMonthMillis = selectedDateUtc ?: todayUtcMidnight,
        initialSelectedDateMillis = selectedDateUtc ?: todayUtcMidnight,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayUtcMidnight
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= LocalDate.now().year
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(state.selectedDateMillis)
                    onDismiss()
                },
                enabled = (state.selectedDateMillis != null)
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = state)
    }
}