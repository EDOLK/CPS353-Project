import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import datastoreapi.DataStoreAPI;
import datastoreapi.DataStoreAPI.BigIntegerListWrapper;
import datastoreapi.DataStoreAPI.IntegerListWrapper;
import datastoreapi.DataStoreAPI.ListWrapper;
import datastoreapi.InputRequest;
import datastoreapi.OutputRequest;
import interfaces.BigIntegerNumStream;
import interfaces.NumStream;

public class Mediator {

	private static Mediator mediator = new Mediator(new DataStoreAPI(), new ComputeEngineImplementation(new ComputationImplementation(), new ComputeRequestHandlerImplementation()));

	private DataStoreAPI dataStoreApi;

	private ComputeEngine computeEngine;

	public Mediator(DataStoreAPI dataStoreApi, ComputeEngine computeEngine) {
		if(dataStoreApi == null) {
			throw new IllegalArgumentException("DataStoreAPI cannot be null");
		}
		if(computeEngine == null) {
			throw new IllegalArgumentException("ComputeEngine cannot be null");
		}
		this.dataStoreApi = dataStoreApi;
		this.computeEngine = computeEngine;
	}

	public static Mediator getMediator() {
		return mediator;
	}

	public static void setMediator(Mediator replacementMediator) {
		if(replacementMediator == null) {
			throw new IllegalArgumentException("Mediator cannot be null");
		}
		mediator = replacementMediator;
	}

	public <T> void sendInput(UserRequestProvider<T> provider) {
		if(provider == null) {
			throw new IllegalArgumentException("UserRequestProvider cannot be null");
		}
		try {
			provider.propigateResponse(sendInputHelper(provider));
		} catch (Exception e) {
			provider.propigateResponse(new EngineResponseExceptionImplementation(e));
		}

	}
	
	private <T> EngineResponse sendInputHelper(UserRequestProvider<T> provider) throws Exception {
		UserRequest userRequest = provider.generateRequest(provider.getInput());

		EngineResponse response = new ConcreteEngineResponse(ResponseCode.FAILED);

		Optional<InputRequest> inRequest = generateInputRequest(userRequest);

		if (inRequest.isPresent()) {
            ListWrapper result = dataStoreApi.readInputMulti(inRequest.get());
            if (result instanceof IntegerListWrapper integerListWrapper){
                NumStream inputNumStream = new NumStreamImplementation(integerListWrapper.getIntegerList());
                userRequest.setRequestStream(inputNumStream);
            } else if (result instanceof BigIntegerListWrapper biListWrapper){
                BigIntegerNumStream inputNumStream = new BigIntegerNumStreamImplementation(biListWrapper.getBigIntegerList());
                userRequest.setRequestStream(inputNumStream);
            }
		}

		EngineResponse engineResponse = computeEngine.submitRequest(userRequest);

		if (!engineResponse.getResponseCode().isFailure()) {
			Optional<OutputRequest> outRequest = generateOutputRequest(userRequest);
			if (outRequest.isPresent()) {
                OutputRequest outputRequest = outRequest.get();
				dataStoreApi.setOutputList(List.of(engineResponse.getRequestResult().getResultString()));
				dataStoreApi.writeOutput(outRequest.get());
				response.setResponseCode(ResponseCode.SUCCESSFUL); 
				response.setRequestResult(engineResponse.getRequestResult());
			}
		}

		return response;
	}

	private Optional<InputRequest> generateInputRequest(UserRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("UserRequest cannot be null");
		}
		if (request.getUserRequestSource() instanceof FileUserRequestSource fileSource) {
			return Optional.of(new InputRequest(fileSource.getFileName()));
		}
		return Optional.empty();
	}

	private Optional<OutputRequest> generateOutputRequest(UserRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("UserRequest cannot be null");
		}
		if (request.getUserRequestDestination() instanceof FileUserRequestDestination fileDestination) {
			return Optional.of(new OutputRequest(fileDestination.getFileName()));
		}
		return Optional.empty();
	}
}
