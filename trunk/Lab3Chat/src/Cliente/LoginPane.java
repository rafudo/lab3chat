package Cliente;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPane extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InterfazCliente interfazCliente;
	private JButton btnConnect;
	private JTextField txtLogin;
	private JPasswordField txtPass;

	public LoginPane(InterfazCliente interfazCliente) {
		this.interfazCliente = interfazCliente;

		setLayout(new FlowLayout());
		txtLogin = new JTextField();
		txtLogin.setToolTipText("nombre de usuario");
		txtLogin.setColumns(18);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3, 1));

		p.add(txtLogin);
		txtPass = new JPasswordField();
		txtPass.setToolTipText("contraseña");
		txtPass.setColumns(18);
		p.add(txtPass);
		btnConnect = new JButton("Conectar");
		btnConnect.addActionListener(this);
		p.add(btnConnect);
		add(p);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnConnect)) {
			interfazCliente.connect();
			if (interfazCliente.getCliente().isConnected()) {
				interfazCliente.connectedScreen();
			}
		}

	}

}
