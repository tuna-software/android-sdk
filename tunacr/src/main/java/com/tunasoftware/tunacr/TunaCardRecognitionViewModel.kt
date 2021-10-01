package com.tunasoftware.tunacr

import androidx.lifecycle.ViewModel
import utils.SingleLiveEvent

class TunaCardRecognitionViewModel : ViewModel() {

    val readNames = mutableMapOf<String, Int>()
    val readNumbers = mutableMapOf<String, Int>()
    val readExpirations = mutableMapOf<String, Int>()

    val actionsLiveData = SingleLiveEvent<Any>()

    var finished = false;

    fun onDataCollected(number:String , name:String, expiration:String){
        if (finished) return
        if (name.isNotBlank()){
            readNames[name] = (readNames[name]?:0).toInt() + 1
        }
        if (expiration.isNotBlank()){
            readExpirations[expiration] = (readExpirations[expiration]?:0).toInt() + 1
        }
        if (number.isNotBlank()){
            readNumbers[number] = (readNumbers[number]?:0).toInt() + 1
        }
        verifyResult()
    }

    fun verifyResult(){
        var readNumber = readNumbers.entries.filter { it.value >= 3 }.maxByOrNull { it.value }?.key
        var readName = readNames.entries.maxByOrNull { it.value }?.key
        var readExpiration = readExpirations.entries.maxByOrNull { it.value }?.key
        if (readNumber != null){
            finished = true
            actionsLiveData.postValue(ActionNumberDetected(readNumber, readName, readExpiration))
        }
    }
}

data class ActionNumberDetected(val number:String, val name:String?, val expiration: String?)

