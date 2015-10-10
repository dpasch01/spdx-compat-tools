package ac.ucy.cs.spdx.exception;

public class UnsupportedSpdxVersionException extends Exception {

	private static final long serialVersionUID = -6248683457602895108L;

	public UnsupportedSpdxVersionException() {
		super();
	}

	public UnsupportedSpdxVersionException(String message) {
		super(message);
	}

	public UnsupportedSpdxVersionException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedSpdxVersionException(Throwable cause) {
		super(cause);
	}
	
}
