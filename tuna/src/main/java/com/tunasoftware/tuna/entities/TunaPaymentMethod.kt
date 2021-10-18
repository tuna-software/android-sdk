package com.tunasoftware.tuna.entities

open class TunaPaymentMethod(val type: TunaPaymentMethodType, val displayName: String)

class TunaCardPaymentMethod(displayName: String, val brands:List<String>) : TunaPaymentMethod(TunaPaymentMethodType.CREDIT_CARD, displayName)

enum class TunaPaymentMethodType(val value:String){
    BANK_SLIP("3"),
    CREDIT_CARD("1"),
}