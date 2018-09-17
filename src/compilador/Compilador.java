package compilador;

import java.util.ArrayList;
import java.util.List;

import analisadorLexico.AnalisadorLexico;
import entidades.Arquivo;
import entidades.Token;
import enums.SimboloEnum;
import exceptions.FimInesperadoDoArquivoException;

public class Compilador {
	
	private List<Token> listaToken;
	private AnalisadorLexico analisadorLexico;
	
	public Compilador() {
		this.listaToken = new ArrayList<>();
		this.analisadorLexico = new AnalisadorLexico();
	}
	
	public List<Token> getListaToken(){
		return this.listaToken;
	}
	
	//TODO mudar o retorno do método conforme a necessidade
	public void compila(Arquivo arquivo) {
		try {
			arquivo.lerCaractere();
		} catch (FimInesperadoDoArquivoException e1) {
			trataFimInesperadoDeArquivo(arquivo);
		}
		while(!arquivo.fimDoArquivo()&&listaToken.size()<6) {
			try {
				listaToken.add(analisadorLexico.analiseLexical(arquivo));
			} catch (FimInesperadoDoArquivoException e) {
				trataFimInesperadoDeArquivo(arquivo);
				break;
			}
		}
	}

	private void trataFimInesperadoDeArquivo(Arquivo arquivo) {
		Token token = new Token();
		token.setLexema("O programa terminou antes do esperado na linha "+
		arquivo.getLinhaCorrente()+" no caractere "+arquivo.getIndiceCorrente());
		token.setSimbolo(SimboloEnum.Serro);
		listaToken = new ArrayList<>();
		listaToken.add(token);
	}

}
