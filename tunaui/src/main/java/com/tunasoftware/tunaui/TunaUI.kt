package com.tunasoftware.tunaui

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.tunaui.domain.entities.PaymentMethodSelectionResult

class TunaUI(private val activity: AppCompatActivity) {

    private val selectPaymentMethodLauncher: ActivityResultLauncher<Intent> =
        activity.activityResultRegistry.register(
            "selectPaymetMethod",
            activity,
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.getSerializableExtra(RESULT_PAYMENT_SELECTION)
                        ?.let { result ->
                            callback?.onPaymentMethodSelected(result as PaymentMethodSelectionResult)
                        } ?: callback?.onCancelled()
                }
                else -> callback?.onCancelled()
            }
            callback = null
        }


    private var callback: TunaSelectPaymentMethodCallback? = null

    companion object {
        const val RESULT_PAYMENT_SELECTION = "RESULT_PAYMENT_SELECTION"
    }


    /**
     * Starts the TunaUI for selecting the payment method ad registering new tokens
     * @param callback
     */
    fun selectPaymentMethod(callback: TunaSelectPaymentMethodCallback) {
        this.callback = callback
        try {
            selectPaymentMethodLauncher.launch(
                Intent(
                    activity,
                    TunaSelectPaymentMethodActivity::class.java
                )
            )
        } catch (e: Throwable) {
            Log.e("erro", e.toString())
        }
    }

    /**
     * Callback for selecting payment method
     */
    interface TunaSelectPaymentMethodCallback {

        /**
         * a payment method was selected
         * @param paymentMethodSelectionResult
         */
        fun onPaymentMethodSelected(paymentMethodSelectionResult: PaymentMethodSelectionResult)

        /**
         * the payment method selection was cancelled
         */
        fun onCancelled()

    }
}

