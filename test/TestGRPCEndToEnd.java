import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

public class TestGRPCEndToEnd {

    @Test
    public void testGRPCEndToEnd() throws Exception{

        File inputFile = new File("input.txt");
        inputFile.createNewFile();
        inputFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(inputFile);
        String inString = "5,7,12,16";
        fos.write(inString.getBytes("UTF-8"));
        fos.close();

        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        threadPool.submit(() -> {
            try {
                ComputeServer.main(new String[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        threadPool.submit(() -> {
            try {
                DataStoreServer.main(new String[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        GRPCClient.main(new String[]{"--file", "input.txt", "output.txt"});
        File outputFile = new File("output.txt");
        assert outputFile.exists();
        outputFile.deleteOnExit();
        int[] correctResults = {3,9,27,37};
        int[] outputResults = new int[4];
        BufferedReader outputFileReader = new BufferedReader(new FileReader(outputFile));
        int index = 0;
        String line;
        while ((line = outputFileReader.readLine()) != null){
            int value = Integer.valueOf(line);
            outputResults[index] = value;
            index++;
        }
        assert outputResults.length == correctResults.length;
        assert outputResults[0] == correctResults[0];
        assert outputResults[1] == correctResults[1];
        assert outputResults[2] == correctResults[2];
        assert outputResults[3] == correctResults[3];
        outputFileReader.close();

    }

}
