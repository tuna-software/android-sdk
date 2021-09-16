package com.tunasoftware.tuna.request.rest.interceptors

import com.tunasoftware.tuna.entities.TunaAPIKey
import okhttp3.Interceptor
import okhttp3.Response

class EngineAPIInterceptor(val tunaApiKey: TunaAPIKey) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader("x-tuna-account", tunaApiKey.accountToken)
        request.addHeader("x-tuna-apptoken", tunaApiKey.appToken)
        return chain.proceed(request.build())
    }

}