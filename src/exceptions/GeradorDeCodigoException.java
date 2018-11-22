package exceptions;

public class GeradorDeCodigoException extends Exception {

	public GeradorDeCodigoException() {
	}

	public GeradorDeCodigoException(String message) {
		super(message);
	}

	public GeradorDeCodigoException(Throwable cause) {
		super(cause);
	}

	public GeradorDeCodigoException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeradorDeCodigoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
