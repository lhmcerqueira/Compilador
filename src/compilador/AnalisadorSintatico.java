package compilador;

import java.util.List;

import entidades.Arquivo;
import entidades.ElementoPosfixa;
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
import utils.ConversorPosfixa;

public class AnalisadorSintatico {

	private AnalisadorLexico analisadorLexico;
	private AnalisadorSemantico analisadorSemantico;
	private GeradorDeCodigo geradorDeCodigo;
	private Token tokenCorrente;
	private int auxAlloc1;
	private int alloc;
	private int rotulo;
	private ConversorPosfixa conversorPisfixa;
	public AnalisadorSintatico() {
		this.analisadorLexico = new AnalisadorLexico();
		this.analisadorSemantico = new AnalisadorSemantico();
		this.geradorDeCodigo = new GeradorDeCodigo();
		this.auxAlloc1 = 1;
		this.alloc = 1;
		this.rotulo = 1;
		conversorPisfixa = new ConversorPosfixa();

	}

	public boolean analisaSintatico(Arquivo arquivo, List<Token> listaToken) throws FimInesperadoDoArquivoException,
			CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException, GeradorDeCodigoException {
		//armazenar� o nome para gravar o arquivo gerado
		String nomeDoArquivo;
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sprograma)) {
			geradorDeCodigo.gera(" START");
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				nomeDoArquivo = tokenCorrente.getLexema();
				//insere na tabela de simbolos
				insereTabela(tokenCorrente.getLexema(), TipoTabelaSimboloEnum.NOME_DE_PROGRAMA,
						NivelTabelaSimbolo.MARCA, null);
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
					analisaBloco(arquivo, listaToken);
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto)) {
						int variaveis = contaVariavelParaDalloc();
						auxAlloc1-=variaveis;
						geradorDeCodigo.gera(" DALLOC "+auxAlloc1+","+variaveis);
						geradorDeCodigo.gera(" HLT");
						//Gera o c�digo final
						geradorDeCodigo.escreveEmArquivo(nomeDoArquivo);
					//	try {
							// TODO ponto de aten��o ao final do arquivo.
							pegaToken(arquivo, listaToken);
						//} catch (Exception e) {
						//	return true;
					//	}
						if (arquivo.fimDoArquivo()) {
							return true;
						} else {
							throw new ErroSintaticoException("Final do Arquivo n�o encontrado.");
						}
					} else {
						throw new ErroSintaticoException("Ponto final faltando.");
					}
				} else {
					throw new ErroSintaticoException("Ponto e vírgula faltando.");
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
					analisaVariaveis(arquivo, listaToken);
					if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
						pegaToken(arquivo, listaToken);
					} else {
						throw new ErroSintaticoException("Ponto e vírgula faltando.");
					}
				}
			} else {
				throw new ErroSintaticoException("Identificador de vari�vel n�o encontrado");
			}
		}
	}

	private void analisaVariaveis(Arquivo arquivo, List<Token> listaToken) throws ErroSintaticoException,
			FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSemanticoException {
		int auxAlloc2 = 0;
		do {
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				if (!pesquisaDuplicidadeVariaveisTabela(tokenCorrente.getLexema())) {
					//insere vari�vel na tabela de simbolos
					insereTabela(tokenCorrente.getLexema(), TipoTabelaSimboloEnum.VARIAVEL, null, new Integer(alloc));
					System.out.println(tokenCorrente.getLexema()+","+alloc);
					//incrementa o auxiliar para a gera��o de c�digo
					alloc++;
					auxAlloc2++;
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
		// ponto de alloc
		geradorDeCodigo.gera(" ALLOC "+auxAlloc1+","+auxAlloc2);
		auxAlloc1+=auxAlloc2;
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
					throw new ErroSintaticoException("Ponto e vírgula faltando.");
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
		Token tokenAux = tokenCorrente;
		conversorPisfixa = new ConversorPosfixa();
		conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente,null, 0));
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Satribuicao)) {
			conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 1));

			pegaToken(arquivo, listaToken);
			//TODO gerar codigo e validar se é do tipo esperado
			
			analisaExpressao(arquivo, listaToken);
			System.out.println(conversorPisfixa.getExpressao());
			geradorDeCodigo.geraPosfixaAtribuicao(conversorPisfixa.getOrdenacaoPosfixa(), analisadorSemantico);
		} else {
			analisaChamadaProcedimento(arquivo, listaToken,tokenAux);
		}
	}

	private void analisaLeia(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		pegaToken(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sabre_parenteses)) {
			pegaToken(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
				Simbolo simbolo = pesquisaDeclaracaoVariavel(tokenCorrente.getLexema());
				if (null!=simbolo) {
					geradorDeCodigo.gera(" RD");
					geradorDeCodigo.gera(" STR "+simbolo.getRotulo().intValue());
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
				Simbolo simbolo = pesquisaDeclaracaoVariavel(tokenCorrente.getLexema());
				if (null!=simbolo) {
					geradorDeCodigo.gera(" LDV "+simbolo.getRotulo().intValue());
					geradorDeCodigo.gera(" PRN");
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
		int auxRot1 = rotulo;
		int auxRot2;
		geradorDeCodigo.gera("L"+rotulo+" NULL");
		rotulo++;
		pegaToken(arquivo, listaToken);
		conversorPisfixa = new ConversorPosfixa();
		analisaExpressao(arquivo, listaToken);
		System.out.println(conversorPisfixa.getExpressao());
		geradorDeCodigo.geraPosfixaBooleano(conversorPisfixa.getOrdenacaoPosfixa(), analisadorSemantico);

		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sfaca)) {
			auxRot2 = rotulo;
			geradorDeCodigo.gera(" JMPF L"+rotulo);
			rotulo++;
			pegaToken(arquivo, listaToken);
			analisaComandoSimples(arquivo, listaToken);
			geradorDeCodigo.gera(" JMP L"+auxRot1);
			geradorDeCodigo.gera("L"+auxRot2+" NULL");
		} else {
			throw new ErroSintaticoException("Comando faça faltando.");
		}
	}

	private void analisaSe(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		int auxRot1 = rotulo;
		int auxRot2 = 0;
		pegaToken(arquivo, listaToken);
		conversorPisfixa = new ConversorPosfixa();
		analisaExpressao(arquivo, listaToken);
		System.out.println(conversorPisfixa.getExpressao());
		geradorDeCodigo.geraPosfixaBooleano(conversorPisfixa.getOrdenacaoPosfixa(), analisadorSemantico);

		//TODO validar se o retorno é boolean

		geradorDeCodigo.gera(" JMPF L"+rotulo);
		rotulo++;
		
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sentao)) {
			pegaToken(arquivo, listaToken);
			analisaComandoSimples(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Ssenao)) {
				geradorDeCodigo.gera(" JMP L"+rotulo);
				geradorDeCodigo.gera("L"+auxRot1+" NULL");
				auxRot2 = rotulo;
				rotulo++;
				pegaToken(arquivo, listaToken);
				analisaComandoSimples(arquivo, listaToken);
				geradorDeCodigo.gera("L"+auxRot2+" NULL");
			} else {
				geradorDeCodigo.gera("L"+auxRot1+" NULL");
			}
		} else {
			throw new ErroSintaticoException("Comando ent�o faltando.");
		}

	}

	private void analisaSubrotinas(Arquivo arquivo, List<Token> listaToken) throws FimInesperadoDoArquivoException,
			CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
	      int auxRot = 0;
		  int flag = 0; 
		  if(tokenCorrente.getSimbolo().equals(SimboloEnum.Sprocedimento) ||
		  tokenCorrente.getSimbolo().equals(SimboloEnum.Sfuncao)) { 
			  auxRot = rotulo;
			  geradorDeCodigo.gera(" JMP L"+rotulo);
			  rotulo++;
			  flag=1;
		  }
		 

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
		
		if(flag == 1) {
			  geradorDeCodigo.gera("L"+auxRot+" NULL");
		}
	}

	private void analisaDeclaracaoProcedimento(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException,
			ErroSemanticoException {
		Token tokenAux = new Token();
		pegaToken(arquivo, listaToken);
		NivelTabelaSimbolo nivel = NivelTabelaSimbolo.NOVO_GALHO;
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			if (!pesquisaDuplicidadeProcedimentoTabela(tokenCorrente.getLexema())) {
				tokenAux = tokenCorrente;
				insereTabela(tokenCorrente.getLexema(), TipoTabelaSimboloEnum.PROCEDIMENTO, nivel, new Integer(rotulo));
				geradorDeCodigo.gera("L"+rotulo+" NULL");
				rotulo++;
				pegaToken(arquivo, listaToken);
				if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sponto_virgula)) {
					analisaBloco(arquivo, listaToken);
				} else {
					throw new ErroSintaticoException("Ponto e vírgula faltando.");
				}
			} else {
				throw new ErroSemanticoException("Nome de procedimento duplicado");
			}
		} else {
			throw new ErroSintaticoException("indentificador de procedimento faltando.");
		}
		int auxDalloc = desempilha(tokenAux.getLexema());
		if (auxDalloc>0) {
			auxAlloc1 -= auxDalloc;
			alloc = auxAlloc1;
			geradorDeCodigo.gera(" DALLOC " + auxAlloc1 + "," + auxDalloc);
		}
		geradorDeCodigo.gera(" RETURN");
	}

	private void analisaDeclaracaoDeFuncao(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException,
			ErroSemanticoException {
		Token tokenAux = new Token();
		pegaToken(arquivo, listaToken);
		NivelTabelaSimbolo nivel = NivelTabelaSimbolo.NOVO_GALHO;
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sidentificador)) {
			if (!pesquisaDuplicidadeFuncaoTabela(tokenCorrente.getLexema())) {
				tokenAux = tokenCorrente;
				insereTabela(tokenCorrente.getLexema(), null, nivel, new Integer(rotulo));
				geradorDeCodigo.gera("L"+rotulo+" NULL");
				rotulo++;
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
		System.out.println("FUNÇÃO CORRENTE"+tokenAux.getLexema());
		int auxDalloc = desempilha(tokenAux.getLexema());
		if (auxDalloc>0) {
			auxAlloc1 -= auxDalloc;
			geradorDeCodigo.gera(" DALLOC " + auxAlloc1 + "," + auxDalloc);
		}
		geradorDeCodigo.gera(" RETURNF");
	}

	private void analisaExpressao(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {

		analisaExpressaoSimples(arquivo, listaToken);
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Smaior)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smaiorig)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smenor)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smenorig)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sdif)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sig)) {
			//TODO GERA COMANDO OPERADOR LÓGICO
			
			conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 3));
			pegaToken(arquivo, listaToken);
			analisaExpressaoSimples(arquivo, listaToken);
		}
	}

	private void analisaExpressaoSimples(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		if (tokenCorrente.getSimbolo().equals(SimboloEnum.Smais)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smenos)) {
			Token tokenAux = new Token();
			tokenAux.setLexema(tokenCorrente.getLexema());
			if(tokenCorrente.getSimbolo().equals(SimboloEnum.Smais)) {
				tokenAux.setSimbolo(SimboloEnum.Smais_sinal);
			} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Smenos)) {
				tokenAux.setSimbolo(SimboloEnum.Smenos_sinal);
			}
			//TODO GERA COMANDO MAIS MENOS
			conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenAux, null, 7));
			pegaToken(arquivo, listaToken);
		}
		analisaTermo(arquivo, listaToken);
		while (tokenCorrente.getSimbolo().equals(SimboloEnum.Smais)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Smenos)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sou)) {
			//TODO GERA COMANDO MAIS MENOS OU 
			if(tokenCorrente.getSimbolo().equals(SimboloEnum.Smais)){
				conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 4));

			} else if(tokenCorrente.getSimbolo().equals(SimboloEnum.Smenos)) {
				conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 5));

			}  else if(tokenCorrente.getSimbolo().equals(SimboloEnum.Sou)) {
				conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 1));

			}
			pegaToken(arquivo, listaToken);
			analisaTermo(arquivo, listaToken);
		}
	}

	private void analisaTermo(Arquivo arquivo, List<Token> listaToken)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException, ErroSemanticoException {
		analisaFator(arquivo, listaToken);
		while (tokenCorrente.getSimbolo().equals(SimboloEnum.Smult) || tokenCorrente.getSimbolo().equals(SimboloEnum.Sdiv)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Se)) {
			//TODO GERA COMANDO MULT DIV E
			if(tokenCorrente.getSimbolo().equals(SimboloEnum.Smult)) {
				conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 6));

			} if(tokenCorrente.getSimbolo().equals(SimboloEnum.Sdiv)) {
				conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 6));

			}else if(tokenCorrente.getSimbolo().equals(SimboloEnum.Se)) {
				conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 2));

			}
			
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
					//TODO GERA COMANDO PARA FUNÇÃO 
					conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, TipoTabelaSimboloEnum.FUNCAO, 0));
					analisaChamadaFuncao(arquivo, listaToken);
				} else {
					//TODO GERA COMANDO PARA VARIÁVEL
					conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, TipoTabelaSimboloEnum.VARIAVEL, 0));
					pegaToken(arquivo, listaToken);
				}
			}else {
				throw new ErroSemanticoException("Identificador utilizado n�o declarado");
			}
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Snumero)) {
			//TODO GERA CARREGA CONSTANTE
			conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 0));
			pegaToken(arquivo, listaToken);
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Snao)) {
			//TODO GERA NÃO
			conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 7));
			pegaToken(arquivo, listaToken);
			analisaFator(arquivo, listaToken);
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sabre_parenteses)) {
			//TODO TRATA ABRE PARENTESES
			conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, -1));
			pegaToken(arquivo, listaToken);
			analisaExpressao(arquivo, listaToken);
			if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sfecha_parenteses)) {
				//TODO FECHA PARENTESES
				conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, -2));
				pegaToken(arquivo, listaToken);
			} else {
				throw new ErroSintaticoException("Falha ao detectar fator");
			}
		} else if (tokenCorrente.getSimbolo().equals(SimboloEnum.Sverdadeiro)
				|| tokenCorrente.getSimbolo().equals(SimboloEnum.Sfalso)) {
			conversorPisfixa.constroiExpressao(new ElementoPosfixa(tokenCorrente, null, 0));
			pegaToken(arquivo, listaToken);
		}
	}

	public void analisaChamadaProcedimento(Arquivo arquivo, List<Token> listaToken, Token tokenAux)
			throws FimInesperadoDoArquivoException, CaractereNaoEsperadoEncontradoException, ErroSintaticoException {
		Simbolo procedimentoTabela = getProcedimentoTabela(tokenAux.getLexema());
		if (null!=procedimentoTabela) {
			geradorDeCodigo.gera(" CALL L" + procedimentoTabela.getRotulo().intValue());
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
    
	private Simbolo pesquisaDeclaracaoVariavel(String lexema) {
		return analisadorSemantico.pesquisaDeclaracaoVariavel(lexema);
	}
	
	private boolean pesquisaDuplicidadeProcedimentoTabela(String lexema) {
		return analisadorSemantico.pesquisaDuplicidadeProcedimentoTabela(lexema);
	}
	
	private Simbolo getProcedimentoTabela(String lexema) {
		return analisadorSemantico.getProcedimentoTabela(lexema);
	}
	
	private boolean pesquisaDuplicidadeFuncaoTabela(String lexema) {
		return analisadorSemantico.pesquisaDuplicidadeFuncaoTabela(lexema);
	}
	
	private Simbolo getFuncaoTabela(String lexema) {
		return analisadorSemantico.getFuncaoTabela(lexema);
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
	
	private int desempilha(String nomeFuncao) {
		return analisadorSemantico.desempilha(nomeFuncao);
	}
	
	private int contaVariavelParaDalloc() {
		return analisadorSemantico.contaVariavelParaDalloc();
	}
	
	public Simbolo pesquisaTabelaSimbolos(String lexema) {
		return analisadorSemantico.pesquisaTabelaSimbolos(lexema);
	}

}
