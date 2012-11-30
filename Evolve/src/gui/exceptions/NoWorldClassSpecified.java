package gui.exceptions;

public class NoWorldClassSpecified extends Exception {
	/**
	 * @param message
	 * @param cause
	 */
	public NoWorldClassSpecified(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public NoWorldClassSpecified(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NoWorldClassSpecified(Throwable cause) {
		super(cause);
	}

}
