package com.example.smsSender.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.smsSender.ContactModel
import com.example.smsSender.ContactsViewModel
import com.example.smsSender.room.contact.Contact
import com.example.smsSender.ui.components.Account
import com.example.smsSender.ui.utils.PhoneNumberVisualTransformation
import kotlinx.coroutines.delay

@Composable
fun ContactsScreen(contactViewModel: ContactsViewModel) {

    val contactState by contactViewModel.contactDetails.collectAsState()
    val contacts by contactViewModel.contacts.collectAsState()

    var showDialogAdding by remember { mutableStateOf(false) }
    var showDialogEditing by remember { mutableStateOf(false) }

    var editingContact by remember { mutableStateOf<Contact?>(null) }

    val context = LocalContext.current


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn {
            itemsIndexed(
                items = contacts,
                key = { _, contact -> contact.id }
            ) { index, contact ->
                var visible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(100L * index)
                    visible = true
                }

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(500)) + expandVertically(),
                        exit  = fadeOut(animationSpec = tween(300)) + shrinkVertically()
                    ) {
                        Account(
                            onDelete = {
                                contactViewModel.deleteContact(
                                    contact = contact,
                                    onSuccess = {
                                        Toast.makeText(context, "Contact deleted", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                )
                            },
                            onEdit = {
                                editingContact = contact
                                editingContact?.let { contactViewModel.transferData(it) }
                                showDialogEditing = true
                            },
                            contact = contact
                        )
                    }

                }
            }


        AddContactFAB(
            onClick = {
                showDialogAdding = true
                editingContact = null
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        )

    }

    if (showDialogAdding) {
        AlertDialogAddContact(
            contactState = contactState,
            contactViewModel = contactViewModel,
            changeState = { newValue -> showDialogAdding = newValue },
            context = context
        )
    }

    if (showDialogEditing) {
        AlertDialogAddContact(
            contactState = contactState,
            isEditingContact = true,
            contactViewModel = contactViewModel,
            changeState = { newValue -> showDialogEditing = newValue },
            context = context
        )
    }
}


@Composable
fun AddContactFAB(onClick: () -> Unit, modifier: Modifier) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .padding(32.dp)
    ) {
        Icon(
            Icons.Default.Add, contentDescription = "Add contact",
        )
    }
}

@Composable
fun AlertDialogAddContact(
    contactState: ContactModel,
    isEditingContact: Boolean = false,
    contactViewModel: ContactsViewModel,
    context: Context,
    changeState: (Boolean) -> Unit
) {

    val isEnabled = contactState.firstName.isNotBlank() && contactState.phoneNumber.length == 9

    val gradient = if (isSystemInDarkTheme()) {
        Brush.linearGradient(
            0.00f to Color.Black.copy(alpha = 0.9f),
            0.40f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            0.60f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            1.00f to Color.Black.copy(alpha = 0.4f),
            start = Offset.Zero,
            end   = Offset(700f, 0f)
        )
    } else {
        Brush.linearGradient(
            colorStops = arrayOf(
                0.00f to Color.White.copy(alpha = 0.9f),                         // 0% – lekko przygaszona biel
                0.40f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),  // od 35% – bardzo subtelny akcent głównego koloru
                0.60f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),  // do 65%
                1.00f to Color.White.copy(alpha = 0.4f)                          // 100% – znowu lekko przygaszona biel
            ),
            start = Offset(0f, 0f),
            end = Offset(700f, 300f)  // poziomo na szerokość ~700px, możesz tu też użyć dynamicznego size.width
        )
    }

    Dialog(
        onDismissRequest = {
            changeState(false)
            contactViewModel.clearData()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .background(brush = gradient, shape = RoundedCornerShape(16.dp))
                .border(
                    0.5f.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    RoundedCornerShape(16.dp)
                )
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = if (isEditingContact) "Edit Contact" else "Add Contact",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = contactState.firstName,
                    onValueChange = { contactViewModel.editName(it) },
                    label = { Text("First Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = contactState.lastName.orEmpty(),
                    onValueChange = { contactViewModel.editLastName(it) },
                    label = { Text("Last Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = contactState.phoneNumber,
                    onValueChange = { raw ->
                        if (raw.length <= 9) contactViewModel.editPhoneNumber(raw.filter { it.isDigit() })
                    },
                    label = { Text("Phone Number") },
                    leadingIcon = { Text("+48") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = PhoneNumberVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = {
                        changeState(false)
                        contactViewModel.clearData()
                    }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            if (isEditingContact) {
                                contactViewModel.updateContact(
                                    Contact(
                                        id = contactState.id,
                                        firstName = contactState.firstName,
                                        lastName = contactState.lastName.orEmpty(),
                                        phoneNumber = contactState.phoneNumber
                                    ),
                                    onSuccess = {
                                        changeState(false)
                                        contactViewModel.clearData()
                                        Toast.makeText(context, "Contact changed", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            } else {
                                contactViewModel.addContactToBd(
                                    Contact(
                                        id = 0,
                                        firstName = contactState.firstName,
                                        lastName = contactState.lastName.orEmpty(),
                                        phoneNumber = contactState.phoneNumber
                                    ),
                                    onSuccess = {
                                        changeState(false)
                                        contactViewModel.clearData()
                                        Toast.makeText(context, "Contact added", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        },
                        enabled = isEnabled
                    ) {
                        Text(
                            text = if (isEditingContact) "Save" else "Add Contact",
                            color = if (isEnabled)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
                        )
                    }
                }
            }
        }
    }
}






