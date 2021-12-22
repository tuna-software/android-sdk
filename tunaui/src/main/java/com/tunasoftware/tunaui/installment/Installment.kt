package com.tunasoftware.tunaui.installment

import java.io.Serializable

open class Installment(
    val id: Int,
    val name: String,
    val description: String,
    var selected: Boolean = false
) : Serializable