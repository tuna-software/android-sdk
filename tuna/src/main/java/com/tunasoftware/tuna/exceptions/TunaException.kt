package com.tunasoftware.tuna.exceptions

import com.tunasoftware.tuna.exceptions.TunaExceptionCodes.*
import java.net.ConnectException
import java.net.SocketTimeoutException

open class TunaException(message: String? = DEFAULT.message, val isRecoverable: Boolean = false, val code: Int = DEFAULT.code) : Exception(message) {

    companion object {
        fun getDefaultException() = TunaException(DEFAULT.message)
    }
}

class TunaSDKNotInitiatedException : TunaException(message = SDK_NOT_INITIATED.message, code = SDK_NOT_INITIATED.code)

class TunaSDKSandboxOnlyException : TunaException(message = SANDBOX_ONLY.message, code = SANDBOX_ONLY.code)

class TunaRequestNullException : TunaException(message = REQUEST_NULL.message, code = REQUEST_NULL.code)

class TunaSessionInvalidException : TunaException(message = SESSION_INVALID.message, isRecoverable = true, code = SESSION_INVALID.code)

class TunaSessionExpiredException : TunaException(message = SESSION_EXPIRED.message, isRecoverable = true, code = SESSION_EXPIRED.code)

class TunaCardDataMissedException : TunaException(message = CARD_DATA_MISSED.message, code = CARD_DATA_MISSED.code)

class TunaCardNumberAlreadyTokenizedException : TunaException(message = CARD_NUMBER_ALREADY_TOKENIZED.message, code = CARD_NUMBER_ALREADY_TOKENIZED.code)

class TunaInvalidExpirationDateException : TunaException(message = INVALID_EXPIRATION_DATE.message, code = INVALID_EXPIRATION_DATE.code)

class TunaInvalidCardNumberException : TunaException(message = INVALID_CARD_NUMBER.message, code = INVALID_CARD_NUMBER.code)

class TunaTokenNotFoundException : TunaException(message = TOKEN_NOT_FOUND.message, code = TOKEN_NOT_FOUND.code)

class TunaTokenCanNotBeRemovedException : TunaException(message = TOKEN_CAN_NOT_BE_REMOVED.message, code = TOKEN_CAN_NOT_BE_REMOVED.code)

class TunaCardCanNotBeRemovedException : TunaException(message = CARD_CAN_NOT_BE_REMOVED.message, code = CARD_CAN_NOT_BE_REMOVED.code)

class TunaPartnerGuidMissedException : TunaException(message = PARTNER_GUID_MISSED.message, code = PARTNER_GUID_MISSED.code)

class TunaPartnerDoesNotExistsException : TunaException(message = PARTNER_DOES_NOT_EXISTS.message, code = PARTNER_DOES_NOT_EXISTS.code)

class TunaInvalidPartnerTokenException : TunaException(message = INVALID_PARTNER_TOKEN.message, code = INVALID_PARTNER_TOKEN.code)

class TunaCustomerDataMissedException : TunaException(message = CUSTOMER_DATA_MISSED.message, code = CUSTOMER_DATA_MISSED.code)

class TunaRequestTokenMissedException : TunaException(message = REQUEST_TOKEN_MISSED.message, code = REQUEST_TOKEN_MISSED.code)

class TunaInvalidCardTokenException : TunaException(message = INVALID_CARD_TOKEN.message, code = INVALID_CARD_TOKEN.code)

class TunaInvalidCardHolderNameException : TunaException(message = INVALID_CARD_HOLDER_NAME.message, code = INVALID_CARD_HOLDER_NAME.code)

class TunaReachedMaxCardsByUserException : TunaException(message = REACHED_MAX_CARDS_BY_USER.message, code = REACHED_MAX_CARDS_BY_USER.code)

class TunaReachedMaxSessionsByUserException : TunaException(message = REACHED_MAX_SESSIONS_BY_USER.message, code = REACHED_MAX_SESSIONS_BY_USER.code)

class TunaConnectException : TunaException(message = CONNECTION.message, code = CONNECTION.code)

class TunaTimeoutException : TunaException(message = TIMEOUT.message, code = TIMEOUT.code)


fun Int.toTunaException(message:String?):TunaException {
    return when (this){
        REQUEST_NULL.code -> TunaRequestNullException()
        SESSION_INVALID.code -> TunaSessionInvalidException()
        SESSION_EXPIRED.code -> TunaSessionExpiredException()
        CARD_DATA_MISSED.code -> TunaCardDataMissedException()
        CARD_NUMBER_ALREADY_TOKENIZED.code -> TunaCardNumberAlreadyTokenizedException()
        INVALID_EXPIRATION_DATE.code -> TunaInvalidExpirationDateException()
        INVALID_CARD_NUMBER.code -> TunaInvalidCardNumberException()
        TOKEN_NOT_FOUND.code -> TunaTokenNotFoundException()
        TOKEN_CAN_NOT_BE_REMOVED.code -> TunaTokenCanNotBeRemovedException()
        CARD_CAN_NOT_BE_REMOVED.code -> TunaCardCanNotBeRemovedException()
        PARTNER_GUID_MISSED.code -> TunaPartnerGuidMissedException()
        PARTNER_DOES_NOT_EXISTS.code -> TunaPartnerDoesNotExistsException()
        INVALID_PARTNER_TOKEN.code -> TunaInvalidPartnerTokenException()
        CUSTOMER_DATA_MISSED.code -> TunaCustomerDataMissedException()
        REQUEST_TOKEN_MISSED.code -> TunaRequestTokenMissedException()
        INVALID_CARD_TOKEN.code -> TunaInvalidCardTokenException()
        INVALID_CARD_HOLDER_NAME.code -> TunaInvalidCardHolderNameException()
        REACHED_MAX_CARDS_BY_USER.code -> TunaReachedMaxCardsByUserException()
        REACHED_MAX_SESSIONS_BY_USER.code -> TunaReachedMaxSessionsByUserException()
        else -> TunaException(message?:DEFAULT.message)
    }
}

fun Throwable.toTunaException(): TunaException {
    if (this is ConnectException) return TunaConnectException()
    else if (this is SocketTimeoutException) return TunaTimeoutException()
    return TunaException.getDefaultException()
}
