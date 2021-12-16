package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetAddressCheckoutBinding

class TunaCheckoutAddressWidget : FrameLayout {

    private val binding: WidgetAddressCheckoutBinding = WidgetAddressCheckoutBinding.inflate(LayoutInflater.from(context), this, true)

    var checkoutTitle: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvTitle.text = value }
        }

    var checkoutLabelPrimary: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvLabelPrimary.text = value }
        }

    var checkoutLabelSecondary: String? = ""
        set(value) {
            field = value
            value.let {
                binding.tvLabelSecondary.text = value
                if (value.isNullOrEmpty()) {
                    binding.tvLabelSecondary.visibility = GONE
                } else {
                    binding.tvLabelSecondary.visibility = VISIBLE
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
        context.obtainStyledAttributes(
            attrs,
            R.styleable.CheckoutAddressWidget,
            0,
            0
        ).apply {
            try {
                checkoutTitle = getString(R.styleable.CheckoutAddressWidget_checkoutAddressTitle)
                checkoutLabelPrimary = getString(R.styleable.CheckoutAddressWidget_checkoutAddressLabelPrimary)
                checkoutLabelSecondary = getString(R.styleable.CheckoutAddressWidget_checkoutAddressLabelSecondary)
            } finally {
                recycle()
            }
        }
    }
}