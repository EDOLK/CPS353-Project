import datastoreapi.DataStoreAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class TestComputationBenchmark {

  @Test
  public void testComputationBenchmark() {
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    ArrayList<Integer> requestList = new ArrayList<>(Arrays.asList(1,10,25));

    DataStoreAPI dataStoreApi = new DataStoreAPI();
    UserRequest userRequest = new UserRequest(new UserRequestSource(), new UserRequestDestination(), new NumStreamImplementation(requestList));

    ComputeRequestHandler computeRequestHandler = new ComputeRequestHandlerImplementation(userRequest);
    computeRequestHandler.setDataApi(dataStoreApi);

    ConcurrentComputationImplementation computationImplementationOld = new ConcurrentComputationImplementation();
    ComputationImplementation computationImplementationNew = new ComputationImplementation();

    ComputeEngine computeEngineImplementationOld = new ComputeEngineImplementation(computationImplementationOld, computeRequestHandler);
    ComputeEngine computeEngineImplementationNew = new ComputeEngineImplementation(computationImplementationNew, computeRequestHandler);

    userRequest.setRequestStream(computeRequestHandler.getUserRequest().getRequestStream());

    // Run using old computation system
    long beforeTime = System.nanoTime();
    computeEngineImplementationOld.submitRequest(userRequest);
    long afterTime = System.nanoTime();

    long elapsedOld = afterTime - beforeTime;
    System.out.println("Elapsed time old: " + elapsedOld + " nano seconds");

    // Run using new computation system
    beforeTime = System.nanoTime();
    computeEngineImplementationNew.submitRequest(userRequest);
    afterTime = System.nanoTime();

    long elapsedNew = afterTime - beforeTime;
    System.out.println("Elapsed time new: " + elapsedNew + " nano seconds");

    double percentDifferance;
    String elapsedDifference;

    if (elapsedNew > elapsedOld) {
      percentDifferance = (double)(elapsedNew - elapsedOld)/elapsedNew;
      elapsedDifference = "slower";
    } else {
      percentDifferance = (double)(elapsedOld - elapsedNew)/elapsedOld;
      elapsedDifference = "faster";
    }

    System.out.println("New computation is " + decimalFormat.format(percentDifferance) + "% " + elapsedDifference + " than old computation");

    Assertions.assertTrue((elapsedNew < elapsedOld) && percentDifferance >= 0.1);
  }
}
