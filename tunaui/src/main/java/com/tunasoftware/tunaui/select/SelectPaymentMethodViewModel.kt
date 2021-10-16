package com.tunasoftware.tunaui.select

import android.content.Context
import androidx.lifecycle.*
import com.tunasoftware.android.Tuna
import com.tunasoftware.tuna.entities.TunaPaymentMethodType
import com.tunasoftware.tunacommons.ui.entities.UIState
import com.tunasoftware.tunakt.deleteCard
import com.tunasoftware.tunakt.getCardList
import com.tunasoftware.tunakt.getPaymentMethods
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.domain.entities.TunaCardFlag
import com.tunasoftware.tunaui.domain.entities.TunaUICard
import com.tunasoftware.tunaui.extensions.default
import com.tunasoftware.tunaui.utils.SingleLiveEvent
import com.tunasoftware.tunaui.utils.announceForAccessibility
import kotlinx.coroutines.launch

class SelectPaymentMethodViewModel(val tuna: Tuna) : ViewModel() {

    val actionsLiveData = SingleLiveEvent<Any>()

    private var selectedPaymentMethod: PaymentMethod? = null

    private val _paymentsLiveData = MutableLiveData<List<PaymentMethod>>()
    private val _cardsLiveData = MutableLiveData<List<PaymentMethod>>()
    private val _paymetsMediatorLiveData = MediatorLiveData<List<PaymentMethod>>().apply {

        var cards: List<PaymentMethod>? = null
        var payments : List<PaymentMethod>? = null

        fun verify(){
            val mCards = cards;
            val mPayments = payments
            if (mCards != null && mPayments != null){
                value = mCards + mPayments
            }
        }

        addSource(_paymentsLiveData){
            payments = it
            verify()
        }

        addSource(_cardsLiveData){
            cards = it
            verify()
        }
    }

    val state = MutableLiveData<UIState<List<PaymentMethod>>>().default(UIState.Loading)

    val paymentsObserver = Observer<List<PaymentMethod>>{
        state.postValue(UIState.Success(it))
    }

    fun init() = viewModelScope.launch {
        _paymetsMediatorLiveData.observeForever(paymentsObserver)
        tuna.getCardList()
            .onSuccess { result ->
                result.map {
                    PaymentMethodCreditCard(methodType = PaymentMethodType.CREDIT_CARD,
                        displayName = it.maskedNumber,
                        disableSwipe = false,
                        selectable = true,
                        flag = TunaCardFlag.fromBrand(it.brand),
                        tunaUICard = it.let { TunaUICard(token = it.token,
                            brand = it.brand,
                            cardHolderName = it.cardHolderName,
                            expirationMonth = it.expirationMonth,
                            expirationYear = it.expirationYear,
                            maskedNumber = it.maskedNumber) })
                }.toList().run {
                    _cardsLiveData.postValue(this)
                }
            }.onFailure {
                //TODO: handle error
                _cardsLiveData.postValue(listOf())
            }
        tuna.getPaymentMethods()
            .onSuccess { list ->
                _paymentsLiveData.postValue(list.map { it ->
                    PaymentMethod(
                        methodType = when (it.type) {
                            TunaPaymentMethodType.CREDIT_CARD -> PaymentMethodType.NEW_CREDIT_CARD
                            TunaPaymentMethodType.BANK_SLIP -> PaymentMethodType.BANK_SLIP
                            else -> TODO()
                        },
                        displayName = it.displayName,
                        disableSwipe = true,
                        selectable = false
                    )
                })
            }
            .onFailure { error ->
                //TODO: handle error
                _paymentsLiveData.postValue(listOf())
            }
    }



    fun onDeleteCard(paymentMethod:PaymentMethodCreditCard, context: Context?) = viewModelScope.launch {
        context?.announceForAccessibility(context.getString(R.string.tuna_accessibility_removing_card))
        tuna.deleteCard(paymentMethod.tunaUICard.token)
            .onSuccess {
                //Nothing to be done, UI has already removed the item from view
                context?.announceForAccessibility(context.getString(R.string.tuna_accessibility_removed_card))
            }
            .onFailure {
                actionsLiveData.postValue(ActionShowErrorDeletingCard())
            }
    }

    fun onPaymentMethodSelected(paymentMethod: PaymentMethod){
        selectedPaymentMethod = paymentMethod
    }

    fun onSubmitClick(){
        selectedPaymentMethod?.let {
            actionsLiveData.postValue(ActionOnPaymentMethodSelected(it))
        }
    }

    override fun onCleared() {
        _paymetsMediatorLiveData.removeObserver(paymentsObserver)
        super.onCleared()
    }

}

class ActionShowErrorDeletingCard
data class ActionOnPaymentMethodSelected(val paymentMethod: PaymentMethod)

