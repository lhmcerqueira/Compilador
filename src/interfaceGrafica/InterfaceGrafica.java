package interfaceGrafica;

import java.io.FileNotFoundException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import utils.AbridorDeArquivos;

public class InterfaceGrafica {

	protected Shell shell;
	private Text text;
	private Button btnAbrirCdigoFnte;
	

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
		shell.setSize(531, 373);
		shell.setText("SWT Application");
		
		text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setBounds(10, 10, 257, 186);
		
		Button btnCompilar = new Button(shell, SWT.NONE);
		btnCompilar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCompilar.setBounds(10, 202, 75, 25);
		btnCompilar.setText("Compilar");
		
		btnAbrirCdigoFnte = new Button(shell, SWT.NONE);
		btnAbrirCdigoFnte.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AbridorDeArquivos abridorDeArquivos = new AbridorDeArquivos();
				 try {
					abridorDeArquivos.abrirArquivo();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnAbrirCdigoFnte.setBounds(273, 10, 110, 25);
		btnAbrirCdigoFnte.setText("Abrir c\u00F3digo fonte");

	}
}
