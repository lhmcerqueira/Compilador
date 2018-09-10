package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import entidades.Arquivo;

public class AbridorDeArquivos {

	/*
	 * String que contem o arquivo lido e separado por linhas
	 */
	private Arquivo arquivo;
	
	public AbridorDeArquivos() {
		this.arquivo = new Arquivo();
	}
	
	public Arquivo getArquivo() {
		return arquivo;
	}

	
	/**
	 * Metodo que abre o arquivo selecionado, ele usa o componente
	 * JfileChooser e armazena o que foi lido no atributo String
	 * arquivo
	 * @throws FileNotFoundException
	 */
	public Arquivo abrirArquivo() throws FileNotFoundException {
		JFileChooser  jFileChooser = new JFileChooser();
		StringBuilder strbldr = new StringBuilder();
		
		//Assim que o arquivo é escolhido o leitor começa a armazenar as linhas.
		if(jFileChooser.showOpenDialog(null) == jFileChooser.APPROVE_OPTION) {
			File file = jFileChooser.getSelectedFile();
			Scanner input = new Scanner(file);
			while(input.hasNext()) {
				//linhas sendo armazendas
				strbldr = strbldr.append(input.nextLine());
			}
			arquivo.setArquivo(strbldr.toString());
			arquivo.setIndiceCorrente(0);
			arquivo.setTamanho(strbldr.toString().length());
			arquivo.setLinhaCorrente(0);
			input.close();	
		} else {
			arquivo.setArquivo("Nenhum arquivo selecionado.");
		}
		return arquivo;	
	}
}
