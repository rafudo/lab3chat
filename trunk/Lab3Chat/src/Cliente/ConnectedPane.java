package Cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

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
	private JLabel usernameLab;
	private JLabel nickLab;

	public ConnectedPane(InterfazCliente interfazCliente) {
		interfaz = interfazCliente;
		panelContactos = new JPanel();
		panelContactos.setLayout(new BorderLayout());
		usernameLab = new JLabel(interfaz.getCliente().getUsername());
		usernameLab.setFont(new Font("Arial", Font.BOLD, 20));
		JPanel panelNorte = new JPanel();
		panelNorte.setLayout(new FlowLayout());
		nickLab = new JLabel(interfaz.getCliente().getFrase());
		nickLab.setFont(new Font("Arial", Font.ITALIC, 10));
		panelNorte.add(usernameLab);
		panelNorte.add(nickLab);
		addTab("Contactos", panelContactos);
		lstContactos = new JList();
		lstContactos.setCellRenderer(new ContactsRenderer());
		lstContactos.setListData(interfaz.getCliente().getContacts());
		JScrollPane sp = new JScrollPane();
		sp.setViewportView(lstContactos);
		panelContactos.add(sp, BorderLayout.CENTER);
		panelContactos.add(panelNorte, BorderLayout.NORTH);

	}

}

class ContactsRenderer extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Contacto c = (Contacto) value;
		if (isSelected) {
			setBorder(BorderFactory.createLineBorder(Color.BLUE));
		} else {
			setBorder(null);
		}
		if (c.isConnected())
			setIcon(new ImageIcon("./data/connected.png"));
		else
			setIcon(new ImageIcon("./data/disconnected.png"));
		setText(c.getUsername() + " - [" + c.getFrase() + "]");

		return this;
	}

}
