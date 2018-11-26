package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import entidades.Arquivo;

public class GerenciadorDeArquivos {

	/**
	 * Metodo que abre o arquivo selecionado, ele usa o componente
	 * JfileChooser e armazena o que foi lido no atributo String
	 * arquivo
	 * @throws FileNotFoundException
	 */
	public String abrirArquivo() throws FileNotFoundException {
		JFileChooser  jFileChooser = new JFileChooser();
		StringBuilder strbldr = new StringBuilder();
		
		//Assim que o arquivo � escolhido o leitor come�a a armazenar as linhas.
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
	
	public void salvarArquivo(String arquivo) throws IOException {
		JFileChooser  jFileChooser = new JFileChooser();
		if(jFileChooser.showSaveDialog(null) == jFileChooser.APPROVE_OPTION) {
			   FileWriter fw = new FileWriter(jFileChooser.getSelectedFile());
	            fw.write(arquivo);
	            fw.close();
		}
	}
	
	public void salvarCodigoGerado(String codigo, String nomeDoArquivo) throws IOException {
			   //Caminho no linux
				FileWriter fw = new FileWriter("/home/luizmc/Documents/Compilador/src/codigoGerado/"+nomeDoArquivo+".txt");
	          //Caminho no windows
				//FileWriter fw = new FileWriter("C:\\Users\\luizh\\eclipse-photom-workspace\\Compilador\\src\\codigoGerado\\"+nomeDoArquivo+".txt");
				fw.write(codigo);
	            fw.close();
	}
}
