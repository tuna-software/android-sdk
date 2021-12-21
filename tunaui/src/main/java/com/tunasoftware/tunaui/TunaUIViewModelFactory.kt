package com.tunasoftware.tunaui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tunasoftware.android.Tuna
import com.tunasoftware.tunaui.creditcard.NewCardViewModel
import com.tunasoftware.tunaui.delivery.TunaSelectDeliveryViewModel
import com.tunasoftware.tunaui.select.SelectPaymentMethodViewModel

class TunaUIViewModelFactory constructor(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SelectPaymentMethodViewModel::class.java) -> {
                provideTunaViewModel(modelClass)
            }
            modelClass.isAssignableFrom(TunaSelectDeliveryViewModel::class.java) -> {
                provideTunaViewModel(modelClass)
            }
            modelClass.isAssignableFrom(NewCardViewModel::class.java) -> {
                provideNewCreditCardViewModel(modelClass)
            }
            else -> modelClass.getConstructor().newInstance()
        }
    }

    private fun <T : ViewModel?> provideTunaViewModel(modelClass: Class<T>): T {
        return modelClass.getConstructor(Tuna::class.java).newInstance(Tuna.getCurrentSession())
    }

    private fun <T : ViewModel?> provideNewCreditCardViewModel(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, Tuna::class.java)
            .newInstance(application, Tuna.getCurrentSession())
    }

}