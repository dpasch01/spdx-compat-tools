package ac.ucy.cs.spdx.exception;

public class LicenseNodeAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -1914251955417392503L;

	public LicenseNodeAlreadyExistsException() {
		super();
	}

	public LicenseNodeAlreadyExistsException(String message) {
		super(message);
	}

	public LicenseNodeAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseNodeAlreadyExistsException(Throwable cause) {
		super(cause);
	}

}
