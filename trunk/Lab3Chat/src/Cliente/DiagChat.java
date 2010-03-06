package Cliente;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DiagChat extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Cliente cliente;
	private Contacto contacto;
	private JTextField txtMsg;
	private JButton btnSend;
	private JTextArea txtChat;

	public DiagChat(Cliente e, Contacto c) {

		super();
		if (c != null)
			setTitle("Conversacion con " + c.getUsername());
		setTitle("Conversacion");
		cliente = e;
		contacto = c;
		contacto.setWindow(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(350, 375);
		setLayout(new BorderLayout());
		txtChat = new JTextArea(
				"asdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\n"
						+ "asdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\nasdasd\n");
		txtChat.setLineWrap(true);
		JScrollPane sp = new JScrollPane();
		sp.setViewportView(txtChat);
		add(sp, BorderLayout.CENTER);

		txtMsg = new JTextField();
		btnSend = new JButton("Enviar");

		JPanel pc = new JPanel();

		pc.setLayout(new BorderLayout());
		pc.add(txtMsg, BorderLayout.CENTER);
		pc.add(btnSend, BorderLayout.EAST);
		add(pc, BorderLayout.SOUTH);

	}

	@Override
	public void dispose() {

		super.dispose();
		System.out.println(contacto.getWindow());
	}

	public static void main(String[] args) {
		(new DiagChat(null, null)).setVisible(true);
	}

}
