package Cliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DiagChat extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Contacto contacto;
	private JTextField txtMsg;
	private JButton btnSend;
	private JScrollPane sp;
	private JTextArea txtChat;

	public DiagChat( Contacto c) {

		super();
		if (c != null)
			setTitle("Conversacion con " + c.getUsername());
		else 
			setTitle("Conversacion");
		
		contacto = c;
		contacto.setWindow(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(350, 375);
		setLayout(new BorderLayout());
		txtChat = new JTextArea();
		txtChat.setLineWrap(true);
		txtChat.setEditable(false);
		sp = new JScrollPane();
		sp.setViewportView(txtChat);
		add(sp, BorderLayout.CENTER);

		txtMsg = new JTextField();
		txtMsg.addActionListener(this);
		btnSend = new JButton("Enviar");
		btnSend.addActionListener(this);
		JPanel pc = new JPanel();

		pc.setLayout(new BorderLayout());
		pc.add(txtMsg, BorderLayout.CENTER);
		pc.add(btnSend, BorderLayout.EAST);
		add(pc, BorderLayout.SOUTH);

	}

	@Override
	public void dispose() {

		super.dispose();
		contacto.close();
		contacto.setWindow(null);
	}

	public static void main(String[] args) {
		(new DiagChat( null)).setVisible(true);
	}

	public void append(String o) {
		txtChat.append(contacto.getUsername()+": "+o+"\n");	
		sp.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getMaximum());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String msg =txtMsg.getText();
		if(contacto.sendMsg(msg))
			txtChat.append("Yo: "+msg+"\n");
		txtMsg.setText("");
		sp.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getMaximum());
		
	}

}
