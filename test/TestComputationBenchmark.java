import datastoreapi.DataStoreAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class TestComputationBenchmark {

  @Test
  public void testComputationBenchmark() {
    // Create list of nums to calculate for
    ArrayList<BigInteger> numList = new ArrayList<>();

    for (int i1 = 0; i1 < 1000000; i1++) {
      numList.add(BigInteger.valueOf((long) (Math.random() * 100)));
    }

    DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    DataStoreAPI dataStoreApi = new DataStoreAPI();
    UserRequest userRequest = new UserRequest(new UserRequestSource(), new UserRequestDestination(), new BigIntegerNumStreamImplementation(numList));

    ComputeRequestHandler computeRequestHandler = new ComputeRequestHandlerImplementation(userRequest);
    computeRequestHandler.setDataApi(dataStoreApi);

    // Computation implementations
    ComputationImplementation computationImplementationOld = new ComputationImplementation();
    ConcurrentComputationImplementation computationImplementationNew = new ConcurrentComputationImplementation();

    ComputeEngine computeEngineImplementationOld = new ComputeEngineImplementation(computationImplementationOld, computeRequestHandler);
    ComputeEngine computeEngineImplementationNew = new ComputeEngineImplementation(computationImplementationNew, computeRequestHandler);

    userRequest.setRequestStream(computeRequestHandler.getUserRequest().getRequestStream());

    // Run using old computation system
    long beforeTime = System.currentTimeMillis();
    computeEngineImplementationOld.submitRequest(userRequest);
    long afterTime = System.currentTimeMillis();

    long elapsedOld = afterTime - beforeTime;
    System.out.println("Elapsed time old: " + elapsedOld + " milliseconds");

    // Run using new computation system
    beforeTime = System.currentTimeMillis();
    computeEngineImplementationNew.submitRequest(userRequest);

    afterTime = System.currentTimeMillis();

    long elapsedNew = afterTime - beforeTime;
    System.out.println("Elapsed time new: " + elapsedNew + " milliseconds");

    double percentDifferance;
    String elapsedDifference;

    if (elapsedNew > elapsedOld) {
      percentDifferance = (double)(elapsedNew - elapsedOld)/elapsedNew;
      elapsedDifference = "slower";
    } else {
      percentDifferance = (double)(elapsedOld - elapsedNew)/elapsedOld;
      elapsedDifference = "faster";
    }

    percentDifferance *= 100;
    System.out.println("New computation is " + decimalFormat.format(percentDifferance) + "% " + elapsedDifference + " than old computation");

    Assertions.assertTrue((elapsedNew < elapsedOld) && percentDifferance >= 10);
  }
}
