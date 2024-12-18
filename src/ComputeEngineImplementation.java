import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import datastoreapi.DataStoreAPI;
import datastoreapi.DataStoreAPI.BigIntegerListWrapper;
import datastoreapi.DataStoreAPI.IntegerListWrapper;
import datastoreapi.DataStoreAPI.ListWrapper;
import datastoreapi.OutputRequest;
import interfaces.BigIntegerNumStream;
import interfaces.NumStream;

public class ComputeEngineImplementation implements ComputeEngine {
    DataStoreAPI dataStoreAPI;
    ComputeEngineComputation computeEngineComputation;
    ComputeRequestHandler computeRequestHandler;

    public ComputeEngineImplementation(ComputeEngineComputation computeEngineComputation, ComputeRequestHandler computeRequestHandler, DataStoreAPI dataStoreAPI) {
        this.computeEngineComputation = computeEngineComputation;
        this.computeRequestHandler = computeRequestHandler;
        this.dataStoreAPI = dataStoreAPI;
    }

    public ComputeEngineImplementation(ComputeEngineComputation computeEngineComputation, ComputeRequestHandler computeRequestHandler) {
        this.computeEngineComputation = computeEngineComputation;
        this.computeRequestHandler = computeRequestHandler;
    }

    public ComputeEngineImplementation(ComputeEngineComputation computeEngineComputation) {
        this.computeEngineComputation = computeEngineComputation;
    }

    public ComputeEngineComputation getComputeEngineComputation() {
        return computeEngineComputation;
    }

    public void setComputeEngineComputation(ComputeEngineComputation computeEngineComputation) {
        this.computeEngineComputation = computeEngineComputation;
    }

    @Override
    public EngineResponse submitRequest(UserRequest userRequest) {
        return submitRequest(userRequest, false);
    }

    @Override
    public EngineResponse submitRequest(UserRequest userRequest, boolean internalRequest) {
        try {
            return submitRequestHelper(userRequest, internalRequest);
        } catch (Exception e) {
            return new EngineResponseExceptionImplementation(e);
        }
    }

    public EngineResponse submitRequestHelper(UserRequest userRequest, boolean internalRequest) throws Exception {
        if(userRequest == null) {
            throw new IllegalArgumentException("UserRequest cannot be null");
        }
        EngineResponse engineResponse = sendStreamForFactorial(userRequest.getRequestStream());
        NumStream resultStream = engineResponse.getRequestResult().getResultNumStream();
        RequestResult requestResult = engineResponse.getRequestResult();

        if (resultStream instanceof BigIntegerNumStream biNumStream){
            requestResult.setResultString(processResultStringForBigInteger(userRequest, biNumStream.getBigIntegers()));
        } else {
            requestResult.setResultString(processResultString(userRequest, resultStream.getIntegers()));
        }

        if (internalRequest) {
            dataStoreAPI.setOutputList(new ArrayList<>(List.of(requestResult.getResultString())));
            UserRequestDestination requestDestination = userRequest.getUserRequestDestination();
            if (requestDestination instanceof FileUserRequestDestination fileRequestDestination){
                dataStoreAPI.writeOutput(new OutputRequest(fileRequestDestination.getFileName()));
            }
        }

        return engineResponse;
    }

    @Override
    public EngineResponse sendStreamForFactorial(NumStream numStream) {
        if(numStream == null) {
            throw new IllegalArgumentException("NumStream cannot be null");
        }
        return computeEngineComputation.doFactorial(numStream);
    }

    

    public String processResultStringForBigInteger(UserRequest userRequest, List<BigInteger> resultList) {
        if(userRequest == null){
            throw new IllegalArgumentException("UserRequest cannot be null");
        }
        NumStream requestNumStream = userRequest.getRequestStream();
        if (!(requestNumStream instanceof BigIntegerNumStream biNumStream)){
            throw new UnsupportedOperationException("BigIntegerNumStream required");
        }
        List<BigInteger> requestStream = biNumStream.getBigIntegers();
        StringBuilder result = new StringBuilder();
        int resultPosition = 0;
        for (BigInteger requestInteger : requestStream) {
            result.append(requestInteger);
            result.append(userRequest.getResultDelimiter());
            result.append(resultList.get(resultPosition));

            if(requestStream.size() > resultPosition + 1) {
                result.append(userRequest.getPairDelimiter());
            }

            resultPosition++;
        }

        return result.toString();

    }

    public String processResultString(UserRequest userRequest, List<Integer> resultList) {
        if(userRequest == null){
            throw new IllegalArgumentException("UserRequest cannot be null");
        }
        List<Integer> requestStream = userRequest.getRequestStream().getIntegers();
        StringBuilder result = new StringBuilder();
        int resultPosition = 0;

        for (Integer requestInteger : requestStream) {
            result.append(requestInteger);
            result.append(userRequest.getResultDelimiter());
            result.append(resultList.get(resultPosition));

            if(requestStream.size() > resultPosition + 1) {
                result.append(userRequest.getPairDelimiter());
            }

            resultPosition++;
        }

        return result.toString();
    }

}
