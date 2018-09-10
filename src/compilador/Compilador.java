package compilador;

import java.util.ArrayList;
import java.util.List;

import analisadorLexico.AnalisadorLexico;
import entidades.Arquivo;
import entidades.Token;

public class Compilador {
	
	private List<Token> listaToken;
	private AnalisadorLexico analisadorLexico;
	
	public List<Token> getListaToken(){
		return this.listaToken;
	}
	
	//TODO mudar o retorno do método conforme a necessidade
	public void compila(Arquivo arquivo) {
		analisadorLexico = new AnalisadorLexico();
		listaToken = new ArrayList<>();
		
		while(!arquivo.fimDoArquivo()) {
			listaToken.add(analisadorLexico.analiseLexical(arquivo));
		}
	}

}
