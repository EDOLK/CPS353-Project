package datastoreapi;

public class InputRequest {
	private String file;

	public InputRequest(String file) {
	if(file == null || file.isEmpty()) {
		throw new IllegalArgumentException("Input file cannot be null or empty");
	}
		this.setFile(file);
	}

	public InputRequest() {
		return;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
	if(file == null || file.isEmpty()) {
		throw new IllegalArgumentException("Input file cannot be null or empty");
	}
		this.file = file;
	}

	public void checkInputResponse(InputRequest inputRequest) {

	}
}
