package com.example.smsSender.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smsSender.DescriptionViewModel
import com.example.smsSender.ui.utils.formatMillisToDateTime

@Composable
fun DescriptionScreen(descriptionViewModel: DescriptionViewModel) {

    val savedText by descriptionViewModel.visitText.collectAsState(initial = "")

    var tfState by remember {
        mutableStateOf(
            TextFieldValue(
                text = savedText,
                selection = TextRange(savedText.length)
            )
        )
    }

    var isEditing by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(savedText) {
        tfState = TextFieldValue(
            text = savedText,
            selection = TextRange(savedText.length)
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 54.dp, end = 24.dp, start = 24.dp, bottom = 24.dp)
        ) {
            OutlinedTextField(
                value = tfState,
                onValueChange = { new ->
                    tfState = new
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .heightIn(min = 100.dp)
                    .height(300.dp),
                textStyle = LocalTextStyle.current.copy(
                    lineHeight = 20.sp
                ),
                label = { Text("Default text a client will receive through SMS") },
                placeholder = { Text("Type ... ") },
                singleLine = false,
                maxLines = 10,
                readOnly = !isEditing,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Default,
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("Instruction:")
            Text(
                "\nIf you add [name], it will automatically be replaced with the contact’s name.\"", style = MaterialTheme.typography.bodySmall
            )
            Text(
                "\nIf you add [date], it will automatically be replaced with the visit date.", style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "\nExample: \"Hello, [name], your visit is on [date]. Don’t forget!\"\n" +
                        "It will be converted to: \"Hello Bob, your visit is on 12.12.2025 12:00. Don’t forget!\"",
                style = MaterialTheme.typography.bodySmall
            )

        }


        if (isEditing) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
                keyboardController?.show()
            }
        }

        FloatingActionButton(
            onClick = {
                if (!isEditing) {
                    isEditing = true
                } else {
                    descriptionViewModel.saveText(tfState.text)
                    isEditing = false
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
                .imePadding()
                .then(
                    if (!isKeyboardVisible() && isEditing) {
                        Modifier
                    } else if (isEditing) {
                        Modifier.offset(y = 100.dp)
                    } else {
                        Modifier
                    }
                )
        ) {
            if (isEditing) {
                Icon(Icons.Default.Save, contentDescription = "Save Text")
            } else {
                Icon(Icons.Default.Edit, contentDescription = "Edit Text")
            }
        }
    }
}

@Composable
fun isKeyboardVisible(): Boolean {
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    return imeBottom > 0
}