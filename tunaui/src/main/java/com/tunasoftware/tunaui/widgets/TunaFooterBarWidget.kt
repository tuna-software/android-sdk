package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.tunasoftware.tunaui.R
import kotlinx.android.synthetic.main.widget_tuna_footer_bar.view.*

class TunaFooterBarWidget : Toolbar {

    private var _onPreviousClickListener: () -> Unit = {}
    private var _onNextClickListener: () -> Unit = {}

    var isLoading = false
    set(value) {
        field = value
        if (value) {
            btnNext.visibility = View.INVISIBLE
            progress_next.visibility = View.VISIBLE
        } else {
            btnNext.visibility = View.VISIBLE
            progress_next.visibility = View.GONE
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
            dotsIndicator.steps = value-1
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
        View.inflate(context, R.layout.widget_tuna_footer_bar, this)

        btnPrevious.setOnClickListener { _onPreviousClickListener.invoke() }
        btnNext.setOnClickListener { _onNextClickListener.invoke() }

        btnPrevious.contentDescription = context.getString(R.string.tuna_accessibility_button_back_field)
        btnNext.contentDescription = context.getString(R.string.tuna_accessibility_button_next_field)
    }

    private fun changeUI() {
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

@BindingAdapter("isLoading")
fun TunaFooterBarWidget.isLoading(isLoading: Boolean){
    this.isLoading = isLoading
}