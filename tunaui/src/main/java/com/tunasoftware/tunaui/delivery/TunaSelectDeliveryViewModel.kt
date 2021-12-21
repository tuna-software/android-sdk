package com.tunasoftware.tunaui.delivery

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunasoftware.android.Tuna
import com.tunasoftware.tunacommons.ui.entities.UIState
import com.tunasoftware.tunaui.extensions.default
import com.tunasoftware.tunaui.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class TunaSelectDeliveryViewModel(val tuna: Tuna) : ViewModel() {

    private var _isInitialized = false
    private val _deliveriesLiveData = MutableLiveData<List<Delivery>>()

    val actionsLiveData = SingleLiveEvent<Any>()
    val state = MutableLiveData<UIState<List<Delivery>>>().default(UIState.Loading)

    fun init() = viewModelScope.launch {
        if (!_isInitialized) {
            _isInitialized = true

            Handler(Looper.getMainLooper()).postDelayed({
                postValue(listOf(
                    Delivery(
                        id = 1,
                        name = "SEDEX Hoje",
                        description = "Entrega no mesmo dia",
                        value = "R$ 10,00",
                        selected = true
                    ),
                    Delivery(
                        id = 2,
                        name = "SEDEX 10",
                        description = "Entrega as 10 da manhã do próximo dia",
                        value = "R$ 8,00"
                    ),
                    Delivery(
                        id = 3,
                        name = "SEDEX",
                        description = "Entrega expressa",
                        value = "R$ 6,00"
                    ),
                    Delivery(
                        id = 4,
                        name = "PAC",
                        description = "Entrega expressa",
                        value = "R$ 4,00"
                    )
                ))
            }, 4000)
        }
    }

    fun onSubmitClick(){
        _deliveriesLiveData.value?.firstOrNull { it.selected }?.let {
            actionsLiveData.postValue(ActionOnDeliverySelected(it))
        }
    }

    fun onDeliverySelected(delivery: Delivery) {
        _deliveriesLiveData.value?.let { list ->
            val newList = list.toMutableList()
            list.find { it.selected }?.let {
                setSelectedDelivery(list, newList, it, false)
            }

            setSelectedDelivery(list, newList, delivery, true)

            postValue(newList)
        }
    }

    private fun postValue(list: List<Delivery>) {
        _deliveriesLiveData.value = list
        state.postValue(UIState.Success(list))
    }

    private fun setSelectedDelivery(list: List<Delivery>, newList: MutableList<Delivery>, delivery: Delivery, selected: Boolean) {
        val index = list.indexOf(delivery)
        if (index > -1) {
            val item = copyDelivery(delivery)
            item.selected = selected
            newList[index] = item
        }
    }

    private fun copyDelivery(delivery: Delivery): Delivery {
        return Delivery(
            id = delivery.id,
            name = delivery.name,
            description = delivery.description,
            value = delivery.value,
            selected = delivery.selected
        )
    }
}

data class ActionOnDeliverySelected(val delivery: Delivery)