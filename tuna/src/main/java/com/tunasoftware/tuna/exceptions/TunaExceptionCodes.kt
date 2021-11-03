package com.tunasoftware.tuna.exceptions

enum class TunaExceptionCodes(val code: Int, val message: String) {
    REQUEST_NULL(100, "Request object is null"),
    SESSION_INVALID(101, "Session object is invalid"),
    SESSION_EXPIRED(102, "Session has expired"),
    CARD_DATA_MISSED(103, "Card data missing"),
    CARD_NUMBER_ALREADY_TOKENIZED(104, "Card already tokenized"),
    INVALID_EXPIRATION_DATE(105, "Expiration date is invalid"),
    INVALID_CARD_NUMBER(106, "Card number is invalid"),
    TOKEN_NOT_FOUND(107, "Invalid Token"),
    TOKEN_CAN_NOT_BE_REMOVED(108, "Token can not be removed"),
    CARD_CAN_NOT_BE_REMOVED(109, "Card can not be removed"),
    PARTNER_GUID_MISSED(110, "Partner Guid is required"),
    PARTNER_DOES_NOT_EXISTS(111, "Partner Not Exists"),
    INVALID_PARTNER_TOKEN(112, "Partner Token is invalid"),
    CUSTOMER_DATA_MISSED(113, "Customer data missing"),
    REQUEST_TOKEN_MISSED(114, "Token data missing"),
    INVALID_CARD_TOKEN(115, "Card token is invalid"),
    INVALID_CARD_HOLDER_NAME(116, "Card holder name is invalid"),
    REACHED_MAX_CARDS_BY_USER(117, "User has reached maximum number of cards allowed"),
    REACHED_MAX_SESSIONS_BY_USER(118, "User has reached maximum number of sessions allowed"),
    SDK_NOT_INITIATED(-2, "SDK should be initiated before start a session"),
    CONNECTION(-3, "Network is unreachable"),
    TIMEOUT(-4, "Request timeout"),
    SANDBOX_ONLY(-5, "This method can only be called in sandbox mode"),
    GOOGLE_PAY_RESULT_ERROR(-6, "Google pay returned with error status"),
    GOOGLE_PAY_API_EXCEPTION(-7, "Google play Api returns an exception"),
    GOOGLE_PAY_RESULT_NULL(-8, "Google play payment data is null"),
    DEFAULT(0, "Tuna Unexpected Server Error");

}