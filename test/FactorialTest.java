import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import interfaces.NumStream;

public class FactorialTest {

	@Test
	public void testDoFactorial() {
	    NumStream numStream = new NumStream() {
	        @Override
	        public List<Integer> getIntegers() {
	            return Arrays.asList(4, 5, 6);   //adds 4 5 6 into the list
	        }
			@Override
			public void setIntegerList(List<Integer> integerList) {
				return;
			}
	    };

	    // 4! = 24   5! = 120   6! = 720
	    List<Integer> expectedResults = Arrays.asList(6, 3, 9);
	    
	    ComputationImplementation computation = new ComputationImplementation();
	    EngineResponse response = computation.doFactorialSum(numStream);
	    assertEquals(ResponseCode.SUCCESSFUL, response.getResponseCode());
	    
	    RequestResult requestResult = response.getRequestResult();
	    NumStream resultNumStream = requestResult.getResultNumStream();
	    
	    List<Integer> actualResults = resultNumStream.getIntegers();
	    assertEquals(expectedResults, actualResults);
	}
}
