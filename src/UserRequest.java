import interfaces.NumStream;

public class UserRequest {
	private NumStream requestStream;
	private UserRequestSource userRequestSource;
	private UserRequestDestination userRequestDestination;
	private char resultDelimiter = ':';
	private char pairDelimiter = ',';

	public UserRequest(NumStream requestStream, UserRequestSource userRequestSource, UserRequestDestination userRequestDestination, char resultDelimiter,
			char pairDelimiter) {
		if(userRequestSource == null) {
			throw new IllegalArgumentException("UserRequestSource cannot be null");
		}
		if(userRequestDestination == null){
			throw new IllegalArgumentException("UserRequestDestination cannot be null");
		}

		this.requestStream = requestStream;
		this.pairDelimiter = pairDelimiter;
		this.resultDelimiter = resultDelimiter;
		this.userRequestDestination = userRequestDestination;
		this.userRequestSource = userRequestSource;
	}
	
	public UserRequest(UserRequestSource userRequestSource, UserRequestDestination userRequestDestination, char resultDelimiter, NumStream requestStream) {
		this.userRequestSource = userRequestSource;
		this.userRequestDestination = userRequestDestination;
		this.resultDelimiter = resultDelimiter;
		this.requestStream = requestStream;
	}

	public UserRequest(UserRequestSource userRequestSource, UserRequestDestination userRequestDestination, NumStream requestStream) {
		this(requestStream, userRequestSource, userRequestDestination, ';', ':');
	}

	public UserRequest(UserRequestSource userRequestSource, UserRequestDestination userRequestDestination) {
		if(userRequestSource == null) {
			throw new IllegalArgumentException("UserRequestSource cannot be null");
		}
		if(userRequestDestination == null) {
			throw new IllegalArgumentException("UserRequestDestination cannot be null");
		}
		this.userRequestSource = userRequestSource;
		this.userRequestDestination = userRequestDestination;
	}

	public NumStream getRequestStream() {
		return requestStream;
	}

	public void setRequestStream(NumStream requestStream) {
		if(requestStream == null) {
			throw new IllegalArgumentException("NumStream cannot be null");
		}
		this.requestStream = requestStream;
	}
	
	public UserRequestSource getUserRequestSource() {
		return userRequestSource;
	}

	public UserRequestDestination getUserRequestDestination() {
		return userRequestDestination;
	}

	public char getResultDelimiter() {
		return resultDelimiter;
	}

	public char getPairDelimiter() {
		return pairDelimiter;
	}
}
