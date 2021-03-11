package com.tunasoftware.tunaui.extensions

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout

fun View.layout(width: Int = ViewGroup.LayoutParams.WRAP_CONTENT, height: Int = WindowManager.LayoutParams.WRAP_CONTENT, layoutGravity: Int = Gravity.LEFT, weight: Float = 0f): View {
    if (this.getParent() is FrameLayout) {
        var res = FrameLayout.LayoutParams(width, height)
        res.gravity = layoutGravity
        this.setLayoutParams(res)
        return this
    } else {
        var res = LinearLayout.LayoutParams(width, height)
        res.gravity = layoutGravity
        res.weight = weight
        this.setLayoutParams(res)
        return this
    }
}

fun View.textLayoutParent(): View{
    return if (this.parent is TextInputLayout){
        this.parent as View
    } else {
        (this.parent as View).textLayoutParent()
    }
}
