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
			arquivo.lerCaractere();
		while(!arquivo.fimDoArquivo()) {
			Token token = analisadorLexico.analiseLexical(arquivo);
				if(token != null && token.getLexema()!=null && token.getSimbolo()!=null) {
					listaToken.add(token);
					if(token.getSimbolo().equals(SimboloEnum.Serro)) {
						break;
					}
				}
		}
	}
}
