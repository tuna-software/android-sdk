package com.tunasoftware.tunaui.delivery

import java.io.Serializable

open class Delivery(
    val id: Int,
    val name: String,
    val description: String,
    val value: String,
    var selected: Boolean = false
): Serializable