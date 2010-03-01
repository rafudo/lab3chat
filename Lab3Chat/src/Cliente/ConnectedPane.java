package Cliente;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;

public class ConnectedPane extends JTabbedPane {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelContactos;
	private JList lstContactos;
	private InterfazCliente interfaz;

	public ConnectedPane(InterfazCliente interfazCliente) {
		interfaz=interfazCliente;
		panelContactos = new JPanel();
		panelContactos.setLayout(new BorderLayout());
		addTab("Contactos", panelContactos);
		lstContactos = new JList();	
		lstContactos.setCellRenderer(new ContactsRenderer());
		lstContactos.setListData(interfaz.getCliente().getContacts());
		JScrollPane sp = new JScrollPane();
		sp.setViewportView(lstContactos);
		panelContactos.add(sp,BorderLayout.CENTER);
		
	}

}

class ContactsRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList list,
	         Object value,
	         int index,
	         boolean isSelected,
	         boolean cellHasFocus) {
		Contacto c = (Contacto) value;
		if(isSelected){
			setBorder(BorderFactory.createLineBorder(Color.BLUE));
		}else{
			setBorder(null);
		}
		if(c.isConnected())
		setIcon(new ImageIcon("./data/connected.png"));
		else
			setIcon(new ImageIcon("./data/disconnected.png"));
		setText(c.getUsername()+" - ["+c.getFrase()+"]");
		
		return this;
	}
	
}
