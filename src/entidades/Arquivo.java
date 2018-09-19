package entidades;

import exceptions.FimInesperadoDoArquivoException;

public class Arquivo {
	private char caractereCorrente;
	private int indiceCorrente;
	private int tamanho;
	private int linhaCorrente;
	private String arquivo;
	
	public Arquivo(char caractereCorrente, int indiceCorrente, int tamanho, int linhaCorrente, String arquivo) {
		super();
		this.caractereCorrente = caractereCorrente;
		this.indiceCorrente = indiceCorrente;
		this.tamanho = tamanho;
		this.linhaCorrente = linhaCorrente;
		this.arquivo = arquivo;
	}

	public char getCaractereCorrente() {
		return this.caractereCorrente;
	}
	
	public int getIndiceCorrente() {
		return indiceCorrente;
	}
	public void setIndiceCorrente(int indiceCorrente) {
		this.indiceCorrente = indiceCorrente;
	}
	public int getTamanho() {
		return tamanho;
	}
	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
	public int getLinhaCorrente() {
		return linhaCorrente;
	}
	public void setLinhaCorrente(int linhaCorrente) {
		this.linhaCorrente = linhaCorrente;
	}
	public String getArquivo() {
		return arquivo;
	}
	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public void incrementaIndice() {
		this.indiceCorrente++;
	}

	public void incrementaLinha() {
		this.linhaCorrente++;
	}
	
	public void lerCaractere() {
		int indiceRequisitado = getIndiceCorrente();
		incrementaIndice();
		try {
			this.caractereCorrente = this.arquivo.charAt(indiceRequisitado);
		}catch (StringIndexOutOfBoundsException e) {
			this.caractereCorrente = '\0';
		}
	}
	
	public boolean fimDoArquivo() {
		if(indiceCorrente>=tamanho) {
			return true;
		}
		return false;
	}
}
