package interfaceGrafica;

import java.io.FileNotFoundException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import compilador.Compilador;
import entidades.Arquivo;
import entidades.Token;
import utils.AbridorDeArquivos;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;

public class InterfaceGrafica {

	protected Shell shell;
	private Text text;
	private Button btnAbrirCdigoFnte;
	Arquivo arquivo;
	private Table tabelaToken;
	private Label lblListaDeTokens;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			InterfaceGrafica window = new InterfaceGrafica();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(797, 573);
		shell.setText("SWT Application");
		
		text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setBounds(10, 10, 535, 346);
		
		tabelaToken = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		tabelaToken.setBounds(10, 411, 146, 113);
		tabelaToken.setHeaderVisible(true);
		tabelaToken.setLinesVisible(true);
		
		lblListaDeTokens = new Label(shell, SWT.NONE);
		lblListaDeTokens.setBounds(10, 390, 85, 15);
		lblListaDeTokens.setText("Lista de Tokens");
		String[] legendaTabelaToken = {"Lexema","Simbolo"};
		for (int i = 0; i < legendaTabelaToken.length; i++) {
		    TableColumn column = new TableColumn(tabelaToken, SWT.NULL);
		    column.setText(legendaTabelaToken[i]);
		}
		
		Button btnCompilar = new Button(shell, SWT.NONE);
		btnCompilar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				arquivo = new Arquivo(0,text.getText().length(),0,text.getText());
				Compilador compilador = new Compilador();
				compilador.compila(arquivo);
				for(Token token : compilador.getListaToken()) {
					TableItem item = new TableItem(tabelaToken, SWT.NULL);
					item.setText(0,token.getLexema());
					item.setText(1, token.getSimbolo().getSimbolo());
				}
				for (int i = 0; i < legendaTabelaToken.length; i++) {
					tabelaToken.getColumn(i).pack();
				}
			}
		});
		btnCompilar.setBounds(10, 362, 75, 25);
		btnCompilar.setText("Compilar");
		
		btnAbrirCdigoFnte = new Button(shell, SWT.NONE);
		btnAbrirCdigoFnte.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AbridorDeArquivos abridorDeArquivos = new AbridorDeArquivos();
				 try {
					text.setText(abridorDeArquivos.abrirArquivo());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnAbrirCdigoFnte.setBounds(551, 8, 110, 25);
		btnAbrirCdigoFnte.setText("Abrir c\u00F3digo fonte");
		
	

	}
}
