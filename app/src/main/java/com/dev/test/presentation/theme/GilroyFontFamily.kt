package com.dev.test.presentation.theme


import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.dev.test.R

val GilroyFontFamily = FontFamily(
    Font(
        resId = R.font.gilroy_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.gilroy_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.gilroy_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.gilroy_bold,
        weight = FontWeight.Bold
    )
)
