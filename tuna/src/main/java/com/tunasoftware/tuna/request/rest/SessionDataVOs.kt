package com.tunasoftware.tuna.request.rest

import com.google.gson.annotations.SerializedName

data class NewSessionDataVO(@SerializedName("Customer") val customer:SessionCustomerDataVO
)

data class SessionCustomerDataVO(
        @SerializedName("ID") val id:String,
        @SerializedName("Email") val email:String
)

data class SessionResultVO(
        @SerializedName("sessionId") val sessionId:String,
        @SerializedName("code") val code:Int,
        @SerializedName("message") val message:String?
)
