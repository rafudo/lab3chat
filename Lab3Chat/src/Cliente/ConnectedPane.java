package Cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;


public class ConnectedPane extends JTabbedPane implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelContactos;
	private JList lstContactos;
	private InterfazCliente interfaz;
	private final Cliente cliente;
	private JLabel usernameLab;
	private JTextField nickLab;

	public ConnectedPane(InterfazCliente interfazCliente, Cliente client) {
		interfaz = interfazCliente;
		this.cliente=client;
		panelContactos = new JPanel();
		panelContactos.setLayout(new BorderLayout());
		usernameLab = new JLabel(cliente.getUsername());
		usernameLab.setFont(new Font("Arial", Font.BOLD, 20));
		JPanel panelNorte = new JPanel();
		panelNorte.setLayout(new FlowLayout());
		nickLab = new JTextField(cliente.getFrase());
		nickLab.setFont(new Font("Arial", Font.ITALIC, 10));
		nickLab.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cliente.cambiarFrase(nickLab.getText());
				
			}
		});
		panelNorte.add(usernameLab);
		panelNorte.add(nickLab);
		addTab("Contactos", panelContactos);
		lstContactos = new JList();
		lstContactos.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					int index=lstContactos.locationToIndex(e.getPoint());
					Contacto c= (Contacto) lstContactos.getModel().getElementAt(index);
										
				}
			}
		});
		lstContactos.setCellRenderer(new ContactsRenderer());

		cliente.addObserver(this);
		lstContactos.setListData(cliente.getContacts().values().toArray());
		JScrollPane sp = new JScrollPane();
		sp.setViewportView(lstContactos);
		panelContactos.add(sp, BorderLayout.CENTER);
		panelContactos.add(panelNorte, BorderLayout.NORTH);

	}

	@Override
	public void update(Observable arg0, Object lista) {
		
		
		System.out.println(((Object[]) lista).length);
		lstContactos.setListData((Object[]) lista);
		
	}

	public void disconnect() {
		if(cliente!=null)
            cliente.disconnect();
		
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
