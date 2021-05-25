package com.tunasoftware.tunaui.utils

class CardMatcher {

    companion object {

        fun matches(number: String): CardFlag {
            val value = number.replace("\\s".toRegex(), "")
            return when {
                (value.length == 16 && "^(606282)".toRegex().matches(value)) || (value.length == 19 && "^(384100|384140|384160)".toRegex().matches(value)) -> CardFlag.HIPERCARD
                (value.length in 12..19 && "^(4011(78|79)|43(1274|8935)|45(1416|7393|763(1|2))|50(4175|6699|67[0-7][0-9]|9000)|50(9[0-9][0-9][0-9])|627780|63(6297|6368)|650(03([^4])|04([0-9])|05(0|1)|05([7-9])|06([0-9])|07([0-9])|08([0-9])|4([0-3][0-9]|8[5-9]|9[0-9])|5([0-9])[0-9]|3[0-8])|9([0-6][0-9]|7[0-8])|7([0-2][0-9])|541|700|720|727|901)|65165([2-9])|6516([6-7][0-9])|65500([0-9])|6550([0-5][0-9])|655021|65505([6-7])|6516([8-9][0-9])|65170([0-4])".toRegex().matches(value)) -> CardFlag.ELO
                (value.length == 15 && "^(34|37)[0-9]{0,13}".toRegex().matches(value)) -> CardFlag.AMEX
                (value.length == 14 || value.length == 16) && "^(300|301|302|303|304|305|36|38)".toRegex().matches(value) -> CardFlag.DINERS
                (value.length == 13 || value.length == 16 || value.length == 19) && "^4[0-9]{0,16}".toRegex().matches(value) -> CardFlag.VISA
                (value.length == 16 && "^(5[1-5][0-9]{14}|2[2-7][0-9]{14})".toRegex().matches(value)) -> CardFlag.MASTER
                else -> CardFlag.UNDEFINED
            }
        }
    }

    enum class CardFlag {
        HIPERCARD,
        ELO,
        AMEX,
        DINERS,
        VISA,
        MASTER,
        UNDEFINED
    }
}