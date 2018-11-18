package entidades;

import java.util.ArrayList;
import java.util.List;

public class TabelaDeSimbolos {
	private List<Simbolo> tabela;

	public TabelaDeSimbolos() {
		super();
		this.tabela = new ArrayList<>();
	}

	public List<Simbolo> getTabela() {
		return tabela;
	}

	public void setTabela(List<Simbolo> tabela) {
		this.tabela = tabela;
	}

	public void insere(Simbolo simbolo) {
		this.tabela.add(simbolo);
	}

	public void remove() {
		if (tabela.size() > 0) {
			this.tabela.remove(tabela.size()-1);
		}
	}
}
