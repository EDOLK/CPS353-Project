import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ComputeServer {
  private Server server;

  void start() throws IOException {
    final int port = 8010;

    server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
            //.addService(new -)
            .addService(new GRPCMediator())
            .addService(ProtoReflectionService.newInstance())
            .build()
            .start();
    System.out.println("Server started on port " + port);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("closing server");
        try {
          if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
          }
        } catch (InterruptedException e) {
          e.printStackTrace(System.err);
        }
        System.err.println("*** server shut down");
      }
    });
  }

  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

    public static void main(String[] args) throws Exception {
        ComputeServer server = new ComputeServer();
        server.start();
        server.blockUntilShutdown();
    }

}
