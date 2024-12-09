package datastoreapi;

import api.ReadInputResponse;
import java.util.List;

public interface InputResponse extends ReadInputResponse{

	List<Integer> readInput(InputRequest inputRequest);

}
