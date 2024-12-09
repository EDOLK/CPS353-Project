package datastoreapi;

public class OutputRequest implements WriteOutputResponse{
	private String file;

	public OutputRequest(String file) {
		if(file == null) {
			throw new IllegalArgumentException("Output file cannot be null");
		}
		this.setFile(file);
	}

	public OutputRequest() {
		return;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		if(file == null) {
			throw new IllegalArgumentException("Output file cannot be null");
		}
		this.file = file;
	}
}
