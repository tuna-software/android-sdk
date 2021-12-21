package com.tunasoftware.tunaui

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.tunaui.checkout.TunaCheckoutActivity
import com.tunasoftware.tunaui.delivery.TunaDeliverySelectionActivity
import com.tunasoftware.tunaui.domain.entities.CheckoutResult
import com.tunasoftware.tunaui.domain.entities.DeliverySelectionResult
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
                        ?.let {
                            selectPaymentMethodCallback?.onPaymentMethodSelected(it as PaymentMethodSelectionResult)
                        } ?: selectPaymentMethodCallback?.onCancelled()
                }
                else -> selectPaymentMethodCallback?.onCancelled()
            }
            selectPaymentMethodCallback = null
        }

    private val checkoutLauncher: ActivityResultLauncher<Intent> =
        activity.activityResultRegistry.register(
            "checkout",
            activity,
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.getSerializableExtra(RESULT_CHECKOUT)
                        ?.let {
                            checkoutCallback?.onCheckoutCompleted(it as CheckoutResult)
                        } ?: checkoutCallback?.onCancelled()
                }
                else -> checkoutCallback?.onCancelled()
            }
            checkoutCallback = null
        }

    private val selectDeliveryLauncher: ActivityResultLauncher<Intent> =
        activity.activityResultRegistry.register(
            "selectDelivery",
            activity,
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.getSerializableExtra(RESULT_DELIVERY_SELECTION)
                        ?.let {
                            selectDeliveryCallback?.onSelectedDelivery(it as DeliverySelectionResult)
                        } ?: selectDeliveryCallback?.onCancelled()
                }
                else -> selectDeliveryCallback?.onCancelled()
            }
            selectDeliveryCallback = null
        }


    private var selectPaymentMethodCallback: TunaSelectPaymentMethodCallback? = null
    private var checkoutCallback: TunaCheckoutCallback? = null
    private var selectDeliveryCallback: TunaDeliverySelectionCallback? = null

    companion object {
        const val RESULT_PAYMENT_SELECTION = "RESULT_PAYMENT_SELECTION"
        const val RESULT_CHECKOUT = "RESULT_CHECKOUT"
        const val RESULT_DELIVERY_SELECTION = "RESULT_DELIVERY_SELECTION"
    }

    /**
     * Starts the TunaUI for selecting the payment method ad registering new tokens
     * @param callback
     */
    fun selectPaymentMethod(callback: TunaSelectPaymentMethodCallback) {
        this.selectPaymentMethodCallback = callback
        try {
            selectPaymentMethodLauncher.launch(
                Intent(
                    activity,
                    TunaSelectPaymentMethodActivity::class.java
                )
            )
        } catch (e: Throwable) {
            Log.e("error", e.toString())
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

    /**
     * Starts the TunaUI to checkout purchases
     * @param callback
     */
    fun checkout(callback: TunaCheckoutCallback) {
        this.checkoutCallback = callback
        try {
            checkoutLauncher.launch(
                Intent(
                    activity,
                    TunaCheckoutActivity::class.java
                )
            )
        } catch (e: Throwable) {
            Log.e("error", e.toString())
        }
    }

    /**
     * Callback for checkout
     */
    interface TunaCheckoutCallback {

        /**
         * the checkout has been completed
         * @param checkoutResult
         */
        fun onCheckoutCompleted(checkoutResult: CheckoutResult)

        /**
         * the checkout was cancelled
         */
        fun onCancelled()

    }

    /**
     * Starts the TunaUI to select delivery
     * @param callback
     */
    fun selectDelivery(callback: TunaDeliverySelectionCallback) {
        this.selectDeliveryCallback = callback
        try {
            val intent = Intent(activity, TunaDeliverySelectionActivity::class.java)
            selectDeliveryLauncher.launch(intent)
        } catch (e: Throwable) {
            Log.e("error", e.toString())
        }
    }

    /**
     * Callback for delivery selection
     */
    interface TunaDeliverySelectionCallback {

        /**
         * the delivery has been selected
         * @param deliverySelectionResult
         */
        fun onSelectedDelivery(deliverySelectionResult: DeliverySelectionResult)

        /**
         * the delivery selection was cancelled
         */
        fun onCancelled()

    }
}

