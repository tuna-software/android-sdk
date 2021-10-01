import com.nhaarman.mockitokotlin2.mock
import com.tunasoftware.java.Tuna
import com.tunasoftware.tuna.TunaCore
import com.tunasoftware.tuna.entities.TunaCard
import com.tunasoftware.tuna.entities.TunaPaymentMethod
import com.tunasoftware.tunakt.*
import junit.framework.Assert.assertTrue
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

class TunaKtExtensionTest {

    private val tunaSuccess = object : Tuna {
        override fun addNewCard(
            cardNumber: String,
            cardHolderName: String,
            expirationMonth: Int,
            expirationYear: Int,
            callback: TunaCore.TunaRequestCallback<TunaCard>
        ) {
            callback.onSuccess(mock())
        }

        override fun addNewCard(
            cardNumber: String,
            cardHolderName: String,
            expirationMonth: Int,
            expirationYear: Int,
            save: Boolean,
            callback: TunaCore.TunaRequestCallback<TunaCard>
        ) {
            callback.onSuccess(mock())
        }

        override fun addNewCard(
            cardNumber: String,
            cardHolderName: String,
            expirationMonth: Int,
            expirationYear: Int,
            cvv: String,
            callback: TunaCore.TunaRequestCallback<TunaCard>
        ) {
            callback.onSuccess(mock())
        }

        override fun addNewCard(
            cardNumber: String,
            cardHolderName: String,
            expirationMonth: Int,
            expirationYear: Int,
            cvv: String,
            save: Boolean,
            callback: TunaCore.TunaRequestCallback<TunaCard>
        ) {
            callback.onSuccess(mock())
        }

        override fun getCardList(callback: TunaCore.TunaRequestCallback<List<TunaCard>>) {
            callback.onSuccess(mock())
        }

        override fun deleteCard(token: String, callback: TunaCore.TunaRequestCallback<Boolean>) {
            callback.onSuccess(true)
        }

        override fun deleteCard(card: TunaCard, callback: TunaCore.TunaRequestCallback<Boolean>) {
            callback.onSuccess(true)
        }

        override fun bind(
            card: TunaCard,
            cvv: String,
            callback: TunaCore.TunaRequestCallback<TunaCard>
        ) {
            callback.onSuccess(mock())
        }

        override fun getPaymentMethods(callback: TunaCore.TunaRequestCallback<List<TunaPaymentMethod>>) {
            TODO("Not yet implemented")
        }

    }

    private val tunaFailure = object : Tuna {
        override fun addNewCard(
            cardNumber: String,
            cardHolderName: String,
            expirationMonth: Int,
            expirationYear: Int,
            callback: TunaCore.TunaRequestCallback<TunaCard>
        ) {
            callback.onFailed(mock())
        }

        override fun addNewCard(
            cardNumber: String,
            cardHolderName: String,
            expirationMonth: Int,
            expirationYear: Int,
            save: Boolean,
            callback: TunaCore.TunaRequestCallback<TunaCard>
        ) {
            callback.onFailed(mock())
        }

        override fun addNewCard(
            cardNumber: String,
            cardHolderName: String,
            expirationMonth: Int,
            expirationYear: Int,
            cvv: String,
            callback: TunaCore.TunaRequestCallback<TunaCard>
        ) {
            callback.onFailed(mock())
        }

        override fun addNewCard(
            cardNumber: String,
            cardHolderName: String,
            expirationMonth: Int,
            expirationYear: Int,
            cvv: String,
            save: Boolean,
            callback: TunaCore.TunaRequestCallback<TunaCard>
        ) {
            callback.onFailed(mock())
        }

        override fun getCardList(callback: TunaCore.TunaRequestCallback<List<TunaCard>>) {
            callback.onFailed(mock())
        }

        override fun deleteCard(token: String, callback: TunaCore.TunaRequestCallback<Boolean>) {
            callback.onFailed(mock())
        }

        override fun deleteCard(card: TunaCard, callback: TunaCore.TunaRequestCallback<Boolean>) {
            callback.onFailed(mock())
        }

        override fun bind(
            card: TunaCard,
            cvv: String,
            callback: TunaCore.TunaRequestCallback<TunaCard>
        ) {
            callback.onFailed(mock())
        }

        override fun getPaymentMethods(callback: TunaCore.TunaRequestCallback<List<TunaPaymentMethod>>) {
            TODO("Not yet implemented")
        }

    }


    @Test
    fun `when a success or failure block is defined twice an exception should be thrown`() {
        var errorThrown = false
        try {
            TunaRequestResult<Any>().onSuccess { }.onSuccess { }
        } catch (e: RuntimeException) {
            errorThrown = true
        }
        assertTrue("An error was thrown", errorThrown)

        errorThrown = false
        try {
            TunaRequestResult<Any>().onFailure { }.onFailure { }
        } catch (e: RuntimeException) {
            errorThrown = true
        }
        assertTrue("An error was thrown", errorThrown)
    }

