package datastoreapi;

import java.util.List;
import api.WriteOutputResponse;

public interface OutputResponse extends WriteOutputResponse{

	List<String> writeOutput(OutputRequest outputRequest);
}
