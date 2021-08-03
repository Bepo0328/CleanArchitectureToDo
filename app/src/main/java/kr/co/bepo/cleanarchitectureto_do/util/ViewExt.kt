package kr.co.bepo.cleanarchitectureto_do.util

import android.view.View
import androidx.annotation.ColorRes

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toInVisible() {
    visibility = View.INVISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}

fun View.color(@ColorRes colorResId: Int) = context.getColor(colorResId)