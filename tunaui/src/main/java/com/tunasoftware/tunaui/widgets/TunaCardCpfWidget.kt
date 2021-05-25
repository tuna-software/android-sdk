package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tunasoftware.tunaui.R
import kotlinx.android.synthetic.main.widget_card_cpf.view.*

class TunaCardCpfWidget : ConstraintLayout {

    var cardCpfName: String? = ""
        set(value) {
            field = value
            value?.let { tvName.text = value }
        }

    var cardCpfNumber: String? = ""
        set(value) {
            field = value
            value?.let { tvCpf.text = value }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        View.inflate(context, R.layout.widget_card_cpf, this)
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
