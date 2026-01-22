package com.dev.test.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = GilroyFontFamily,
        fontWeight = FontWeight.Normal
    ),
    titleLarge = TextStyle(
        fontFamily = GilroyFontFamily,
        fontWeight = FontWeight.Bold
    ),
    labelLarge = TextStyle(
        fontFamily = GilroyFontFamily,
        fontWeight = FontWeight.Medium
    )

)