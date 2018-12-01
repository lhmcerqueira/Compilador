package compilador;


import entidades.Arquivo;
import entidades.Token;
import enums.SimboloEnum;
import exceptions.CaractereNaoEsperadoEncontradoException;
import exceptions.FimInesperadoDoArquivoException;

public class AnalisadorLexico {
	
	private static final String PONTUACAO = "[\\.\\;\\,\\(\\)]";
	private static final String OPERADOR_RELACIONAL = "[\\<\\>\\!\\=]";
	private static final String OPERADOR_ARITIMETICO = "[\\+\\-\\*]";
	private static final String DOIS_PONTOS = ":";
	private static final String LETRA_DIGITO_SUBLINHADO = "[a-zA-Z0-9_]";
	private static final String LETRA = "[a-zA-Z]";
	private static final String DIGITO = "\\d";
			/*
			 * tratar tab, tratar coment�rio tratar exclama��o
			 */
	public Token analiseLexical(Arquivo arquivo) throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException {
		
		while(!arquivo.fimDoArquivo()) {
			while((arquivo.getCaractereCorrente()=='{'|| arquivo.getCaractereCorrente()==' '|| arquivo.getCaractereCorrente()=='\t'|| arquivo.getCaractereCorrente()=='\n'|| arquivo.getCaractereCorrente()=='\r')&& !arquivo.fimDoArquivo()) {
				if(arquivo.getCaractereCorrente()=='{') {
					while(arquivo.getCaractereCorrente()!='}' && !arquivo.fimDoArquivo()) {
						lerCaractere(arquivo);
					}
					lerCaractere(arquivo);
					if(arquivo.fimDoArquivo()) {
						//TRATAR O ERRO NO RETORNO DO ARQUIVO
						throw new FimInesperadoDoArquivoException("falta }");
					}
				}
				while(arquivo.getCaractereCorrente()==' '|| arquivo.getCaractereCorrente()=='\t') {
					lerCaractere(arquivo);
				}
				while(arquivo.getCaractereCorrente()=='\r'|| arquivo.getCaractereCorrente()=='\n') {
					if(arquivo.getCaractereCorrente()=='\n') {
						arquivo.incrementaLinha();
					}
					lerCaractere(arquivo);
				}
			}
			if(!arquivo.fimDoArquivo()) {
				return pegaToken(arquivo);
			}
		}
		
		return new Token();
	}

	private Token pegaToken(Arquivo arquivo) throws CaractereNaoEsperadoEncontradoException {
		
		String valorStringDoCaractere = String.valueOf(arquivo.getCaractereCorrente());
		if(valorStringDoCaractere.matches(DIGITO)) {
			return trataDigito(arquivo);
		} else if(valorStringDoCaractere.matches(LETRA)) {
			return trataIdentificadorEPalavraReservada(arquivo);
		} else if(valorStringDoCaractere.matches(DOIS_PONTOS)) {
			return trataAtribuicao(arquivo);
		} else if(valorStringDoCaractere.matches(OPERADOR_ARITIMETICO)) {
			return trataOperadorAritimetico(arquivo);
		} else if(valorStringDoCaractere.matches(OPERADOR_RELACIONAL)) {
			return trataOperadorRelacional(arquivo);
		} else if(valorStringDoCaractere.matches(PONTUACAO)) {
			return trataPontuacao(arquivo);
		}
		//TRATAR O ERRO NO RETORNO DO ARQUIVO
		throw new CaractereNaoEsperadoEncontradoException(valorStringDoCaractere);
		
	}
	
	private Token trataDigito(Arquivo arquivo) {
		StringBuilder num = new StringBuilder();
		num = num.append(String.valueOf(arquivo.getCaractereCorrente()));
		lerCaractere(arquivo);
		while(String.valueOf(arquivo.getCaractereCorrente()).matches(DIGITO)) {
			num = num.append(String.valueOf(arquivo.getCaractereCorrente()));
			lerCaractere(arquivo);
		}
		return new Token(SimboloEnum.Snumero, num.toString());
	}
	
	private Token trataIdentificadorEPalavraReservada(Arquivo arquivo) {
		StringBuilder id = new StringBuilder();
		id = id.append(arquivo.getCaractereCorrente());
		lerCaractere(arquivo);
		while(String.valueOf(arquivo.getCaractereCorrente()).matches(LETRA_DIGITO_SUBLINHADO)) {
			id.append(arquivo.getCaractereCorrente());
			lerCaractere(arquivo);
		}
		Token token = new Token();
		token.setLexema(id.toString());
		switch (id.toString()) {
			case "programa":
				token.setSimbolo(SimboloEnum.Sprograma);
				break;
			case "se":
				token.setSimbolo(SimboloEnum.Sse);
				break;
			case "entao":
				token.setSimbolo(SimboloEnum.Sentao);
				break;
			case "senao":
				token.setSimbolo(SimboloEnum.Ssenao);
				break;
			case "enquanto":
				token.setSimbolo(SimboloEnum.Senquanto);
				break;
			case "faca":
				token.setSimbolo(SimboloEnum.Sfaca);
				break;
			case "inicio":
				token.setSimbolo(SimboloEnum.Sinicio);
				break;
			case "fim":
				token.setSimbolo(SimboloEnum.Sfim);
				break;
			case "escreva":
				token.setSimbolo(SimboloEnum.Sescreva);
				break;
			case "var":
				token.setSimbolo(SimboloEnum.Svar);
				break;
			case "inteiro":
				token.setSimbolo(SimboloEnum.Sinteiro);
				break;
			case "booleano":
				token.setSimbolo(SimboloEnum.Sbooleano);
				break;
			case "verdadeiro":
				token.setSimbolo(SimboloEnum.Sverdadeiro);
				break;
			case "falso":
				token.setSimbolo(SimboloEnum.Sfalso);
				break;
			case "procedimento":
				token.setSimbolo(SimboloEnum.Sprocedimento);
				break;
			case "funcao":
				token.setSimbolo(SimboloEnum.Sfuncao);
				break;
			case "div":
				token.setSimbolo(SimboloEnum.Sdiv);
				break;
			case "e":
				token.setSimbolo(SimboloEnum.Se);
				break;
			case "ou":
				token.setSimbolo(SimboloEnum.Sou);
				break;
			case "nao":
				token.setSimbolo(SimboloEnum.Snao);
				break;
			case "leia":
				token.setSimbolo(SimboloEnum.Sleia);
				break;
			default:
				token.setSimbolo(SimboloEnum.Sidentificador);
				break;
		}
		
		return token;
	}
	
