import com.tunasoftware.tuna.TunaCore
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class TunaSessionTest {

    @Before
    fun setup(){
        TunaCore.init("api token")
    }


    @Test
    fun `when a session is initiated the current session should return tha last started session`(){
        TunaCore.startSession("asdfs")
        assertNotNull(TunaCore.getCurrentSession())
    }

}