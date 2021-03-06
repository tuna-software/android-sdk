package com.tunasoftware.tunakt

import com.tunasoftware.java.Tuna
import com.tunasoftware.tuna.TunaCore
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tuna.entities.TunaPaymentMethod

open class TunaRequestResult<T>{

    private var success: ((T) -> Unit)? = null
    private var failure: ((Throwable) -> Unit)? = null

    var successResult: T? = null
    var failureResult: Throwable? = null

    fun invokeSuccess(result: T){
        successResult = result
        success?.run {
            invoke(result)
            successResult = null
        }
    }

    fun invokeFailure(result: Throwable){
        failureResult = result
        failure?.run {
            invoke(result)
            failureResult = null
        }
    }

    fun onSuccess(block: (T) -> Unit): TunaRequestResult<T>{
        if (success != null)
            throw RuntimeException("The onSuccess block for this request is already defined, , please define only one onSuccess block")
        success = block
        successResult?.let {
            block(it)
            successResult = null
        }
        return this
    }

    fun onFailure(block: (Throwable) -> Unit): TunaRequestResult<T>{
        if (failure != null)
            throw RuntimeException("The onFailure block for this request is already defined, please define only one onFailure block!")
        failure = block
        failureResult?.let {
            block(it)
            failureResult = null
        }
        return this
    }

}

/**
 * Create a new credit card for a single use, this card will not be saved for further transactions
 * @see getCardList to get all saved card
 * @param cardNumber Complete credit card number
 * @param cardHolderName Complete name exactly written on the card
 * @param expirationMonth Month of expiration
 * @param expirationYear Year of expiration
 */
fun TunaCore.addNewCard(cardNumber: String,
                        cardHolderName: String,
                        expirationMonth: Int,
                        expirationYear: Int
) = TunaRequestResult<TunaCard>().apply {
    addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, true, object : TunaCore.TunaRequestCallback<TunaCard> {
        override fun onSuccess(result: TunaCard) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

/**
 * Create a new credit card for a single use, this card will not be saved for further transactions
 * @see getCardList to get all saved card
 * @param cardNumber Complete credit card number
 * @param cardHolderName Complete name exactly written on the card
 * @param expirationMonth Month of expiration
 * @param expirationYear Year of expiration
 */
fun TunaCore.addNewCard(cardNumber: String,
                        cardHolderName: String,
                        expirationMonth: Int,
                        expirationYear: Int,
                        save: Boolean
) = TunaRequestResult<TunaCard>().apply {
    addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, save, object : TunaCore.TunaRequestCallback<TunaCard> {
        override fun onSuccess(result: TunaCard) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

/**
 * Create a new credit card for a single use, this card will not be saved for further transactions
 * @see getCardList to get all saved card
 * @param cardNumber Complete credit card number
 * @param cardHolderName Complete name exactly written on the card
 * @param expirationMonth Month of expiration
 * @param expirationYear Year of expiration
 * @param cvv card sercurity code
 */
fun TunaCore.addNewCard(
    cardNumber: String,
    cardHolderName: String,
    expirationMonth: Int,
    expirationYear: Int,
    cvv: String,
) = TunaRequestResult<TunaCard>().apply {
    addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, cvv, true, object : TunaCore.TunaRequestCallback<TunaCard> {
        override fun onSuccess(result: TunaCard) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

/**
 * Create a new credit card for a single use, this card will not be saved for further transactions
 * @see getCardList to get all saved card
 * @param cardNumber Complete credit card number
 * @param cardHolderName Complete name exactly written on the card
 * @param expirationMonth Month of expiration
 * @param expirationYear Year of expiration
 * @param cvv card sercurity code
 */
fun TunaCore.addNewCard(
    cardNumber: String,
    cardHolderName: String,
    expirationMonth: Int,
    expirationYear: Int,
    cvv: String,
    save: Boolean,
) = TunaRequestResult<TunaCard>().apply {
    addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, cvv, save, object : TunaCore.TunaRequestCallback<TunaCard> {
        override fun onSuccess(result: TunaCard) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

fun TunaCore.getCardList() = TunaRequestResult<List<TunaCard>>().apply {
    getCardList(callback = object : TunaCore.TunaRequestCallback<List<TunaCard>> {
        override fun onSuccess(result: List<TunaCard>) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

fun TunaCore.getPaymentMethods() = TunaRequestResult<List<TunaPaymentMethod>>().apply {
    getPaymentMethods(callback = object : TunaCore.TunaRequestCallback<List<TunaPaymentMethod>> {
        override fun onSuccess(result: List<TunaPaymentMethod>) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

fun TunaCore.deleteCard(token: String) = TunaRequestResult<Boolean>().apply {
    deleteCard(token, callback = object : TunaCore.TunaRequestCallback<Boolean> {
        override fun onSuccess(result: Boolean) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

fun TunaCore.deleteCard(card: TunaCard) = deleteCard(card.token)

fun TunaCore.bind(card: TunaCard, cvv: String) = TunaRequestResult<TunaCard>().apply {
    bind(card, cvv, callback = object : TunaCore.TunaRequestCallback<TunaCard> {
        override fun onSuccess(result: TunaCard) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

fun Tuna.Companion.getSandboxSessionId() = TunaRequestResult<String>().apply {
    getSandboxSessionId(callback = object : TunaCore.TunaRequestCallback<String> {
        override fun onSuccess(result: String) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}
