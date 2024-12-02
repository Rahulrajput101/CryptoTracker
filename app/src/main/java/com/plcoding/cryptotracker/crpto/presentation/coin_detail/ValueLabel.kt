package com.plcoding.cryptotracker.crpto.presentation.coin_detail

import android.icu.number.NumberFormatter
import java.text.NumberFormat
import java.util.Locale

data class ValueLabel(
    val value: Float,
    val unit: String,
){

    fun formatted(): String{
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            val fractionDigit = when {
                value > 1000 -> 0 // If value is greater than 1000, no decimal places.
                value in 2f..999f -> 2 // For values between 2 and 999, use 2 decimal places.
                else -> 3 // For values less than 2, use 3 decimal places.
            }
            maximumFractionDigits = fractionDigit // Set the maximum allowed fraction digits.
            minimumIntegerDigits = 0 // No specific restriction on minimum integer digits.
        }
        return "${formatter.format(value)}$unit"
    }
    
}
