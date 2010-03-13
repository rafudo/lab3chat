package Cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import Servidor.Grupo;

public class ConnectedPane extends JTabbedPane implements Observer,
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelContactos;
	private JPanel panelGrupos;
	private JList lstContactos;
	private InterfazCliente interfaz;
	private Cliente cliente;
	private JLabel usernameLab;
	private JTextField nickLab;
	private JMenuItem itmCerrar;
	private JMenuItem itmCambiar;
	private JMenuItem itmAgregar;
	private JList lstGrupos;
	public ConnectedPane(InterfazCliente interfazCliente, Cliente client) {
		interfaz = interfazCliente;
		
		JMenu menuSesion = new JMenu("Sesión");
		itmCambiar = new JMenuItem("Cambiar contraseña");
		itmCambiar.addActionListener(this);
		menuSesion.add(itmCambiar);
		menuSesion.addSeparator();
		itmCerrar = new JMenuItem("Cerrar sesión");
		itmCerrar.addActionListener(this);
		menuSesion.add(itmCerrar);
		JMenu menuCon = new JMenu("Contactos");
		itmAgregar = new JMenuItem("Agregar contacto");
		itmAgregar.addActionListener(this);
		menuCon.add(itmAgregar);
		interfaz.getJMenuBar().removeAll();
		interfaz.getJMenuBar().add(menuSesion);
		interfaz.getJMenuBar().add(menuCon);
		this.cliente = client;
		panelContactos = new JPanel();
		panelContactos.setLayout(new BorderLayout());
		usernameLab = new JLabel(cliente.getUsername());
		usernameLab.setFont(new Font("Arial", Font.BOLD, 20));
		JPanel panelNorte = new JPanel();
		panelNorte.setLayout(new BorderLayout());
		panelNorte.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		nickLab = new JTextField(cliente.getFrase());
		nickLab.setBackground(usernameLab.getBackground());
		nickLab.setFont(new Font("Arial", Font.ITALIC|Font.BOLD, 12));
		nickLab.addActionListener(this);
		panelNorte.add(usernameLab,BorderLayout.CENTER);
		panelNorte.add(nickLab,BorderLayout.SOUTH);
	
		lstContactos = new JList();
		lstContactos.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = lstContactos.locationToIndex(e.getPoint());
					Contacto c = (Contacto) lstContactos.getModel()
							.getElementAt(index);

					c.openWindow();

				}
			}
		});
		lstContactos.setCellRenderer(new ContactsRenderer());

		cliente.addObserver(this);
		lstContactos.setListData(cliente.getContacts().values().toArray());
		JScrollPane spc = new JScrollPane();
		spc.setViewportView(lstContactos);
		panelContactos.add(spc, BorderLayout.CENTER);
		panelContactos.add(panelNorte, BorderLayout.NORTH);
		
		panelGrupos= new JPanel();
		panelGrupos.setLayout(new GridLayout(2,1));
		JScrollPane spg = new JScrollPane();
		lstGrupos= new JList();
		lstGrupos.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = lstGrupos.locationToIndex(e.getPoint());
					Grupo g = (Grupo) lstGrupos.getModel()
							.getElementAt(index);

					

				}
			}
		});
		lstContactos.setListData(cliente.getGrupos().values().toArray());
		spg.setViewportView(lstGrupos);
		addTab("Contactos", panelContactos);

	}

	@Override
	public void update(Observable arg0, Object lista) {

		System.out.println(((Object[]) lista).length);
		lstContactos.setListData((Object[]) lista);

	}

	public void disconnect() {
		if (cliente != null)
			cliente.disconnect();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(nickLab))
			cliente.changeFrase(nickLab.getText());
		else if(e.getSource().equals(itmCerrar)){
			cliente.disconnect();
			interfaz.loginScreen();
		}else if(e.getSource().equals(itmCambiar)){
			if(DiagChangePass.changePassword(interfaz, cliente))
			{
			JOptionPane
			.showMessageDialog(this,"La contraseña fue cambiada exitosamente",
					"Cambio de contraseña",JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane
				.showMessageDialog(this,"La contraseña no pudo ser cambiada",
						"Cambio de contraseña",JOptionPane.ERROR_MESSAGE);
			}
		}else if(e.getSource().equals(itmAgregar)){
			String con = JOptionPane.showInputDialog(this, "Escribe el username de tu contacto", "Agregar contacto", JOptionPane.PLAIN_MESSAGE);
			if(cliente.addContact(con))
				JOptionPane
				.showMessageDialog(this,"El contacto fue agregado con exito",
						 "Agregar contacto",JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane
				.showMessageDialog(this,"El contacto no pudo ser agregado",
						 "Agregar contacto",JOptionPane.ERROR_MESSAGE);
		}

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
