package enums;

public enum SimboloEnum {
	Snumero("Snumero"),
	Sprograma("Sprograma"),
	Sse("Sse"),
	Sentao("Sentao"),
	Ssenao("Ssenao"),
	Senquanto("Senquanto"),
	Sfaca("Sfaca"),
	Sinicio("Sinicio"),
	Sfim("Sfim"),
	Sescreva("Sescreva"),
	Sleia("Sleia"),
	Svar("Svar"),
	Sinteiro("Sinteiro"),
	Sbooleano("Sbooleano"),
	Sverdadeiro("Sverdadeiro"),
	Sfalso("Sfalso"),
	Sprocedimento("Sprocedimento"),
	Sfuncao("Sfuncao"),
	Sdiv("Sdiv"),
	Se("Se"),
	Sou("Sou"),
	Snao("Snao"),
	Sidentificador("Sidentificador"),
	Satribuicao("Satribuicao"),
	Sdoispontos("Sdoispontos"),
	Smais("Smais"),
	Smenos("Smenos"),
	Smult("Smult"),
	Smenorig("Smenorig"),
	Smenor("Smenor"),
	Smaiorig("Smaiorig"),
	Smaior("Smaior"),
	Sdif("Sdif"),
	Sig("Sig"),
	Sponto("Sponto"),
	Sponto_virgula("Sponto_virgula"),
	Svirgula("Svirgula"),
	Serro("Serro");
	private String simbolo;
	
	private SimboloEnum(String simbolo) {
		this.simbolo = simbolo;
	}
	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}


}
