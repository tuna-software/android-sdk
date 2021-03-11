package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tunasoftware.tunaui.R
import kotlinx.android.synthetic.main.widget_card.view.*

class TunaCardWidget : ConstraintLayout {

    var cardNumber: String? = ""
        set(value) {
            field = value
            value?.let {
                tvNumber.value = value
//                widgetFlag.apply { cardNumber = value }
                if (value.isNotEmpty()) {
                    content.setBackgroundResource(R.drawable.card_enabled_background)
                } else {
                    content.setBackgroundResource(R.drawable.card_input_background)
                }
            }
        }

    var cardName: String? = ""
        set(value) {
            field = value
            value?.let { tvName.value = value }
        }

    var cardExpirationDate: String? = ""
        set(value) {
            field = value
            value?.let { tvValidate.value = value }
        }

    var cardCvv: String? = ""
        set(value) {
            field = value
            value?.let { tvCvv.value = value }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        View.inflate(context, R.layout.widget_card, this)
        context?.obtainStyledAttributes(
            attrs,
            R.styleable.CardWidget,
            0, 0
        )?.apply {
            try {
                cardNumber = getString(R.styleable.CardWidget_cardNumber)
                cardName = getString(R.styleable.CardWidget_cardName)
                cardExpirationDate = getString(R.styleable.CardWidget_cardExpirationDate)
                cardCvv = getString(R.styleable.CardWidget_cardCvv)
            } finally {
                recycle()
            }
        }
    }

}
