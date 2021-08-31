package com.tunasoftware.googlepay.utils

import android.app.Activity
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.tunasoftware.googlepay.TunaGooglePayBillingAddressFormat
import com.tunasoftware.googlepay.TunaGooglePayConfig
import com.tunasoftware.googlepay.TunaGooglePayPaymentData
import com.tunasoftware.tuna.Tuna.TunaRequestCallback
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode

object TunaGooglePayUtil {

    fun createPaymentsClient(activity: Activity): PaymentsClient {
        // TODO: Check test and production mode

        val options = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()

        return Wallet.getPaymentsClient(activity, options)
    }

    fun isReadyToPayRequest(callback: TunaRequestCallback<JSONObject>) {
        getConfig(callback = object : TunaRequestCallback<TunaGooglePayConfig> {
            override fun onFailed(e: Throwable) {
                callback.onFailed(e)
            }

            override fun onSuccess(result: TunaGooglePayConfig) {
                baseRequest.apply {
                    put("allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod(result)))
                    callback.onSuccess(this)
                }
            }
        })
    }

    fun getPaymentDataRequest(price: Double, callback: TunaRequestCallback<JSONObject>) {
        getConfig(callback = object : TunaRequestCallback<TunaGooglePayConfig> {
            override fun onFailed(e: Throwable) {
                callback.onFailed(e)
            }

            override fun onSuccess(result: TunaGooglePayConfig) {
                // TODO: Check gateway with tuna
                val tokenizationSpecification = JSONObject().apply {
                    put("type", "PAYMENT_GATEWAY")
                    put("parameters", JSONObject(mapOf("gateway" to "tuna", "gatewayMerchantId" to result.gatewayMerchantId)))
                }

                val cardPaymentMethod = baseCardPaymentMethod(result)
                cardPaymentMethod.put("tokenizationSpecification", tokenizationSpecification)

                val transactionInfo = JSONObject().apply {
                    put("totalPrice", price.centsToString())
                    put("totalPriceStatus", "FINAL")
                    put("countryCode", "BR")
                    put("currencyCode", "BRL")
                }

                val merchantInfo: JSONObject = JSONObject().put("merchantName", result.merchantName)

                baseRequest.apply {
                    put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod))
                    put("transactionInfo", transactionInfo)
                    put("merchantInfo", merchantInfo)

                    val shippingAddressParameters = JSONObject().apply {
                        put("phoneNumberRequired", result.phoneNumberRequired)
                        put("allowedCountryCodes", JSONArray(result.allowedCountryCodes))
                    }

                    put("shippingAddressParameters", shippingAddressParameters)
                    put("shippingAddressRequired", result.shippingAddressRequired)

                    callback.onSuccess(this)
                }
            }
        })
    }

    fun handlePaymentSuccess(paymentData: PaymentData): TunaGooglePayPaymentData {
        val data = paymentData.toJson()
        return TunaGooglePayPaymentData(data = JSONObject(data))
    }

    private val baseRequest = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
    }

    private fun baseCardPaymentMethod(config: TunaGooglePayConfig): JSONObject {
        return JSONObject().apply {

            val parameters = JSONObject().apply {
                put("allowedAuthMethods", JSONArray(config.allowedAuthMethods))
                put("allowedCardNetworks", JSONArray(config.allowedCardNetworks))
                put("billingAddressRequired", config.billingAddressRequired)
                put("billingAddressParameters", JSONObject().apply {
                    put("format", config.billingAddressFormat)
                })
            }

            put("type", "CARD")
            put("parameters", parameters)
        }
    }

    private fun getConfig(callback: TunaRequestCallback<TunaGooglePayConfig>) {
        // TODO: Fetch from Tuna api

        val config = TunaGooglePayConfig(
            allowedAuthMethods = listOf("PAN_ONLY"),
            allowedCardNetworks = listOf("AMEX", "DISCOVER", "JCB", "MASTERCARD", "VISA"),
            merchantName = "Mercado Livre",
            gatewayMerchantId = "mercadoLivreId",
            allowedCountryCodes = listOf("US", "BR"),
            billingAddressFormat = TunaGooglePayBillingAddressFormat.FULL
        )

        callback.onSuccess(config)
    }

    private fun Double.centsToString() = BigDecimal(this)
        .divide(BigDecimal(100))
        .setScale(2, RoundingMode.HALF_EVEN)
        .toString()
}