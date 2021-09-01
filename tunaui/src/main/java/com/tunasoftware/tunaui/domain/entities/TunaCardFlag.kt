package com.tunasoftware.tunaui.domain.entities

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

