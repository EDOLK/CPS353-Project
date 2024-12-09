package datastoreapi;

import java.util.List;

public interface OutputResponse extends WriteOutputResponse{

	List<String> writeOutput(OutputRequest outputRequest);
}
