package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetCardCpfBinding

class TunaCardCpfWidget : ConstraintLayout {

    private val binding : WidgetCardCpfBinding

    var cardCpfName: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvName.text = value }
        }

    var cardCpfNumber: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvCpf.text = value }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding = WidgetCardCpfBinding.inflate(LayoutInflater.from(context), this, true)
        context?.obtainStyledAttributes(
            attrs,
            R.styleable.CardCpfWidget,
            0, 0
        )?.apply {
            try {
                cardCpfName = getString(R.styleable.CardCpfWidget_cardCpfName)
                cardCpfNumber = getString(R.styleable.CardCpfWidget_cardCpfNumber)
            } finally {
                recycle()
            }
        }
    }

}
