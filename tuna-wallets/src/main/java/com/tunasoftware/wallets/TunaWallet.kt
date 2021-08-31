package com.tunasoftware.wallets

import android.content.Intent
import com.tunasoftware.tuna.Tuna.TunaRequestCallback
import com.tunasoftware.tunakt.TunaRequestResult

interface TunaWallet {

    /**
     * Requesting payment, opens Google Play screen
     * @param price The price provided for the API must be the full price.
     */
    fun requestPayment(price: Double, callback: TunaRequestCallback<Boolean>)

    /**
     * Process the result in onActivityResult
     * @param requestCode requestCode from onActivityResult
     * @param resultCode resultCode from onActivityResult
     * @param data Intent from onActivityResult
     * @param callback Callback to send the result
     */
    fun processResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        // TODO: Make generic for google and other wallets
        callback: TunaWalletCallback<TunaWalletPaymentData>
    ): Boolean

    /**
     * Checks if Google Pay is available for the device
     */
    fun isAvailable(callback: TunaRequestCallback<Boolean>)

}

interface TunaWalletCallback<T> : TunaRequestCallback<T> {
    fun onCanceled()
}

/**
 * Requesting payment, opens Google Play screen
 * @param price The price provided for the API must be the full price.
 */
fun TunaWallet.requestPayment(price: Double) = TunaRequestResult<Boolean>().apply {
    requestPayment(price = price, callback = object : TunaRequestCallback<Boolean> {
        override fun onSuccess(result: Boolean) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

/**
 * Checks if Google Pay is available for the device
 */
fun TunaWallet.isAvailable() = TunaRequestResult<Boolean>().apply {
    isAvailable(callback = object : TunaRequestCallback<Boolean> {
        override fun onSuccess(result: Boolean) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}