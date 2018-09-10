package entidades;

public class Arquivo {
	private int indiceCorrente;
	private int tamanho;
	private int linhaCorrente;
	private String arquivo;
	
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
	
	public char lerCaractere() {
		int indiceRequisitado = getIndiceCorrente();
		incrementaIndice();
		return this.arquivo.charAt(indiceRequisitado);
	}
	
	public boolean fimDoArquivo() {
		if(indiceCorrente>=tamanho) {
			return true;
		}
		return false;
	}
}
