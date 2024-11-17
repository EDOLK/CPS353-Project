package mainpack;

import java.util.ArrayList;
import java.util.List;

import interfaces.NumStream;

public class ComputationImplementation implements ComputeEngineComputation {
	@Override
	public EngineResponse doFactorialSum(NumStream numStream) {
		if(numStream == null) {
			throw new IllegalArgumentException("NumStream cannot be null");
		}
		ArrayList<Integer> resultList = new ArrayList<>();

		factorialSumLoop(numStream.getIntegers(), resultList);

		return new EngineResponseImplementation(ResponseCode.SUCCESSFUL, resultList);
	}

	public void factorialSumLoop(List<Integer> inputList, ArrayList<Integer> resultList) {
		if(inputList == null || inputList.isEmpty()) {
			throw new IllegalArgumentException("Input list cannot be null or empty");
		}
		if(resultList == null) {
			throw new IllegalArgumentException("Output list cannot be null");
		}
		//finds the factorial of the input
		for (Integer inputNum : inputList) {
			if(inputNum < 0) {
				throw new IllegalArgumentException("Cannot compute the factorial of " + inputNum);
			}
			int result = 1;
			for (int interval = inputNum; interval > 0; interval--) {
				result = result * interval;
			}
			//find the sum of the digits of the factorial
			int sumDigits =  0;
			while(result != 0) {
				sumDigits += result %10;
				result = result / 10;
			}
			//add to result list
			resultList.add(sumDigits);
		}
	}
}
