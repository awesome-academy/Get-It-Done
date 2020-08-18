package com.sunasterisk.getitdone.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.content.ContextCompat

@SuppressLint("ClickableViewAccessibility")
fun TextView.setOnTouchDrawableEndListener(
    onMainTouch: () -> Unit = {},
    onDrawableTouch: () -> Unit
) {
    this.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            this.compoundDrawables[Constants.DRAWABLE_RIGHT]?.let {
                if (event.x >= (this.right - it.bounds.width() - this.paddingEnd)) {
                    onDrawableTouch()
                    return@setOnTouchListener true
                } else {
                    onMainTouch()
                }
            }
        }
        return@setOnTouchListener false
    }
}

fun TextView.addDrawableEnd(drawable: Int) {
    val imgDrawable = ContextCompat.getDrawable(context, drawable)
    compoundDrawablePadding = 10
    setCompoundDrawablesWithIntrinsicBounds(null, null, imgDrawable, null)
}

fun TextView.removeDrawables() {
    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
}
