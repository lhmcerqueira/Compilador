package compilador;

import java.io.IOException;
import java.util.List;

import entidades.ElementoPosfixa;
import entidades.Simbolo;
import enums.SimboloEnum;
import enums.TipoTabelaSimboloEnum;
import exceptions.GeradorDeCodigoException;
import utils.GerenciadorDeArquivos;

public class GeradorDeCodigo {

	private StringBuffer codigo;
	private GerenciadorDeArquivos gerenciador;
	
	public GeradorDeCodigo() {
		super();
		this.codigo = new StringBuffer();
		this.gerenciador =  new GerenciadorDeArquivos();
	}

	public void gera(String comando) {
		codigo.append(comando).append(System.getProperty("line.separator"));
	}
	
	public void geraPosfixaAtribuicao(List<ElementoPosfixa> ordenacaoPosfixa, AnalisadorSemantico analisadorSemantico) {
		for (int i = 1; i<= ordenacaoPosfixa.size()-1; i++) {
			if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sidentificador)) {
				Simbolo simbolo = analisadorSemantico.getFuncaoTabela(ordenacaoPosfixa.get(i).getToken().getLexema());
				if(null!=simbolo) {
					gera(" CALL L"+simbolo.getRotulo().intValue());
				} else {
					simbolo = analisadorSemantico.pesquisaDeclaracaoVariavel(ordenacaoPosfixa.get(i).getToken().getLexema());
					gera(" LDV "+simbolo.getRotulo().intValue());
				}
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Snumero)) {
				gera(" LDC "+ordenacaoPosfixa.get(i).getToken().getLexema());
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smais)) {
				gera(" ADD");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smenos)) {
				gera(" SUB");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smult)) {
				gera(" MULT");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sdiv)) {
				gera(" DIVI");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smais_sinal)) {
				
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smenos_sinal)) {
				gera(" INV");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Se)) {
				gera(" AND");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sou)) {
				gera(" OR");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Snao)) {
				gera(" NEG");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smaior)) {
				gera(" CMA");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smaiorig)) {
				gera(" CMAQ");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smenor)) {
				gera(" CME");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smenorig)) {
				gera(" CMEQ");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sig)) {
				gera(" CEQ");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sdif)) {
				gera(" CDIF");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sverdadeiro)) {
				gera(" LDC 1");
			} else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sfalso)) {
				gera(" LDC 0");
			}
			else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Satribuicao)) {
				Simbolo simbolo = analisadorSemantico.getFuncaoTabela(ordenacaoPosfixa.get(0).getToken().getLexema());
				if(null!=simbolo) {
					
				} else {
					simbolo = analisadorSemantico.pesquisaDeclaracaoVariavel(ordenacaoPosfixa.get(0).getToken().getLexema());
					gera(" STR "+simbolo.getRotulo().intValue());
				}
			}
		}
	}
	
	public void geraPosfixaBooleano(List<ElementoPosfixa> ordenacaoPosfixa, AnalisadorSemantico analisadorSemantico) {
		for (int i = 0; i<= ordenacaoPosfixa.size()-1; i++) {
			if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sidentificador)) {
				Simbolo simbolo = analisadorSemantico.getFuncaoTabela(ordenacaoPosfixa.get(i).getToken().getLexema());
				if(null!=simbolo) {
					gera(" CALL L"+simbolo.getRotulo().intValue());
					
				} else {
					simbolo = analisadorSemantico.pesquisaDeclaracaoVariavel(ordenacaoPosfixa.get(i).getToken().getLexema());
					gera(" LDV "+simbolo.getRotulo().intValue());
				}
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Snumero)) {
				gera(" LDC "+ordenacaoPosfixa.get(i).getToken().getLexema());
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smais)) {
				gera(" ADD");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smenos)) {
				gera(" SUB");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smult)) {
				gera(" MULT");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sdiv)) {
				gera(" DIVI");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smais_sinal)) {
				
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smenos_sinal)) {
				gera(" INV");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Se)) {
				gera(" AND");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sou)) {
				gera(" OR");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Snao)) {
				gera(" NEG");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smaior)) {
				gera(" CMA");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smaiorig)) {
				gera(" CMAQ");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smenor)) {
				gera(" CME");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Smenorig)) {
				gera(" CMEQ");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sig)) {
				gera(" CEQ");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sdif)) {
				gera(" CDIF");
			}else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sverdadeiro)) {
				gera(" LDC 1");
			} else if(ordenacaoPosfixa.get(i).getToken().getSimbolo().equals(SimboloEnum.Sfalso)) {
				gera(" LDC 0");
			}
		}
	}
	
	public void escreveEmArquivo(String nomeDoArquivo) throws GeradorDeCodigoException {
		try {
			gerenciador.salvarCodigoGerado(codigo.toString(),nomeDoArquivo);
		} catch (IOException e) {
			throw new GeradorDeCodigoException("não foi possivel gravar no arquivo");
		}
	}
}
