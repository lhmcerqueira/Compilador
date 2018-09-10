package entidades;

import enums.SimboloEnum;

public class Token {
	private SimboloEnum simbolo;
	private String lexema;
	
	public Token(SimboloEnum simbolo, String lexema) {
		super();
		this.simbolo = simbolo;
		this.lexema = lexema;
	}
	
	public Token() {
		super();
	}
	
	public SimboloEnum getSimbolo() {
		return simbolo;
	}
	public void setSimbolo(SimboloEnum simbolo) {
		this.simbolo = simbolo;
	} 
	public String getLexema() {
		return lexema;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	
}
