package compilador;

import java.util.List;

import entidades.Arquivo;
import entidades.Simbolo;
import entidades.Token;
import enums.NivelTabelaSimbolo;
import enums.SimboloEnum;
import enums.TipoTabelaSimboloEnum;
import enums.TipoVariavelTabelaSimbolosEnum;
import exceptions.CaractereNaoEsperadoEncontradoException;
import exceptions.ErroSemanticoException;
import exceptions.ErroSintaticoException;
import exceptions.FimInesperadoDoArquivoException;
import exceptions.GeradorDeCodigoException;

public class AnalisadorSintatico {

	private AnalisadorLexico analisadorLexico;
	private AnalisadorSemantico analisadorSemantico;
	private GeradorDeCodigo geradorDeCodigo;
	private Token tokenCorrente;

	public AnalisadorSintatico() {
		this.analisadorLexico = new AnalisadorLexico();
		this.analisadorSemantico = new AnalisadorSemantico();
		this.geradorDeCodigo = new GeradorDeCodigo();
	}

	public boolean analisaSintatico(Arquivo arquivo, List<Token> listaToken) throws FimInesperadoDoArquivoException,
			CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException, GeradorDeCodigoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sprograma)) {
			geradorDeCodigo.gera("START");
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				insereTabela(tokenCorrente.getLexema(), TipoTabelaSimboloEnum.NOME_DE_PROGRAMA,
						NivelTabelaSimbolo.MARCA, null);
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
					analisaBloco(arquivo, listaToken);
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto)) {
						geradorDeCodigo.escreveEmArquivo();
						// TODO ponto de aten��o ao final do arquivo.
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

	private void analisaBloco(Arquivo arquivo, List<Token> listaToken) throws FimInesperadoDoArquivoException,
			CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		pegaToken(arquivo, listaToken);
		analisaEtapaVariaveis(arquivo, listaToken);
		analisaSubrotinas(arquivo, listaToken);
		analisaComandos(arquivo, listaToken);
	}

	private void analisaEtapaVariaveis(Arquivo arquivo, List<Token> listaToken) throws ErroSintaticoException,
			FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSemanticoException {
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Svar)) {
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				while (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
					geradorDeCodigo.gera("ALLOC");
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
		}
	}

	private void analisaVariaveis(Arquivo arquivo, List<Token> listaToken) throws ErroSintaticoException,
			FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSemanticoException {
		do {
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				if (!pesquisaDuplicidadeVariaveisTabela(tokenCorrente.getLexema())) {
					insereTabela(tokenCorrente.getLexema(), TipoTabelaSimboloEnum.VARIAVEL, null, null);
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
					throw new ErroSemanticoException("vari�vel com nome duplicado.");
				}
			} else {
				throw new ErroSintaticoException("nome de vari�vel n�o declarado.");
			}
		} while (!tokenCorrente.getSimbolo().equals(SimboloEnum.Sdoispontos));
		pegaToken(arquivo, listaToken);
		analisaTipo(arquivo, listaToken);
	}

	private void analisaTipo(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		if (!tokenCorrente.getSimbolo().equals(SimboloEnum.Sinteiro)
				&& !tokenCorrente.getSimbolo().equals(SimboloEnum.Sbooleano)) {
			throw new ErroSintaticoException("declara��o incorreta de vari�veis: Tipo errado de vari�vel.");
		}
		colocaTipoVariavel(tokenCorrente.getSimbolo());
		pegaToken(arquivo, listaToken);
	}

	private void analisaComandos(Arquivo arquivo, List<Token> listaToken)
			throws ErroSintaticoException, FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSemanticoException {
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
					throw new ErroSintaticoException("Ponto e v�rgula faltando.");
				}
			}
			pegaToken(arquivo, listaToken);
		} else {
			throw new ErroSintaticoException("declara��o incorreta de comandos.");
		}
	}

	private void analisaComandoSimples(Arquivo arquivo, List<Token> listaToken)
			throws ErroSintaticoException, FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSemanticoException {
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			if(pesquisaTabelaSimbolos(tokenCorrente.getLexema())==null) {
				throw new ErroSemanticoException("Identificador não declarado utilizado.");
			}
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
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Satribuicao)) {
			pegaToken(arquivo, listaToken);
			//TODO validar a expressão semanticamente
			analisaExpressao(arquivo, listaToken);
		} else {
			analisaChamadaProcedimento(arquivo, listaToken);
		}
	}

	private void analisaLeia(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sabre_parenteses)) {
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				if (pesquisaDeclaracaoVariavel(tokenCorrente.getLexema())) {
					pegaToken(arquivo, listaToken);
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sfecha_parenteses)) {
						pegaToken(arquivo, listaToken);
					} else {
						throw new ErroSintaticoException(") faltando.");
					} 
				} else {
					throw new ErroSemanticoException("Chamada de vari�vel n�o declarada");
				}
			} else {
				throw new ErroSintaticoException("Identificador do comando leia faltando.");
			}
		} else {
			throw new ErroSintaticoException("( faltando.");
		}
	}

	private void analisaEscreva(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sabre_parenteses)) {
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				if (pesquisaDeclaracaoVariavel(tokenCorrente.getLexema())) {
					pegaToken(arquivo, listaToken);
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sfecha_parenteses)) {
						pegaToken(arquivo, listaToken);
					} else {
						throw new ErroSintaticoException(") faltando.");
					} 
				}  else {
					throw new ErroSemanticoException("Chamada de vari�vel n�o declarada");
				}
			} else {
				throw new ErroSintaticoException("Identificador do comando escreva faltando.");
			}
		} else {
			throw new ErroSintaticoException("( faltando.");
		}
	}

	private void analisaEnquanto(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		pegaToken(arquivo, listaToken);
		analisaExpressao(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sfaca)) {
			pegaToken(arquivo, listaToken);
			analisaComandoSimples(arquivo, listaToken);
		} else {
			throw new ErroSintaticoException("Comando fa�a faltando.");
		}
	}

	private void analisaSe(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
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
			throw new ErroSintaticoException("Comando ent�o faltando.");
		}

	}

	private void analisaSubrotinas(Arquivo arquivo, List<Token> listaToken) throws FimInesperadoDoArquivoException,
			CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		/*
		 * int flag = 0; if
		 * (tokenCorrente.getSimbolo().equals(SimboloEnum.Sprocedimento) ||
		 * tokenCorrente.getSimbolo().equals(SimboloEnum.Sfuncao)) { implementa��o
		 * do semantico }
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
				throw new ErroSintaticoException("Ponto e v�rgula faltando.");
			}
		}

	}

	private void analisaDeclaracaoProcedimento(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException,
			ErroSemanticoException {
		pegaToken(arquivo, listaToken);
		NivelTabelaSimbolo nivel = NivelTabelaSimbolo.NOVO_GALHO;
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			if (!pesquisaDuplicidadeProcedimentoTabela(tokenCorrente.getLexema())) {
				insereTabela(tokenCorrente.getLexema(), TipoTabelaSimboloEnum.PROCEDIMENTO, nivel, null);
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
					analisaBloco(arquivo, listaToken);
				} else {
					throw new ErroSintaticoException("Ponto e v�rgula faltando.");
				}
			} else {
				throw new ErroSemanticoException("Nome de procedimento duplicado");
			}
		} else {
			throw new ErroSintaticoException("indentificador de procedimento faltando.");
		}
		desempilha();
	}

	private void analisaDeclaracaoDeFuncao(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException,
			ErroSemanticoException {
		pegaToken(arquivo, listaToken);
		NivelTabelaSimbolo nivel = NivelTabelaSimbolo.NOVO_GALHO;
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			if (!pesquisaDuplicidadeFuncaoTabela(tokenCorrente.getLexema())) {
				insereTabela(tokenCorrente.getLexema(), null, nivel, null);
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sdoispontos)) {
					pegaToken(arquivo, listaToken);
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sinteiro)
							|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sbooleano)) {
						colocaTipoFuncao(tokenCorrente.getSimbolo());
						pegaToken(arquivo, listaToken);
						if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
							analisaBloco(arquivo, listaToken);
						}
					} else {
						throw new ErroSintaticoException("tipo de fun��o faltando.");
					}
				} else {
					throw new ErroSintaticoException(": faltando.");
				}
			} else {
				throw new ErroSemanticoException("Nome de fun��o duplicado.");
			}
		} else {
			throw new ErroSintaticoException("indentificador de fun��o faltando.");
		}
		desempilha();
	}

	private void analisaExpressao(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
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
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Smais)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smenos)) {
			pegaToken(arquivo, listaToken);
		}
		analisaTermo(arquivo, listaToken);
		while (tokenCorrente.getSimbolo().equals(SimboloEnum.Smais)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smenos)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sou)) {
			pegaToken(arquivo, listaToken);
			analisaTermo(arquivo, listaToken);
		}
	}

	private void analisaTermo(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		analisaFator(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Smult) || tokenCorrente.getSimbolo().equals(SimboloEnum.Sdiv)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Se)) {
			pegaToken(arquivo, listaToken);
			analisaFator(arquivo, listaToken);
		}

	}

	private void analisaFator(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			Simbolo pesquisaTabelaSimbolos = pesquisaTabelaSimbolos(tokenCorrente.getLexema());
			if (null != pesquisaTabelaSimbolos) {
				if (TipoTabelaSimboloEnum.FUNCAO_INTEIRO.equals(pesquisaTabelaSimbolos.getTipo()) 
						|| TipoTabelaSimboloEnum.FUNCAO_BOOLEANO.equals(pesquisaTabelaSimbolos.getTipo())) {
					analisaChamadaFuncao(arquivo, listaToken);
				} else {
					pegaToken(arquivo, listaToken);
				}
			}else {
				throw new ErroSemanticoException("Identificador utilizado n�o declarado");
			}
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

	private void insereTabela(String lexema, TipoTabelaSimboloEnum tipo, NivelTabelaSimbolo nivel, Integer rotulo) {
		analisadorSemantico.insereTabela(lexema, tipo, nivel, rotulo);
	}

	private boolean pesquisaDuplicidadeVariaveisTabela(String lexema) {
		return analisadorSemantico.pesquisaDuplicidadeVariaveisTabela(lexema);
	}

	private void colocaTipoVariavel(SimboloEnum simbolo) {
		TipoVariavelTabelaSimbolosEnum tipoVariavel;
		if (SimboloEnum.Sinteiro.equals(simbolo)) {
			tipoVariavel = TipoVariavelTabelaSimbolosEnum.INTEIRO;
		} else {
			tipoVariavel = TipoVariavelTabelaSimbolosEnum.BOOLEANO;
		}
		analisadorSemantico.colocaTipoTabela(tipoVariavel);
	}
    
	private boolean pesquisaDeclaracaoVariavel(String lexema) {
		return analisadorSemantico.pesquisaDeclaracaoVariavel(lexema);
	}
	
	private boolean pesquisaDuplicidadeProcedimentoTabela(String lexema) {
		return analisadorSemantico.pesquisaDuplicidadeProcedimentoTabela(lexema);
	}

	private boolean pesquisaDuplicidadeFuncaoTabela(String lexema) {
		return analisadorSemantico.pesquisaDuplicidadeFuncaoTabela(lexema);
	}
	
	private void colocaTipoFuncao(SimboloEnum simbolo) {
		TipoTabelaSimboloEnum tipo;
		if (SimboloEnum.Sinteiro.equals(simbolo)) {
			tipo = TipoTabelaSimboloEnum.FUNCAO_INTEIRO;
		} else {
			tipo = TipoTabelaSimboloEnum.FUNCAO_BOOLEANO;
		}
		analisadorSemantico.colocaTipoFuncao(tipo);
	}
	
	private void desempilha() {
		analisadorSemantico.desempilha();
	}
	
	public Simbolo pesquisaTabelaSimbolos(String lexema) {
		return analisadorSemantico.pesquisaTabelaSimbolos(lexema);
	}

}
