package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetShimmerInstallmentBinding

class TunaInstallmentShimmerWidget : FrameLayout {

    private val binding = WidgetShimmerInstallmentBinding.inflate(LayoutInflater.from(context), this, true)

    var installmentShimmerSelected: Boolean? = false
        set(value) {
            field = value
            binding.apply {
                value?.let {
                    if (it) {
                        content.setCardBackgroundColor(ContextCompat.getColor(context, R.color.tuna_divider))
                    } else {
                        content.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
                    }
                }
            }
        }

    var installmentShimmerNameWidth: Float? = 0F
        set(value) {
            field = value
            value?.let { binding.tvName.width = value.toInt() }
        }

    var installmentShimmerDescriptionWidth: Float? = 0F
        set(value) {
            field = value
            value?.let { binding.tvDescription.width = value.toInt() }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.InstallmentShimmerWidget,
            0,
            0
        ).apply {
            try {
                installmentShimmerSelected = getBoolean(R.styleable.InstallmentShimmerWidget_installmentShimmerSelected, false)
                installmentShimmerNameWidth = getDimension(R.styleable.InstallmentShimmerWidget_installmentShimmerNameWidth, 0F)
                installmentShimmerDescriptionWidth = getDimension(R.styleable.InstallmentShimmerWidget_installmentShimmerDescriptionWidth, 0F)
            } finally {
                recycle()
            }
        }
    }
}