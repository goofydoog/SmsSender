package com.example.smsSender.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with

import androidx.compose.ui.unit.sp
import com.example.sms_sender.R

// 1️⃣ Twoje fontProvider, DisplayFont i BodyFont…

private val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs
)

private val DisplayFont = FontFamily(
    Font(googleFont = GoogleFont("Bebas Neue"), fontProvider = fontProvider)
)

private val BodyFont = FontFamily(
    Font(googleFont = GoogleFont("Poppins"), fontProvider = fontProvider)
)


// 2️⃣ Nowa typografia

val AppTypography = Typography(
    // dla dużych nagłówków (np. ekranów, tytułów sekcji)…
    displayLarge = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Normal,
        fontSize   = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Normal,
        fontSize   = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Normal,
        fontSize   = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // dla standardowego tekstu (body)
    bodyLarge = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // możesz też nadpisać pozostałe style, np. title, label itp., jeśli chcesz używać DisplayFont lub BodyFont:
    titleLarge = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.Medium,
        fontSize   = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    labelSmall = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Medium,
        fontSize   = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
