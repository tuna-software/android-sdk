package com.tunasoftware.tuna.request.rest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

interface TunaAPI {

    @POST("/api/Token/Generate")
    fun generate(@Body requestVO: GenerateCardRequestVO): Call<GenerateCardResultVO>

    @POST("/api/Token/NewSession")
    fun newSession(@Header("x-tuna-apptoken") appToken:String, @Body requestVO: NewSessionDataVO): Call<SessionResultVO>

    @POST("/api/Token/List")
    fun list(@Body requestVO: SessionRequestVO): Call<ListCardsResultVO>

    @POST("/api/Token/Bind")
    fun bind(@Body bindCardRequestVO: BindCardRequestVO): Call<BindCardResultVO>

    @HTTP(method = "DELETE", path = "/api/Token/Delete", hasBody = true)
    fun delete(@Body requestVO: DeleteCardRequestVO): Call<DeleteCardResultVO>

}