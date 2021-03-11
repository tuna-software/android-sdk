package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.widget_text_view_masked.view.*
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.extensions.px

class TextViewMaskedWidget : ConstraintLayout {

    var maskTextColor: Int = 0
        set(value) {
            field = value
            mask.backgroundTintList = ContextCompat.getColorStateList(context, value)
        }

    var textColor: Int? = 0
        set(value) {
            field = value
            value?.let { tvValue.setTextColor(ContextCompat.getColor(context, value)) }
        }

    var maskWidth: Int = 0
        set(value) {
            field = value
            mask.layoutParams.width = value.px
        }

    var value: String? = ""
        set(value) {
            field = value
            value?.let {
                tvValue.text = value
                mask.visibility = if (value.isNotBlank()) View.GONE else View.VISIBLE
            }
        }

    var maskActive: Boolean? = false
        set(value) {
            field = value
            value?.let {
                maskTextColor = if (value) {
                    R.color.tuna_bg_text_view_masked_50
                } else {
                    R.color.tuna_bg_text_view_masked_20
                }
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        View.inflate(context, R.layout.widget_text_view_masked, this)
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
                maskWidth = getInt(R.styleable.TextViewMaskedWidget_maskWidth, 0)
                maskActive = getBoolean(R.styleable.TextViewMaskedWidget_maskActive, false)
                value = getString(R.styleable.TextViewMaskedWidget_value)
            } finally {
                recycle()
            }
        }
    }

}
