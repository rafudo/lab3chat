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
import javax.swing.JTextField;



public class DiagNewAccount extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static boolean created = false;
	private static DiagNewAccount diag;
	private JButton btnChange;
	private JButton btnCancel;
	private JTextField txtUsername;
	private JPasswordField txtContra;
	private JPasswordField txtConf;
	

	private DiagNewAccount(InterfazCliente interfaz) {
		super(interfaz, "Cambio contraseña", true);
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		btnChange = new JButton("Crear");
		btnChange.addActionListener(this);
		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(this);
		ActionListener a=new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnChange.doClick();
				
			}
			
		};
		JLabel labActual = new JLabel("Nombre de usuario");
		txtUsername = new JTextField();
		txtUsername.addActionListener(a);
		JLabel labNueva = new JLabel("Contraseña");

		txtContra = new JPasswordField();
		txtContra.addActionListener(a);
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
		pc.add(txtUsername);
		pc.add(labNueva);
		pc.add(txtContra);
		pc.add(labConf);
		pc.add(txtConf);
		add(pc, BorderLayout.CENTER);
		add(ps, BorderLayout.SOUTH);
		pack();
	}

	public static boolean createNewAccount(InterfazCliente interfaz) {

		diag = new DiagNewAccount(interfaz);
		diag.setVisible(true);

		return created;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnChange)) {
			if (txtContra.getText().equals(txtConf.getText())) {
				created = Cliente.createNewAccount(txtUsername.getText(),txtContra.getText());				
				diag.dispose();
			}else{
				JOptionPane.showMessageDialog(this,"La contraseña y la confirmación deben ser iguales","Crear cuenta", JOptionPane.ERROR_MESSAGE);
			}
		}else{
			diag.dispose();
		}
		

	}

}
