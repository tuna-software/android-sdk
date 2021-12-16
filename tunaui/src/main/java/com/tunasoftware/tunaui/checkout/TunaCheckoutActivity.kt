package com.tunasoftware.tunaui.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.tunaui.R

class TunaCheckoutActivity : AppCompatActivity(), TunaCheckoutResultHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuna_checkout)
    }

    override fun onCheckoutCompleted(result: Any) {
        TODO("Not yet implemented")
    }

}

interface TunaCheckoutResultHandler {

    fun onCheckoutCompleted(result: Any)

}