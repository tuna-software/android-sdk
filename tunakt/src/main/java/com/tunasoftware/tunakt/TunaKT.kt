package com.tunasoftware.tunakt

import com.tunasoftware.tuna.Tuna
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tuna.entities.TunaCustomer
import com.tunasoftware.tuna.entities.TunaPaymentMethod
import java.lang.RuntimeException

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
fun Tuna.addNewCard(cardNumber: String,
                    cardHolderName: String,
                    expirationMonth: Int,
                    expirationYear: Int
) = TunaRequestResult<TunaCard>().apply {
    addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, true, object : Tuna.TunaRequestCallback<TunaCard> {
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
fun Tuna.addNewCard(cardNumber: String,
                    cardHolderName: String,
                    expirationMonth: Int,
                    expirationYear: Int,
                    save: Boolean
) = TunaRequestResult<TunaCard>().apply {
    addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, save, object : Tuna.TunaRequestCallback<TunaCard> {
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
fun Tuna.addNewCard(
    cardNumber: String,
    cardHolderName: String,
    expirationMonth: Int,
    expirationYear: Int,
    cvv: String,
) = TunaRequestResult<TunaCard>().apply {
    addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, cvv, true, object : Tuna.TunaRequestCallback<TunaCard> {
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
fun Tuna.addNewCard(
    cardNumber: String,
    cardHolderName: String,
    expirationMonth: Int,
    expirationYear: Int,
    cvv: String,
    save: Boolean,
) = TunaRequestResult<TunaCard>().apply {
    addNewCard(cardNumber, cardHolderName, expirationMonth, expirationYear, cvv, save, object : Tuna.TunaRequestCallback<TunaCard> {
        override fun onSuccess(result: TunaCard) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

fun Tuna.getCardList() = TunaRequestResult<List<TunaCard>>().apply {
    getCardList(callback = object : Tuna.TunaRequestCallback<List<TunaCard>> {
        override fun onSuccess(result: List<TunaCard>) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

fun Tuna.getPaymentMethods() = TunaRequestResult<List<TunaPaymentMethod>>().apply {
    getPaymentMethods(callback = object : Tuna.TunaRequestCallback<List<TunaPaymentMethod>> {
        override fun onSuccess(result: List<TunaPaymentMethod>) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

fun Tuna.deleteCard(token: String) = TunaRequestResult<Boolean>().apply {
    deleteCard(token, callback = object : Tuna.TunaRequestCallback<Boolean> {
        override fun onSuccess(result: Boolean) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

fun Tuna.deleteCard(card: TunaCard) = deleteCard(card.token)

fun Tuna.bind(card: TunaCard, cvv: String) = TunaRequestResult<TunaCard>().apply {
    bind(card, cvv, callback = object : Tuna.TunaRequestCallback<TunaCard> {
        override fun onSuccess(result: TunaCard) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

fun Tuna.Companion.getSandboxSessionId() = TunaRequestResult<String>().apply {
    getSandboxSessionId(callback = object : Tuna.TunaRequestCallback<String> {
        override fun onSuccess(result: String) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}

