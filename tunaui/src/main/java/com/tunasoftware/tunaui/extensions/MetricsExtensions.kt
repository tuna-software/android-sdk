package com.tunasoftware.tunaui.extensions

import android.content.Context
import android.content.res.Resources

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline val Context.displayMetrics: android.util.DisplayMetrics
    get() = resources.displayMetrics