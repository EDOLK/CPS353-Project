import java.util.List;

import computeengine.ComputeEngineServiceGrpc.ComputeEngineServiceImplBase;
import computeengine.Computeengngine.computeRequest;
import computeengine.Computeengngine.computeResponse;
import datastoreapi.DataStoreAPI;
import datastoreapi.InputRequest;
import datastoreapi.OutputRequest;
import interfaces.NumStream;
import io.grpc.stub.StreamObserver;

/**
 * GRPCMediator
 */

public class GRPCMediator extends ComputeEngineServiceImplBase{

    private ComputeEngine engine = new ComputeEngineImplementation(new ComputationImplementation());
    // TODO: add datastore server
    private DataStoreAPI datastore = new DataStoreAPI();

    @Override
    public void compute(computeRequest request, StreamObserver<computeResponse> responseObserver) {
        computeResponse.Builder computeResponseBuilder = computeResponse.newBuilder();
        List<Integer> inputList = request.getInputList();
        NumStream requestStream;
        if (inputList.isEmpty()){
            if (request.getInputPath() != null && !request.getInputPath().equals("")){
                // TODO: input path exists, fetch input file using datastore server and set input
                List<Integer> inList = datastore.readInput(new InputRequest(request.getInputPath()));
                requestStream = new NumStreamImplementation(inList);
            } else {
                computeResponseBuilder.setCode(computeResponse.responseCode.FAILED);
                responseObserver.onNext(computeResponseBuilder.build());
                responseObserver.onCompleted();
                return;
            }
        } else {
            requestStream = new NumStreamImplementation(inputList);
        }
        UserRequest userRequest = new UserRequest(
            new FileUserRequestSource(request.getInputPath()),
            new FileUserRequestDestination(request.getOutputPath()),
            requestStream
        );
        EngineResponse response = engine.submitRequest(userRequest);
        ResponseCode responseCode = response.getResponseCode();
        if (responseCode.isFailure()){
            computeResponseBuilder.setCode(computeResponse.responseCode.FAILED);
        } else {
            computeResponseBuilder.setCode(computeResponse.responseCode.SUCCESSFUL);

            RequestResult r = response.getRequestResult();
            if (r instanceof RequestResultImplementation rImp){

                // TODO: send results and output path to datastore server and evaluate response

                datastore.setOutputList(List.of(rImp.getResultString()));
                datastore.writeOutput(new OutputRequest(request.getOutputPath()));
                computeResponseBuilder.addAllOutput(rImp.getResultNumStream().getIntegers());
            }

        }
        responseObserver.onNext(computeResponseBuilder.build());
        responseObserver.onCompleted();
    }

}
