package compilador;

import java.util.ArrayList;
import java.util.List;

import analisadorLexico.AnalisadorLexico;
import analisadorSintatico.AnalisadorSintatico;
import entidades.Arquivo;
import entidades.Token;
import entidades.TokenErro;
import enums.SimboloEnum;
import exceptions.CaractereNaoEsperadoEncontradoException;
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

	// TODO mudar o retorno do método conforme a necessidade
	/*public void compila(Arquivo arquivo) {
		arquivo.lerCaractere();
		while (!arquivo.fimDoArquivo()) {
			Token token;
			try {
				token = analisadorLexico.analiseLexical(arquivo);
				if (token.getLexema()!=null && token.getSimbolo()!=null) {
					listaToken.add(token);
				}
			} catch (FimInesperadoDoArquivoException e) {
				listaToken.add(new TokenErro(SimboloEnum.Serro, e.getMessage(),"Final inesperado do arquivo:"));
				break;
			} catch (CaractereNaoEsperadoEncontradoException e) {
				listaToken.add(new TokenErro(SimboloEnum.Serro, e.getMessage(),"Caractere não reconhecido encontrado:"));
				break;
			}
		}
	}*/
	
	public void compila(Arquivo arquivo) throws ErroSintaticoException {
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