    @Test
    fun `when add new card a success with a tuna card should be called`() {
        val future: CompletableFuture<TunaCard> = CompletableFuture()
        tunaSuccess
            .addNewCard("", "", 1, 1, true)
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        val result = future.get()
        Assert.assertTrue("result is Tuna Card", result is TunaCard)
    }

    @Test
    fun `when add new card a failure with an Exception should be called`() {
        val future: CompletableFuture<TunaCard> = CompletableFuture()
        tunaFailure
            .addNewCard("", "", 1, 1, true)
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        try {
            future.get()
            Assert.fail("Expected Exception")
        } catch (e: ExecutionException) {
        }
    }

    @Test
    fun `when add new card with cvv a success with a tuna card should be called`() {
        val future: CompletableFuture<TunaCard> = CompletableFuture()
        tunaSuccess
            .addNewCard("", "", 1, 1, "1", true)
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        val result = future.get()
        Assert.assertTrue("result is Tuna Card", result is TunaCard)
    }

    @Test
    fun `when add new card with cvv a failure with an Exception should be called`() {
        val future: CompletableFuture<TunaCard> = CompletableFuture()
        tunaFailure
            .addNewCard("", "", 1, 1, "1", true)
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        try {
            future.get()
            Assert.fail("Expected Exception")
        } catch (e: ExecutionException) {
        }
    }

    @Test
    fun `when get new single use card a success with a tuna card should be called`() {
        val future: CompletableFuture<TunaCard> = CompletableFuture()
        tunaSuccess
            .addNewCard("", "", 1, 1, false)
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        val result = future.get()
        Assert.assertTrue("result is Tuna Card", result is TunaCard)
    }

    @Test
    fun `when get new single use card a failure with an Exception should be called`() {
        val future: CompletableFuture<TunaCard> = CompletableFuture()
        tunaFailure
            .addNewCard("", "", 1, 1, false)
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        try {
            future.get()
            Assert.fail("Expected Exception")
        } catch (e: ExecutionException) {
        }
    }

    @Test
    fun `when get new single use card with cvv a success with a tuna card should be called`() {
        val future: CompletableFuture<TunaCard> = CompletableFuture()
        tunaSuccess
            .addNewCard("", "", 1, 1, "1", false)
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        val result = future.get()
        Assert.assertTrue("result is Tuna Card", result is TunaCard)
    }

    @Test
    fun `when get new single use card with cvv a failure with an Exception should be called`() {
        val future: CompletableFuture<TunaCard> = CompletableFuture()
        tunaFailure
            .addNewCard("", "", 1, 1, "1", false)
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        try {
            future.get()
            Assert.fail("Expected Exception")
        } catch (e: ExecutionException) {
        }
    }

    @Test
    fun `when bind onSuccess should be called`() {
        val future: CompletableFuture<TunaCard> = CompletableFuture()
        tunaSuccess
            .bind(mock(), "111")
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        val result = future.get()
        Assert.assertTrue("result is Tuna Card", result is TunaCard)
    }

    @Test
    fun `when bind onFailure should be called`() {
        val future: CompletableFuture<TunaCard> = CompletableFuture()
        tunaFailure
            .bind(mock(), "111")
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        try {
            future.get()
            Assert.fail("Expected Exception")
        } catch (e: ExecutionException) {
        }
    }

    @Test
    fun `when delete onSuccess should be called`() {
        val future: CompletableFuture<Boolean> = CompletableFuture()
        tunaSuccess
            .deleteCard("")
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        val result = future.get()
        Assert.assertTrue("card deleted", result)
    }

    @Test
    fun `when delete onFailure should be called`() {
        val future: CompletableFuture<Boolean> = CompletableFuture()
        tunaFailure
            .deleteCard("")
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        try {
            future.get()
            Assert.fail("Expected Exception")
        } catch (e: ExecutionException) {
        }
    }

    @Test
    fun `when delete with card onSuccess should be called`() {
        val future: CompletableFuture<Boolean> = CompletableFuture()
        val card = TunaCard("", "", "", 1, 1,"")
        tunaSuccess
            .deleteCard(card)
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        val result = future.get()
        Assert.assertTrue("card deleted", result)
    }

    @Test
    fun `when delete with card onFailure should be called`() {
        val future: CompletableFuture<Boolean> = CompletableFuture()
        val card = TunaCard("", "", "", 1, 1,"")
        tunaFailure
            .deleteCard(card)
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        try {
            future.get()
            Assert.fail("Expected Exception")
        } catch (e: ExecutionException) {
        }
    }

    @Test
    fun `when get a card list onSuccess should be called`() {
        val future: CompletableFuture<List<TunaCard>> = CompletableFuture()
        tunaSuccess
            .getCardList()
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        val result = future.get()
        Assert.assertNotNull("card list returned", result)
    }

    @Test
    fun `when get a card list onFailure should be called`() {
        val future: CompletableFuture<List<TunaCard>> = CompletableFuture()
        tunaFailure
            .getCardList()
            .onSuccess {
                future.complete(it)
            }.onFailure {
                future.completeExceptionally(it)
            }
        try {
            future.get()
            Assert.fail("Expected Exception")
        } catch (e: ExecutionException) {
        }
    }

}