package com.example.playlistmaker.player.ui

import android.content.Context
import android.util.TypedValue

//конвертация dp в px
fun Context.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    ).toInt()
}
//перегрузка для Float
fun Context.dpToPx(dp: Float, asFloat: Boolean = false): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )
}