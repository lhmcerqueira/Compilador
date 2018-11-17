package entidades;

import enums.NivelTabelaSimbolo;
import enums.TipoTabelaSimboloEnum;

public class Simbolo {
	private String lexema;
	private TipoTabelaSimboloEnum tipo;
	private NivelTabelaSimbolo nivel;
	private int rotulo;

	public Simbolo(String lexema, TipoTabelaSimboloEnum tipo, NivelTabelaSimbolo nivel, int rotulo) {
		super();
		this.lexema = lexema;
		this.tipo = tipo;
		this.nivel = nivel;
		this.rotulo = rotulo;
	}

	public Simbolo(String lexema, TipoTabelaSimboloEnum tipo) {
		super();
		this.lexema = lexema;
		this.tipo = tipo;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	public TipoTabelaSimboloEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoTabelaSimboloEnum tipo) {
		this.tipo = tipo;
	}

	public NivelTabelaSimbolo getNivel() {
		return nivel;
	}

	public void setNivel(NivelTabelaSimbolo nivel) {
		this.nivel = nivel;
	}

	public int getRotulo() {
		return rotulo;
	}

	public void setRotulo(int rotulo) {
		this.rotulo = rotulo;
	}
	
	
}
