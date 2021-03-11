package com.tunasoftware.tunaui.creditcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.extensions.*
import com.tunasoftware.tunaui.utils.TextFieldState

class NewCardViewModel(application: Application) : AndroidViewModel(application) {
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

    val cardPhone = CreditCardField(CreditCardFieldType.PHONE){ field, showError ->
        if (field.getValue().isPhone()){
            true
        } else {
            if (showError) field.setError(context.getString(R.string.tuna_card_phone_invalid))
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

    fun onPreviousClick() {
//        submitAction(ActionFieldBack())
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
    PHONE,
    CPF
}
