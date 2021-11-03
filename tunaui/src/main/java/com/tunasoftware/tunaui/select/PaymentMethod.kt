package com.tunasoftware.tunaui.select

import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.domain.entities.TunaCardFlag
import com.tunasoftware.tunaui.domain.entities.TunaUICard



open class PaymentMethod(
    val methodType: PaymentMethodType,
    val displayName: String,
    val disableSwipe: Boolean = false,
    val selectable: Boolean = true
)

class  PaymentMethodCreditCard(methodType: PaymentMethodType,
                               displayName: String,
                               disableSwipe: Boolean = false,
                               selectable: Boolean = true,
                               val flag: TunaCardFlag,
                               val tunaUICard: TunaUICard
): PaymentMethod(methodType, displayName, disableSwipe, selectable)

fun PaymentMethodCreditCard.getTunaCard() = TunaCard(token = this.tunaUICard.token,
        brand = this.tunaUICard.brand,
        cardHolderName = this.tunaUICard.cardHolderName,
        expirationMonth = this.tunaUICard.expirationMonth,
        expirationYear = this.tunaUICard.expirationYear,
        maskedNumber = this.tunaUICard.maskedNumber
    )



val PaymentMethod.methodFlag
    get() = when (this.methodType) {
        PaymentMethodType.BANK_SLIP -> R.drawable.tuna_ic_barcode
        PaymentMethodType.CREDIT_CARD -> {
            if (this is PaymentMethodCreditCard) {
                when (this.flag) {
                    TunaCardFlag.VISA -> {
                        R.drawable.tuna_ic_visa
                    }
                    TunaCardFlag.HIPERCARD -> R.drawable.tuna_ic_hipercard
                    TunaCardFlag.ELO -> R.drawable.tuna_ic_elo
                    TunaCardFlag.AMEX -> R.drawable.tuna_ic_amex
                    TunaCardFlag.DINERS -> R.drawable.tuna_ic_diners
                    TunaCardFlag.MASTER -> R.drawable.tuna_ic_master
                    TunaCardFlag.UNDEFINED -> R.drawable.tuna_ic_generic_card
                }
            } else {
                R.drawable.tuna_ic_generic_card
            }
        }
        PaymentMethodType.NEW_CREDIT_CARD -> R.drawable.tuna_ic_generic_card
        PaymentMethodType.GOOGLE_PAY -> R.drawable.tuna_ic_google_pay
        PaymentMethodType.PAYPAL -> R.drawable.tuna_ic_paypal
        PaymentMethodType.PIX -> R.drawable.tuna_ic_pix
        PaymentMethodType.SAMSUNG_PAY -> R.drawable.tuna_ic_samsung_pay
        else -> 0
    }

enum class PaymentMethodType(val value: Int) {
    CREDIT_CARD(0),
    BANK_SLIP(1),
    PIX(2),
    GOOGLE_PAY(3),
    SAMSUNG_PAY(4),
    PAYPAL(5),
    NEW_CREDIT_CARD(6),
}