package com.tunasoftware.tuna.entities

import java.io.Serializable

/**
 *
 */
data class TunaCard(val token:String,
                    val brand:String,
                    val cardHolderName:String,
                    val expirationMonth:Int,
                    val expirationYear:Int,
                    val maskedNumber:String): Serializable

