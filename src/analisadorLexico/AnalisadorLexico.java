package analisadorLexico;

import entidades.Arquivo;
import entidades.Token;

public class AnalisadorLexico {
	
	private char caractereCorrente;
			
	public Token analiseLexical(Arquivo arquivo) {
		Token token = new Token();
		lerCaractere(arquivo);
		while(!arquivo.fimDoArquivo()) {
			while((caractereCorrente=='{'||caractereCorrente==' ')&& !arquivo.fimDoArquivo()) {
				if(caractereCorrente=='{') {
					while(caractereCorrente!='}' && !arquivo.fimDoArquivo()) {
						lerCaractere(arquivo);
					}
					lerCaractere(arquivo);
				}
				while(caractereCorrente==' ') {
					lerCaractere(arquivo);
				}
			}
			if(!arquivo.fimDoArquivo()) {
				token = pegaToken();
				//TODO implementar o pegaToken
			}
		}
		
		return token;
	}

	private Token pegaToken() {
		return null;
	}
	
	private void lerCaractere(Arquivo arquivo) {
		caractereCorrente = arquivo.lerCaractere();
	}
}
