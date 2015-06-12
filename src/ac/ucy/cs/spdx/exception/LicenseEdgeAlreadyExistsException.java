package ac.ucy.cs.spdx.exception;

public class LicenseEdgeAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -5295906599641916972L;

	public LicenseEdgeAlreadyExistsException() {
		super();
	}

	public LicenseEdgeAlreadyExistsException(String message) {
		super(message);
	}

	public LicenseEdgeAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseEdgeAlreadyExistsException(Throwable cause) {
		super(cause);
	}

}
