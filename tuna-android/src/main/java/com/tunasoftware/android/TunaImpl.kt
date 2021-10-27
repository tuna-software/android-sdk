package com.tunasoftware.android

import android.app.Activity
import android.util.Log
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalEnvironment
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalRenderType
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalUiType
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalActionCode
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalConfigurationParameters
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalValidateReceiver
import com.cardinalcommerce.shared.userinterfaces.UiCustomization
import com.tunasoftware.tuna.request.TunaCoreImp
import com.tunasoftware.tuna.request.rest.TunaAPI
import com.tunasoftware.tuna.request.rest.TunaEngineAPI
import org.json.JSONArray

class TunaImpl(sessionId: String, tunaAPI: TunaAPI, tunaEngineAPI: TunaEngineAPI) : TunaCoreImp(sessionId, tunaAPI, tunaEngineAPI), Tuna {

    private val cardinal: Cardinal = Cardinal.getInstance()

    override fun init3DS(activity: Activity) {
        val renderType = JSONArray()
        renderType.put(CardinalRenderType.OTP)
        renderType.put(CardinalRenderType.SINGLE_SELECT)
        renderType.put(CardinalRenderType.MULTI_SELECT)
        renderType.put(CardinalRenderType.OOB)
        renderType.put(CardinalRenderType.HTML)

        val parameters = CardinalConfigurationParameters()
        parameters.environment = CardinalEnvironment.STAGING
        parameters.requestTimeout = 8000
        parameters.challengeTimeout = 5
        parameters.renderType = renderType
        parameters.uiType = CardinalUiType.BOTH
        parameters.uiCustomization = UiCustomization()

        cardinal.configure(activity, parameters)

        val service = object : CardinalInitService {
            /**
             * You may have your Submit button disabled on page load. Once you are set up for CCA,
             * you may then enable it. This will prevent users from submitting their order before
             * CCA is ready.
             */
            override fun onSetupCompleted(consumerSessionId: String) {
                Log.d("onSetupCompleted", consumerSessionId)
            }

            /**
             * If there was an error with set up, Cardinal will call this function with validate
             * response and empty serverJWT
             *
             * @param validateResponse
             * @param serverJwt will be an empty
             */
            override fun onValidated(validateResponse: ValidateResponse?, serverJwt: String?) {
                validateResponse?.payment?.processorTransactionId
                serverJwt?.let { Log.d("onValidated", it) }
            }
        }

        val jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhNWE1OWJmYi1hYzA2LTRjNWYtYmU1Yy0zNTFiNjRhZTYwOGUiLCJpYXQiOjE2MzQ2NDk5ODAsImlzcyI6IjU2NTYwYTM1OGI5NDZlMGM4NDUyMzY1ZHMiLCJPcmdVbml0SWQiOiI1NjU2MDdjMThiOTQ2ZTA1ODQ2M2RzOHIiLCJQYXlsb2FkIjp7Im51bWJlciI6IjBlNWM1YmYyLWVhNjQtNDJlOC05ZWUxLTcxZmZmNjUyMmUxNSIsImFtb3VudCI6IjE1MDAiLCJjdXJyZW5jeV9jb2RlIjoiODQwIn0sImV4cCI6MTYzNDY1MDg4MH0.0J03hm1oveKyAx2vpFq2sg3ohipwD--rY4EWMjAlaY4"

        cardinal.init(jwt, service)
    }

    fun continue3DS(transactionId: String, payload: String, activity: Activity) {
        val receiver =
            /**
             * This method is triggered when the transaction has been terminated. This is how SDK
             * hands back control to the merchant's application. This method will include data on
             * how the transaction attempt ended and you should have your logic for reviewing the
             * results of the transaction and making decisions regarding next steps. JWT will be
             * empty if validate was not successful.
             *
             * @param validateResponse
             * @param serverJWT
             */
            CardinalValidateReceiver { currentContext, validateResponse, serverJWT ->
                when (validateResponse?.actionCode) {
                    CardinalActionCode.SUCCESS -> {
                        // Handle successful transaction, send JWT to backend to verify
                    }
                    CardinalActionCode.NOACTION -> {
                        // Handle no actionable outcome
                    }
                    CardinalActionCode.FAILURE -> {
                        // Handle failed transaction attempt
                    }
                    CardinalActionCode.CANCEL -> {
                        // Handle cancel transaction
                    }
                    CardinalActionCode.ERROR -> {
                        // Handle service level error
                    }
                    CardinalActionCode.TIMEOUT -> {
                        // Handle timeout
                    }
                }
            }
        cardinal.cca_continue(transactionId, payload, activity, receiver)
    }
}