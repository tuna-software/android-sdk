package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetCardBinding
import com.tunasoftware.tunaui.domain.entities.TunaCardFlag


class TunaCardWidget : ConstraintLayout {

    private val binding: WidgetCardBinding

    var cardNumber: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvNumber.value = value }
        }

    var cardName: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvName.value = value }
        }

    var cardExpirationDate: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvValidate.value = value }
        }

    var cardFlag: TunaCardFlag? = TunaCardFlag.UNDEFINED
        set(value) {
            field = value
            value?.let {
                with(binding){
                    ivFlag.backgroundTintList = null
                    when (it) {
                        TunaCardFlag.HIPERCARD -> {
                            ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_hipercard)
                            content.setBackgroundResource(R.drawable.tuna_bg_card_hipercard)
                        }
                        TunaCardFlag.ELO -> {
                            ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_elo)
                            content.setBackgroundResource(R.drawable.tuna_bg_card_elo)
                        }
                        TunaCardFlag.AMEX -> {
                            ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_amex)
                            content.setBackgroundResource(R.drawable.tuna_bg_card_amex)
                        }
                        TunaCardFlag.DINERS -> {
                            ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_diners)
                            content.setBackgroundResource(R.drawable.tuna_bg_card_diners)
                        }
                        TunaCardFlag.VISA -> {
                            ivFlag.setImageResource(R.drawable.tuna_ic_flag_card_visa)
                            content.setBackgroundResource(R.drawable.tuna_bg_card_visa)
                        }
                        TunaCardFlag.MASTER -> {
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
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding = WidgetCardBinding.inflate(LayoutInflater.from(context), this, true)
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
        binding.ivFlag.backgroundTintList = ContextCompat.getColorStateList(context, R.color.tuna_bg_text_view_masked_20)
    }

}
