package analisadorLexico;

import entidades.Arquivo;
import entidades.Token;

public class AnalisadorLexico {
	private Arquivo arquivo;
	private char caractereCorrente;
	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	public Token analiseLexical() {
		Token token = new Token();
		lerCaractere();
		while(!arquivo.fimDoArquivo()) {
			while((caractereCorrente=='{'||caractereCorrente==' ')&& !arquivo.fimDoArquivo()) {
				if(caractereCorrente=='{') {
					while(caractereCorrente!='}' && !arquivo.fimDoArquivo()) {
						lerCaractere();
					}
					lerCaractere();
				}
				while(caractereCorrente==' ') {
					lerCaractere();
				}
			}
			if(!arquivo.fimDoArquivo()) {
				token = pegaToken();
			}
		}
		
		return token;
	}

	private Token pegaToken() {
		return null;
	}
	
	private void lerCaractere() {
		caractereCorrente = arquivo.lerCaractere();
	}
}
