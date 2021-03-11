package com.tunasoftware.tunaui.extensions

import java.util.*
import java.util.regex.Pattern

fun String.isPhone(): Boolean {
    val phone = Regex("[^A-Za-z0-9 ]").replace(this, "")
    return phone.length >= 10
}

fun String.isYearMonth(): Boolean {
    return if (isEmpty() || length < 5) {
        false
    } else {
        val yearMonth = split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val cardMonth = Integer.valueOf(yearMonth[0])
        val cardYear = Integer.valueOf(yearMonth[1])
        val year = Calendar.getInstance().get(Calendar.YEAR) % 100
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1

        cardMonth in 1..12 && (cardYear > year || cardYear == year && cardMonth >= month)
    }
}

fun String.isCreditCard(): Boolean {
    val (digits, others) = filterNot(Char::isWhitespace).partition(Char::isDigit)

    if (digits.length <= 1 || others.isNotEmpty()) {
        return false
    }

    val checksum = digits
        .map { it.toInt() - '0'.toInt() }
        .reversed()
        .mapIndexed { index, value ->
            if (index % 2 == 1 && value < 9) value * 2 % 9 else value
        }
        .sum()

    return checksum % 10 == 0
}

fun String.isCpf(): Boolean {
    val isValidCPF = true

    val cpf = Regex("[^A-Za-z0-9 ]").replace(this, "")

    if (cpf.length != 11) {
        return !isValidCPF
    }

    val numDig = cpf.substring(0, 9)
    val compareDigVerif = calcDigVerif(numDig) == cpf.substring(9, 11)
    if (compareDigVerif && isCpfSequenceValid(cpf)) {
        return isValidCPF
    }

    return !isValidCPF
}

fun String.getMonthFromCardDate(): Int {
    val date = split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    return if (date.size > 1) {
        Integer.valueOf(date[0])
    } else 0
}

fun String.getYearFromCardDate(): String {
    val date = split("/".toRegex()).dropLastWhile { isEmpty() }.toTypedArray()
    return if (date.size > 1) {
        date[1]
    } else ""
}

private fun isCpfSequenceValid(cpf: String): Boolean {
    val cpfLength = cpf.length
    val regex = "^(\\d)\\1{" + (cpfLength - 1) + "}"

    return !Pattern.compile(regex).matcher(cpf).find()
}

private fun calcDigVerif(num: String): String {
    val primDig: Int?
    val segDig: Int?
    var soma = 0
    var peso = 10
    for (i in 0 until num.length)
        soma += Integer.parseInt(num.substring(i, i + 1)) * peso--

    primDig = if ((soma % 11 == 0) or (soma % 11 == 1))
        0
    else
        11 - soma % 11

    soma = 0
    peso = 11
    for (i in 0 until num.length)
        soma += Integer.parseInt(num.substring(i, i + 1)) * peso--

    soma += primDig.toInt() * 2
    segDig = if ((soma % 11 == 0) or (soma % 11 == 1))
        0
    else
        11 - soma % 11

    return primDig.toString() + segDig.toString()
}


