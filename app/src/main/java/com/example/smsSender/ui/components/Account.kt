package com.example.smsSender.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.smsSender.room.contact.Contact
import com.example.smsSender.ui.utils.printPhoneNumberWithSpaces

// fun Account jest wyswietlana w trzech sytuacjach:
// 1) ContactsScreen
// 2) ListOfPlannedScreen, kiedy sie wybiera contact do ktorego przypisac zaplanowany sms
// 3) ListOfPlannedScreen, kiedy wyswietla sie zaplanowany Sms - maybe

enum class AccountMode { CONTACT_SCREEN, SELECTING_CONTACT }

@Composable
fun Account(
    contact: Contact,
    mode: AccountMode = AccountMode.CONTACT_SCREEN,
    isSelected: Boolean = false,
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
    onContactSelected: (Contact) -> Unit = {}
) {
    val isSelecting = (mode == AccountMode.SELECTING_CONTACT)


    // tło tylko w trybie SELECTING_CONTACT
    val containerColor = if (isSelecting) {
        if (isSelected)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }


    val gradient = if (isSystemInDarkTheme())  Brush.linearGradient(
        // definiujesz pary <pozycja 0–1> to <kolor>
        colorStops = arrayOf(
            0.00f to Color.Black.copy(alpha = 0.9f),    // od 0% szerokości – czarny
            0.40f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),    // od 40% – początek białego
            0.60f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),    // do 60% – koniec białego
            1.00f to Color.Black.copy(alpha = 0.4f)     // 100% – znowu czarny
        ),
        start = Offset(0f, 0f),
        end = Offset(700f, 300f)  // zakres w pikselach, jak jest w horizontalGradient
    ) else {
        Brush.linearGradient(
            colorStops = arrayOf(
                0.00f to Color.White.copy(alpha = 0.9f),                         // 0% – lekko przygaszona biel
                0.40f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),  // od 35% – bardzo subtelny akcent głównego koloru
                0.65f to MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),  // do 65%
                1.00f to Color.White.copy(alpha = 0.4f)                          // 100% – znowu lekko przygaszona biel
            ),
            start = Offset(0f, 0f),
            end = Offset(700f, 300f)  // poziomo na szerokość ~700px, możesz tu też użyć dynamicznego size.width
        )
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .padding(if (isSelecting) 4.dp else 12.dp)
            .then(
                if (isSelecting)
                    Modifier.clickable { onContactSelected(contact) }
                else
                    Modifier
            ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (mode == AccountMode.CONTACT_SCREEN) 5.dp else 0.dp
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .blur(8.dp)
                .background(Color.White.copy(alpha = 0.1f))
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(12.dp)
                )
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(brush = gradient)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(contact.firstName, overflow = TextOverflow.Ellipsis, maxLines = 1)
                        Spacer(Modifier.width(10.dp))
                        if (contact.lastName.isNotEmpty()) {
                            Text(contact.lastName, overflow = TextOverflow.Ellipsis, maxLines = 1)
                            Spacer(Modifier.width(10.dp))
                        }
                    }
                    Text(printPhoneNumberWithSpaces(contact.phoneNumber))
                }
                if (!isSelecting) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            Modifier.size(35.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit, contentDescription = "Edit", Modifier.size(35.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
