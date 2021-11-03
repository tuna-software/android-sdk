package com.tunasoftware.tunasdk

import android.app.Application
import com.tunasoftware.android.Tuna

class TunaExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Tuna.init("a3823a59-66bb-49e2-95eb-b47c447ec7a7", "demo", sandbox = true)
    }

}