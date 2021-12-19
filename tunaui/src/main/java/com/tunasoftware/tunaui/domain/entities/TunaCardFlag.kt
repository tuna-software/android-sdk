package com.tunasoftware.tunaui.domain.entities

import com.tunasoftware.tunaui.R

enum class TunaCardFlag(val value: String) {
    HIPERCARD("HIPERCARD"),
    ELO("ELO"),
    AMEX("AMEX"),
    DINERS("DINNER"),
    VISA("VISA"),
    MASTER("MasterCard"),
    UNDEFINED("UNDEFINED");

    companion object  {
        fun fromBrand(brand:String): TunaCardFlag {
            return runCatching { values().first { it.value.equals(brand, ignoreCase = true) } }.getOrNull()?:UNDEFINED
        }
    }
}

val TunaCardFlag.cardFlag get() = when (this) {
    TunaCardFlag.VISA -> R.drawable.tuna_ic_visa
    TunaCardFlag.HIPERCARD -> R.drawable.tuna_ic_hipercard
    TunaCardFlag.ELO -> R.drawable.tuna_ic_elo
    TunaCardFlag.AMEX -> R.drawable.tuna_ic_amex
    TunaCardFlag.DINERS -> R.drawable.tuna_ic_diners
    TunaCardFlag.MASTER -> R.drawable.tuna_ic_master
    TunaCardFlag.UNDEFINED -> R.drawable.tuna_ic_generic_card
}

