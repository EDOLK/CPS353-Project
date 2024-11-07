import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import datastore.DataStoreSystemGrpc.DataStoreSystemImplBase;
import datastore.Datastore.InputRequest;
import datastore.Datastore.InputResponse;
import datastore.Datastore.OutputRequest;
import datastore.Datastore.OutputResponse;
import io.grpc.stub.StreamObserver;

public class DataStoreServerImpl extends DataStoreSystemImplBase {

	public void readInput(InputRequest request, StreamObserver<InputResponse> responseObserver) {
		String fileName = request.getFileName();
		InputResponse.Builder responseBuilder = InputResponse.newBuilder();

		List<Integer> inputList = new ArrayList<>();
		BufferedReader reader = null;

		try{
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = reader.readLine()) != null) {
				try{
					inputList.add(Integer.parseInt(line.trim()));			
				}catch(NumberFormatException e){
					e.printStackTrace();
				}
			}
		}catch(FileNotFoundException e) {
			System.err.println("File not found: " + fileName);
		}catch (IOException e) {
			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		responseBuilder.addAllInputList(inputList);
		InputResponse response = responseBuilder.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	public void writeOutput(OutputRequest request, StreamObserver<OutputResponse> responseObserver) {
		String fileName = request.getFileName();
		OutputResponse.Builder responseBuilder = OutputResponse.newBuilder();

		List<Integer> outputList = new ArrayList<>();
		boolean outputWritten = false;

		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(fileName));
			for(int output : outputList) {
				writer.write(String.valueOf(output));
				writer.newLine();
			}
			outputWritten = true;
		}catch(IOException e){
			e.printStackTrace();
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		responseBuilder.setOutputWritten(outputWritten);
		OutputResponse response = responseBuilder.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
