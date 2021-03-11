package com.tunasoftware.tunaui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TunaSelectPaymentMethodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuna_select_payment_method)
    }

    companion object {

        fun startForResult(activity:Activity, requestCode:Int){
            Intent(activity, TunaSelectPaymentMethodActivity::class.java).also {
                activity.startActivityForResult(it, requestCode)
            }
        }

    }

}