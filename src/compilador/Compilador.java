package compilador;

import java.util.ArrayList;
import java.util.List;

import analisadorLexico.AnalisadorLexico;
import entidades.Arquivo;
import entidades.Token;
import entidades.TokenErro;
import enums.SimboloEnum;
import exceptions.CaractereNaoEsperadoEncontradoException;
import exceptions.FimInesperadoDoArquivoException;

public class Compilador {

	private List<Token> listaToken;
	private AnalisadorLexico analisadorLexico;

	public Compilador() {
		this.listaToken = new ArrayList<>();
		this.analisadorLexico = new AnalisadorLexico();
	}

	public List<Token> getListaToken() {
		return this.listaToken;
	}

	// TODO mudar o retorno do método conforme a necessidade
	public void compila(Arquivo arquivo) {
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
	}
}
