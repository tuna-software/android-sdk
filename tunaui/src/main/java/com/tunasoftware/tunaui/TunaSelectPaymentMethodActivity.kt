package com.tunasoftware.tunaui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.tunasoftware.tuna.Tuna
import com.tunasoftware.tuna.exceptions.TunaSDKNotInitiatedException
import com.tunasoftware.tuna.exceptions.TunaSessionExpiredException
import com.tunasoftware.tunaui.select.PaymentMethod
import kotlinx.android.synthetic.main.activity_tuna_select_payment_method.*
import kotlinx.android.synthetic.main.select_payment_method_fragment.*

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