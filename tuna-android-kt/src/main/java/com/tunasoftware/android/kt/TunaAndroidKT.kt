package com.tunasoftware.android.kt

import com.tunasoftware.android.Tuna
import com.tunasoftware.tuna.TunaCore
import com.tunasoftware.tunakt.TunaRequestResult

fun Tuna.Companion.getSandboxSessionId() = TunaRequestResult<String>().apply {
    getSandboxSessionId(callback = object : TunaCore.TunaRequestCallback<String> {
        override fun onSuccess(result: String) {
            invokeSuccess(result)
        }

        override fun onFailed(e: Throwable) {
            invokeFailure(e)
        }
    })
}