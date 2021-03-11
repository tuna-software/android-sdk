package com.tunasoftware.tuna.request

import com.tunasoftware.tuna.Tuna
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tuna.exceptions.*
import com.tunasoftware.tuna.exceptions.TunaExceptionCodes.*
import com.tunasoftware.tuna.request.rest.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TunaImp(private val sessionId: String, private val tunaAPI: TunaAPI): Tuna {

    override fun addNewCard(
        cardNumber: String,
        cardHolderName: String,
        expirationMonth: Int,
        expirationYear: Int,
        callback: Tuna.TunaRequestCallback<TunaCard>
    ) {
        addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, "", true, callback)
    }

    override fun addNewCard(
        cardNumber: String,
        cardHolderName: String,
        expirationMonth: Int,
        expirationYear: Int,
        cvv: String,
        callback: Tuna.TunaRequestCallback<TunaCard>
    ) {
        addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, cvv, true, callback)
    }

    override fun addNewCard(
        cardNumber: String,
        cardHolderName: String,
        expirationMonth: Int,
        expirationYear: Int,
        save: Boolean,
        callback: Tuna.TunaRequestCallback<TunaCard>
    ) {
        addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, "", save, callback)
    }

    override fun addNewCard(
        cardNumber: String,
        cardHolderName: String,
        expirationMonth: Int,
        expirationYear: Int,
        cvv: String,
        save: Boolean,
        callback: Tuna.TunaRequestCallback<TunaCard>
    ) {

        val card = CardRequestVO(
            cardNumber = cardNumber,
            cardHolderName = cardHolderName,
            expirationMonth = expirationMonth,
            expirationYear = expirationYear,
            singleUse = !save,
            cvv = cvv
        )

        tunaAPI.generate(GenerateCardRequestVO(sessionId, card = card))
            .enqueue(object : Callback<GenerateCardResultVO> {
                override fun onResponse(
                    call: Call<GenerateCardResultVO>,
                    response: Response<GenerateCardResultVO>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { result ->
                            if (result.code == 1) {
                                callback.onSuccess(result = createTunaCardByResult(card, result))
                            } else {
                                callback.onFailed(result.code.toTunaException(result.message))
                            }
                        } ?: callback.onFailed(TunaException.getDefaultException())
                    } else {
                        callback.onFailed(TunaException.getDefaultException())
                    }
                }

                override fun onFailure(call: Call<GenerateCardResultVO>, t: Throwable) {
                    callback.onFailed(t.toTunaException())
                }
            })
    }

    override fun getCardList(callback: Tuna.TunaRequestCallback<List<TunaCard>>) {
        tunaAPI.list(SessionRequestVO(sessionId))
                .enqueue(object : Callback<ListCardsResultVO> {
                    override fun onResponse(
                            call: Call<ListCardsResultVO>,
                            response: Response<ListCardsResultVO>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let { result ->
                                if (result.code == 1) {
                                    callback.onSuccess(result.toTunaCardList())
                                } else {
                                    callback.onFailed(result.code.toTunaException(result.message))
                                }
                            } ?: callback.onFailed(TunaException.getDefaultException())
                        } else {
                            callback.onFailed(TunaException.getDefaultException())
                        }
                    }

                    override fun onFailure(call: Call<ListCardsResultVO>, t: Throwable) {
                        callback.onFailed(t.toTunaException())
                    }
                })
    }

    override fun deleteCard(token: String, callback: Tuna.TunaRequestCallback<Boolean>) {
        tunaAPI.delete(DeleteCardRequestVO(sessionId, token)).enqueue(object : Callback<DeleteCardResultVO>{
            override fun onResponse(call: Call<DeleteCardResultVO>, response: Response<DeleteCardResultVO>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        if (result.code == 1) {
                            callback.onSuccess(true)
                        } else {
                            callback.onFailed(result.code.toTunaException(result.message))
                        }
                    } else {
                        callback.onFailed(TunaException.getDefaultException())
                    }
                } else {
                    callback.onFailed(TunaException.getDefaultException())
                }
            }

            override fun onFailure(call: Call<DeleteCardResultVO>, t: Throwable) {
                callback.onFailed(t.toTunaException())
            }

        })
    }

    override fun deleteCard(card: TunaCard, callback: Tuna.TunaRequestCallback<Boolean>) {
        deleteCard(card.token, callback)
    }

    override fun bind(card: TunaCard, cvv: String,  callback: Tuna.TunaRequestCallback<TunaCard>) {
        tunaAPI.bind(BindCardRequestVO(token = card.token, sessionId = sessionId, cvv = cvv))
                .enqueue(object : Callback<BindCardResultVO>{
                    override fun onResponse(call: Call<BindCardResultVO>, response: Response<BindCardResultVO>) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            if (result != null) {
                                if (result.code == 1) {
                                    callback.onSuccess(card)
                                } else {
                                    callback.onFailed(result.code.toTunaException(result.message))
                                }
                            } else {
                                callback.onFailed(TunaException.getDefaultException())
                            }
                        } else {
                            callback.onFailed(TunaException.getDefaultException())
                        }
                    }

                    override fun onFailure(call: Call<BindCardResultVO>, t: Throwable) {
                        callback.onFailed(t.toTunaException())
                    }
                })
    }

    private fun createTunaCardByResult(card: CardRequestVO, result: GenerateCardResultVO): TunaCard {
        return TunaCard(
                result.token,
                result.brand,
                card.cardHolderName,
                card.expirationMonth,
                card.expirationYear,
                card.cardNumber.let {
                    "${it.substring(0, 6)}xxxxxx${
                        it.substring(
                                it.length - 4,
                                it.length
                        )
                    }"
                })
    }

    private fun ListCardsResultVO.toTunaCardList(): List<TunaCard> {
        return (tokens?:listOf()).map {
            TunaCard(
                    it.token,
                    it.brand,
                    it.cardHolderName,
                    it.expirationMonth,
                    it.expirationYear,
                    it.maskedNumber
            )
        }
    }
}
