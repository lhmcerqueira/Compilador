package compilador;

import entidades.TabelaDeSimbolos;

import java.util.List;

import entidades.Simbolo;
import enums.NivelTabelaSimbolo;
import enums.TipoTabelaSimboloEnum;
import enums.TipoVariavelTabelaSimbolosEnum;

public class AnalisadorSemantico {
	private TabelaDeSimbolos tabelaDeSimbolos;

	public AnalisadorSemantico() {
		this.tabelaDeSimbolos = new TabelaDeSimbolos();
	}

	public void insereTabela(String lexema, TipoTabelaSimboloEnum tipo, NivelTabelaSimbolo nivel, Integer rotulo) {
		tabelaDeSimbolos.insere(new Simbolo(lexema, tipo, nivel, rotulo));
	}

	public boolean pesquisaDuplicidadeVariaveisTabela(String lexema) {
		List<Simbolo> tabela = tabelaDeSimbolos.getTabela();
		for (int i = tabela.size(); i > 0; i--) {
			if (!NivelTabelaSimbolo.NOVO_GALHO.equals(tabela.get(i - 1).getNivel())) {
				if (TipoTabelaSimboloEnum.VARIAVEL.equals(tabela.get(i - 1).getTipo())
						&& tabela.get(i - 1).getLexema().equals(lexema)) {
					return true;
				}
			} else {
				break;
			}
		}
		return false;
	}

	public boolean pesquisaDuplicidadeProcedimentoTabela(String lexema) {
		List<Simbolo> tabela = tabelaDeSimbolos.getTabela();
		for (int i = tabela.size(); i > 0; i--) {
			if (TipoTabelaSimboloEnum.PROCEDIMENTO.equals(tabela.get(i - 1).getTipo())
					&& tabela.get(i - 1).getLexema().equals(lexema)) {
				return true;
			}
		}
		return false;
	}

	public boolean pesquisaDuplicidadeFuncaoTabela(String lexema) {
		List<Simbolo> tabela = tabelaDeSimbolos.getTabela();
		for (int i = tabela.size(); i > 0; i--) {
			if ((TipoTabelaSimboloEnum.FUNCAO_INTEIRO.equals(tabela.get(i - 1).getTipo())
					|| TipoTabelaSimboloEnum.FUNCAO_BOOLEANO.equals(tabela.get(i - 1).getTipo()))
					&& tabela.get(i - 1).getLexema().equals(lexema)) {
				return true;
			}
		}
		return false;
	}

	public int desempilha() {
		List<Simbolo> tabela = tabelaDeSimbolos.getTabela();
		int desempilhados = 0;
		for (int i = tabela.size(); i > 0; i--) {
			if (!NivelTabelaSimbolo.NOVO_GALHO.equals(tabela.get(i - 1).getNivel())) {
				if (TipoTabelaSimboloEnum.VARIAVEL.equals(tabela.get(i - 1).getTipo())) {
					tabelaDeSimbolos.getTabela().remove(i - 1);
					desempilhados++;
				}
			} else {
				break;
			}
		}
		return desempilhados;
	}

	public int contaVariavelParaDalloc() {
		List<Simbolo> tabela = tabelaDeSimbolos.getTabela();
		int encontradas = 0;
		for (int i = tabela.size(); i > 0; i--) {
			if (!TipoTabelaSimboloEnum.NOME_DE_PROGRAMA.equals(tabela.get(i - 1).getTipo())) {
				if (TipoTabelaSimboloEnum.VARIAVEL.equals(tabela.get(i - 1).getTipo())) {
					encontradas++;
				}
			} else {
				break;
			}
		}
		return encontradas;
	}
	
	public void colocaTipoTabela(TipoVariavelTabelaSimbolosEnum tipoVariavel) {
		List<Simbolo> tabela = tabelaDeSimbolos.getTabela();
		for (int i = tabela.size(); i > 0; i--) {
			if (!NivelTabelaSimbolo.NOVO_GALHO.equals(tabela.get(i - 1).getNivel())
					&& null == tabela.get(i - 1).getTipoVariavel()) {
				if (TipoTabelaSimboloEnum.VARIAVEL.equals(tabela.get(i - 1).getTipo())) {
					tabela.get(i - 1).setTipoVariavel(tipoVariavel);
				}
			} else {
				break;
			}
		}
	}

	public void colocaTipoFuncao(TipoTabelaSimboloEnum tipo) {
		List<Simbolo> tabela = tabelaDeSimbolos.getTabela();
		for (int i = tabela.size(); i > 0; i--) {
			if (null == tabela.get(i - 1).getTipo()) {
				tabela.get(i - 1).setTipo(tipo);
				break;
			}
		}
	}

	public Simbolo pesquisaDeclaracaoVariavel(String lexema) {
		List<Simbolo> tabela = tabelaDeSimbolos.getTabela();
		for (int i = tabela.size(); i > 0; i--) {
			if (TipoTabelaSimboloEnum.VARIAVEL.equals(tabela.get(i - 1).getTipo())
					&& tabela.get(i - 1).getLexema().equals(lexema)) {
				return tabela.get(i - 1);
			}
		}
		return null;
	}
	
	public Simbolo pesquisaTabelaSimbolos(String lexema) {
		List<Simbolo> tabela = tabelaDeSimbolos.getTabela();
		for (int i = tabela.size(); i > 0; i--) {
			if (tabela.get(i - 1).getLexema().equals(lexema)) {
				return  tabela.get(i - 1);
			}
		}
		return null;
	}
}
