import com.tunasoftware.tuna.request.TunaCoreImp
import com.tunasoftware.tuna.request.rest.TunaAPI
import com.tunasoftware.tuna.request.rest.TunaEngineAPI

class TunaTestImpl(sessionId: String, tunaAPI: TunaAPI, tunaEngineAPI: TunaEngineAPI) :
    TunaCoreImp(sessionId, tunaAPI, tunaEngineAPI)