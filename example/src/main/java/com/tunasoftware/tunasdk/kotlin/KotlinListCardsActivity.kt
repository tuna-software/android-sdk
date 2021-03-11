package com.tunasoftware.tunasdk.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tunasoftware.tuna.Tuna
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tunakt.getCardList
import com.tunasoftware.tunakt.getSandboxSessionId
import com.tunasoftware.tunasdk.R
import com.tunasoftware.tunasdk.java.adapters.ListCardsAdapter
import com.tunasoftware.tunasdk.java.interfaces.RecyclerViewOnClickListener
import com.tunasoftware.tunasdk.java.listners.RecyclerViewTouchListener
import com.tunasoftware.tunasdk.java.utils.Extras
import com.tunasoftware.tunasdk.kotlin.MainActivity.Companion.printTunaException
import kotlinx.android.synthetic.main.activity_list_cards.*

class KotlinListCardsActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_NEW_CARD = 1001
        const val REQUEST_CODE_DETAIL_CARD = 1002
    }

    private val cardListAdapter = ListCardsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_cards)

        supportActionBar?.title = "List of cards"

        btnAddCard.setOnClickListener {
            val intent = Intent(this, KotlinNewCardActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_NEW_CARD)
        }

        listCards.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@KotlinListCardsActivity)
            addItemDecoration(DividerItemDecoration(this@KotlinListCardsActivity, DividerItemDecoration.VERTICAL))
            addOnItemTouchListener(RecyclerViewTouchListener(this@KotlinListCardsActivity, this, onRecyclerViewOnClickListener()))
            adapter = cardListAdapter
        }

        Tuna.getCurrentSession()?.let { tuna ->
            getCards(tuna)
        }?: getSessionIdFromBackend()
    }

    private fun getSessionIdFromBackend() {
        showLoading()
        /**
         * in production you should get session id from your own backend
         */
        Tuna.getSandboxSessionId()
            .onSuccess { sessionId ->
                //startSession returns a tuna session
                val tuna = Tuna.startSession(sessionId)
                getCards(tuna)
                hideLoading()
            }.onFailure {
                hideLoading()
                printTunaException(it)
            }
    }

    private fun getCards(tuna: Tuna) {
        tuna.getCardList()
            .onSuccess {
                hideLoading()
                cardListAdapter.setItems(it)
            }
            .onFailure {
                hideLoading()
                printTunaException(it)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_NEW_CARD -> cardListAdapter.addItemTop(
                    TunaCard(
                        data.getStringExtra(Extras.CARD_TOKEN)!!,
                        data.getStringExtra(Extras.CARD_BRAND)!!,
                        data.getStringExtra(Extras.CARD_HOLDER_NAME)!!,
                        data.getIntExtra(Extras.CARD_EXPIRATION_MONTH, 0),
                        data.getIntExtra(Extras.CARD_EXPIRATION_YEAR, 0),
                        data.getStringExtra(Extras.CARD_MASKED_NUMBER)!!
                    )
                )
                REQUEST_CODE_DETAIL_CARD -> cardListAdapter.deleteItemByToken(data.getStringExtra(Extras.CARD_TOKEN))
            }
        }
    }

    private fun onRecyclerViewOnClickListener(): RecyclerViewOnClickListener {
        return object : RecyclerViewOnClickListener {
            override fun onClick(view: View, position: Int) {
                val card = cardListAdapter.getItem(position) ?: return

                val intent = Intent(this@KotlinListCardsActivity, KotlinDetailCardActivity::class.java)
                intent.putExtra(Extras.CARD_MASKED_NUMBER, card.maskedNumber)
                intent.putExtra(Extras.CARD_HOLDER_NAME, card.cardHolderName)
                intent.putExtra(Extras.CARD_BRAND, card.brand)
                intent.putExtra(Extras.CARD_EXPIRATION_MONTH, card.expirationMonth)
                intent.putExtra(Extras.CARD_EXPIRATION_YEAR, card.expirationYear)
                intent.putExtra(Extras.CARD_TOKEN, card.token)

                startActivityForResult(intent, REQUEST_CODE_DETAIL_CARD)
            }

            override fun onLongClick(view: View, position: Int) {}
        }
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
    }
}