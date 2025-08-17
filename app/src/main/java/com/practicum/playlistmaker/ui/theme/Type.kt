package com.practicum.playlistmaker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R

/*<item name="android:textSize">16sp</item>
<item name="android:fontFamily">@font/ys_display_regular</item>
<item name="android:textFontWeight">400</item>*/

/*<item name="android:fontFamily">@font/ys_display_regular</item>
<item name="android:textFontWeight">400</item>
<item name="android:textColor">?attr/colorPrimary</item>
<item name="android:textSize">11sp</item>*/

private val fontFamilyYS = FontFamily(
    listOf(
        Font(
            resId = R.font.ys_display_regular
        ),
        Font(
            resId = R.font.ys_display_bold,
            weight = FontWeight.Bold
        ),
        Font(
            resId = R.font.ys_display_heavy,
            weight = FontWeight.Black
        ),
        Font(
            resId = R.font.ys_display_light,
            weight = FontWeight.Light
        ),
        Font(
            resId = R.font.ys_display_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resId = R.font.ys_display_medium,
            weight = FontWeight.Normal
        ),
        Font(
            resId = R.font.ys_display_thin,
            weight = FontWeight.Thin
        )
    )
)

val typography = Typography(
    titleSmall = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = fontFamilyYS
    ),
    titleMedium = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = fontFamilyYS
    ),
    headlineMedium = TextStyle(
        fontSize = 19.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = fontFamilyYS
    ),
    bodySmall = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = fontFamilyYS
    ),
    bodyMedium = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = fontFamilyYS,
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = fontFamilyYS,
    ),
    labelMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = fontFamilyYS,
    )
)