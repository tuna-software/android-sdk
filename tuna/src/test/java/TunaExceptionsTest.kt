import com.tunasoftware.tuna.exceptions.*
import com.tunasoftware.tuna.TunaCore
import com.tunasoftware.tuna.entities.TunaCard
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.junit.runners.Parameterized.Parameters
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

@RunWith(Parameterized::class)
class TunaExceptionsTest: TunaTest() {

    @JvmField
    @Parameter
    var code: Int? = null

    @JvmField
    @Parameter(1)
    var message: String? = null

    @JvmField
    @Parameter(2)
    var exception: Class<TunaException>? = null


    @Test
    fun `when trying to add a async card and an error is encountered, the tuna API should be called and a TunaException should be generated`() {
        server.enqueueResponse("/exceptions/${code}.json", 200)

        val future: CompletableFuture<TunaCard> = CompletableFuture()

        tuna.addNewCard(
                cardHolderName = "Tunilson da Silva",
                cardNumber = "4111111111111111",
                expirationMonth = 12,
                expirationYear = 2022,
                callback = object : TunaCore.TunaRequestCallback<TunaCard> {
                    override fun onFailed(e: Throwable) {
                        future.completeExceptionally(e)
                    }
                    override fun onSuccess(result: TunaCard) {
                        future.complete(result)
                    }
                }
        )

        try {
            val card: TunaCard = future.get()
            fail("Expected TunaException")
        } catch (e: ExecutionException) {
            val error = e.cause
            if (error is TunaException) {
                assertEquals(code, error.code)
                assertEquals(exception, error::class.java)
            } else {
                fail("Expected TunaException")
            }
        }
    }


    @Test
    fun `when trying to add a async single card and an error is encountered, the tuna API should be called and a TunaException should be generated`() {
        server.enqueueResponse("/exceptions/${code}.json", 200)

        val future: CompletableFuture<TunaCard> = CompletableFuture()

        tuna.addNewCard(
                cardHolderName = "Tunilson da Silva",
                cardNumber = "4111111111111111",
                expirationMonth = 12,
                expirationYear = 2022,
                save = false,
                callback = object : TunaCore.TunaRequestCallback<TunaCard> {
                    override fun onFailed(e: Throwable) {
                        future.completeExceptionally(e)
                    }
                    override fun onSuccess(result: TunaCard) {
                        future.complete(result)
                    }
                }
        )

        try {
            val card: TunaCard = future.get()
            fail("Expected TunaException")
        } catch (e: ExecutionException) {
            val error = e.cause
            if (error is TunaException) {
                assertEquals(code, error.code)
                assertEquals(exception, error::class.java)
            } else {
                fail("Expected TunaException")
            }
        }
    }

    companion object {
        @JvmStatic
        @get:Parameters(name = "{0} - {1}")
        val parameters: Collection<Array<Any>>
            get() = listOf(
                    arrayOf(TunaExceptionCodes.REQUEST_NULL.code, TunaExceptionCodes.REQUEST_NULL.message, TunaRequestNullException::class.java),
                    arrayOf(TunaExceptionCodes.SESSION_INVALID.code, TunaExceptionCodes.SESSION_INVALID.message, TunaSessionInvalidException::class.java),
                    arrayOf(TunaExceptionCodes.SESSION_EXPIRED.code, TunaExceptionCodes.SESSION_EXPIRED.message, TunaSessionExpiredException::class.java),
                    arrayOf(TunaExceptionCodes.CARD_DATA_MISSED.code, TunaExceptionCodes.CARD_DATA_MISSED.message, TunaCardDataMissedException::class.java),
                    arrayOf(TunaExceptionCodes.CARD_NUMBER_ALREADY_TOKENIZED.code, TunaExceptionCodes.CARD_NUMBER_ALREADY_TOKENIZED.message, TunaCardNumberAlreadyTokenizedException::class.java),
                    arrayOf(TunaExceptionCodes.INVALID_EXPIRATION_DATE.code, TunaExceptionCodes.INVALID_EXPIRATION_DATE.message, TunaInvalidExpirationDateException::class.java),
                    arrayOf(TunaExceptionCodes.INVALID_CARD_NUMBER.code, TunaExceptionCodes.INVALID_CARD_NUMBER.message, TunaInvalidCardNumberException::class.java),
                    arrayOf(TunaExceptionCodes.TOKEN_NOT_FOUND.code, TunaExceptionCodes.TOKEN_NOT_FOUND.message, TunaTokenNotFoundException::class.java),
                    arrayOf(TunaExceptionCodes.TOKEN_CAN_NOT_BE_REMOVED.code, TunaExceptionCodes.TOKEN_CAN_NOT_BE_REMOVED.message, TunaTokenCanNotBeRemovedException::class.java),
                    arrayOf(TunaExceptionCodes.CARD_CAN_NOT_BE_REMOVED.code, TunaExceptionCodes.CARD_CAN_NOT_BE_REMOVED.message, TunaCardCanNotBeRemovedException::class.java),
                    arrayOf(TunaExceptionCodes.PARTNER_GUID_MISSED.code, TunaExceptionCodes.PARTNER_GUID_MISSED.message, TunaPartnerGuidMissedException::class.java),
                    arrayOf(TunaExceptionCodes.PARTNER_DOES_NOT_EXISTS.code, TunaExceptionCodes.PARTNER_DOES_NOT_EXISTS.message, TunaPartnerDoesNotExistsException::class.java),
                    arrayOf(TunaExceptionCodes.INVALID_PARTNER_TOKEN.code, TunaExceptionCodes.INVALID_PARTNER_TOKEN.message, TunaInvalidPartnerTokenException::class.java),
                    arrayOf(TunaExceptionCodes.CUSTOMER_DATA_MISSED.code, TunaExceptionCodes.CUSTOMER_DATA_MISSED.message, TunaCustomerDataMissedException::class.java),
                    arrayOf(TunaExceptionCodes.REQUEST_TOKEN_MISSED.code, TunaExceptionCodes.REQUEST_TOKEN_MISSED.message, TunaRequestTokenMissedException::class.java),
                    arrayOf(TunaExceptionCodes.INVALID_CARD_TOKEN.code, TunaExceptionCodes.INVALID_CARD_TOKEN.message, TunaInvalidCardTokenException::class.java),
                    arrayOf(TunaExceptionCodes.INVALID_CARD_HOLDER_NAME.code, TunaExceptionCodes.INVALID_CARD_HOLDER_NAME.message, TunaInvalidCardHolderNameException::class.java),
                    arrayOf(TunaExceptionCodes.REACHED_MAX_CARDS_BY_USER.code, TunaExceptionCodes.REACHED_MAX_CARDS_BY_USER.message, TunaReachedMaxCardsByUserException::class.java),
                    arrayOf(TunaExceptionCodes.REACHED_MAX_SESSIONS_BY_USER.code, TunaExceptionCodes.REACHED_MAX_SESSIONS_BY_USER.message, TunaReachedMaxSessionsByUserException::class.java),
                    arrayOf(TunaExceptionCodes.DEFAULT.code, TunaExceptionCodes.DEFAULT.message, TunaException::class.java)
            )
    }
}