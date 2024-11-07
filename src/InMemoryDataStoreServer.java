import java.util.ArrayList;
import java.util.List;

import datastore.DataStoreSystemGrpc.DataStoreSystemImplBase;
import datastore.Datastore.InputRequest;
import datastore.Datastore.InputResponse;
import datastore.Datastore.OutputRequest;
import datastore.Datastore.OutputResponse;
import io.grpc.stub.StreamObserver;

public class InMemoryDataStoreServer extends DataStoreSystemImplBase {

	private final List<Integer> inMemoryInputList = new ArrayList<>();
	private final List<Integer> inMemoryOutputList = new ArrayList<>();

	public void readInput(InputRequest request, StreamObserver<InputResponse> responseObserver) {
		InputResponse.Builder responseBuilder = InputResponse.newBuilder();

		responseBuilder.addAllInputList(inMemoryInputList);
		InputResponse response = responseBuilder.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	public void writeOutput(OutputRequest request, StreamObserver<OutputResponse> responseObserver) {
		OutputResponse.Builder responseBuilder = OutputResponse.newBuilder();

		inMemoryOutputList.addAll(request.getOutputListList());
		responseBuilder.setOutputWritten(true);
		OutputResponse response = responseBuilder.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	public List<Integer> getInMemoryInputList() {
		return new ArrayList<>(inMemoryInputList);
	}

	public List<Integer> getInMemoryOutputList() {
		return new ArrayList<>(inMemoryOutputList);
	}

	public void addInputData(List<Integer> data) {
		inMemoryInputList.addAll(data);
	}
}
