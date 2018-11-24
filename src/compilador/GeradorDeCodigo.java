package compilador;

import java.io.IOException;

import exceptions.GeradorDeCodigoException;
import utils.GerenciadorDeArquivos;

public class GeradorDeCodigo {

	private StringBuffer codigo;
	private GerenciadorDeArquivos gerenciador;
	
	public GeradorDeCodigo() {
		super();
		this.codigo = new StringBuffer();
		this.gerenciador =  new GerenciadorDeArquivos();
	}

	public void gera(String comando) {
		codigo.append(comando).append(System.getProperty("line.separator"));
	}
	
	public void escreveEmArquivo(String nomeDoArquivo) throws GeradorDeCodigoException {
		try {
			gerenciador.salvarCodigoGerado(codigo.toString(),nomeDoArquivo);
		} catch (IOException e) {
			throw new GeradorDeCodigoException("não foi possivel gravar no arquivo");
		}
	}
}
