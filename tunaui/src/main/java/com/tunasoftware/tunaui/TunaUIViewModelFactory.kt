package com.tunasoftware.tunaui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tunasoftware.tuna.Tuna
import com.tunasoftware.tunaui.creditcard.NewCardViewModel
import com.tunasoftware.tunaui.select.SelectPaymentMethodViewModel

class TunaUIViewModelFactory constructor(private val application:Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectPaymentMethodViewModel::class.java)){
            return provideSelectPaymentMethodViewModel(modelClass)
        } else if (modelClass.isAssignableFrom(NewCardViewModel::class.java)){
            return provideNewCreditCardViewModel(modelClass)
        }
        return modelClass.getConstructor().newInstance()
    }

    private fun <T : ViewModel?> provideSelectPaymentMethodViewModel(modelClass: Class<T>) : T{
        return modelClass.getConstructor(Tuna::class.java).newInstance(Tuna.getCurrentSession())
    }

    private fun <T : ViewModel?> provideNewCreditCardViewModel(modelClass: Class<T>) : T{
        return modelClass.getConstructor(Application::class.java, Tuna::class.java).newInstance(application, Tuna.getCurrentSession())
    }


}