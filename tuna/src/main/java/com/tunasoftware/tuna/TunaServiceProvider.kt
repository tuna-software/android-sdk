package com.tunasoftware.tuna

import com.google.gson.GsonBuilder
import com.tunasoftware.tuna.entities.TunaAPIKey
import com.tunasoftware.tuna.request.TunaSessionProvider
import com.tunasoftware.tuna.request.rest.PaymentMethodsResultVO
import com.tunasoftware.tuna.request.rest.TunaAPI
import com.tunasoftware.tuna.request.rest.TunaEngineAPI
import com.tunasoftware.tuna.request.rest.deserializers.PaymentMethodsDeserializer
import com.tunasoftware.tuna.request.rest.interceptors.EngineAPIInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TunaServiceProvider {

    var isSandbox:Boolean = false

    private val retrofitToken : Retrofit
    get() = OkHttpClient
        .Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build().let {
            Retrofit.Builder()
                .baseUrl(if (isSandbox) "https://token.tuna-demo.uy" else "https://token.tunagateway.com")
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .client(it)
                .build()
        }

    private fun provideEngineRetrofit(tunaApiKey: TunaAPIKey): Retrofit {
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(EngineAPIInterceptor(tunaApiKey))
            .addInterceptor(HttpLoggingInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl(if (isSandbox) "https://sandbox.tuna-demo.uy" else "https://engine.tunagateway.com")
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
            .client(okHttpClient)
            .build()
    }

    private fun provideGson() = GsonBuilder().registerTypeAdapter(PaymentMethodsResultVO::class.java, PaymentMethodsDeserializer()).create()

    val tunaAPI:TunaAPI get() = retrofitToken.create(TunaAPI::class.java)

    fun provideTunaEngineAPI(tunaApiKey: TunaAPIKey):TunaEngineAPI{
        return provideEngineRetrofit(tunaApiKey).let {
            it.create(TunaEngineAPI::class.java)
        }
    }

    fun getTunaSessionProvider() = TunaSessionProvider(tunaAPI)

}