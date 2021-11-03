package com.tunasoftware.tunaui.creditcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tunasoftware.android.Tuna
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tunakt.addNewCard
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.domain.entities.TunaCardFlag
import com.tunasoftware.tunaui.domain.entities.TunaUICard
import com.tunasoftware.tunaui.extensions.*
import com.tunasoftware.tunaui.utils.CardMatcher
import com.tunasoftware.tunaui.utils.SingleLiveEvent
import com.tunasoftware.tunaui.utils.TextFieldState
import com.tunasoftware.tunaui.utils.announceForAccessibility

class NewCardViewModel(application: Application, val tuna: Tuna) : AndroidViewModel(application) {

    val actionsLiveData = SingleLiveEvent<Any>()

    val isSaving = MutableLiveData<Boolean>().default(false)

    var cardFlag = MutableLiveData<TunaCardFlag>().default(TunaCardFlag.UNDEFINED)

    var showCpfField: Boolean = false

    val cardNumber = CreditCardField(CreditCardFieldType.NUMBER, { field, showError ->
        if (field.getValue().isCreditCard())
            true else {
            if (showError) field.setError(context.getString(R.string.tuna_card_number_invalid))
            false
        }
    }, {
        onNextClick(it.type)
    })

    val cardName = CreditCardField(CreditCardFieldType.NAME, { field, showError ->
        if (field.getValue().isNotBlank() && field.getValue().split(" ").size >= 2) {
            true
        } else {
            if (showError) field.setError(context.getString(R.string.tuna_card_name_invalid))
            false
        }
    }, {
        onNextClick(it.type)
    })

    val cardExpirationDate = CreditCardField(CreditCardFieldType.EX_DATE, { field, showError ->
        if (field.getValue().isYearMonth()){
            true
        } else {
            if (showError) field.setError(context.getString(R.string.tuna_card_expitarion_date_invalid))
            false
        }
    }, {
        onNextClick(it.type)
    })

    val cardCvv = CreditCardField(CreditCardFieldType.CVV, { field, showError ->
        if (field.getValue().length >= 3){
            true
        } else {
            if (showError) field.setError(context.getString(R.string.tuna_card_cvv_invalid))
            false
        }
    }, {
        onNextClick(it.type)
    })

    val cardCpf = CreditCardField(CreditCardFieldType.CPF, { field, showError ->
        if (field.getValue().isCpf()){
            true
        } else {
            if (showError) field.setError(context.getString(R.string.tuna_card_cpf_invalid))
            false
        }
    }, {
        onNextClick(it.type)
    })

    fun setCardData(name:String , number:String, expiration:String){
        cardName.text.value = name
        cardNumber.text.value = number
        cardExpirationDate.text.value = expiration
    }

    fun onPreviousClick() {
        actionsLiveData.postValue(ActionFieldBack())
    }

    fun onNextClick(currentField:CreditCardFieldType) {
        if ((currentField == CreditCardFieldType.CPF && showCpfField) || (currentField == CreditCardFieldType.CVV && !showCpfField)) {
            saveCard()
        } else {
            actionsLiveData.postValue(ActionFieldNext(currentField))
        }
    }

    private fun saveCard() {
        context.announceForAccessibility(context.getString(R.string.tuna_accessibility_saving_card))
        isSaving.postValue(true)
        val expiration = cardExpirationDate.getValue().split("/")
        val expirationYear = expiration.last()
        val expirationMonth = expiration.first()
        tuna.addNewCard(
            cardNumber = cardNumber.getValue().replace(" ", ""),
            cardHolderName = cardName.getValue(),
            expirationYear = expirationYear.toInt(),
            expirationMonth = expirationMonth.toInt(),
            cvv = cardCvv.getValue(),
            save = true
        ).onSuccess {
            isSaving.postValue(false)
            actionsLiveData.postValue(
                ActionFinish(
                    it.toTunaUICard()
                )
            )
        }.onFailure {
            isSaving.postValue(false)
            actionsLiveData.postValue(ActionErrorSavingCard())
        }
    }

    init {
        cardNumber.text.observeForever { cardFlag.value = CardMatcher.matches(it.toString()) }
    }
}

fun TunaCard.toTunaUICard() = TunaUICard(
        token = token,
        brand = brand,
        cardHolderName = cardHolderName,
        maskedNumber = maskedNumber,
        expirationMonth = expirationMonth,
        expirationYear = expirationYear
    )


class CreditCardField(var type: CreditCardFieldType, val validation : (CreditCardField, Boolean) -> Boolean, val next : (CreditCardField) -> Unit): TextFieldState(){

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
class ActionFieldNext(val currentType: CreditCardFieldType)
class ActionErrorSavingCard
class ActionFinish(val card: TunaUICard)
