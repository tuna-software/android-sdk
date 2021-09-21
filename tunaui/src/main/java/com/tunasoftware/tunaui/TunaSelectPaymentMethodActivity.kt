package com.tunasoftware.tunaui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.tuna.Tuna
import com.tunasoftware.tuna.exceptions.TunaSessionExpiredException
import com.tunasoftware.tunaui.select.PaymentMethod

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

    override fun onPaymentSelected(paymentMethod: PaymentMethod) {

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