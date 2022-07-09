package com.tunasoftware.tuna.entities

import java.io.Serializable
import java.util.*

/**
 *
 */
data class TunaCard(
    val token: String,
    val brand: String,
    val cardHolderName: String,
    val expirationMonth: Int,
    val expirationYear: Int,
    val maskedNumber: String
) : Serializable {

    private var _bindExpiration: Date? = null

    /**
     * This method returns how many seconds until last bind to expired
     *
     * @return Int seconds
     * */
    fun secondsBindToExpire(): Int {
        if (bindHasExpired()) return 0
        val diff: Long = _bindExpiration?.time?.minus(Date(System.currentTimeMillis()).time) ?: 0
        return (diff / 1000).toInt()
    }

    /**
     * This method returns whether or not the last bind has expired
     *
     * @return Boolean bind expired
     * */
    fun bindHasExpired() = _bindExpiration?.before(Date(System.currentTimeMillis())) ?: true

    /**
     * This method to set the bind result
     * */
    fun setBindResult(validFor: Int = 0) {
        _bindExpiration = Date(System.currentTimeMillis() + (validFor * 1000))
    }

}

