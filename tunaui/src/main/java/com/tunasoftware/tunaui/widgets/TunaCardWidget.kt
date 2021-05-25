package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.utils.CardMatcher
import kotlinx.android.synthetic.main.widget_card.view.*

class TunaCardWidget : ConstraintLayout {

    var cardNumber: String? = ""
        set(value) {
            field = value
            value?.let { tvNumber.value = value }
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

    var cardFlag: CardMatcher.CardFlag? = CardMatcher.CardFlag.UNDEFINED
        set(value) {
            field = value
            value?.let {
                ivFlag.backgroundTintList = null
                when (it) {
                    CardMatcher.CardFlag.HIPERCARD -> {
                        ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_hipercard)
                        content.setBackgroundResource(R.drawable.tuna_bg_card_hipercard)
                    }
                    CardMatcher.CardFlag.ELO -> {
                        ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_elo)
                        content.setBackgroundResource(R.drawable.tuna_bg_card_elo)
                    }
                    CardMatcher.CardFlag.AMEX -> {
                        ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_amex)
                        content.setBackgroundResource(R.drawable.tuna_bg_card_amex)
                    }
                    CardMatcher.CardFlag.DINERS -> {
                        ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_diners)
                        content.setBackgroundResource(R.drawable.tuna_bg_card_diners)
                    }
                    CardMatcher.CardFlag.VISA -> {
                        ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_visa)
                        content.setBackgroundResource(R.drawable.tuna_bg_card_visa)
                    }
                    CardMatcher.CardFlag.MASTER -> {
                        ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_master)
                        content.setBackgroundResource(R.drawable.tuna_bg_card_master)
                    }
                    else -> {
                        ivFlag.setImageDrawable(null)
                        content.background = ContextCompat.getDrawable(context, R.drawable.tuna_bg_card_master)
                        ivFlag.backgroundTintList = ContextCompat.getColorStateList(context, R.color.tuna_bg_text_view_masked_20)
                    }
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
            } finally {
                recycle()
            }
        }
        ivFlag.backgroundTintList = ContextCompat.getColorStateList(context, R.color.tuna_bg_text_view_masked_20)
    }

}
