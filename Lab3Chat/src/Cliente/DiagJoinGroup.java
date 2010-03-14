package Cliente;

import java.awt.BorderLayout;
import java.awt.FlowLayout;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Servidor.Grupo;


public class DiagJoinGroup extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static boolean changed = false;
	private static DiagJoinGroup diag;
	private JButton btnChange;
	private JButton btnCancel;
	private JList lstGroups;
	private Cliente cliente;

	private DiagJoinGroup(InterfazCliente interfaz, Cliente cliente) {
		super(interfaz, "Unirse a un grupo", true);
		this.cliente = cliente;
		changed=false;
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		btnChange = new JButton("Unirse");
		btnChange.addActionListener(this);
		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(this);		
		JPanel pl = new JPanel();
		pl.setLayout(new BorderLayout());
		pl.setBorder(BorderFactory.createTitledBorder("Grupos"));
		
		lstGroups = new JList();
		lstGroups.setListData(cliente.getGroupList().toArray());
		lstGroups.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					btnChange.doClick();

				}
			}
		});
		JScrollPane sp= new JScrollPane();
		sp.setViewportView(lstGroups);
		pl.add(sp);

		
		JPanel ps = new JPanel();
		ps.setLayout(new FlowLayout());

		ps.add(btnCancel);
		ps.add(btnChange);
		
		
		add(pl, BorderLayout.CENTER);
		add(ps, BorderLayout.SOUTH);
		pack();
	}

	public static boolean joinGroup(InterfazCliente interfaz,
			Cliente cliente) {

		diag = new DiagJoinGroup(interfaz, cliente);
		diag.setVisible(true);

		return changed;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnChange)) {
			Grupo g =(Grupo) lstGroups.getSelectedValue();
			changed=cliente.joinGroup(g);
			
		}else{
			
		}
		diag.dispose();
	}

}
