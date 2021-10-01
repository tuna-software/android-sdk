package com.tunasoftware.tuna

import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tuna.entities.TunaPaymentMethod

interface TunaCore {

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

    /**
     * get payment methods available
     */
    fun getPaymentMethods(callback: TunaRequestCallback<List<TunaPaymentMethod>>)

    interface TunaRequestCallback<T> {
        fun onSuccess(result:T)
        fun onFailed(e:Throwable)
    }

}



