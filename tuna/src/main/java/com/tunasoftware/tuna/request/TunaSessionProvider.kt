package com.tunasoftware.tuna.request

import com.tunasoftware.tuna.entities.TunaAPIKey
import com.tunasoftware.tuna.entities.TunaCustomer
import com.tunasoftware.tuna.exceptions.TunaException
import com.tunasoftware.tuna.exceptions.toTunaException
import com.tunasoftware.tuna.request.rest.NewSessionDataVO
import com.tunasoftware.tuna.request.rest.SessionCustomerDataVO
import com.tunasoftware.tuna.request.rest.TunaAPI
import kotlinx.coroutines.Deferred

class TunaSessionProvider(private val tunaAPI: TunaAPI) {

    fun getNewSession(tunaAPIKey: TunaAPIKey, tunaCustomer: TunaCustomer):String {
        return NewSessionDataVO(
                customer = SessionCustomerDataVO(
                        id = tunaCustomer.id,
                        email = tunaCustomer.email
                )
        ).let {
           tunaAPI.newSession(tunaAPIKey.appToken, it).execute().let { result->
               if (result.isSuccessful) {
                   result.body()?.let { sessionResult ->
                       if (sessionResult.code == 1) {
                           sessionResult.sessionId
                       } else {
                           throw result.code().toTunaException(result.message()?:TunaException.getDefaultException().message)
                       }
                   }?:throw TunaException.getDefaultException()

               } else {
                   throw TunaException.getDefaultException()
               }
           }
        }
    }

}