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
        BufferedReader outputFileReader = new BufferedReader(new FileReader(outputFile));
        int index = 0;
        String line;
        while ((line = outputFileReader.readLine()) != null){
            assert Integer.valueOf(line).equals(correctResults[index]);
            index++;
        }
        outputFileReader.close();

    }

}
