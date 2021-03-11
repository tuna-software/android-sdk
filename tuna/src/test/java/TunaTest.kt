import com.tunasoftware.tuna.Tuna
import com.tunasoftware.tuna.request.TunaImp
import com.tunasoftware.tuna.request.rest.TunaAPI
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class TunaTest {

    lateinit var tuna: Tuna
    val server = MockWebServer()

    private val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
                .baseUrl(server.url("/"))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        tuna = TunaImp("test session", retrofit.create(TunaAPI::class.java))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}