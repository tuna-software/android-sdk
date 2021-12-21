package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetItemCheckoutBinding

class TunaCheckoutItemWidget : FrameLayout {

    private val binding: WidgetItemCheckoutBinding = WidgetItemCheckoutBinding.inflate(LayoutInflater.from(context), this, true)

    var checkoutTitle: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvTitle.text = value }
        }

    var checkoutFlag: Drawable? = null
        set(value) {
            field = value
            value.let {
                binding.ivFlag.setImageDrawable(value)
                if (value == null) {
                    binding.ivFlag.visibility = GONE
                } else {
                    binding.ivFlag.visibility = VISIBLE
                }
            }
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
            R.styleable.CheckoutItemWidget,
            0,
            0
        ).apply {
            try {
                checkoutTitle = getString(R.styleable.CheckoutItemWidget_checkoutTitle)
                checkoutFlag = getDrawable(R.styleable.CheckoutItemWidget_checkoutFlag)
                checkoutLabelPrimary = getString(R.styleable.CheckoutItemWidget_checkoutLabelPrimary)
                checkoutLabelSecondary = getString(R.styleable.CheckoutItemWidget_checkoutLabelSecondary)
            } finally {
                recycle()
            }
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        binding.cardViewContent.setOnClickListener(listener)
    }
}