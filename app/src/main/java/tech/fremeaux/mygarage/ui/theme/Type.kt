package tech.fremeaux.mygarage.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import tech.fremeaux.mygarage.R

val Rajdhani = FontFamily(
    Font(R.font.rajdhani_bold,     FontWeight.Bold),
    Font(R.font.rajdhani_semibold, FontWeight.SemiBold),
)

val DmSans = FontFamily(
    Font(R.font.dm_sans_regular, FontWeight.Normal),
    Font(R.font.dm_sans_medium,  FontWeight.Medium),
)

val MyGarageTypography = Typography(
    displayLarge   = TextStyle(fontFamily = Rajdhani, fontWeight = FontWeight.Bold,     fontSize = 32.sp, letterSpacing = 2.sp),
    headlineMedium = TextStyle(fontFamily = Rajdhani, fontWeight = FontWeight.Bold,     fontSize = 22.sp),
    titleLarge     = TextStyle(fontFamily = Rajdhani, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    bodyLarge      = TextStyle(fontFamily = DmSans,   fontWeight = FontWeight.Normal,   fontSize = 15.sp),
    bodyMedium     = TextStyle(fontFamily = DmSans,   fontWeight = FontWeight.Normal,   fontSize = 13.sp),
    bodySmall      = TextStyle(fontFamily = DmSans,   fontWeight = FontWeight.Normal,   fontSize = 11.sp),
)