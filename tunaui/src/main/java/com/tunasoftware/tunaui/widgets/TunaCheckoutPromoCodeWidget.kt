package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetPromoCodeCheckoutBinding
import com.tunasoftware.tunaui.extensions.dp

class TunaCheckoutPromoCodeWidget : FrameLayout {

    private val _binding: WidgetPromoCodeCheckoutBinding = WidgetPromoCodeCheckoutBinding.inflate(LayoutInflater.from(context), this, true)
    private var _onRedeemListener: (promoCode: String) -> Unit = {}
    private var _onRemovedListener: () -> Unit = {}

    var checkoutTitle: String? = ""
        set(value) {
            field = value
            value?.let { _binding.tvTitle.text = value }
        }

    var checkoutLabelSuccess: String? = ""
        set(value) {
            field = value
            value.let {
                _binding.tvLabelSuccess.text = value
                if (it.isNullOrBlank()) {
                    _binding.tvLabelSuccess.visibility = GONE
                } else {
                    _binding.tvLabelError.visibility = GONE
                    _binding.tvMessageError.visibility = GONE
                    _binding.tvLabelSuccess.visibility = VISIBLE
                }
            }
        }

    var checkoutLabelError: String? = ""
        set(value) {
            field = value
            value.let {
                _binding.tvLabelError.text = value
                if (it.isNullOrBlank()) {
                    _binding.tvLabelError.visibility = GONE
                } else {
                    _binding.tvLabelError.visibility = VISIBLE
                    _binding.tvLabelSuccess.visibility = GONE
                }
            }
        }

    var checkoutMessageError: String? = ""
        set(value) {
            field = value
            value.let {
                _binding.tvMessageError.text = value
                if (it.isNullOrBlank()) {
                    _binding.tvMessageError.visibility = GONE
                } else {
                    _binding.tvMessageError.visibility = VISIBLE
                    _binding.tvLabelSuccess.visibility = GONE
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
                    _binding.tvLabelSuccess.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                }

                ContextCompat.getDrawable(context, R.drawable.tuna_ic_close_circle_outline)?.let {
                    val drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(it.toBitmap(), 16.dp, 16.dp, true))
                    _binding.tvLabelError.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                }
            } finally {
                recycle()
            }

            _binding.edtPromoCode.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    _binding.tvMessageError.visibility = GONE
                    _binding.tvLabelError.visibility = GONE
                    _binding.tvLabelSuccess.visibility = GONE
                    _binding.progress.visibility = GONE

                    if (s.isNullOrBlank()) {
                        _binding.btnRedeem.visibility = GONE
                    } else {
                        _binding.btnRedeem.visibility = VISIBLE
                    }
                }
            })

            _binding.btnRedeem.setOnClickListener {
                _onRedeemListener.invoke(_binding.edtPromoCode.text.toString())
            }

            _binding.btnRemovePromoCode.setOnClickListener {
                checkoutLabelSuccess = ""
                _binding.edtPromoCode.setText("")
                _binding.layoutPromoCodeSuccess.visibility = GONE
                _binding.edtPromoCode.visibility = VISIBLE
                _binding.edtPromoCode.requestFocus()
                _onRemovedListener.invoke()
            }
        }
    }

    fun setOnRedeemListener(listener: (promoCode: String) -> Unit) {
        this._onRedeemListener = listener
    }

    fun setOnRemovedListener(listener: () -> Unit) {
        this._onRemovedListener = listener
    }

    fun setState(state: State) {
        when (state) {
            is State.Loading -> {
                _binding.btnRedeem.visibility = GONE
                if (state.loading) {
                    _binding.progress.visibility = VISIBLE
                } else {
                    _binding.progress.visibility = GONE
                }
            }
            is State.Success -> {
                _binding.btnRedeem.visibility = GONE
                _binding.edtPromoCode.visibility = GONE
                _binding.tvPromoCode.text = _binding.edtPromoCode.text
                _binding.layoutPromoCodeSuccess.visibility = VISIBLE
                checkoutLabelSuccess = state.value
            }
            is State.Error -> {
                _binding.btnRedeem.visibility = GONE
                checkoutLabelError = state.error
                checkoutMessageError = state.message
            }
        }
    }
}

sealed class State {
    class Loading(val loading: Boolean): State()
    class Success(val value: String): State()
    class Error(val error: String, val message: String): State()
}