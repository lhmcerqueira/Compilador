package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import entidades.Arquivo;

public class AbridorDeArquivos {

	/**
	 * Metodo que abre o arquivo selecionado, ele usa o componente
	 * JfileChooser e armazena o que foi lido no atributo String
	 * arquivo
	 * @throws FileNotFoundException
	 */
	public String abrirArquivo() throws FileNotFoundException {
		JFileChooser  jFileChooser = new JFileChooser();
		StringBuilder strbldr = new StringBuilder();
		
		//Assim que o arquivo é escolhido o leitor começa a armazenar as linhas.
		if(jFileChooser.showOpenDialog(null) == jFileChooser.APPROVE_OPTION) {
			File file = jFileChooser.getSelectedFile();
			Scanner input = new Scanner(file);
			while(input.hasNext()) {
				//linhas sendo armazendas
				strbldr = strbldr.append(input.nextLine()).append('\n');
			}
			
			input.close();	
		} else {
			strbldr.append("Nenhum arquivo selecionado.");
		}
		return strbldr.toString();	
	}
}
