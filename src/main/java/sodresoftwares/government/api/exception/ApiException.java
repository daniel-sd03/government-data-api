package sodresoftwares.government.api.exception;

public class ApiException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private final int statusCode;

	public ApiException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
