package com.tunasoftware.tuna

import com.tunasoftware.tuna.entities.TunaAPIKey
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tuna.entities.TunaCustomer
import com.tunasoftware.tuna.exceptions.TunaSDKNotInitiatedException
import com.tunasoftware.tuna.exceptions.TunaSDKSandboxOnlyException
import com.tunasoftware.tuna.request.TunaImp
import com.tunasoftware.tuna.request.rest.TunaAPI
import kotlinx.coroutines.*

interface Tuna {

    companion object {

        private lateinit var tunaAPIKey:TunaAPIKey
        private var hasBeenInitialized = false
        private var currentSession: Tuna? = null

        fun init(appToken: String, sandbox:Boolean = false){
            TunaServiceProvider.isSandbox = sandbox
            this.tunaAPIKey = TunaAPIKey(appToken)
            hasBeenInitialized = true
        }

        @JvmStatic
        fun startSession(sessionId:String): Tuna {
            if (!hasBeenInitialized)
                throw TunaSDKNotInitiatedException()
            return TunaImp(sessionId, TunaServiceProvider.tunaAPI).also {
                currentSession = it
            }
        }

        @JvmStatic
        fun getCurrentSession():Tuna? = currentSession

        @JvmStatic
        fun getSandboxSessionId(callback: TunaRequestCallback<String>) {
            if (!hasBeenInitialized)
                throw TunaSDKNotInitiatedException()
            if (!TunaServiceProvider.isSandbox)
                throw TunaSDKSandboxOnlyException()
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    TunaServiceProvider.getTunaSessionProvider().let { provider ->

                        provider.getNewSession(tunaAPIKey, TunaCustomer("1", "sandbox@tuna.uy"))
                    }
                }.onSuccess { sessionId ->
                    withContext(Dispatchers.Main){
                        callback.onSuccess(sessionId)
                    }
                }.onFailure {
                    withContext(Dispatchers.Main) {
                        callback.onFailed(it)
                    }
                }
            }
        }

    }

    /**
     * Creates and save a new credit card, after calling this method
     * @see getCardList to get all saved card
     * @param cardNumber Complete credit card number
     * @param cardHolderName Complete name exactly written on the card
     * @param expirationMonth Month of expiration
     * @param expirationYear Year of expiration
     */
     fun addNewCard(cardNumber: String,
                    cardHolderName: String,
                    expirationMonth: Int,
                    expirationYear: Int,
                    callback: TunaRequestCallback<TunaCard>)

    /**
     * Creates and save a new credit card, after calling this method
     * @see getCardList to get all saved card
     * @param cardNumber Complete credit card number
     * @param cardHolderName Complete name exactly written on the card
     * @param expirationMonth Month of expiration
     * @param expirationYear Year of expiration
     */
    fun addNewCard(cardNumber: String,
                   cardHolderName: String,
                   expirationMonth: Int,
                   expirationYear: Int,
                   cvv: String,
                   callback: TunaRequestCallback<TunaCard>)

    /**
     * Creates and save a new credit card, after calling this method
     * @see getCardList to get all saved card
     * @param cardNumber Complete credit card number
     * @param cardHolderName Complete name exactly written on the card
     * @param expirationMonth Month of expiration
     * @param expirationYear Year of expiration
     */
    fun addNewCard(cardNumber: String,
                   cardHolderName: String,
                   expirationMonth: Int,
                   expirationYear: Int,
                   save: Boolean = true,
                   callback: TunaRequestCallback<TunaCard>)

    /**
     * Creates and save a new credit card, after calling this method
     * @see getCardList to get all saved card
     * @param cardNumber Complete credit card number
     * @param cardHolderName Complete name exactly written on the card
     * @param expirationMonth Month of expiration
     * @param expirationYear Year of expiration
     */
    fun addNewCard(cardNumber: String,
                   cardHolderName: String,
                   expirationMonth: Int,
                   expirationYear: Int,
                   cvv: String,
                   save: Boolean = true,
                   callback: TunaRequestCallback<TunaCard>)


    /**
     * Get the list of saved cards
     */
    fun getCardList(callback: TunaRequestCallback<List<TunaCard>>)

    /**
     * Delete a saved card
     * @param token of the card to be deleted
     */
    fun deleteCard(token:String, callback: TunaRequestCallback<Boolean>)

    /**
     * Delete a saved card
     * @param card to be deleted
     */
    fun deleteCard(card:TunaCard, callback: TunaRequestCallback<Boolean>)

    /**
     * binds card with the security code (CVV)
     * this method should be called before using the token
     * @param card to bind
     * @param cvv card security code
     */
    fun bind(card:TunaCard, cvv: String, callback: TunaRequestCallback<TunaCard>)

    interface TunaRequestCallback<T> {
        fun onSuccess(result:T)
        fun onFailed(e:Throwable)
    }

}