	private Token trataAtribuicao(Arquivo arquivo) {
		StringBuilder atrib = new StringBuilder();
		atrib = atrib.append(arquivo.getCaractereCorrente());
		lerCaractere(arquivo);
		if(String.valueOf(arquivo.getCaractereCorrente()).equals("=")) {
			atrib = atrib.append(arquivo.getCaractereCorrente());
			lerCaractere(arquivo);
			return new Token(SimboloEnum.Satribuicao, atrib.toString());
		} 
		return new Token(SimboloEnum.Sdoispontos, atrib.toString());
	}
	
	private Token trataOperadorAritimetico(Arquivo arquivo) {
		String arit = new String();
		arit = String.valueOf(arquivo.getCaractereCorrente());
		Token token = new Token();
		token.setLexema(arit);
		if(arit.matches("[\\+]")) {
			token.setSimbolo(SimboloEnum.Smais);
		} else if(arit.matches("[\\-]")) {
			token.setSimbolo(SimboloEnum.Smenos);
		} else if(arit.matches("[\\*]")) {
			token.setSimbolo(SimboloEnum.Smult);
		}
		lerCaractere(arquivo);
		return token;
	}
	
	private Token trataOperadorRelacional(Arquivo arquivo) throws CaractereNaoEsperadoEncontradoException {
		StringBuilder relat = new StringBuilder();

		if(String.valueOf(arquivo.getCaractereCorrente()).matches("[\\<]")) {
			relat = relat.append(arquivo.getCaractereCorrente());
			lerCaractere(arquivo);
			if(String.valueOf(arquivo.getCaractereCorrente()).equals("=")) {
				relat = relat.append(arquivo.getCaractereCorrente());
				lerCaractere(arquivo);
				return new Token(SimboloEnum.Smenorig, relat.toString());
			}
			return new Token(SimboloEnum.Smenor, relat.toString());
		}

		if(String.valueOf(arquivo.getCaractereCorrente()).matches("[\\>]")) {
			relat = relat.append(arquivo.getCaractereCorrente());
			lerCaractere(arquivo);
			if(String.valueOf(arquivo.getCaractereCorrente()).equals("=")) {
				relat = relat.append(arquivo.getCaractereCorrente());
				lerCaractere(arquivo);
				return new Token(SimboloEnum.Smaiorig, relat.toString());
			}
			return new Token(SimboloEnum.Smaior, relat.toString());
		}
		

		if(String.valueOf(arquivo.getCaractereCorrente()).matches("[\\!]")) {
			relat = relat.append(arquivo.getCaractereCorrente());
			lerCaractere(arquivo);
			if(String.valueOf(arquivo.getCaractereCorrente()).equals("=")) {
				relat = relat.append(arquivo.getCaractereCorrente());
				lerCaractere(arquivo);
				return new Token(SimboloEnum.Sdif, relat.toString());
			}else {
				// TRATAR O ERRO NO RETORNO DO ARQUIVO
				throw new CaractereNaoEsperadoEncontradoException("! sem =");
			}
			
		} 
		if(String.valueOf(arquivo.getCaractereCorrente()).equals("=")) {
			relat = relat.append(arquivo.getCaractereCorrente());
			lerCaractere(arquivo);
			return new Token(SimboloEnum.Sig, relat.toString());
		}
		return null;
	}
	
	private Token trataPontuacao(Arquivo arquivo) {
		String arit = new String();
		arit = String.valueOf(arquivo.getCaractereCorrente());
		Token token = new Token();
		token.setLexema(arit);
		if(arit.matches("[\\.]")) {
			token.setSimbolo(SimboloEnum.Sponto);
		} else if(arit.matches("[\\;]")) {
			token.setSimbolo(SimboloEnum.Sponto_virgula);
		} else if(arit.matches("[\\,]")) {
			token.setSimbolo(SimboloEnum.Svirgula);
		} else if(arit.matches("[\\(]")) {
			token.setSimbolo(SimboloEnum.Sabre_parenteses);
		} else if(arit.matches("[\\)]")) {
			token.setSimbolo(SimboloEnum.Sfecha_parenteses);
		} 
		lerCaractere(arquivo);
		return token;
	}
	
	private void lerCaractere(Arquivo arquivo) {
		arquivo.lerCaractere();
	}
}
