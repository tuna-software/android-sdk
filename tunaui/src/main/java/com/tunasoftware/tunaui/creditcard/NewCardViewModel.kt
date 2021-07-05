package com.tunasoftware.tunaui.creditcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.domain.entities.TunaUICard
import com.tunasoftware.tunaui.extensions.*
import com.tunasoftware.tunaui.utils.CardMatcher
import com.tunasoftware.tunaui.utils.SingleLiveEvent
import com.tunasoftware.tunaui.utils.TextFieldState
import com.tunasoftware.tunaui.utils.default

class NewCardViewModel(application: Application) : AndroidViewModel(application) {

    val actionsLiveData = SingleLiveEvent<Any>()

    var cardFlag = MutableLiveData<CardMatcher.CardFlag>().default(CardMatcher.CardFlag.UNDEFINED)

    val cardNumber = CreditCardField(CreditCardFieldType.NUMBER){ field, showError ->
        if (field.getValue().isCreditCard())
            true else {
            if (showError) field.setError(context.getString(R.string.tuna_card_number_invalid))
            false
        }
    }

    val cardName = CreditCardField(CreditCardFieldType.NAME){ field, showError ->
        if (field.getValue().isNotBlank() && field.getValue().split(" ").size >= 2) {
            true
        } else {
            if (showError) field.setError(context.getString(R.string.tuna_card_name_invalid))
            false
        }
    }

    val cardExpirationDate = CreditCardField(CreditCardFieldType.EX_DATE){ field, showError ->
        if (field.getValue().isYearMonth()){
            true
        } else {
            if (showError) field.setError(context.getString(R.string.tuna_card_expitarion_date_invalid))
            false
        }
    }

    val cardCvv = CreditCardField(CreditCardFieldType.CVV){ field, showError ->
        if (field.getValue().length >= 3){
            true
        } else {
            if (showError) field.setError(context.getString(R.string.tuna_card_cvv_invalid))
            false
        }
    }

    val cardCpf = CreditCardField(CreditCardFieldType.CPF){ field, showError ->
        if (field.getValue().isCpf()){
            true
        } else {
            if (showError) field.setError(context.getString(R.string.tuna_card_cpf_invalid))
            false
        }
    }

    fun setCardData(name:String , number:String, expiration:String){
        cardName.text.value = name
        cardNumber.text.value = number
        cardExpirationDate.text.value = expiration
    }

    fun onPreviousClick() {
        actionsLiveData.postValue(ActionFieldBack())
    }

    fun onNextClick(currentField:CreditCardFieldType) {
        if (currentField == CreditCardFieldType.CPF) {
            saveCard()
        } else {
            actionsLiveData.postValue(ActionFieldNext())
        }
    }

    fun saveCard(){
        //TODO: Save card
        actionsLiveData.postValue(
            ActionFinish(
                TunaUICard(
                    token = "",
                    brand = "brand",
                    cardHolderName = cardName.getValue(),
                    maskedNumber = cardNumber.getValue().let {
                        "* * * * ${
                            it.substring(
                                it.length - 4,
                                it.length
                            )
                        }"
                                                             },
                    expirationMonth = 2,
                    expirationYear = 2023
                )
            )
        )
    }

    init {
        cardNumber.text.observeForever { cardFlag.value = CardMatcher.matches(it.toString()) }
    }
}

class CreditCardField(var type: CreditCardFieldType, val validation : (CreditCardField, Boolean) -> Boolean): TextFieldState(){

    fun validate() = validation.invoke(this, true)
    fun validate(showError:Boolean) = validation.invoke(this, showError)

}

enum class CreditCardFieldType {
    NUMBER,
    NAME,
    EX_DATE,
    CVV,
    CPF
}

class ActionFieldBack
class ActionFieldNext
class ActionFinish(val card: TunaUICard)
