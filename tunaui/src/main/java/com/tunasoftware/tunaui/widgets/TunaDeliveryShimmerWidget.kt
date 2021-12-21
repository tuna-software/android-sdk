package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetShimmerDeliveryBinding

class TunaDeliveryShimmerWidget : FrameLayout {

    private val binding = WidgetShimmerDeliveryBinding.inflate(LayoutInflater.from(context), this, true)

    var deliveryShimmerSelected: Boolean? = false
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

    var deliveryShimmerNameWidth: Float? = 0F
        set(value) {
            field = value
            value?.let { binding.tvName.width = value.toInt() }
        }

    var deliveryShimmerDescriptionWidth: Float? = 0F
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
            R.styleable.DeliveryShimmerWidget,
            0,
            0
        ).apply {
            try {
                deliveryShimmerSelected = getBoolean(R.styleable.DeliveryShimmerWidget_deliveryShimmerSelected, false)
                deliveryShimmerNameWidth = getDimension(R.styleable.DeliveryShimmerWidget_deliveryShimmerNameWidth, 0F)
                deliveryShimmerDescriptionWidth = getDimension(R.styleable.DeliveryShimmerWidget_deliveryShimmerDescriptionWidth, 0F)
            } finally {
                recycle()
            }
        }
    }
}