package com.tunasoftware.tunaui.utils

import androidx.lifecycle.MutableLiveData

open class TextFieldState {

    val text = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    init {
        text.observeForever {
            if (it.isNotBlank()) {
                setError("")
            }
        }
    }

    fun getValue(): String = text.value ?: ""

    fun setError(error: String) {
        this.error.postValue(error)
    }

}