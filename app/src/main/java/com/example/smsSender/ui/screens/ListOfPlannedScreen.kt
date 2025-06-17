package com.example.smsSender.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.smsSender.ContactsViewModel
import com.example.smsSender.DescriptionViewModel
import com.example.smsSender.VisitModel
import com.example.smsSender.VisitsViewModel
import com.example.smsSender.room.SmsWithContact
import com.example.smsSender.room.contact.Contact
import com.example.smsSender.ui.components.Account
import com.example.smsSender.ui.components.AccountMode
import com.example.smsSender.ui.components.DatePickerModal
import com.example.smsSender.ui.components.TimePickerDialogModal
import com.example.smsSender.ui.utils.combineDateAndTime
import com.example.smsSender.ui.utils.formatMillisToDateTime
import com.example.smsSender.ui.utils.printPhoneNumberWithSpaces
import kotlinx.coroutines.delay

@Composable
fun ListOfPlannedScreen(
    visitsViewModel: VisitsViewModel,
    contactsViewModel: ContactsViewModel,
    descriptionViewModel: DescriptionViewModel
) {
    val context = LocalContext.current
    var showDialogAddVisit by remember { mutableStateOf(false) } // alertDialog add visit
    var showDialogSelectContact by remember { mutableStateOf(false) } // alertDialog select contact
    var showDatePicker by remember { mutableStateOf(false) } // pozwala wybrac dd.mm.yyyy
    var showTimePicker by remember { mutableStateOf(false) } // pozwala wybrac hh:mm

    var selectedDate by remember { mutableStateOf<Long?>(null) } // wyswietla tylko date bez godzin i minut
    var selectedTime by remember { mutableStateOf<Pair<Int, Int>?>(null) } // przechowuje tylko godziny i minuty

    var selectedContact by remember { mutableStateOf<Contact?>(null) }

    val visitModelState by visitsViewModel.visitModel.collectAsState() // data class pojedynczego visit

    val listOfContacts by contactsViewModel.contacts.collectAsState()

    val listOfPlannedSmsWithFullInfo by visitsViewModel.listOfPlannedSmsWithFullInfo.collectAsState()

    var isAddingSmsDate by remember { mutableStateOf(false) }
    var tempSmsDateOverall by remember { mutableStateOf<Long?>(null) }

    var tempValueToEdit by remember { mutableStateOf<Long?>(null) }
    var tempValueIndexToEdit by remember { mutableStateOf<Int?>(null) }

    val savedText by descriptionViewModel.visitText.collectAsState("")

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (listOfPlannedSmsWithFullInfo.isEmpty()) {
                Text(
                    text = "Nothing there",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {

                LazyColumn {
                    itemsIndexed(
                        items = listOfPlannedSmsWithFullInfo,
                        key = { _, record -> record.sms.id }
                    ) { index, record ->
                        var visible by remember { mutableStateOf(false) }

                        LaunchedEffect(Unit) {
                            delay(100L * index)
                            visible = true
                        }

                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(animationSpec = tween(500)) + expandVertically(),
                            exit = fadeOut(animationSpec = tween(300)) + shrinkVertically()
                        ) {
                            SingleRecordOfPlannedSms(
                                singlePlannedSms = record,
                                onDelete = {
                                    visitsViewModel.deletePlannedSms(record.sms)
                                    visitsViewModel.deleteWorkReqById(record.sms.id, context)
                                },
                                onEdit = {

                                }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialogAddVisit = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add visit")
        }

        if (showDialogAddVisit) {
            AlertDialogAddVisit(
                overallTime = visitModelState.timeOfVisit,
                visitState = visitModelState,
                openDatePicker = { showDatePicker = true },
                onConfirm = {
                    showDialogSelectContact = true
                },
                addNewSmsDate = {
                    showDatePicker = true
                    isAddingSmsDate = true
                },
                onDeleteSmsDateClick = { index ->
                    visitsViewModel.removeSmsDateFromList(index)
                },
                onDismiss = { showDialogAddVisit = false },
                saveDateToEdit = { index, value ->
                    tempValueToEdit = value
                    tempValueIndexToEdit = index
                },
                isEnabledSelectContact = visitModelState.listOfSmsDates.isNotEmpty(),
                isEnableAddNewDate = visitModelState.timeOfVisit != null && visitModelState.listOfSmsDates.size < 5,
            )
        }

        if (showDatePicker) {
            DatePickerModal(
                selectedDateUtc = if (tempValueToEdit != null) tempValueToEdit else selectedDate,
                onDateSelected = { date ->
                    when {
                        isAddingSmsDate ->            // nowy SMS
                            tempSmsDateOverall = date

                        tempValueToEdit != null ->    // edycja istniejącego SMS-a
                            tempValueToEdit = date

                        else ->                       // data wizyty
                            selectedDate = date
                    }
                    showTimePicker = true
                    showDatePicker = false
                },
                onDismiss = {
                    showDatePicker = false
                }
            )
        }

        if (showTimePicker) {
            TimePickerDialogModal(
                selectedHour = selectedTime?.first,
                selectedMinute = selectedTime?.second,
                onConfirm = { hour, minute ->

                    when {
                        /* ---------- dodawanie NOWEGO SMS-a ---------- */
                        isAddingSmsDate -> {
                            val ts = tempSmsDateOverall
                                ?.let { combineDateAndTime(it, hour, minute) }
                            ts?.let { visitsViewModel.addSmsDateToList(it) }
                            tempSmsDateOverall = null
                        }

                        /* ---------- EDYCJA istniejącego SMS-a ---------- */
                        tempValueToEdit != null -> {
                            val ts = combineDateAndTime(tempValueToEdit!!, hour, minute)
                            tempValueIndexToEdit?.let { visitsViewModel.updateSmsDate(it, ts) }
                            tempValueToEdit = null
                            tempValueIndexToEdit = null
                        }

                        /* ---------- ustawianie daty wizyty ---------- */
                        else -> {
                            selectedTime = hour to minute
                            selectedDate?.let { date ->
                                val ts = combineDateAndTime(date, hour, minute)
                                visitsViewModel.addDateOfVisit(ts)
                            }
                        }
                    }

                    // wspólne czyszczenie i zamknięcie
                    showTimePicker = false
                    isAddingSmsDate = false
                },
                onDismiss = {
                    showTimePicker = false
                    isAddingSmsDate = false
                    tempValueToEdit = null
                }
            )
        }

        if (showDialogSelectContact) {

            AlertDialogSelectContact(
                listOfContacts = listOfContacts,
                selectedContact = selectedContact,
                onContactPicked = { contact ->
                    visitsViewModel.addContactId(contact.id)
                    selectedContact = contact
                },
                onConfirm = {

                    visitsViewModel.saveVisit(
                        phoneNumber = selectedContact?.phoneNumber.orEmpty(),
                        model = visitModelState,
                        message = prepareTextForSms(
                            savedText = savedText,
                            nameOfClient = selectedContact?.firstName ?: "",
                            dateOfVisit = formatMillisToDateTime(visitModelState.timeOfVisit)
                        ),
                        context = context
                    )

                    showDialogSelectContact = false
                    showDialogAddVisit = false
                },
                onDismiss = {
                    showDialogSelectContact = false
                }
            )
        }
    }
}

@Composable
fun SingleRecordOfPlannedSms(
    singlePlannedSms: SmsWithContact,
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    val containerColor = MaterialTheme.colorScheme.surface

    val gradient = if (isSystemInDarkTheme()) {
        Brush.linearGradient(
            colorStops = arrayOf(
                0.00f to Color.Black.copy(alpha = 0.9f),
                0.40f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                0.60f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                1.00f to Color.Black.copy(alpha = 0.4f)
            ),
            start = Offset.Zero,
            end = Offset(300f, 300f)
        )
    } else {
        Brush.linearGradient(
            colorStops = arrayOf(
                0.00f to Color.White.copy(alpha = 0.9f),
                0.40f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                0.60f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                1.00f to Color.White.copy(alpha = 0.4f)
            ),
            start = Offset.Zero,
            end = Offset(300f, 300f)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Box(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .blur(8.dp)
                .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(12.dp)
                )
        )

        Row(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(brush = gradient, shape = RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = singlePlannedSms.contact.firstName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (singlePlannedSms.contact.lastName.isNotBlank()) {
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = singlePlannedSms.contact.lastName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Text(
                    text = printPhoneNumberWithSpaces(singlePlannedSms.contact.phoneNumber),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "SMS Date: ${formatMillisToDateTime(singlePlannedSms.sms.timeOfSms)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)

                )
            }
            IconButton(onClick = onEdit) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)

                )
            }
        }
    }
}


@Composable
fun AlertDialogAddVisit(
    saveDateToEdit: (Int, Long) -> Unit,
    visitState: VisitModel,
    isEnabledSelectContact: Boolean,
    isEnableAddNewDate: Boolean,
    overallTime: Long?,
    onDeleteSmsDateClick: (Int) -> Unit,
    openDatePicker: () -> Unit,
    addNewSmsDate: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    // gradient dobrany pod tryb ciemny/jasny
    val gradient = if (isSystemInDarkTheme()) {
        Brush.linearGradient(
            0.00f to Color.Black.copy(alpha = 0.9f),
            0.40f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            0.60f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            1.00f to Color.Black.copy(alpha = 0.4f),
            start = Offset.Zero,
            end = Offset(700f, 0f)
        )
    } else {
        Brush.linearGradient(
            0.00f to Color.White.copy(alpha = 0.9f),
            0.40f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            0.60f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            1.00f to Color.White.copy(alpha = 0.4f),
            start = Offset.Zero,
            end = Offset(700f, 0f)
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(16.dp))
                // 1) pełne kryjące tło
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                // 2) gradient na wierzchu
                .background(brush = gradient, shape = RoundedCornerShape(16.dp))
                // 3) subtelna ramka
                .border(
                    0.5.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    RoundedCornerShape(16.dp)
                )
        ) {
            Column(Modifier.padding(20.dp)) {
                // — Tytuł
                Text(
                    text = "Add Visit",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),

                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                Spacer(Modifier.height(16.dp))

                // — Data wizyty
                OutlinedTextField(
                    value = formatMillisToDateTime(overallTime),
                    onValueChange = { },
                    label = { Text("Date of visit") },
                    trailingIcon = {
                        IconButton(onClick = openDatePicker) {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = "Pick visit date"
                            )
                        }
                    },
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // — Daty SMS
                visitState.listOfSmsDates.forEachIndexed { index, value ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = formatMillisToDateTime(value),
                            onValueChange = { },
                            label = { Text("SMS Date") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    saveDateToEdit(index, value)
                                    openDatePicker()
                                }) {
                                    Icon(
                                        Icons.Default.CalendarMonth,
                                        contentDescription = "Pick SMS time"
                                    )
                                }
                            },
                            readOnly = true,
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(10.dp))
                        IconButton(onClick = { onDeleteSmsDateClick(index) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete SMS time",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }

                Spacer(Modifier.height(12.dp))

                // — Dodaj nową datę SMS
                Button(
                    onClick = addNewSmsDate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(10.dp),
                    enabled = isEnableAddNewDate
                ) {

                    Text("Add new date")
                }

                Spacer(Modifier.height(24.dp))

                // — Przyciskówka: Cancel / Confirm
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            "Cancel",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        onClick = onConfirm,
                        enabled = isEnabledSelectContact,
                    ) {
                        Text(
                            "Select contact", color = if (isEnabledSelectContact)
                                MaterialTheme.colorScheme.primary
                            else
                                Color.Gray
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AlertDialogSelectContact(
    listOfContacts: List<Contact>,
    selectedContact: Contact? = null,
    onContactPicked: (Contact) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Select Contact",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp) // np. max 300dp wysokości
            ) {
                LazyColumn {
                    items(listOfContacts) { contact ->
                        Account(
                            contact = contact,
                            mode = AccountMode.SELECTING_CONTACT,
                            isSelected = (contact == selectedContact),
                            onContactSelected = { userContact ->
                                onContactPicked(userContact)
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() },
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

fun prepareTextForSms(savedText: String, nameOfClient: String, dateOfVisit: String): String {
    return savedText.replace("[name]", nameOfClient).replace("[date]", dateOfVisit)
}



