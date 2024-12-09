import java.lang.reflect.Array;
import java.util.ArrayList;

public class EngineResponseImplementation implements EngineResponse {
	ResponseCode responseCode;
	RequestResult requestResult;

	public EngineResponseImplementation() {
	}

	public EngineResponseImplementation(ResponseCode responseCode, ArrayList<Integer> requestResult) {
		if(responseCode == null) {
			throw new IllegalArgumentException("ResponseCode cannot be null");
		}
		if(requestResult == null) {
			throw new IllegalArgumentException("Result list cannot be null");
		}
		this.responseCode = responseCode;
		this.requestResult = new RequestResultImplementation(requestResult);
	}

	public EngineResponseImplementation(ResponseCode responseCode, RequestResult requestResult) {
		if(responseCode == null) {
			throw new IllegalArgumentException("ResponseCode cannot be null");
		}
		if(requestResult == null) {
			throw new IllegalArgumentException("Result list cannot be null");
		}
		this.responseCode = responseCode;
		this.requestResult = requestResult;
	}

	@Override
	public void setRequestResult(RequestResult result) {
		if(result == null) {
			throw new IllegalArgumentException("RequestResult cannot be null");
		}
		this.requestResult = result;
	}

	@Override
	public void setResponseCode(ResponseCode responseCode) {
		if(responseCode == null) {
			throw new IllegalArgumentException("ResponseCode cannot be null");
		}
		this.responseCode = responseCode;
	}

	@Override
	public ResponseCode getResponseCode() {
		return this.responseCode;
	}

	@Override
	public RequestResult getRequestResult() {
		return this.requestResult;
	}
}
