package compilador;

import analisadorLexico.AnalisadorLexico;
import entidades.Arquivo;

public class Compilador {
	private AnalisadorLexico analisadorLexico;
	private Arquivo arquivo;
	
	public Compilador(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	//TODO mudar o retorno do método conforme a necessidade
	public void compila() {
		analisadorLexico = new AnalisadorLexico();
		
		
	}
	

}
