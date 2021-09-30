package com.tunasoftware.googlepay

import android.app.Activity
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.tunasoftware.googlepay.exceptions.TunaGooglePayException
import com.tunasoftware.googlepay.exceptions.TunaGooglePayResultErrorException
import com.tunasoftware.googlepay.exceptions.TunaGooglePayResultNullException
import com.tunasoftware.googlepay.utils.TunaGooglePayUtil
import com.tunasoftware.tuna.Tuna.TunaRequestCallback
import com.tunasoftware.wallets.TunaWallet
import com.tunasoftware.wallets.TunaWalletCallback
import com.tunasoftware.wallets.TunaWalletPaymentData
import org.json.JSONObject

class TunaGooglePay(private val activity: Activity) : TunaWallet {

    companion object {
        private const val TUNA_GOOGLE_PAY_REQUEST_CODE = 5858
    }

    private val paymentsClient: PaymentsClient = TunaGooglePayUtil.createPaymentsClient(activity)

    override fun requestPayment(price: Double, callback: TunaRequestCallback<Boolean>) {
        TunaGooglePayUtil.getPaymentDataRequest(price, callback = object : TunaRequestCallback<JSONObject> {
            override fun onFailed(e: Throwable) {
                callback.onFailed(e)
            }

            override fun onSuccess(result: JSONObject) {
                val request = PaymentDataRequest.fromJson(result.toString())
                val task = paymentsClient.loadPaymentData(request)
                AutoResolveHelper.resolveTask(task, activity, TUNA_GOOGLE_PAY_REQUEST_CODE)
                callback.onSuccess(true)
            }
        })
    }

    override fun processResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        callback: TunaWalletCallback<TunaWalletPaymentData>
    ): Boolean {
        when (requestCode) {
            TUNA_GOOGLE_PAY_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent).let { paymentData ->
                                if (paymentData != null) {
                                    callback.onSuccess(TunaGooglePayUtil.handlePaymentSuccess(paymentData))
                                } else {
                                    callback.onFailed(TunaGooglePayResultNullException())
                                }
                            }
                        }

                    Activity.RESULT_CANCELED -> {
                        callback.onCanceled()
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data).let {
                            callback.onFailed(TunaGooglePayResultErrorException(it))
                        }
                    }
                }

                return true
            }
        }

        return false
    }

    override fun isAvailable(callback: TunaRequestCallback<Boolean>) {
        TunaGooglePayUtil.isReadyToPayRequest(callback = object : TunaRequestCallback<JSONObject> {
            override fun onFailed(e: Throwable) {
                callback.onFailed(e)
            }

            override fun onSuccess(result: JSONObject) {
                val request = IsReadyToPayRequest.fromJson(result.toString())
                    ?: return callback.onSuccess(false)

                paymentsClient.isReadyToPay(request).addOnCompleteListener { task ->
                    try {
                        task.getResult(ApiException::class.java)?.let {
                            callback.onSuccess(it)
                        }
                    } catch (e: ApiException) {
                        callback.onFailed(TunaGooglePayException(e))
                    }
                }
            }
        })
    }
}