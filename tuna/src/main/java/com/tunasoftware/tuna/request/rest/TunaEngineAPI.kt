package com.tunasoftware.tuna.request.rest

import retrofit2.Call
import retrofit2.http.GET


interface TunaEngineAPI {

    @GET("/api/Partner/GetAvailablePaymentMethodTypes")
    fun getPaymentMethods(): Call<PaymentMethodsResultVO>

}