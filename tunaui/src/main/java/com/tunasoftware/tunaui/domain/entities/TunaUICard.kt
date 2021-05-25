package com.tunasoftware.tunaui.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TunaUICard(val token:String,
                    val brand:String,
                    val cardHolderName:String,
                    val expirationMonth:Int,
                    val expirationYear:Int,
                    val maskedNumber:String): Parcelable