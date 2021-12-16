package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetPromoCodeCheckoutBinding
import com.tunasoftware.tunaui.extensions.dp

class TunaCheckoutPromoCodeWidget : FrameLayout {

    private val binding: WidgetPromoCodeCheckoutBinding = WidgetPromoCodeCheckoutBinding.inflate(LayoutInflater.from(context), this, true)

    var checkoutTitle: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvTitle.text = value }
        }

    var checkoutLabelSuccess: String? = ""
        set(value) {
            field = value
            value.let {
                binding.tvLabelSuccess.text = value
                if (it.isNullOrBlank()) {
                    binding.tvLabelSuccess.visibility = GONE
                } else {
                    binding.tvLabelError.visibility = GONE
                    binding.tvLabelSuccess.visibility = VISIBLE
                }
            }
        }

    var checkoutLabelError: String? = ""
        set(value) {
            field = value
            value.let {
                binding.tvLabelError.text = value
                if (it.isNullOrBlank()) {
                    binding.tvLabelError.visibility = GONE
                } else {
                    binding.tvLabelError.visibility = VISIBLE
                    binding.tvLabelSuccess.visibility = GONE
                }
            }
        }

    var checkoutMessageError: String? = ""
        set(value) {
            field = value
            value.let {
                binding.tvMessageError.text = value
                if (it.isNullOrBlank()) {
                    binding.tvMessageError.visibility = GONE
                } else {
                    binding.tvMessageError.visibility = VISIBLE
                    binding.tvLabelSuccess.visibility = GONE
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
            R.styleable.CheckoutPromoCodeWidget,
            0,
            0
        ).apply {
            try {
                checkoutTitle = getString(R.styleable.CheckoutPromoCodeWidget_checkoutPromoCodeTitle)
                checkoutLabelSuccess = getString(R.styleable.CheckoutPromoCodeWidget_checkoutPromoCodeLabelSuccess)
                checkoutLabelError = getString(R.styleable.CheckoutPromoCodeWidget_checkoutPromoCodeLabelError)
                checkoutMessageError = getString(R.styleable.CheckoutPromoCodeWidget_checkoutPromoCodeMessageError)

                ContextCompat.getDrawable(context, R.drawable.tuna_ic_check_circle_outline)?.let {
                    val drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(it.toBitmap(), 16.dp, 16.dp, true))
                    binding.tvLabelSuccess.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                }

                ContextCompat.getDrawable(context, R.drawable.tuna_ic_close_circle_outline)?.let {
                    val drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(it.toBitmap(), 16.dp, 16.dp, true))
                    binding.tvLabelError.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                }
            } finally {
                recycle()
            }
        }
    }
}