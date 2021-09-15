package com.tunasoftware.tunaui.select

import androidx.lifecycle.*
import com.tunasoftware.tuna.Tuna
import com.tunasoftware.tuna.entities.TunaCardPaymentMethod
import com.tunasoftware.tuna.entities.TunaPaymentMethodType
import com.tunasoftware.tunacommons.ui.entities.UIState
import com.tunasoftware.tunakt.deleteCard
import com.tunasoftware.tunakt.getCardList
import com.tunasoftware.tunakt.getPaymentMethods
import com.tunasoftware.tunaui.domain.entities.TunaCardFlag
import com.tunasoftware.tunaui.domain.entities.TunaUICard
import com.tunasoftware.tunaui.utils.SingleLiveEvent
import com.tunasoftware.tunaui.extensions.default
import kotlinx.coroutines.launch

class SelectPaymentMethodViewModel(val tuna: Tuna) : ViewModel() {

    val actionsLiveData = SingleLiveEvent<Any>()

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
            .onSuccess {
                it.map {
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
                _paymentsLiveData.postValue(list.map {
                    PaymentMethod(
                        methodType = when (it.type) {
                            TunaPaymentMethodType.CREDIT_CARD -> PaymentMethodType.NEW_CREDIT_CARD
                            TunaPaymentMethodType.BANK_SLIP -> PaymentMethodType.BANK_SLIP
                        },
                        displayName = it.displayName,
                        disableSwipe = true,
                        selectable = false
                    )
                })
            }
            .onFailure {
                //TODO: handle error
                _paymentsLiveData.postValue(listOf())
            }
    }

    fun onDeleteCard(paymentMethod:PaymentMethodCreditCard) = viewModelScope.launch {
        tuna.deleteCard(paymentMethod.tunaUICard.token)
            .onSuccess {
                //Nothing to be done, UI has already removed the item from view
            }
            .onFailure {
                actionsLiveData.postValue(ActionShowErrorDeletingCard())
            }
    }


    private fun mockPaymentMethods(): MutableList<PaymentMethod> {
        return mutableListOf(
            PaymentMethod(PaymentMethodType.NEW_CREDIT_CARD, "Novo cartão de crédito",true, selectable = false),
            PaymentMethod(PaymentMethodType.GOOGLE_PAY, "Google pay", true, selectable = false),
            PaymentMethod(PaymentMethodType.PIX, "Pix", true, selectable = false),
            PaymentMethod(PaymentMethodType.BANK_SLIP, "Boleto", true, selectable = false)
        )
    }

    override fun onCleared() {
        _paymetsMediatorLiveData.removeObserver(paymentsObserver)
        super.onCleared()
    }

}

class ActionShowErrorDeletingCard()

