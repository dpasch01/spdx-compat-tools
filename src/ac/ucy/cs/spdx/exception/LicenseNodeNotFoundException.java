package ac.ucy.cs.spdx.exception;

public class LicenseNodeNotFoundException extends Exception {

	private static final long serialVersionUID = 2385615480134840857L;

	public LicenseNodeNotFoundException() {
		super();
	}

	public LicenseNodeNotFoundException(String message) {
		super(message);
	}

	public LicenseNodeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseNodeNotFoundException(Throwable cause) {
		super(cause);
	}

}
