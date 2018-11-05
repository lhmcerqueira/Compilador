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
				//TODO insereTabela(token.lexema,"nomedeprograma","","");
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
					analisaBloco(arquivo, listaToken);
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto)) {
						//TODO ponto de atenção ao final do arquivo.
						pegaToken(arquivo, listaToken);
						if (arquivo.fimDoArquivo()) {
							return true;
						} else {
							throw new ErroSintaticoException("Final do Arquivo não encontrado.");
						}
					} else {
						throw new ErroSintaticoException("Ponto final faltando.");
					}
				} else {
					throw new ErroSintaticoException("Ponto e vírgula faltando.");
				}
			} else {
				throw new ErroSintaticoException("Nome do programa não encontrado.");
			}
		} else {
			throw new ErroSintaticoException("Inicio do programa não encontrado.");
		}
	}

	private void analisaBloco(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		analisaEtapaVariaveis(arquivo, listaToken);
		analisaSubrotinas(arquivo, listaToken);
		analisaComandos(arquivo, listaToken);
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
						throw new ErroSintaticoException("Ponto e vírgula faltando.");
					}
				}
			} else {
				throw new ErroSintaticoException("Identificador de variável não encontrado");
			}
		}
	}

	private void analisaVariaveis(Arquivo arquivo, List<Token> listaToken)
			throws ErroSintaticoException, FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException {
		do {
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				//TODO pesquisa na tabela a duplicicade de símbolos de variáveis
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Svirgula)
						|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sdoispontos)) {
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Svirgula)) {
						pegaToken(arquivo, listaToken);
						if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sdoispontos)) {
							throw new ErroSintaticoException("declaração incorreta de variáveis.");
						}
					}
				} else {
					throw new ErroSintaticoException("declaração incorreta de variáveis.");
				}
			} else {
				throw new ErroSintaticoException("nome de variável não declarado.");
			}
		} while (!tokenCorrente.getSimbolo().equals(SimboloEnum.Sdoispontos));
		pegaToken(arquivo, listaToken);
		analisaTipo(arquivo, listaToken);
	}

	private void analisaTipo(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		if (!tokenCorrente.getSimbolo().equals(SimboloEnum.Sinteiro)
				&& !tokenCorrente.getSimbolo().equals(SimboloEnum.Sbooleano)) {
			throw new ErroSintaticoException("declaração incorreta de variáveis: Tipo errado de variável.");
		}
		pegaToken(arquivo, listaToken);
	}

	private void analisaComandos(Arquivo arquivo, List<Token> listaToken)
			throws ErroSintaticoException, FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException {
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sinicio)) {
			pegaToken(arquivo, listaToken);
			analisaComandoSimples(arquivo, listaToken);
			while (!tokenCorrente.getSimbolo().equals(SimboloEnum.Sfim)) {
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
					pegaToken(arquivo, listaToken);
					if (!tokenCorrente.getSimbolo().equals(SimboloEnum.Sfim)) {
						analisaComandoSimples(arquivo, listaToken);
					}
				} else {
					throw new ErroSintaticoException("Ponto e vírgula faltando.");
				}
			}
		} else {
			throw new ErroSintaticoException("declaração incorreta de comandos.");
		}
	}

	private void analisaComandoSimples(Arquivo arquivo, List<Token> listaToken)
			throws ErroSintaticoException, FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException {
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			analisaAtribuicaoChamadaDeProcedimento(arquivo, listaToken);
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sse)) {
			analisaSe(arquivo, listaToken);
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Senquanto)) {
			analisaEnquanto(arquivo, listaToken);
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sleia)) {
			analisaLeia(arquivo, listaToken);
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sescreva)) {
			analisaEscreva(arquivo, listaToken);
		} else {
			analisaComandos(arquivo, listaToken);
		}
	}

	private void analisaAtribuicaoChamadaDeProcedimento(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Satribuicao)) {
			//analisa atribuicao, neste caso é uma expressão e de acordo com a bnf
			//<comando atribuicao>::= <identificador> := <expressão> 
			//já lemos o identificador e a atribuiçao, falta apenas a expressão
			pegaToken(arquivo, listaToken);
			analisaExpressao(arquivo, listaToken);
		} else {
			analisaChamadaProcedimento(arquivo, listaToken);
		}
	}

	private void analisaLeia(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sabre_parenteses)) {
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sfecha_parenteses)) {
					pegaToken(arquivo, listaToken);
				} else {
					throw new ErroSintaticoException(") faltando.");
				}
			} else {
				throw new ErroSintaticoException("Identificador do comando leia faltando.");
			}
		} else {
			throw new ErroSintaticoException("( faltando.");
		}
	}

	private void analisaEscreva(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sabre_parenteses)) {
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sfecha_parenteses)) {
					pegaToken(arquivo, listaToken);
				} else {
					throw new ErroSintaticoException(") faltando.");
				}
			} else {
				throw new ErroSintaticoException("Identificador do comando escreva faltando.");
			}
		} else {
			throw new ErroSintaticoException("( faltando.");
		}
	}

	private void analisaEnquanto(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		analisaExpressao(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sfaca)) {
			pegaToken(arquivo, listaToken);
			analisaComandoSimples(arquivo, listaToken);
		} else {
			throw new ErroSintaticoException("Comando faça faltando.");
		}
	}

	private void analisaSe(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		analisaExpressao(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sentao)) {
			pegaToken(arquivo, listaToken);
			analisaComandoSimples(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Ssenao)) {
				pegaToken(arquivo, listaToken);
				analisaComandoSimples(arquivo, listaToken);
			}
		} else {
			throw new ErroSintaticoException("Comando então faltando.");
		}

	}

	private void analisaSubrotinas(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		/*
		 * int flag = 0; if
		 * (tokenCorrente.getSimbolo().equals(SimboloEnum.Sprocedimento) ||
		 * tokenCorrente.getSimbolo().equals(SimboloEnum.Sfuncao)) { implementação do
		 * semantico }
		 */

		while (tokenCorrente.getSimbolo().equals(SimboloEnum.Sprocedimento)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sfuncao)) {
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sprocedimento)) {
				analisaDeclaracaoProcedimento(arquivo, listaToken);
			} else {
				analisaDeclaracaoDeFuncao(arquivo, listaToken);
			}
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
				pegaToken(arquivo, listaToken);
			} else {
				throw new ErroSintaticoException("Ponto e vírgula faltando.");
			}
		}

	}

	private void analisaDeclaracaoProcedimento(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
				analisaBloco(arquivo, listaToken);
			} else {
				throw new ErroSintaticoException("Ponto e vírgula faltando.");
			}
		} else {
			throw new ErroSintaticoException("indentificador de procedimento faltando.");
		}
	}

	private void analisaDeclaracaoDeFuncao(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sdoispontos)) {
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sinteiro)
						|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sbooleano)) {
					pegaToken(arquivo, listaToken);
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
						analisaBloco(arquivo, listaToken);
					} else {
						throw new ErroSintaticoException("Ponto e vírgula faltando.");
					}
				} else {
					throw new ErroSintaticoException("tipo de função faltando.");
				}
			} else {
				throw new ErroSintaticoException(": faltando.");
			}
		} else {
			throw new ErroSintaticoException("indentificador de função faltando.");
		}
	}

	private void analisaExpressao(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		analisaExpressaoSimples(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Smaior)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smaiorig)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smenor)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smenorig)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sdif)) {
			pegaToken(arquivo, listaToken);
			analisaExpressaoSimples(arquivo, listaToken);
		} 
	}

	private void analisaExpressaoSimples(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Smais)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smenos)) {
			pegaToken(arquivo, listaToken);
			analisaTermo(arquivo, listaToken);
		} 
		analisaTermo(arquivo, listaToken);
		while(tokenCorrente.getSimbolo().equals(SimboloEnum.Smais)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smenos)
				||tokenCorrente.getSimbolo().equals(SimboloEnum.Sou)) {
			pegaToken(arquivo, listaToken);
			analisaTermo(arquivo, listaToken);		
			}
	}

	private void analisaTermo(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		analisaFator(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Smult) || tokenCorrente.getSimbolo().equals(SimboloEnum.Sdiv)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Se)) {
			pegaToken(arquivo, listaToken);
			analisaFator(arquivo, listaToken);
		} 
	}

	private void analisaFator(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			analisaChamadaFuncao(arquivo, listaToken);
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Snumero)) {
			pegaToken(arquivo, listaToken);
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Snao)) {
			pegaToken(arquivo, listaToken);
			analisaFator(arquivo, listaToken);
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sabre_parenteses)) {
			pegaToken(arquivo, listaToken);
			analisaExpressao(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sfecha_parenteses)) {
				pegaToken(arquivo, listaToken);
			} else {
				throw new ErroSintaticoException("Falha ao detectar fator");
			}
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sverdadeiro)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sfalso)) {
			pegaToken(arquivo, listaToken);
		} 
	}

	public void analisaChamadaProcedimento(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
			pegaToken(arquivo, listaToken);
		} else {
			throw new ErroSintaticoException("ponto e vírgula faltando na chamada de procedimento.");
		}
	}

	public void analisaChamadaFuncao(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		pegaToken(arquivo, listaToken);
	}
	
	private void pegaToken(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException {
		tokenCorrente = analisadorLexico.analiseLexical(arquivo);
		if (tokenCorrente.getLexema() != null && tokenCorrente.getSimbolo() != null) {
			listaToken.add(tokenCorrente);
		}
	}
}
