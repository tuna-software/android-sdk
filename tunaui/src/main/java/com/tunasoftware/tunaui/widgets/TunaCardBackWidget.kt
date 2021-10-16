package com.tunasoftware.tunaui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.WidgetCardBackBinding
import com.tunasoftware.tunaui.domain.entities.TunaCardFlag
import com.tunasoftware.tunaui.utils.CardMatcher

class TunaCardBackWidget : ConstraintLayout {

    private val binding : WidgetCardBackBinding

    var cardCvv: String? = ""
        set(value) {
            field = value
            value?.let { binding.tvCvv.text = value }
        }

    var cardFlag: TunaCardFlag? = TunaCardFlag.UNDEFINED
        set(value) {
            field = value
            with(binding){
                value?.let {
                    when (it) {
                        TunaCardFlag.HIPERCARD -> content.setBackgroundResource(R.drawable.tuna_bg_card_hipercard_back)
                        TunaCardFlag.ELO -> content.setBackgroundResource(R.drawable.tuna_bg_card_elo_back)
                        TunaCardFlag.AMEX -> content.setBackgroundResource(R.drawable.tuna_bg_card_amex_back)
                        TunaCardFlag.DINERS -> content.setBackgroundResource(R.drawable.tuna_bg_card_diners_back)
                        TunaCardFlag.VISA -> content.setBackgroundResource(R.drawable.tuna_bg_card_visa_back)
                        TunaCardFlag.MASTER -> content.setBackgroundResource(R.drawable.tuna_bg_card_master_back)
                        else -> content.background = ContextCompat.getDrawable(context, R.drawable.tuna_bg_card_master)
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
        binding = WidgetCardBackBinding.inflate(LayoutInflater.from(context), this, true)
    }

}
