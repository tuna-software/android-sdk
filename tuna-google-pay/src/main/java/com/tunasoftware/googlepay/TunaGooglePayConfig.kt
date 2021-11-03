package com.tunasoftware.googlepay

class TunaGooglePayConfig(
    val allowedAuthMethods: List<String>,
    val allowedCardNetworks: List<String>,
    val billingAddressRequired: Boolean = false,
    val billingAddressFormat: TunaGooglePayBillingAddressFormat = TunaGooglePayBillingAddressFormat.MIN,
    val shippingAddressRequired: Boolean = false,
    val phoneNumberRequired: Boolean = false,
    val gatewayMerchantId: String,
    val merchantName: String,
    val allowedCountryCodes: List<String>
)

enum class TunaGooglePayBillingAddressFormat {
    MIN,
    FULL
}