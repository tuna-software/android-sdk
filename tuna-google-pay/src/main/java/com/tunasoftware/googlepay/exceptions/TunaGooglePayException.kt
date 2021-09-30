package com.tunasoftware.googlepay.exceptions

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.tunasoftware.tuna.exceptions.TunaException
import com.tunasoftware.tuna.exceptions.TunaExceptionCodes
import com.tunasoftware.tuna.exceptions.TunaExceptionCodes.*

class TunaGooglePayException(val apiException: ApiException) : TunaException(message = GOOGLE_PAY_API_EXCEPTION.message, code = GOOGLE_PAY_API_EXCEPTION.code)

class TunaGooglePayResultErrorException(val status: Status?) : TunaException(message = GOOGLE_PAY_RESULT_ERROR.message, code = GOOGLE_PAY_RESULT_ERROR.code)

class TunaGooglePayResultNullException : TunaException(message = GOOGLE_PAY_RESULT_NULL.message, code = GOOGLE_PAY_RESULT_NULL.code)