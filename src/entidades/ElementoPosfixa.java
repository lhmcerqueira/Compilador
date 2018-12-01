package entidades;

import enums.TipoTabelaSimboloEnum;

public class ElementoPosfixa {

	private Token token;
	private TipoTabelaSimboloEnum tipo;
	private int prioridade;
	
	public ElementoPosfixa(Token token, TipoTabelaSimboloEnum tipo, int prioridade) {
		super();
		this.token = token;
		this.tipo = tipo;
		this.prioridade = prioridade;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public TipoTabelaSimboloEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoTabelaSimboloEnum tipo) {
		this.tipo = tipo;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}
	
}
