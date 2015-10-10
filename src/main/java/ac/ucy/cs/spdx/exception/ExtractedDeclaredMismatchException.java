package ac.ucy.cs.spdx.exception;

public class ExtractedDeclaredMismatchException extends Exception {

	private static final long serialVersionUID = 828342734285458087L;

	public ExtractedDeclaredMismatchException() {
		super();
	}

	public ExtractedDeclaredMismatchException(String message) {
		super(message);
	}

	public ExtractedDeclaredMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExtractedDeclaredMismatchException(Throwable cause) {
		super(cause);
	}
	
}