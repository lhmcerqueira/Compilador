package analisadorSintatico;

import java.util.List;

import analisadorLexico.AnalisadorLexico;
import entidades.Arquivo;
import entidades.Token;
import enums.SimboloEnum;
import exceptions.CaractereNaoEsperadoEncontradoException;
import exceptions.ErroSintaticoException;
import exceptions.FimInesperadoDoArquivoException;

public class AnalisadorSintatico {

	private AnalisadorLexico analisadorLexico;
	private Token tokenCorrente;

	public AnalisadorSintatico() {
		this.analisadorLexico = new AnalisadorLexico();
	}

	public boolean analisaSintatico(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sprograma)) {
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
					analisaBloco(arquivo, listaToken);

					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto)) {
						pegaToken(arquivo, listaToken);
						if (arquivo.fimDoArquivo()) {
							return true;
						} else {
							throw new ErroSintaticoException("Final do Arquivo n�o encontrado.");
						}
					} else {
						throw new ErroSintaticoException("Ponto final faltando.");
					}
				} else {
					throw new ErroSintaticoException("Ponto e v�rgula faltando.");
				}
			} else {
				throw new ErroSintaticoException("Nome do programa n�o encontrado.");
			}
		} else {
			throw new ErroSintaticoException("Inicio do programa n�o encontrado.");
		}
	}

	private void analisaBloco(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		analisaEtapaVariaveis(arquivo, listaToken);
	}

	private void analisaEtapaVariaveis(Arquivo arquivo, List<Token> listaToken)
			throws ErroSintaticoException, FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException {
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Svar)) {
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				while (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
					analisaVariaveis(arquivo, listaToken);
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
						pegaToken(arquivo, listaToken);
					} else {
						throw new ErroSintaticoException("Ponto e v�rgula faltando.");
					}
				}
			} else {
				throw new ErroSintaticoException("Identificador de vari�vel n�o encontrado");
			}
		} else {
			throw new ErroSintaticoException("Inicio do programa n�o encontrado.");
		}
	}

	private void analisaVariaveis(Arquivo arquivo, List<Token> listaToken)
			throws ErroSintaticoException, FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException {
		do {
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Svirgula)
						|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sdoispontos)) {
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Svirgula)) {
						pegaToken(arquivo, listaToken);
						if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sdoispontos)) {
							throw new ErroSintaticoException("declara��o incorreta de vari�veis.");
						}
					}
				} else {
					throw new ErroSintaticoException("declara��o incorreta de vari�veis.");
				}
			} else {
				throw new ErroSintaticoException("nome de vari�vel n�o declarado.");
			}
		} while (tokenCorrente.getSimbolo().equals(SimboloEnum.Sdoispontos));
		pegaToken(arquivo, listaToken);
		analisaTipo(arquivo, listaToken);
	}

	private void analisaTipo(Arquivo arquivo, List<Token> listaToken) throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		if (!tokenCorrente.getSimbolo().equals(SimboloEnum.Sinteiro)
				&& !tokenCorrente.getSimbolo().equals(SimboloEnum.Sbooleano)) {
			throw new ErroSintaticoException("declara��o incorreta de vari�veis: Tipo errado de vari�vel.");
		}
		pegaToken(arquivo, listaToken);
	}

	private void analisaSubRotinas(Arquivo arquivo, List<Token> listaToken) {

	}

	private void analisaComandos(Arquivo arquivo, List<Token> listaToken) throws ErroSintaticoException, FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException {
		if(tokenCorrente.getSimbolo().equals(SimboloEnum.Sinteiro)) {
			pegaToken(arquivo, listaToken);
			analisaComandoSimples(arquivo, listaToken);
			while(!tokenCorrente.getSimbolo().equals(SimboloEnum.Sfim)) {
				if(tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
					pegaToken(arquivo, listaToken);
					if(!tokenCorrente.getSimbolo().equals(SimboloEnum.Sfim)) {
						//TODO analisa comando simples
					}
				} else {
					throw new ErroSintaticoException("Ponto e v�rgula faltando.");
				}
			}
		} else {
			throw new ErroSintaticoException("declara��o incorreta de comandos.");
		}
	}

	private void analisaComandoSimples(Arquivo arquivo, List<Token> listaToken) throws ErroSintaticoException, FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException {
		if(tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			
		} else if(tokenCorrente.getSimbolo().equals(SimboloEnum.Sse)) {
			
		} else if(tokenCorrente.getSimbolo().equals(SimboloEnum.Senquanto)) {
			
		} else if(tokenCorrente.getSimbolo().equals(SimboloEnum.Sleia)) {
			
		} else if(tokenCorrente.getSimbolo().equals(SimboloEnum.Sescreva)) {
			
		}  else {
			analisaComandos(arquivo, listaToken);
		}
	}
	
	private void pegaToken(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException {
		tokenCorrente = analisadorLexico.analiseLexical(arquivo);
		if (tokenCorrente.getLexema() != null && tokenCorrente.getSimbolo() != null) {
			listaToken.add(tokenCorrente);
		}
	}
}
