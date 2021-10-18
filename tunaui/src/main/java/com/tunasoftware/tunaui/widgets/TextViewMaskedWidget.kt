package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetTextViewMaskedBinding

class TextViewMaskedWidget : ConstraintLayout {

    private val binding : WidgetTextViewMaskedBinding

    var maskTextColor: Int = 0
        set(value) {
            field = value
            binding.mask.setTextColor(ContextCompat.getColor(context, value))
        }

    var maskText:String = ""
        set(value) {
            field = value
            binding.mask.text = value
        }

    var textColor: Int? = 0
        set(value) {
            field = value
            value?.let { binding.tvValue.setTextColor(ContextCompat.getColor(context, value)) }
        }

    var value: String? = ""
        set(value) {
            field = value
            value?.let {
                binding.tvValue.text = value
                binding.mask.visibility = if (value.isNotBlank()) View.GONE else View.VISIBLE
            }
        }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding = WidgetTextViewMaskedBinding.inflate(LayoutInflater.from(context), this, true)
        context?.obtainStyledAttributes(
            attrs,
            R.styleable.TextViewMaskedWidget,
            0, 0
        )?.apply {
            try {
                maskTextColor = getResourceId(
                    R.styleable.TextViewMaskedWidget_maskTextColor,
                    android.R.color.transparent
                )
                textColor = getResourceId(
                    R.styleable.TextViewMaskedWidget_textColor,
                    android.R.color.white
                )
                value = getString(R.styleable.TextViewMaskedWidget_value)
                maskText = getString(R.styleable.TextViewMaskedWidget_maskText)?:""
            } finally {
                recycle()
            }
        }
    }

}
