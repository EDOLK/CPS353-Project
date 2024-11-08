import computeengine.ComputeEngineServiceGrpc;
import computeengine.ComputeEngineServiceGrpc.ComputeEngineServiceBlockingStub;
import computeengine.Computeengngine.computeRequest;
import computeengine.Computeengngine.computeResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * GRPCClient
 */
public class GRPCClient{
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8010)
            .usePlaintext()
            .build();
        ComputeEngineServiceBlockingStub stub = ComputeEngineServiceGrpc.newBlockingStub(channel);
        computeRequest.Builder requestBuilder = computeRequest.newBuilder();
        if (args[0].equals("--file")){
            requestBuilder.setInputPath(args[1]);
        } else if (args[0].equals("--manual")){
            String[] splitArg = args[1].split(",");
            for (String str : splitArg) {
                requestBuilder.addInput(Integer.parseInt(str));
            }
        } else {
            System.err.println("Error: invalid input argument, use --file for input file path, or --manual for manual input");
            System.err.println("Ex: GRPCClient --file path/to/input/file.txt path/to/output/file.txt");
            System.err.println("Ex: GRPCClient --manual 2,5,8,10,12 path/to/output/file.txt");
            channel.shutdown();
            System.exit(1);
        }

        requestBuilder.setOutputPath(args[2]);

        computeResponse response = stub.compute(requestBuilder.build());

        if (response.getCode() == computeResponse.responseCode.SUCCESSFUL){
            System.out.println("");
            System.out.println("Calculation successful.");
            System.out.println("Output written at: " + args[2]);
        } else {
            System.out.println("Failed!");
            System.out.println(response);
        }
        channel.shutdown();

    }
}
