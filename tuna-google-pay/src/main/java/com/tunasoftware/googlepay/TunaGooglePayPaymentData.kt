package com.tunasoftware.googlepay

import com.tunasoftware.wallets.TunaWalletPaymentData
import org.json.JSONObject

class TunaGooglePayPaymentData(val data: JSONObject) : TunaWalletPaymentData()