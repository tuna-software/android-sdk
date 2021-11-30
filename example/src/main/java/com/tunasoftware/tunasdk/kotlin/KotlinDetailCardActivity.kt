package com.tunasoftware.tunasdk.kotlin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.android.Tuna
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tuna.exceptions.*
import com.tunasoftware.tunakt.bind
import com.tunasoftware.tunakt.deleteCard
import com.tunasoftware.tunasdk.R
import com.tunasoftware.tunasdk.java.utils.Extras
import com.tunasoftware.tunasdk.kotlin.MainActivity.Companion.printTunaException
import kotlinx.android.synthetic.main.activity_detail_card.*
import kotlinx.android.synthetic.main.model_card.*

class KotlinDetailCardActivity : AppCompatActivity() {

    private var tuna: Tuna? = null
    lateinit var card: TunaCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_card)

        if (intent != null) {
            val cardMaskedNumber = intent.getStringExtra(Extras.CARD_MASKED_NUMBER)
            val cardHolderName = intent.getStringExtra(Extras.CARD_HOLDER_NAME)
            val cardBrand = intent.getStringExtra(Extras.CARD_BRAND)
            val cardExpirationMonth = intent.getIntExtra(Extras.CARD_EXPIRATION_MONTH, 0)
            val cardExpirationYear = intent.getIntExtra(Extras.CARD_EXPIRATION_YEAR, 0)
            val cardToken = intent.getStringExtra(Extras.CARD_TOKEN)
            card = TunaCard(
                cardToken!!,
                cardBrand!!,
                cardHolderName!!,
                cardExpirationMonth,
                cardExpirationYear,
                cardMaskedNumber!!
            )
            tvNumber.text = String.format(
                "%s - %s",
                cardMaskedNumber.trim(),
                cardBrand
            )
            tvHolderName.text = cardHolderName
            tvExpiration.text = String.format(
                "%s/%s",
                cardExpirationMonth,
                cardExpirationYear
            )
            btnBind.setOnClickListener { showDialog() }
        }

        tuna = Tuna.getCurrentSession()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail_card, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_card) {
            deleteCard()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindCard(cvv: String) {
        showLoading()
        tuna?.run {
            bind(card = card, cvv = cvv)
                .onSuccess {
                    Toast.makeText(
                        this@KotlinDetailCardActivity,
                        "Bind card success!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    hideLoading()
                }
                .onFailure {
                    hideLoading()
                    printTunaException(it)
                }
        }
    }

    private fun deleteCard() {
        showLoading()
        tuna?.run {
            deleteCard(card = card)
                .onSuccess {
                    Toast.makeText(
                        this@KotlinDetailCardActivity,
                        "Card successfully deleted!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent()
                    intent.putExtra(Extras.CARD_TOKEN, card.token)
                    setResult(RESULT_OK, intent)
                    finish()
                }
                .onFailure {
                    hideLoading()
                    printTunaException(it)
                }
        }
    }

    private fun showDialog() {
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val editText = EditText(this)
        editText.layoutParams = params
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.filters = arrayOf<InputFilter>(LengthFilter(4))
        editText.hint = "CVV"
        val padding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            resources.displayMetrics
        ).toInt()
        val layout = LinearLayout(this)
        layout.layoutParams = params
        layout.addView(editText)
        layout.setPadding(padding, padding, padding, padding)
        AlertDialog.Builder(this)
            .setTitle("Inform the cvv of the card")
            .setView(layout)
            .setNegativeButton("CANCEL", null)
            .setPositiveButton("OK") { _: DialogInterface?, _: Int -> bindCard(editText.text.toString()) }
            .create()
            .show()
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
        btnBind.visibility = View.GONE
        cardModel.visibility = View.GONE
    }

    private fun hideLoading() {
        btnBind.visibility = View.VISIBLE
        cardModel.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }
}