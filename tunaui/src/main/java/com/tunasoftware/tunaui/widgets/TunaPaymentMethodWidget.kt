package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetPaymentMethodBinding
import com.tunasoftware.tunaui.extensions.dp
import com.tunasoftware.tunaui.utils.disableForAccessibility


class TunaPaymentMethodWidget : FrameLayout {

    private val binding: WidgetPaymentMethodBinding

    var paymentMethodFlag: Int? = 0
        set(value) {
            field = value
            value?.let { binding.ivFlag.setImageResource(value) }
        }

    var paymentMethodLabelPrimary: String? = ""
        set(value) {
            field = value
            value?.let {
                setLabel(value, paymentMethodLabelSecondary)
            }
        }

    var paymentMethodLabelSecondary: String? = ""
        set(value) {
            field = value
            value?.let {
                setLabel(paymentMethodLabelPrimary, value)
            }
        }

    var paymentMethodSelected: Boolean? = false
        set(value) {
            field = value
            value?.let {
                with(binding){
                    if (value) {
                        ivSelected.visibility = VISIBLE
                        content.setCardBackgroundColor(ContextCompat.getColor(context, R.color.tunaui_primary_color))
                        content.cardElevation = 2.dp.toFloat()
                    } else {
                        ivSelected.visibility = GONE
                        content.setCardBackgroundColor(ContextCompat.getColor(context, R.color.tuna_background))
                        content.cardElevation = 0f
                    }
                }
                setLabel(paymentMethodLabelPrimary, paymentMethodLabelSecondary)
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding = WidgetPaymentMethodBinding.inflate(LayoutInflater.from(context), this, true)
        context.obtainStyledAttributes(
            attrs,
            R.styleable.PaymentMethodWidget,
            0,
            0
        ).apply {
            try {
                paymentMethodSelected = getBoolean(R.styleable.PaymentMethodWidget_paymentMethodSelected, false)
                paymentMethodFlag = getInt(R.styleable.PaymentMethodWidget_paymentMethodFlag, 0)
                paymentMethodLabelPrimary = getString(R.styleable.PaymentMethodWidget_paymentMethodLabelPrimary)
                paymentMethodLabelSecondary = getString(R.styleable.PaymentMethodWidget_paymentMethodLabelSecondary)
            } finally {
                recycle()
            }

            binding.content.disableForAccessibility()
        }
    }

    private fun setLabel(primary: String?, secondary: String?) {
        var text = ""

        if (!primary.isNullOrBlank()) text = "$primary "
        if (!secondary.isNullOrBlank()) text += secondary

        val spannable = SpannableString(text)

        if (paymentMethodSelected == true && !primary.isNullOrBlank()) {
            val black = ContextCompat.getColor(context, R.color.tuna_black)
            spannable.setSpan(ForegroundColorSpan(black), 0, primary.length, SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        binding.tvLabel.text = spannable
    }
}