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


public class DiagLeaveGroup extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static boolean changed = false;
	private static DiagLeaveGroup diag;
	private JButton btnChange;
	private JButton btnCancel;
	private JList lstGroups;
	private Cliente cliente;

	private DiagLeaveGroup(InterfazCliente interfaz, Cliente cliente) {
		super(interfaz, "Dejar Grupo", true);
		this.cliente = cliente;
		changed=false;
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		btnChange = new JButton("Dejar");
		btnChange.addActionListener(this);
		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(this);		
		JPanel pl = new JPanel();
		pl.setLayout(new BorderLayout());
		pl.setBorder(BorderFactory.createTitledBorder("Grupos"));
		
		lstGroups = new JList();
		lstGroups.setListData(cliente.getGrupos().values().toArray());
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

	public static boolean leaveGroup(InterfazCliente interfaz,
			Cliente cliente) {

		diag = new DiagLeaveGroup(interfaz, cliente);
		diag.setVisible(true);

		return changed;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnChange)) {
			Grupo g =(Grupo) lstGroups.getSelectedValue();
			changed=cliente.leaveGroup(g.getIp());
			
		}
		diag.dispose();
	}

}
