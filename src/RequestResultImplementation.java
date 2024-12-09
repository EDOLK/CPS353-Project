import interfaces.NumStream;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RequestResultImplementation implements RequestResult {
    NumStream result;
    String resultString;

    public RequestResultImplementation() {
    }

    public RequestResultImplementation(ArrayList<Integer> result) {
        if(result == null) {
            throw new IllegalArgumentException("Result list cannot be null");
        }
        this.result = new NumStreamImplementation(result);
    }

    public NumStream getResultNumStream() {
        return result;
    }

    @Override
    public String getResultString() {
        return resultString;
    }

    public void setResultNumStream(NumStream resultNumStream) {
        if(resultNumStream == null) {
            throw new IllegalArgumentException("NumStream cannot be null");
        }
        this.result = resultNumStream;
    }

    public void setResultString(String resultString) {
        if(resultString == null) {
            throw new IllegalArgumentException("Result cannot be null");
        }
        this.resultString = resultString;
    }
}
