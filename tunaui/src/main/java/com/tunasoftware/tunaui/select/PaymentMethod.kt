package com.tunasoftware.tunaui.select

import com.tunasoftware.tunaui.R

class PaymentMethod(
    val methodType: Int,
    val displayName: String,
    val disableSwipe: Boolean = false,
    val selectable: Boolean = true
)

val PaymentMethod.flag
    get() = when (this.methodType) {
        PaymentMethodType.AMEX.value -> R.drawable.tuna_ic_amex
        PaymentMethodType.APPLE_PAY.value -> R.drawable.tuna_ic_apple_pay
        PaymentMethodType.BANK_SLIP.value -> R.drawable.tuna_ic_barcode
        PaymentMethodType.CREDIT_CARD.value -> R.drawable.tuna_ic_generic_card
        PaymentMethodType.DEBIT_CARD.value -> R.drawable.tuna_ic_generic_card
        PaymentMethodType.DINERS.value -> R.drawable.tuna_ic_diners
        PaymentMethodType.ELO.value -> R.drawable.tuna_ic_elo
        PaymentMethodType.GOOGLE_PAY.value -> R.drawable.tuna_ic_google_pay
        PaymentMethodType.HIPERCARD.value -> R.drawable.tuna_ic_hipercard
        PaymentMethodType.MASTER.value -> R.drawable.tuna_ic_master
        PaymentMethodType.PAYPAL.value -> R.drawable.tuna_ic_paypal
        PaymentMethodType.PIX.value -> R.drawable.tuna_ic_pix
        PaymentMethodType.SAMSUNG_PAY.value -> R.drawable.tuna_ic_samsung_pay
        PaymentMethodType.VISA.value -> R.drawable.tuna_ic_visa
        else -> 0
    }

enum class PaymentMethodType(val value: Int) {
    CREDIT_CARD(0),
    DEBIT_CARD(1),
    BANK_SLIP(2),
    PIX(3),
    APPLE_PAY(4),
    GOOGLE_PAY(5),
    SAMSUNG_PAY(6),
    PAYPAL(7),
    AMEX(8),
    DINERS(9),
    ELO(10),
    HIPERCARD(11),
    MASTER(12),
    VISA(13),
}