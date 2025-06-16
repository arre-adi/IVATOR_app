package com.example.ivator.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.ivator.R
import com.example.ivator.R.font.urbanist_normal

val dongleFontFamily = FontFamily(
    Font(R.font.dongle_regular, FontWeight.W400),
    Font(R.font.dongle_bold, FontWeight.Bold),
    Font(R.font.dongle_light, FontWeight.Light)
)


val urbanistFontFamily = FontFamily(
    Font(R.font.urbanist_normal, FontWeight.Medium),

)

val Typography = Typography(
    displayLarge = TextStyle( // Example of a large display style
        fontFamily = urbanistFontFamily,
        fontWeight = FontWeight.Black, // Or 1000, if you map it correctly
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    headlineLarge = TextStyle( // Consider this for your cardHeading
        fontFamily = urbanistFontFamily,
        fontWeight = FontWeight.Black, // Or 1000
        fontSize = 25.sp,
        color = Color(0xFF00218E) // Define colors in your theme primarily
    ),
    displaySmall = TextStyle( // Consider this for your liveDropRate
        fontFamily = urbanistFontFamily,
        fontWeight = FontWeight.ExtraBold, // Or 800
        fontSize = 40.sp,
        color = Color(0xFF1575FB)
    ),
    titleLarge = TextStyle( // Consider this for your cardUnit
        fontFamily = urbanistFontFamily,
        fontWeight = FontWeight.SemiBold, // Or 600
        fontSize = 25.sp,
        color = Color(0xFF1575FB)
    ),
    bodyLarge = TextStyle( // Consider this for your "set rate" text
        fontFamily = urbanistFontFamily,
        fontWeight = FontWeight.SemiBold, // Or 600
        fontSize = 20.sp,
        color = Color(0xFF00218E)
    ),
    labelSmall = TextStyle( // Consider this for your "change" text
        fontFamily = urbanistFontFamily,
        fontWeight = FontWeight.ExtraBold, // Or 800
        fontSize = 18.sp,
        textDecoration = TextDecoration.Underline,
        color = Color(0xFF00218E)
    )
)