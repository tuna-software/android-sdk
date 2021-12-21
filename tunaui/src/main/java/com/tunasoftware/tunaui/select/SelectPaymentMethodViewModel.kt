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
    val state = MutableLiveData<UIState<List<PaymentMethod>>>().default(UIState.Loading)

    private var _isInitialized = false

    private val _paymentsLiveData = MutableLiveData<List<PaymentMethod>>()
    private val _cardsLiveData = MutableLiveData<List<PaymentMethod>>()
    private val _paymentsMediatorLiveData = MediatorLiveData<List<PaymentMethod>>().apply {

        var cards: List<PaymentMethod>? = null
        var payments : List<PaymentMethod>? = null

        fun verify(){
            val mCards = cards
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

    private val paymentsObserver = Observer<List<PaymentMethod>>{
        state.postValue(UIState.Success(it))
    }

    fun init() = viewModelScope.launch {
        if (!_isInitialized) {
            _isInitialized = true
            _paymentsMediatorLiveData.observeForever(paymentsObserver)
            tuna.getCardList()
                .onSuccess { result ->
                    result.map {
                        PaymentMethodCreditCard(
                            methodType = PaymentMethodType.CREDIT_CARD,
                            displayName = it.maskedNumber,
                            disableSwipe = false,
                            selectable = true,
                            selected = false,
                            flag = TunaCardFlag.fromBrand(it.brand),
                            tunaUICard = it.let {
                                TunaUICard(
                                    token = it.token,
                                    brand = it.brand,
                                    cardHolderName = it.cardHolderName,
                                    expirationMonth = it.expirationMonth,
                                    expirationYear = it.expirationYear,
                                    maskedNumber = it.maskedNumber)
                            })
                    }.toList().run {
                        selectFirstCard(this)
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
    }

    fun onDeleteCard(paymentMethod:PaymentMethodCreditCard, context: Context?) = viewModelScope.launch {
        _cardsLiveData.value?.let {
            val cards = (it.toMutableList() - paymentMethod).toMutableList()
            if (paymentMethod.selected) {
                selectFirstCard(cards)
            } else {
                _cardsLiveData.value = cards
            }
        }

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
        _cardsLiveData.value?.let {
            handlePaymentMethodSelected(it, paymentMethod)
        }
    }

    fun onNewCardAdded(paymentMethod: PaymentMethod){
        _cardsLiveData.value?.let { listOf(paymentMethod) + it }?.let {
            handlePaymentMethodSelected(it, paymentMethod)
        }
    }

    fun onSubmitClick(){
        _cardsLiveData.value?.firstOrNull { it.selected }?.let {
            actionsLiveData.postValue(ActionOnPaymentMethodSelected(it))
        }
    }

    override fun onCleared() {
        _paymentsMediatorLiveData.removeObserver(paymentsObserver)
        super.onCleared()
    }

    private fun selectFirstCard(list: List<PaymentMethod>) {
        val newList = list.toMutableList()
        if (list.none { it.selected }) {
            list.firstOrNull { it.selectable }?.run {
                setSelectedPaymentMethod(list, newList, this, true)
            }
        }
        _cardsLiveData.value = newList
    }

    private fun handlePaymentMethodSelected(list: List<PaymentMethod>, paymentMethod: PaymentMethod) {
        val newList = list.toMutableList()
        if (paymentMethod.selectable) {
            list.find { it.selected }?.let {
                setSelectedPaymentMethod(list, newList, it, false)
            }

            setSelectedPaymentMethod(list, newList, paymentMethod, true)
        }

        _cardsLiveData.value = newList
    }

    private fun setSelectedPaymentMethod(list: List<PaymentMethod>, newList: MutableList<PaymentMethod>, paymentMethod: PaymentMethod, selected: Boolean) {
        val index = list.indexOf(paymentMethod)
        if (index > -1) {
            val item = copyPaymentMethod(paymentMethod)
            item.selected = selected
            newList[index] = item
        }
    }

    private fun copyPaymentMethod(paymentMethod: PaymentMethod): PaymentMethod {
        if (paymentMethod is PaymentMethodCreditCard) {
            return PaymentMethodCreditCard(
                methodType = paymentMethod.methodType,
                displayName = paymentMethod.displayName,
                disableSwipe = paymentMethod.disableSwipe,
                selectable = paymentMethod.selectable,
                selected = paymentMethod.selected,
                flag = paymentMethod.flag,
                tunaUICard = paymentMethod.tunaUICard
            )
        }

        return PaymentMethod(
            methodType = paymentMethod.methodType,
            displayName = paymentMethod.displayName,
            disableSwipe = paymentMethod.disableSwipe,
            selectable = paymentMethod.selectable,
            selected = paymentMethod.selected
        )
    }
}

class ActionShowErrorDeletingCard
data class ActionOnPaymentMethodSelected(val paymentMethod: PaymentMethod)

