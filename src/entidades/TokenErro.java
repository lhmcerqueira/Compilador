package entidades;

import enums.SimboloEnum;

public class TokenErro extends Token {
	private String erro;
	public TokenErro(SimboloEnum simbolo, String lexema,String erro) {
		super(simbolo, lexema);
		this.erro = erro;
	}
	public String getErro() {
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}
	
}
