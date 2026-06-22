@file:Suppress("unused")

package org.example.dyds_proyecto2_ramones.presentation.common

import kotlin.math.abs
import kotlin.math.roundToInt

fun Double.formatHoursOneDecimal(): String {
    val scaled = (this * 10).roundToInt()
    val whole = scaled / 10
    val decimal = abs(scaled % 10)
    return "$whole.$decimal"
}


