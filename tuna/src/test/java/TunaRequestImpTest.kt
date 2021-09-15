import com.tunasoftware.tuna.Tuna
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tuna.entities.TunaCardPaymentMethod
import com.tunasoftware.tuna.entities.TunaPaymentMethod
import com.tunasoftware.tuna.entities.TunaPaymentMethodType
import com.tunasoftware.tuna.exceptions.TunaCardCanNotBeRemovedException
import com.tunasoftware.tuna.exceptions.TunaExceptionCodes
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

class TunaRequestImpTest : TunaTest() {

    @Test
    fun `when add async card success, the tuna API must be called and a TunaCard must be returned`() {
        server.enqueueResponse("generate-card-success.json", 200)

        val future: CompletableFuture<TunaCard> = CompletableFuture()

        tuna.addNewCard(
                cardHolderName = "Tunilson da Silva",
                cardNumber = "4111111111111111",
                expirationMonth = 12,
                expirationYear = 2022,
                callback = object : Tuna.TunaRequestCallback<TunaCard> {
                    override fun onFailed(e: Throwable) {
                        future.completeExceptionally(e)
                    }
                    override fun onSuccess(result: TunaCard) {
                        future.complete(result)
                    }
                }
        )

        val tunaCard: TunaCard? = future.get()

        assertNotNull(tunaCard)
        assertEquals("411111xxxxxx1111", tunaCard?.maskedNumber)
        assertEquals("Tunilson da Silva", tunaCard?.cardHolderName)
        assertEquals(12, tunaCard?.expirationMonth)
        assertEquals(2022, tunaCard?.expirationYear)
        assertNotNull(tunaCard?.token)
        assertFalse(tunaCard?.token!!.isEmpty())
    }

    @Test
    fun `when add async card passing cvv success, the tuna API must be called and a TunaCard must be returned`() {
        server.enqueueResponse("generate-card-success.json", 200)

        val future: CompletableFuture<TunaCard> = CompletableFuture()

        tuna.addNewCard(
                cardHolderName = "Tunilson da Silva",
                cardNumber = "4111111111111111",
                expirationMonth = 12,
                expirationYear = 2022,
                cvv = "123",
                callback = object : Tuna.TunaRequestCallback<TunaCard> {
                    override fun onFailed(e: Throwable) {
                        future.completeExceptionally(e)
                    }
                    override fun onSuccess(result: TunaCard) {
                        future.complete(result)
                    }
                }
        )

        val tunaCard: TunaCard? = future.get()

        assertNotNull(tunaCard)
        assertEquals("411111xxxxxx1111", tunaCard?.maskedNumber)
        assertEquals("Tunilson da Silva", tunaCard?.cardHolderName)
        assertEquals(12, tunaCard?.expirationMonth)
        assertEquals(2022, tunaCard?.expirationYear)
        assertNotNull(tunaCard?.token)
        assertFalse(tunaCard?.token!!.isEmpty())
    }

    @Test
    fun `when bind a card successfully, the tuna API must be called and a TunaCard must be returned`() {
        server.enqueueResponse("bind-card-success.json", 200)

        val future: CompletableFuture<TunaCard> = CompletableFuture()

        tuna.bind(TunaCard(
                cardHolderName = "Tunilson da Silva",
                token = "card token",
                expirationMonth = 12,
                expirationYear = 2022,
                brand = "Visa",
                maskedNumber = "411111xxxxxx1111"),
                callback = object : Tuna.TunaRequestCallback<TunaCard> {
                    override fun onFailed(e: Throwable) {
                        future.complete(null)
                    }
                    override fun onSuccess(result: TunaCard) {
                        future.complete(result)
                    }
                },
                cvv = "111"
        )

        val tunaCard: TunaCard? = future.get()

        assertNotNull(tunaCard)
        assertEquals("411111xxxxxx1111", tunaCard?.maskedNumber)
        assertEquals("Tunilson da Silva", tunaCard?.cardHolderName)
        assertEquals(12, tunaCard?.expirationMonth)
        assertEquals(2022, tunaCard?.expirationYear)
        assertNotNull(tunaCard?.token)
        assertFalse(tunaCard?.token!!.isEmpty())
    }

    @Test
    fun `when obtaining a list of cards success, the tuna API should be called and a list of TunaCard should be returned`() {
        server.enqueueResponse("list-cards-success.json", 200)

        val future: CompletableFuture<List<TunaCard>> = CompletableFuture()

        tuna.getCardList(object : Tuna.TunaRequestCallback<List<TunaCard>>{
            override fun onSuccess(result: List<TunaCard>) {
                future.complete(result)
            }

            override fun onFailed(e: Throwable) {
                future.completeExceptionally(e)
            }
        })

        val result = future.get()
        assertNotNull(result)
        assertEquals(2, result.size)
        result.forEach{ assertNotNull(it.token) }
    }

