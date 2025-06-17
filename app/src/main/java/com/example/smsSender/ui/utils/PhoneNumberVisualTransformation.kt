package com.example.smsSender.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

fun printPhoneNumberWithSpaces(phoneNumber: String): String {
    return phoneNumber.chunked(3).joinToString(" ")
}


class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 1) wyciągamy tylko cyfry
        val digits = text.text.filter { it.isDigit() }
        // 2) formatujemy: grupujemy po 3, łączymy spacjami
        val formatted = digits.chunked(3).joinToString(" ")
        // 3) definiujemy mapowanie pozycji kursora
        val offsetMap = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // ile spacji znalazło się **przed** tą pozycją?
                val spacesBefore = if (offset == 0) 0 else (offset - 1) / 3
                return offset + spacesBefore
            }

            override fun transformedToOriginal(offset: Int): Int {
                // ile spacji było w sformatowanym tekście **przed** tą pozycją?
                val spaces = formatted
                    .take(offset)
                    .count { it == ' ' }
                return offset - spaces
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMap)
    }
}