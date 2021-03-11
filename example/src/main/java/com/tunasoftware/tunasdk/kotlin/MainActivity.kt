package com.tunasoftware.tunasdk.kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.tuna.exceptions.*
import com.tunasoftware.tunasdk.R
import com.tunasoftware.tunasdk.java.JavaListCardsActivity
import com.tunasoftware.tunasdk.java.utils.Extras
import com.tunasoftware.tunaui.TunaMotionSwipeActivity
import com.tunasoftware.tunaui.TunaSelectPaymentMethodActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btnJavaExample).setOnClickListener {
            startActivity(Intent(this, JavaListCardsActivity::class.java))
        }

        findViewById<Button>(R.id.btnKotlinExample).setOnClickListener {
            startActivity(Intent(this, KotlinListCardsActivity::class.java))
        }

        findViewById<Button>(R.id.btnTunaUiExample).setOnClickListener {
//            startActivity(Intent(this, TunaMotionSwipeActivity::class.java))
            TunaSelectPaymentMethodActivity.startForResult(this, 1)
        }
    }

    companion object {

        fun printTunaException(e: Throwable) {
            when (e) {
                is TunaSDKNotInitiatedException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaRequestNullException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaSessionExpiredException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaSessionInvalidException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaCardDataMissedException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaCardNumberAlreadyTokenizedException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaInvalidExpirationDateException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaInvalidCardNumberException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaTokenNotFoundException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaTokenCanNotBeRemovedException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaCardCanNotBeRemovedException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaPartnerGuidMissedException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaInvalidPartnerTokenException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaCustomerDataMissedException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaRequestTokenMissedException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaInvalidCardTokenException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaInvalidCardHolderNameException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaReachedMaxCardsByUserException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaReachedMaxSessionsByUserException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaConnectException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaTimeoutException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                is TunaException -> Log.e(Extras.LOG, "Tuna exception!!!", e)
                else -> Log.e(Extras.LOG, "Tuna exception!!!", e)
            }
        }
    }
}