package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.widget_text_view_masked.view.*
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.extensions.dp

class TextViewMaskedWidget : ConstraintLayout {

    var maskTextColor: Int = 0
        set(value) {
            field = value
            mask.setTextColor(ContextCompat.getColor(context, value))
        }

    var maskText:String = ""
        set(value) {
            field = value
            mask.text = value
        }

    var textColor: Int? = 0
        set(value) {
            field = value
            value?.let { tvValue.setTextColor(ContextCompat.getColor(context, value)) }
        }

    var value: String? = ""
        set(value) {
            field = value
            value?.let {
                tvValue.text = value
                mask.visibility = if (value.isNotBlank()) View.GONE else View.VISIBLE
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
                value = getString(R.styleable.TextViewMaskedWidget_value)
                maskText = getString(R.styleable.TextViewMaskedWidget_maskText)?:""
            } finally {
                recycle()
            }
        }
    }

}
