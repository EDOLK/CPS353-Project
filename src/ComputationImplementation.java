import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import interfaces.BigIntegerNumStream;
import interfaces.NumStream;

public class ComputationImplementation implements ComputeEngineComputation {
	@Override
	public EngineResponse doFactorial(NumStream numStream) {
		if(numStream == null) {
			throw new IllegalArgumentException("NumStream cannot be null");
		}
        if (numStream instanceof BigIntegerNumStream biNumStream){
            ArrayList<BigInteger> resultList = new ArrayList<>();
            factorialSumLoopForBigIntegers(biNumStream.getBigIntegers(), resultList);
            NumStream resultStream = new BigIntegerNumStream() {

                @Override
                public List<Integer> getIntegers() {
                    throw new UnsupportedOperationException("Unimplemented method 'getIntegers'");
                }

                @Override
                public void setIntegerList(List<Integer> integerList) {
                    throw new UnsupportedOperationException("Unimplemented method 'setIntegerList'");
                }

                @Override
                public List<BigInteger> getBigIntegers() {
                    return resultList;
                }

                @Override
                public void setBigIntegerList(List<BigInteger> bigIntegerList) {
                    throw new UnsupportedOperationException("Unimplemented method 'setBigIntegerList'");
                }

            };
            RequestResultImplementation requestResult = new RequestResultImplementation();
            requestResult.setResultNumStream(resultStream);
            return new EngineResponseImplementation(ResponseCode.SUCCESSFUL, requestResult);
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

    public void factorialSumLoopForBigIntegers(List<BigInteger> inputList, ArrayList<BigInteger> resultList) {
		if(inputList == null || inputList.isEmpty()) {
			throw new IllegalArgumentException("Input list cannot be null or empty");
		}
		if(resultList == null) {
			throw new IllegalArgumentException("Output list cannot be null");
		}

		//finds the factorial of the input
		for (BigInteger inputNum : inputList) {
			if(inputNum.compareTo(new BigInteger("0")) < 0) {
				throw new IllegalArgumentException("Cannot compute the factorial of " + inputNum);
			}
			BigInteger result = new BigInteger("1");
			for (BigInteger interval = inputNum; interval.compareTo(new BigInteger("0")) > 0; interval = interval.subtract(new BigInteger("1"))) {
                result = result.multiply(interval);
			}
			//find the sum of the digits of the factorial
			BigInteger sumDigits = new BigInteger("0");
            while (result.compareTo(new BigInteger("0")) != 0) {
                sumDigits = sumDigits.add(result.divide(new BigInteger("10")));
                result = result.divide(new BigInteger("10"));
            }
			resultList.add(sumDigits);
		}
	}
}
