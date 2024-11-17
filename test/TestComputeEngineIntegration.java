import datastoreapi.DataStoreAPI;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class TestComputeEngineIntegration {

  @Test
  public void testComputeEngineIntegration() {
    ArrayList<Integer> requestList = new ArrayList<>(Arrays.asList(1,10,12));

    // Create a computeRequestHandler with userRequest, and set it's datastoreapi
    DataStoreAPI dataStoreApi = new DataStoreAPI();
    UserRequest userRequest = new UserRequest(new UserRequestSource(), new UserRequestDestination(), new NumStreamImplementation(requestList));

    ComputeRequestHandler computeRequestHandler = new ComputeRequestHandlerImplementation(userRequest);
    computeRequestHandler.setDataApi(dataStoreApi);

    ComputeEngineComputation computationImplementation = new ComputationImplementation();

    ComputeEngine computeEngineImplementation = new ComputeEngineImplementation(computationImplementation, computeRequestHandler);

    userRequest.setRequestStream(computeRequestHandler.getUserRequest().getRequestStream());

    EngineResponse engineResponse = computeEngineImplementation.submitRequest(userRequest);

    /*
     The factorial of 25 here is incorrect due max size of an int.
     If numbers >12 will be submitted to the api then all instances of
     <Integer> lists should be changed to <Float> lists
    */
    Assert.assertEquals("1;1:10;27:12;27", engineResponse.getRequestResult().getResultString());
  }
}
