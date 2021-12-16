package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetHeaderCheckoutBinding
import com.tunasoftware.tunaui.extensions.dp

class TunaCheckoutHeader : FrameLayout {

    private val binding: WidgetHeaderCheckoutBinding = WidgetHeaderCheckoutBinding.inflate(LayoutInflater.from(context), this, true)

    var checkoutTotalWithoutDiscount: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvTotalWithoutDiscount.text = value }
        }

    var checkoutDiscountPercentage: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvDiscountPercentage.text = value }
        }

    var checkoutTotalWithDiscount: String? = ""
        set(value) {
            field = value
            value?.let {
                val spannable = SpannableString(value)
                spannable.setSpan(RelativeSizeSpan(0.65f), 0, 2, 0)
                spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.tuna_gray)), 0, 2, 0)
                binding.tvTotalWithDiscount.text = spannable
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
            R.styleable.CheckoutHeaderWidget,
            0,
            0
        ).apply {
            try {
                checkoutTotalWithDiscount = getString(R.styleable.CheckoutHeaderWidget_checkoutTotalWithDiscount)
                checkoutDiscountPercentage = getString(R.styleable.CheckoutHeaderWidget_checkoutDiscountPercentage)
                checkoutTotalWithoutDiscount = getString(R.styleable.CheckoutHeaderWidget_checkoutTotalWithoutDiscount)
            } finally {
                recycle()
            }

            binding.tvTotalWithoutDiscount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            val drawable = ContextCompat.getDrawable(context, R.drawable.tuna_ic_arrow_downward)
            if (drawable != null) {
                drawable.bounds = Rect(0, 0, 16.dp, 16.dp)
                DrawableCompat.setTint(drawable, ContextCompat.getColor(context, R.color.tuna_green))
            }
            binding.tvDiscountPercentage.setCompoundDrawables(drawable, null, null, null)
        }
    }
}