import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import interfaces.NumStream;

import java.util.ArrayList;

import java.util.List;

import org.junit.jupiter.api.Test;

public class MediatorClassTest {

	@Test
	public void testToStringList() {

       		NumStreamImplementation numStream = new NumStreamImplementation();

     		List<Integer> testList = List.of(0, 1, 2, 3);
      		numStream.setIntegerList(testList);  

        	List<String> output = toStringList(numStream);

        	List<String> expectedOutput = List.of("0", "1" , "2", "3");

        	assertEquals(expectedOutput, output);
    }

	private List<String> toStringList(NumStream stream){
		List<Integer> integers = stream.getIntegers();
		List<String> strings = new ArrayList<>();
		for (Integer integer : integers) {
			strings.add(String.valueOf(integer));
		}
		return strings;

	}

	@Test
	public void testParameterValidation() {
		IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,  () -> Mediator.setMediator(null));

		assertEquals("Mediator cannot be null", illegalArgumentException.getMessage());
	}

	@Test
	public void testMediatorErrorIntegration() {
		ComputeEngineComputation computeEngineComputation = new ComputationImplementation();
		ComputeEngine computeEngine = new ComputeEngineImplementation(computeEngineComputation);

		EngineResponse engineResponse = computeEngine.submitRequest(null);

		assertTrue(engineResponse.getResponseCode().isFailure());
		assertTrue(engineResponse instanceof EngineResponseException);
	}
}
