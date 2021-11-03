package com.tunasoftware.tunaui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.tunasoftware.android.Tuna
import com.tunasoftware.tuna.entities.TunaPaymentMethod
import com.tunasoftware.tuna.entities.TunaPaymentMethodType
import com.tunasoftware.tuna.exceptions.TunaSessionExpiredException
import com.tunasoftware.tunaui.domain.entities.PaymentMethodSelectionResult
import com.tunasoftware.tunaui.select.*

class TunaSelectPaymentMethodActivity : AppCompatActivity(), TunaPaymentMethodResultHandler  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuna_select_payment_method)
    }

    companion object {

        /**
         * @param activity
         * @param requestCode
         * @throws TunaSessionExpiredException when tuna sdk has no valid session
         */
        fun startForResult(activity:Activity, requestCode:Int){
            if (Tuna.getCurrentSession() == null) {
                throw TunaSessionExpiredException()
            }
            Intent(activity, TunaSelectPaymentMethodActivity::class.java).also {
                activity.startActivityForResult(it, requestCode)
            }
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is NavHostFragment) {
                fragment.childFragmentManager.fragments.forEach {
                    if (it is SelectPaymentMethodFragment) {
                        it.onActivityResult(requestCode, resultCode, data)
                    }
                }
            }
        }
    }

    override fun onPaymentSelected(paymentMethod: PaymentMethod) {
        Intent().apply {
            putExtra(TunaUI.RESULT_PAYMENT_SELECTION, PaymentMethodSelectionResult(
                paymentMethodType = when(paymentMethod.methodType){
                    PaymentMethodType.CREDIT_CARD -> TunaPaymentMethodType.CREDIT_CARD
                    PaymentMethodType.BANK_SLIP -> TunaPaymentMethodType.BANK_SLIP
                    PaymentMethodType.PIX -> TODO()
                    PaymentMethodType.GOOGLE_PAY -> TODO()
                    PaymentMethodType.SAMSUNG_PAY -> TODO()
                    PaymentMethodType.PAYPAL -> TODO()
                    PaymentMethodType.NEW_CREDIT_CARD -> throw Exception("unexpected payment method")
                },
                cardInfo = paymentMethod.let { if (it is PaymentMethodCreditCard) it.getTunaCard() else null }
            ))
        }.also {
            setResult(RESULT_OK, it)
        }
        finish()
    }
}

interface TunaPaymentMethodResultHandler {

    fun onPaymentSelected(paymentMethod: PaymentMethod)

}

fun Activity.closeKeyboard() {
    val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = this.currentFocus
    if (view == null) view = View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}