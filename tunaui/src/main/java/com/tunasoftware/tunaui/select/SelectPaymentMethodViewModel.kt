package com.tunasoftware.tunaui.select

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunasoftware.tuna.Tuna
import com.tunasoftware.tunacommons.ui.entities.UIState
import com.tunasoftware.tunakt.deleteCard
import com.tunasoftware.tunakt.getCardList
import com.tunasoftware.tunaui.domain.entities.TunaCardFlag
import com.tunasoftware.tunaui.domain.entities.TunaUICard
import com.tunasoftware.tunaui.utils.SingleLiveEvent
import com.tunasoftware.tunaui.extensions.default
import kotlinx.coroutines.launch

class SelectPaymentMethodViewModel(val tuna: Tuna) : ViewModel() {

    val actionsLiveData = SingleLiveEvent<Any>()

    val state = MutableLiveData<UIState<List<PaymentMethod>>>().default(UIState.Loading)

    fun init() = viewModelScope.launch {
        val paymentMethods = mockPaymentMethods()
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
                    state.postValue(UIState.Success(this + paymentMethods))
                }
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

}

class ActionShowErrorDeletingCard()

