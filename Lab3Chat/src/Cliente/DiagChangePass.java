package Cliente;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;


public class DiagChangePass extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static boolean changed = false;
	private static DiagChangePass diag;
	private JButton btnChange;
	private JButton btnCancel;
	private JPasswordField txtActual;
	private JPasswordField txtNueva;
	private JPasswordField txtConf;
	private Cliente cliente;

	private DiagChangePass(InterfazCliente interfaz, Cliente cliente) {
		super(interfaz, "Cambio contraseña", true);
		this.cliente = cliente;
		changed=false;
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		btnChange = new JButton("Cambiar");
		btnChange.addActionListener(this);
		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(this);
		ActionListener a=new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnChange.doClick();
				
			}
			
		};
		JLabel labActual = new JLabel("Contraseña Actual");
		txtActual = new JPasswordField();
		txtActual.addActionListener(a);
		JLabel labNueva = new JLabel("Contraseña Nueva");

		txtNueva = new JPasswordField();
		txtNueva.addActionListener(a);
		JLabel labConf = new JLabel("Confirmación");
		txtConf = new JPasswordField();
		txtConf.addActionListener(a);
		JPanel ps = new JPanel();
		ps.setLayout(new FlowLayout());

		ps.add(btnCancel);
		ps.add(btnChange);

		JPanel pc = new JPanel();
		pc.setLayout(new GridLayout(3, 2));
		pc.add(labActual);
		pc.add(txtActual);
		pc.add(labNueva);
		pc.add(txtNueva);
		pc.add(labConf);
		pc.add(txtConf);
		add(pc, BorderLayout.CENTER);
		add(ps, BorderLayout.SOUTH);
		pack();
	}

	public static boolean changePassword(InterfazCliente interfaz,
			Cliente cliente) {

		diag = new DiagChangePass(interfaz, cliente);
		diag.setVisible(true);

		return changed;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnChange)) {
			if (txtNueva.getText().equals(txtConf.getText())) {
				changed = cliente.changePassword(txtActual.getText(),txtNueva.getText());
				if(changed){
						
					diag.dispose();	
				}else{
					JOptionPane.showMessageDialog(this,"La contraseña no pudo ser cambiada","Cambio contraseña", JOptionPane.ERROR_MESSAGE);
				}
				
			}else{
				JOptionPane.showMessageDialog(this,"La contraseña nueva y la confirmación, deben ser iguales","Cambio contraseña", JOptionPane.ERROR_MESSAGE);
			}
		}else{
			diag.dispose();
		}
		

	}

}
