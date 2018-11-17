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
	
	
}
