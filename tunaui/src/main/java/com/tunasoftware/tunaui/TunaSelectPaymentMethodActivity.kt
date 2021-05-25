package com.tunasoftware.tunaui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_tuna_select_payment_method.*
import kotlinx.android.synthetic.main.select_payment_method_fragment.*

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