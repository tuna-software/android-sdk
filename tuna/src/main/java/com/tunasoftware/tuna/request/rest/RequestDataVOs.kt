package com.tunasoftware.tuna.request.rest

import com.google.gson.annotations.SerializedName
import com.tunasoftware.tuna.entities.TunaPaymentMethod

data class GenerateCardRequestVO(
        @SerializedName("SessionId") val sessionId: String,
        @SerializedName("Card") val card: CardRequestVO
)

data class CardRequestVO(
        @SerializedName("CardNumber") val cardNumber: String,
        @SerializedName("CardHolderName") val cardHolderName: String,
        @SerializedName("ExpirationMonth") val expirationMonth: Int,
        @SerializedName("ExpirationYear") val expirationYear: Int,
        @SerializedName("SingleUse") val singleUse: Boolean,
        @SerializedName("cvv") val cvv: String?
)

data class GenerateCardResultVO(
        @SerializedName("token") val token: String,
        @SerializedName("brand") val brand: String,
        @SerializedName("code") val code: Int,
        @SerializedName("message") val message: String?,
)

data class SessionRequestVO(
        @SerializedName("SessionId") val sessionId: String
)

data class CardResultVO(
        @SerializedName("token") val token: String,
        @SerializedName("brand") val brand: String,
        @SerializedName("cardHolderName") val cardHolderName: String,
        @SerializedName("expirationMonth") val expirationMonth: Int,
        @SerializedName("expirationYear") val expirationYear: Int,
        @SerializedName("maskedNumber") val maskedNumber: String,
        @SerializedName("singleUse") val singleUse: Boolean,
)

data class ListCardsResultVO(
        @SerializedName("tokens") val tokens: List<CardResultVO>?,
        @SerializedName("code") val code: Int,
        @SerializedName("message") val message: String?,
)

data class DeleteCardRequestVO(
        @SerializedName("sessionID") val sessionId: String,
        @SerializedName("token") val token: String
)

data class DeleteCardResultVO(
        @SerializedName("status") val status: String,
        @SerializedName("code") val code: Int,
        @SerializedName("message") val message: String,
)

data class BindCardRequestVO(
        @SerializedName("sessionID") val sessionId: String,
        @SerializedName("token") val token: String,
        @SerializedName("CVV") val cvv: String,
)

data class BindCardResultVO(
        @SerializedName("code") val code: Int,
        @SerializedName("message") val message: String?,
)

data class PaymentMethodsResultVO(
        @SerializedName("code") val code: Int,
        @SerializedName("message") val message: String?,
        val paymentMethods: List<TunaPaymentMethod>? = null
)