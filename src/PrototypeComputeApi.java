import interfaces.NumStream;

public class PrototypeComputeApi {

  public void prototypeComputeRequest(ComputeRequestHandler computeRequestHandler, ComputeEngineComputation computeEngine) {
    // Get the ints to calculate factorials for, from user request
    NumStream computeEngineInput = computeRequestHandler.getUserRequest().getRequestStream();

    // Send the ints over to the engine for computation
    EngineResponse computeResult = computeEngine.doFactorialSum(computeEngineInput);

    // Generate a response string and pass it along
    computeRequestHandler.generateAndSendResponseMessage(computeResult.getRequestResult().getResultNumStream());
  }
}
