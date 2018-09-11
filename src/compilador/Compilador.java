package compilador;

import java.util.ArrayList;
import java.util.List;

import analisadorLexico.AnalisadorLexico;
import entidades.Arquivo;
import entidades.Token;

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
		
		while(!arquivo.fimDoArquivo()) {
			listaToken.add(analisadorLexico.analiseLexical(arquivo));
		}
	}

}
