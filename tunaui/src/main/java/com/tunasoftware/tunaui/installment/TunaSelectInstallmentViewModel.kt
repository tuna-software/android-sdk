package com.tunasoftware.tunaui.installment

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

class TunaSelectInstallmentViewModel(val tuna: Tuna) : ViewModel() {

    private var _isInitialized = false
    private val _installmentsLiveData = MutableLiveData<List<Installment>>()

    val actionsLiveData = SingleLiveEvent<Any>()
    val state = MutableLiveData<UIState<List<Installment>>>().default(UIState.Loading)

    fun init() = viewModelScope.launch {
        if (!_isInitialized) {
            _isInitialized = true

            Handler(Looper.getMainLooper()).postDelayed({
                postValue(listOf(
                    Installment(
                        id = 1,
                        name = "1x interest - free",
                        description = "(1 x 2.843,00 = 2.843,00)",
                        selected = true
                    ),
                    Installment(
                        id = 2,
                        name = "2x interest - free",
                        description = "(2 x 1.421,50 = 2.843,00)"
                    ),
                    Installment(
                        id = 3,
                        name = "3x interest - free",
                        description = "(3 x 947,66 = 2.843,00)"
                    ),
                    Installment(
                        id = 4,
                        name = "4x with interest",
                        description = "5% (4 x 712,66 = 2.854,35)"
                    ),
                    Installment(
                        id = 5,
                        name = "5x with interest",
                        description = "5% (5 x 568,60 = 2.854,35)"
                    ),
                    Installment(
                        id = 6,
                        name = "6x with interest",
                        description = "5% (6 x 473,86 = 2.854,35)"
                    )
                ))
            }, 4000)
        }
    }

    fun onSubmitClick(){
        _installmentsLiveData.value?.firstOrNull { it.selected }?.let {
            actionsLiveData.postValue(ActionOnInstallmentSelected(it))
        }
    }

    fun onInstallmentSelected(installment: Installment) {
        _installmentsLiveData.value?.let { list ->
            val newList = list.toMutableList()
            list.find { it.selected }?.let {
                setSelectedInstallment(list, newList, it, false)
            }

            setSelectedInstallment(list, newList, installment, true)

            postValue(newList)
        }
    }

    private fun postValue(list: List<Installment>) {
        _installmentsLiveData.value = list
        state.postValue(UIState.Success(list))
    }

    private fun setSelectedInstallment(list: List<Installment>, newList: MutableList<Installment>, installment: Installment, selected: Boolean) {
        val index = list.indexOf(installment)
        if (index > -1) {
            val item = copyInstallment(installment)
            item.selected = selected
            newList[index] = item
        }
    }

    private fun copyInstallment(installment: Installment): Installment {
        return Installment(
            id = installment.id,
            name = installment.name,
            description = installment.description,
            selected = installment.selected
        )
    }
}

data class ActionOnInstallmentSelected(val installment: Installment)