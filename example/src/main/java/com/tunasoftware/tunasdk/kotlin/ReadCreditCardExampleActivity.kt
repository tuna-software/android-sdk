package com.tunasoftware.tunasdk.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tunasoftware.tunacr.TunaCardRecognition
import com.tunasoftware.tunasdk.R
import kotlinx.android.synthetic.main.activity_read_credit_card_example.*

class ReadCreditCardExampleActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CARD_RECOGNITION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_credit_card_example)

        btnReadCreditCard.setOnClickListener {
            TunaCardRecognition.startForResult(this, REQUEST_CARD_RECOGNITION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CARD_RECOGNITION){
            data?.run {
                cardHolderName.text = getStringExtra(TunaCardRecognition.RESULT_NAME)?:""
                cardNumber.text = getStringExtra(TunaCardRecognition.RESULT_NUMBER)?:""
                cardExpiration.text = getStringExtra(TunaCardRecognition.RESULT_EXPIRATION)?:""
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}