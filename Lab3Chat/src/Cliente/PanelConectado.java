package Cliente;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class PanelConectado extends JTabbedPane {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelContactos;
	private 

	public PanelConectado(InterfazCliente interfazCliente) {
		panelContactos = new JPanel();
		addTab("Contactos", panelContactos);
		
	}

}
