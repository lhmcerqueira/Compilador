package exceptions;

public class FimInesperadoDoArquivoException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1399139445841196946L;

	public FimInesperadoDoArquivoException() {
		super();
	}
	
	public FimInesperadoDoArquivoException(String message) {
		super(message);
	}

	public FimInesperadoDoArquivoException(Throwable e) {
		super(e);
	}
}
