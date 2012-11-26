package exceptions;

public class NoWorldClassSpecified extends Exception {
	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NoWorldClassSpecified(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

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
