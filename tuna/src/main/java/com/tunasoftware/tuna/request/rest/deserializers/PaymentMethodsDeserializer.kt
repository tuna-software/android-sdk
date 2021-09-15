package com.tunasoftware.tuna.request.rest.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tunasoftware.tuna.entities.TunaCardPaymentMethod
import com.tunasoftware.tuna.entities.TunaPaymentMethod
import com.tunasoftware.tuna.entities.TunaPaymentMethodType
import com.tunasoftware.tuna.request.rest.PaymentMethodsResultVO
import java.lang.reflect.Type

class PaymentMethodsDeserializer : JsonDeserializer<PaymentMethodsResultVO>{

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PaymentMethodsResultVO {
        val element = json.asJsonObject
        val code = element["code"].asInt
        val message = element["message"]?.asJsonObject?.get("message")?.asString
        if (code == 1) {
            val brands = element["brands"]?.asJsonArray?.map {
                it.asString
            }?.toList() ?: listOf<String>()
            val paymentMethods = element["paymentMethods"]?.asJsonArray?.map {
                val obj = it.asJsonObject
                val type =
                    TunaPaymentMethodType.values().first { it.value == obj["methodType"].asInt }
                val displayName = obj["displayName"].asString
                val paymentMethod = if (type == TunaPaymentMethodType.CREDIT_CARD) {
                    TunaCardPaymentMethod(displayName, brands)
                } else {
                    TunaPaymentMethod(type, displayName)
                }
                paymentMethod
            }?.toList() ?: listOf<TunaPaymentMethod>()
            return PaymentMethodsResultVO(code, message, paymentMethods)
        }
        return PaymentMethodsResultVO(code, message)
    }
}