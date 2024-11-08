import java.util.ArrayList;
import java.util.List;

import interfaces.NumStream;

public class ComputationImplementation implements ComputeEngineComputation {
  @Override
  public EngineResponse doFactorial(NumStream numStream) {
    if(numStream == null) {
	throw new IllegalArgumentException("NumStream cannot be null");
    }
    ArrayList<Integer> resultList = new ArrayList<>();

    factorialLoop(numStream.getIntegers(), resultList);

    return new EngineResponseImplementation(ResponseCode.SUCCESSFUL, resultList);
  }

  public void factorialLoop(List<Integer> inputList, ArrayList<Integer> resultList) {
  	if(inputList == null || inputList.isEmpty()) {
		throw new IllegalArgumentException("Input list cannot be null or empty");
	}
	if(resultList == null) {
		throw new IllegalArgumentException("Output list cannot be null");
	}
    
    for (Integer inputNum : inputList) {
	if(inputNum < 0) {
		throw new IllegalArgumentException("Cannot compute the factorial of " + inputNum);
	}
      
      int result = 1;

      for (int interval = inputNum; interval > 0; interval--) {
        result = result * interval;
      }

      resultList.add(result);
    }
  }
}
