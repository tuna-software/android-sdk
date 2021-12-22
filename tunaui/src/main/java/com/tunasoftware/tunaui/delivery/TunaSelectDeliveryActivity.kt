package com.tunasoftware.tunaui.delivery

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.TunaUI
import com.tunasoftware.tunaui.domain.entities.DeliverySelectionResult

class TunaSelectDeliveryActivity : AppCompatActivity(), TunaDeliverySelectionResultHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuna_select_delivery)
    }

    override fun onSelectedDelivery(result: Delivery) {
        Intent().apply {
            putExtra(TunaUI.RESULT_DELIVERY_SELECTION, DeliverySelectionResult(delivery = result))
        }.also {
            setResult(RESULT_OK, it)
        }
        finish()
    }

}

interface TunaDeliverySelectionResultHandler {

    fun onSelectedDelivery(result: Delivery)

}