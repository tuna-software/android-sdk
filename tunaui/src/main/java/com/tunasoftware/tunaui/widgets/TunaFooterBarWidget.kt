package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetTunaFooterBarBinding


class TunaFooterBarWidget : Toolbar {

    private val binding : WidgetTunaFooterBarBinding

    private var _onPreviousClickListener: () -> Unit = {}
    private var _onNextClickListener: () -> Unit = {}

    var isLoading = false
    set(value) {
        field = value
        binding.apply {
            if (value) {
                btnNext.visibility = View.INVISIBLE
                progressNext.visibility = View.VISIBLE
            } else {
                btnNext.visibility = View.VISIBLE
                progressNext.visibility = View.GONE
            }
        }

    }

    fun setOnPreviousClickListener(listener: () -> Unit) {
        _onPreviousClickListener = listener
    }

    fun setOnNextClickListener(listener: () -> Unit) {
        _onNextClickListener = listener
    }

    var steps: Int = 0
        set(value) {
            field = value
            binding.dotsIndicator.steps = value-1
            changeUI()
        }

    var currentStep: Int = 0
        set(value) {
            field = value
            changeUI()
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding = WidgetTunaFooterBarBinding.inflate(LayoutInflater.from(context), this, true).apply {
            btnPrevious.setOnClickListener { _onPreviousClickListener.invoke() }
            btnNext.setOnClickListener { _onNextClickListener.invoke() }

            btnPrevious.contentDescription = context.getString(R.string.tuna_accessibility_button_back_field)
            btnNext.contentDescription = context.getString(R.string.tuna_accessibility_button_next_field)
        }

    }

    private fun changeUI() {
        binding.apply {
            btnPrevious.isEnabled = currentStep > 0

            if (currentStep < (steps - 1)) {
                btnNext.text = context.getString(R.string.tuna_footer_bar_btn_next)
                btnNext.contentDescription = context.getString(R.string.tuna_accessibility_button_next_field)
            } else {
                btnNext.text = context.getString(R.string.tuna_footer_bar_btn_finish)
                btnNext.contentDescription = context.getString(R.string.tuna_accessibility_button_finish_add_card)
            }

            dotsIndicator.current = currentStep
        }
    }
}

@BindingAdapter("isLoading")
fun TunaFooterBarWidget.isLoading(isLoading: Boolean){
    this.isLoading = isLoading
}