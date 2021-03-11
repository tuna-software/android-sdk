import com.tunasoftware.tuna.Tuna
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class TunaSessionTest {

    @Before
    fun setup(){
        Tuna.init("api token")
    }


    @Test
    fun `when a session is initiated the current session should return tha last started session`(){
        Tuna.startSession("asdfs")
        assertNotNull(Tuna.getCurrentSession())
    }

}