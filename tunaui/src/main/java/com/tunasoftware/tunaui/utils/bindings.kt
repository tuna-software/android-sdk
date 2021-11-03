package com.tunasoftware.tunaui.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("error")
fun TextInputLayout.error(error: String?) {
    setError(error)
    errorIconDrawable = null
}