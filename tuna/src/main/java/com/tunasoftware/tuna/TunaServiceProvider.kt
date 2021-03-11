package com.tunasoftware.tuna

import com.tunasoftware.tuna.request.TunaSessionProvider
import com.tunasoftware.tuna.request.rest.TunaAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TunaServiceProvider {

    var isSandbox:Boolean = false

    private val retrofit : Retrofit
    get() = Retrofit.Builder()
        .baseUrl(if (isSandbox) "https://token.tuna-demo.uy" else "https://token.tunagateway.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val tunaAPI:TunaAPI get() = retrofit.create(TunaAPI::class.java)

    fun getTunaSessionProvider() = TunaSessionProvider(tunaAPI)

}