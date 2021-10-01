package com.tunasoftware.java

import com.tunasoftware.tuna.TunaCore
import com.tunasoftware.tuna.TunaServiceProvider
import com.tunasoftware.tuna.entities.TunaAPIKey
import com.tunasoftware.tuna.entities.TunaCustomer
import com.tunasoftware.tuna.exceptions.TunaSDKNotInitiatedException
import com.tunasoftware.tuna.exceptions.TunaSDKSandboxOnlyException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface Tuna : TunaCore {

    companion object {

        private lateinit var tunaAPIKey: TunaAPIKey
        private var hasBeenInitialized = false
        private var currentSession: Tuna? = null

        fun init(appToken: String, accountToken: String = "", sandbox: Boolean = false) {
            TunaServiceProvider.isSandbox = sandbox
            this.tunaAPIKey = TunaAPIKey(appToken, accountToken)
            hasBeenInitialized = true
        }

        @JvmStatic
        fun startSession(sessionId: String): Tuna {
            if (!hasBeenInitialized)
                throw TunaSDKNotInitiatedException()
            return TunaImpl(
                sessionId,
                TunaServiceProvider.tunaAPI,
                TunaServiceProvider.provideTunaEngineAPI(tunaAPIKey)
            ).also {
                currentSession = it
            }
        }

        @JvmStatic
        fun getCurrentSession(): Tuna? = currentSession

        @JvmStatic
        fun getSandboxSessionId(callback: TunaCore.TunaRequestCallback<String>) {
            if (!hasBeenInitialized)
                throw TunaSDKNotInitiatedException()
            if (!TunaServiceProvider.isSandbox)
                throw TunaSDKSandboxOnlyException()
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    TunaServiceProvider.getTunaSessionProvider()
                        .getNewSession(tunaAPIKey, TunaCustomer("1", "sandbox@tuna.uy"))
                }.onSuccess { sessionId ->
                    withContext(Dispatchers.Main) {
                        callback.onSuccess(sessionId)
                    }
                }.onFailure {
                    withContext(Dispatchers.Main) {
                        callback.onFailed(it)
                    }
                }
            }
        }
    }
}