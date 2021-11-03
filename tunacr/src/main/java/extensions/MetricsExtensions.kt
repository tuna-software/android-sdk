package extensions

import android.content.Context
import android.content.res.Resources

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Double.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline val Context.displayMetrics: android.util.DisplayMetrics
    get() = resources.displayMetrics