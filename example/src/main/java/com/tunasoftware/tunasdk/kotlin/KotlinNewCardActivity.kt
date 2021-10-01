package com.tunasoftware.tunasdk.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.android.Tuna
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tunakt.addNewCard
import com.tunasoftware.tunasdk.R
import com.tunasoftware.tunasdk.java.JavaDetailCardActivity
import com.tunasoftware.tunasdk.java.utils.Extras
import com.tunasoftware.tunasdk.kotlin.MainActivity.Companion.printTunaException
import kotlinx.android.synthetic.main.activity_new_card.*

class KotlinNewCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_card)
        supportActionBar?.title = "Add new card"
        btnGenerateCard.setOnClickListener {
            Tuna.getCurrentSession()?.let { session ->
                addNewCard(session)
            }
        }
        cbInformCvv.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            edtCardCvv.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun addNewCard(tuna: Tuna) {
        showLoading()
        val number = edtCardNumber.text.toString()
        val name = edtCardHolderName.text.toString()
        val month = edtCardExpirationMonth.text.toString()
        val year = edtCardExpirationYear.text.toString()
        val cvv = edtCardCvv.text.toString()
        val save = cbSaveCard.isChecked

        if (cvv.isEmpty()) {
            tuna.addNewCard(cardNumber = number,
                            cardHolderName = name,
                            expirationMonth = month.toInt(),
                            expirationYear = year.toInt(),
                            save = save)
                .onSuccess { onSuccess(!save, it) }
                .onFailure { printTunaException(it) }
        } else {
            tuna.addNewCard(cardNumber = number,
                            cardHolderName = name,
                            expirationMonth = month.toInt(),
                            expirationYear = year.toInt(),
                            cvv = cvv,
                            save = save)
                .onSuccess { onSuccess(!save, it) }
                .onFailure { printTunaException(it) }
        }
    }

    private fun onSuccess(detail: Boolean, result: TunaCard) {
        hideLoading()
        Toast.makeText(
            this@KotlinNewCardActivity,
            "Card " + result.maskedNumber + " successfully added!!!",
            Toast.LENGTH_LONG
        ).show()
        val intent = if (detail) Intent(
            this@KotlinNewCardActivity,
            JavaDetailCardActivity::class.java
        ) else Intent()
        intent.putExtra(Extras.CARD_TOKEN, result.token)
        intent.putExtra(Extras.CARD_MASKED_NUMBER, result.maskedNumber)
        intent.putExtra(Extras.CARD_HOLDER_NAME, result.cardHolderName)
        intent.putExtra(Extras.CARD_BRAND, result.brand)
        intent.putExtra(Extras.CARD_EXPIRATION_MONTH, result.expirationMonth)
        intent.putExtra(Extras.CARD_EXPIRATION_YEAR, result.expirationYear)
        if (detail) {
            startActivity(intent)
            setResult(RESULT_CANCELED)
        } else {
            setResult(RESULT_OK, intent)
        }
        finish()
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
    }
}