    @Test
    fun `when add a async single card success, tuna API should be called and a TunaCard should return`(){
        server.enqueueResponse("generate-card-success.json", 200)

        val future: CompletableFuture<TunaCard> = CompletableFuture()

        tuna.addNewCard(
                cardHolderName = "Tunilson da Silva",
                cardNumber = "4111111111111111",
                expirationMonth = 12,
                expirationYear = 2022,
                save = false,
                callback = object : Tuna.TunaRequestCallback<TunaCard> {
                    override fun onFailed(e: Throwable) {
                        future.completeExceptionally(e)
                    }
                    override fun onSuccess(result: TunaCard) {
                        future.complete(result)
                    }
                }
        )

        val tunaCard: TunaCard? = future.get()

        assertNotNull(tunaCard)
        assertEquals("411111xxxxxx1111", tunaCard?.maskedNumber)
        assertEquals("Tunilson da Silva", tunaCard?.cardHolderName)
        assertEquals(12, tunaCard?.expirationMonth)
        assertEquals(2022, tunaCard?.expirationYear)
        assertNotNull(tunaCard?.token)
        assertFalse(tunaCard?.token!!.isEmpty())
    }

    @Test
    fun `when add a async single card pass cvv success, tuna API should be called and a TunaCard should return`(){
        server.enqueueResponse("generate-card-success.json", 200)

        val future: CompletableFuture<TunaCard> = CompletableFuture()

        tuna.addNewCard(
                cardHolderName = "Tunilson da Silva",
                cardNumber = "4111111111111111",
                expirationMonth = 12,
                expirationYear = 2022,
                save = false,
                cvv = "123",
                callback = object : Tuna.TunaRequestCallback<TunaCard> {
                    override fun onFailed(e: Throwable) {
                        future.completeExceptionally(e)
                    }
                    override fun onSuccess(result: TunaCard) {
                        future.complete(result)
                    }
                }
        )

        val tunaCard: TunaCard? = future.get()

        assertNotNull(tunaCard)
        assertEquals("411111xxxxxx1111", tunaCard?.maskedNumber)
        assertEquals("Tunilson da Silva", tunaCard?.cardHolderName)
        assertEquals(12, tunaCard?.expirationMonth)
        assertEquals(2022, tunaCard?.expirationYear)
        assertNotNull(tunaCard?.token)
        assertFalse(tunaCard?.token!!.isEmpty())
    }

    @Test
    fun `when delete a card success, tuna API should be called and no error should occur`(){
        server.enqueueResponse("delete-card-success.json", 200)

        val future: CompletableFuture<Boolean> = CompletableFuture()

        tuna.deleteCard(token = "token of card", callback = object : Tuna.TunaRequestCallback<Boolean>{
            override fun onSuccess(result: Boolean) {
                future.complete(result)
            }

            override fun onFailed(e: Throwable) {
                future.completeExceptionally(e)
            }

        })

        val deleted: Boolean = future.get()
        assertTrue(deleted)
    }

    @Test
    fun `generate an error when delete a card, the tuna API should be called and a TunaCardCanNotBeRemovedException should be thrown`(){
        server.enqueueResponse("exceptions/109.json", 200)

        val future: CompletableFuture<Boolean> = CompletableFuture()

        tuna.deleteCard(token = "token of card", callback = object : Tuna.TunaRequestCallback<Boolean>{
            override fun onSuccess(result: Boolean) {
                future.complete(result)
            }

            override fun onFailed(e: Throwable) {
                future.completeExceptionally(e)
            }

        })

        try {
            val result: Boolean = future.get()
            fail("Expected TunaCardCanNotBeRemovedException")
            
        } catch (e:ExecutionException) {
            val error = e.cause
            if (error is TunaCardCanNotBeRemovedException) {
                assertEquals(TunaExceptionCodes.CARD_CAN_NOT_BE_REMOVED.code, error.code)
                assertEquals(TunaCardCanNotBeRemovedException::class.java, error::class.java)
            } else {
                fail("Expected TunaCardCanNotBeRemovedException")
            }
        }
    }

    @Test
    fun `when get payment methods successfully`(){
        server.enqueueResponse("payment-methods-success.json", 200)

        val future: CompletableFuture<List<TunaPaymentMethod>> = CompletableFuture()

        tuna.getPaymentMethods(callback = object : Tuna.TunaRequestCallback<List<TunaPaymentMethod>>{
            override fun onSuccess(result: List<TunaPaymentMethod>) {
                future.complete(result)
            }

            override fun onFailed(e: Throwable) {
                future.completeExceptionally(e)
            }

        })

        val result: List<TunaPaymentMethod> = future.get()
        assertNotNull(result)
        assertEquals(2, result.size)
        val creditCard = result.firstOrNull { it.type == TunaPaymentMethodType.CREDIT_CARD }
        assertNotNull(creditCard)
        assert(creditCard is TunaCardPaymentMethod)
        if (creditCard is TunaCardPaymentMethod){
            assertEquals(3, creditCard.brands.size)
        }

    }
}