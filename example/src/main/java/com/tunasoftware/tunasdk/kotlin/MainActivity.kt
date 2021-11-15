package com.tunasoftware.tunasdk.kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.android.Tuna
import com.tunasoftware.googlepay.TunaGooglePay
import com.tunasoftware.googlepay.TunaGooglePayPaymentData
import com.tunasoftware.tuna.exceptions.*
import com.tunasoftware.android.kt.getSandboxSessionId
import com.tunasoftware.tuna.entities.TunaPaymentMethodType
import com.tunasoftware.tunasdk.R
import com.tunasoftware.tunasdk.java.JavaListCardsActivity
import com.tunasoftware.tunasdk.java.utils.Extras
import com.tunasoftware.tunaui.TunaUI
import com.tunasoftware.tunaui.domain.entities.PaymentMethodSelectionResult
import com.tunasoftware.wallets.TunaWalletCallback
import com.tunasoftware.wallets.TunaWalletPaymentData
import com.tunasoftware.wallets.isAvailable
import com.tunasoftware.wallets.requestPayment
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var tunaGooglePay: TunaGooglePay

    lateinit var tunaUI : TunaUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tunaUI = TunaUI(this)

        tunaGooglePay = TunaGooglePay(this)

        btnJavaExample.setOnClickListener {
            startActivity(Intent(this, JavaListCardsActivity::class.java))
        }

        btnKotlinExample.setOnClickListener {
            startActivity(Intent(this, KotlinListCardsActivity::class.java))
        }

        btnTunaUiExample.setOnClickListener {
            Tuna.getSandboxSessionId()
                .onSuccess {
                    Tuna.startSession(it)
                    tunaUI.selectPaymentMethod(object : TunaUI.TunaSelectPaymentMethodCallback {
                        override fun onPaymentMethodSelected(paymentMethodSelectionResult: PaymentMethodSelectionResult) {
                            when (paymentMethodSelectionResult.paymentMethodType) {
                                TunaPaymentMethodType.BANK_SLIP -> TODO()
                                TunaPaymentMethodType.CREDIT_CARD -> {
                                    val tunacard = paymentMethodSelectionResult.cardInfo
                                        ?: throw Exception("Tuna card should not be null")
                                    Log.i(Extras.LOG_TAG, tunacard.maskedNumber)
                                }
                            }
                        }

                        override fun onCancelled() {
                            Log.i(Extras.LOG_TAG, "cancelled")
                        }

                    })
                }
                .onFailure {
                    Log.e(Extras.LOG_TAG, "Error start session", it)
                }
        }

        btnTunaCardRecognitionExample.setOnClickListener {
            startActivity(Intent(this, ReadCreditCardExampleActivity::class.java))
        }

        tunaGooglePay.isAvailable()
            .onSuccess { available ->
                if (available) {
                    btnTunaGooglePayExample.visibility = View.VISIBLE
                    btnTunaGooglePayExample.setOnClickListener {
                        tunaGooglePay.requestPayment(125.0)
                            .onSuccess {
                                Log.i(Extras.LOG_TAG, "Request payment: $it")
                            }
                            .onFailure {
                                Log.e(Extras.LOG_TAG, "Error checking if google play is available", it)
                            }
                    }
                } else {
                    btnTunaGooglePayExample.visibility = View.GONE
                }
            }
            .onFailure {
                Log.e(Extras.LOG_TAG, "Error checking if google play is available", it)
                btnTunaGooglePayExample.visibility = View.GONE
            }
    }

    companion object {

        fun printTunaException(e: Throwable) {
            when (e) {
                is TunaSDKNotInitiatedException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaRequestNullException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaSessionExpiredException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaSessionInvalidException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaCardDataMissedException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaCardNumberAlreadyTokenizedException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaInvalidExpirationDateException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaInvalidCardNumberException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaTokenNotFoundException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaTokenCanNotBeRemovedException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaCardCanNotBeRemovedException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaPartnerGuidMissedException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaInvalidPartnerTokenException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaCustomerDataMissedException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaRequestTokenMissedException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaInvalidCardTokenException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaInvalidCardHolderNameException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaReachedMaxCardsByUserException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaReachedMaxSessionsByUserException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaConnectException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaTimeoutException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                is TunaException -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
                else -> Log.e(Extras.LOG_TAG, "Tuna exception!!!", e)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = tunaGooglePay.processResult(requestCode, resultCode, data, tunaWalletCallback())

        if (!result) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun tunaWalletCallback(): TunaWalletCallback<TunaWalletPaymentData> {
        return object : TunaWalletCallback<TunaWalletPaymentData> {
            override fun onCanceled() {
                Log.i(Extras.LOG_TAG, "Process result canceled")
            }

            override fun onSuccess(result: TunaWalletPaymentData) {
                Toast.makeText(this@MainActivity, "Token Google Pay Generated!!!", Toast.LENGTH_SHORT).show()
                if (result is TunaGooglePayPaymentData) {
                    Log.i(Extras.LOG_TAG, result.data.toString())
                }
            }

            override fun onFailed(e: Throwable) {
                Log.e(Extras.LOG_TAG, "Error process result Google Pay", e)
            }
        }
    }
}