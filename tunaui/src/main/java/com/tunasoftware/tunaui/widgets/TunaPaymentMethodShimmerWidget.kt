package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetShimmerPaymentMethodBinding


class TunaPaymentMethodShimmerWidget : FrameLayout {

    private val binding : WidgetShimmerPaymentMethodBinding

    var paymentMethodShimmerSelected: Boolean? = false
        set(value) {
            field = value
            binding.apply {
                value?.let {
                    if (it) {
                        content.setCardBackgroundColor(context.resources.getColor(R.color.tuna_divider))
                        ivSelected.visibility = View.VISIBLE
                    } else {
                        content.setCardBackgroundColor(context.resources.getColor(android.R.color.transparent))
                        ivSelected.visibility = View.GONE
                    }
                }
            }
        }

    var paymentMethodShimmerLabelWidth: Float? = 0F
        set(value) {
            field = value
            value?.let { binding.tvLabel.width = value.toInt() }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding = WidgetShimmerPaymentMethodBinding.inflate(LayoutInflater.from(context), this, true)
        context.obtainStyledAttributes(
            attrs,
            R.styleable.PaymentMethodShimmerWidget,
            0,
            0
        ).apply {
            try {
                paymentMethodShimmerSelected = getBoolean(R.styleable.PaymentMethodShimmerWidget_paymentMethodShimmerSelected, false)
                paymentMethodShimmerLabelWidth = getDimension(R.styleable.PaymentMethodShimmerWidget_paymentMethodShimmerLabelWidth, 0F)
            } finally {
                recycle()
            }
        }
    }
}