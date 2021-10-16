package com.tunasoftware.tunaui.domain.entities
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tuna.entities.TunaPaymentMethodType
import com.tunasoftware.tunaui.select.PaymentMethodType
import java.io.Serializable

class PaymentMethodSelectionResult(val paymentMethodType: TunaPaymentMethodType, val cardInfo: TunaCard? = null) : Serializable