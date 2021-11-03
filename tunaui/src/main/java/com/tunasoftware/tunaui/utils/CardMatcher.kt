package com.tunasoftware.tunaui.utils

import com.tunasoftware.tunaui.domain.entities.TunaCardFlag

class CardMatcher {

    companion object {

        fun matches(number: String): TunaCardFlag {
            val value = number.replace("\\s".toRegex(), "")
            return when {
                ("^(606282)".toRegex().matches(value)) || ("^(384100|384140|384160)".toRegex().matches(value)) -> TunaCardFlag.HIPERCARD
                ("^(4011(78|79)|43(1274|8935)|45(1416|7393|763(1|2))|50(4175|6699|67[0-7][0-9]|9000)|50(9[0-9][0-9][0-9])|627780|63(6297|6368)|650(03([^4])|04([0-9])|05(0|1)|05([7-9])|06([0-9])|07([0-9])|08([0-9])|4([0-3][0-9]|8[5-9]|9[0-9])|5([0-9])[0-9]|3[0-8])|9([0-6][0-9]|7[0-8])|7([0-2][0-9])|541|700|720|727|901)|65165([2-9])|6516([6-7][0-9])|65500([0-9])|6550([0-5][0-9])|655021|65505([6-7])|6516([8-9][0-9])|65170([0-4])".toRegex().matches(value)) -> TunaCardFlag.ELO
                ("^(34|37)[0-9]{0,13}".toRegex().matches(value)) -> TunaCardFlag.AMEX
                ("^(300|301|302|303|304|305|36|38)".toRegex().matches(value)) -> TunaCardFlag.DINERS
                ("^4[0-9]{0,16}".toRegex().matches(value)) -> TunaCardFlag.VISA
                ("^(5[1-5][0-9]{14}|2[2-7][0-9]{14})".toRegex().matches(value)) -> TunaCardFlag.MASTER
                else -> TunaCardFlag.UNDEFINED
            }
        }
    }


}