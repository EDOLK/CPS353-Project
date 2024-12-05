import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import computeengine.ComputeEngineServiceGrpc.ComputeEngineServiceImplBase;
import computeengine.Computeengngine.computeRequest;
import computeengine.Computeengngine.computeResponse;
import datastore.DataStoreSystemGrpc;
import datastore.DataStoreSystemGrpc.DataStoreSystemBlockingStub;
import datastore.Datastore.InputRequest;
import datastore.Datastore.InputResponse;
import datastore.Datastore.OutputRequest;
import datastore.Datastore.OutputResponse;
import interfaces.NumStream;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * GRPCMediator
 */

public class GRPCMediator extends ComputeEngineServiceImplBase{

    private ComputeEngine engine = new ComputeEngineImplementation(new ComputationImplementation());

    @Override
    public void compute(computeRequest request, StreamObserver<computeResponse> responseObserver) {
        computeResponse.Builder computeResponseBuilder = computeResponse.newBuilder();
        List<Integer> inputList = request.getInputList();
        NumStream requestStream;

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 27027)
            .usePlaintext()
            .build();
        DataStoreSystemBlockingStub dataStoreServerStub = DataStoreSystemGrpc.newBlockingStub(channel);

        if (inputList.isEmpty()){
            if (request.getInputPath() != null && !request.getInputPath().equals("")){
                InputRequest.Builder inputRequestBuilder = InputRequest.newBuilder();
                inputRequestBuilder.setFileName(request.getInputPath());
                InputResponse inputResponse = dataStoreServerStub.readInput(inputRequestBuilder.build());
                inputList = inputResponse.getInputListList();
            } else {
                channel.shutdown();
                computeResponseBuilder.setCode(computeResponse.responseCode.FAILED);
                responseObserver.onNext(computeResponseBuilder.build());
                responseObserver.onCompleted();
                return;
            }
        }
        boolean useBigIntegers = false;
        for (Integer integer : inputList) {
            if (integer > 12){
                useBigIntegers = true;
                break;
            }
        }
        if (useBigIntegers){
            List<BigInteger> bigIntInputList = new ArrayList<BigInteger>();
            for (Integer integer : inputList) {
                bigIntInputList.add(new BigInteger(String.valueOf(integer)));
            }
            requestStream = new BigIntegerNumStreamImplementation(bigIntInputList);
        } else {
            requestStream = new NumStreamImplementation(inputList);
        }
        UserRequest userRequest = new UserRequest(
            new FileUserRequestSource("N/A"),
            new FileUserRequestDestination("N/A"),
            requestStream
        );
        EngineResponse response = engine.submitRequest(userRequest);
        ResponseCode responseCode = response.getResponseCode();
        if (!responseCode.isFailure()){

            RequestResult r = response.getRequestResult();
            if (r instanceof RequestResultImplementation requestResultImplementation){

                OutputRequest.Builder outputRequestBuilder = OutputRequest.newBuilder();

                NumStream resultNumStream = requestResultImplementation.getResultNumStream();
                if (resultNumStream instanceof BigIntegerNumStreamImplementation bigIntImp){
                    List<Integer> resultList = new ArrayList<>();
                    for (BigInteger bigInteger : bigIntImp.getBigIntegers()) {
                        resultList.add(bigInteger.intValue());
                    }
                    outputRequestBuilder.addAllOutputList(resultList);
                } else if (resultNumStream instanceof NumStreamImplementation intImp){
                    outputRequestBuilder.addAllOutputList(intImp.getIntegers());
                }

                outputRequestBuilder.setFileName(request.getOutputPath());

                OutputResponse dataStoreResponse = dataStoreServerStub.writeOutput(outputRequestBuilder.build());
                if (dataStoreResponse.getOutputWritten()){
                    computeResponseBuilder.setCode(computeResponse.responseCode.SUCCESSFUL);
                } else {
                    computeResponseBuilder.setCode(computeResponse.responseCode.FAILED);
                }
            }

        } else {
            computeResponseBuilder.setCode(computeResponse.responseCode.FAILED);
            if (response instanceof EngineResponseException ex) {
            	ex.getException().printStackTrace();
            }
        }
        channel.shutdown();
        responseObserver.onNext(computeResponseBuilder.build());
        responseObserver.onCompleted();
    }

}
