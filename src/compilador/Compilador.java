package compilador;

import java.util.ArrayList;
import java.util.List;

import entidades.Arquivo;
import entidades.Token;
import entidades.TokenErro;
import enums.SimboloEnum;
import exceptions.CaractereNaoEsperadoEncontradoException;
import exceptions.ErroSemanticoException;
import exceptions.ErroSintaticoException;
import exceptions.FimInesperadoDoArquivoException;

public class Compilador {

	private AnalisadorSintatico analisadorSintatico;
	private List<Token> listaToken;
	public Compilador() {
		this.analisadorSintatico = new AnalisadorSintatico();
		this.listaToken = new ArrayList<>();
	}

	public List<Token> getListaToken() {
		return this.listaToken;
	}

	public void compila(Arquivo arquivo) throws ErroSintaticoException, ErroSemanticoException {
		arquivo.lerCaractere();
			try {
				analisadorSintatico.analisaSintatico(arquivo, listaToken);
			} catch (FimInesperadoDoArquivoException e) {
				listaToken.add(new TokenErro(SimboloEnum.Serro, e.getMessage(),"Final inesperado do arquivo:"));
			} catch (CaractereNaoEsperadoEncontradoException e) {
				listaToken.add(new TokenErro(SimboloEnum.Serro, e.getMessage(),"Caractere não reconhecido encontrado:"));
			} 
		
	}
	
}
