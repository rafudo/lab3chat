package Cliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DiagChat extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Contacto contacto;
	private JTextField txtMsg;
	private JButton btnSend;
	private JScrollPane sp;
	private JTextArea txtChat;
	private JMenuItem itmSendFile;

	public DiagChat(Contacto c) {

		super();

		setJMenuBar(new JMenuBar());
		if (c != null)
			setTitle("Conversacion con " + c.getUsername());
		else
			setTitle("Conversacion");
		JMenu acciones = new JMenu("Acciones");
		itmSendFile = new JMenuItem("Enviar Archivo");
		itmSendFile.addActionListener(this);
		acciones.add(itmSendFile);
		getJMenuBar().add(acciones);

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

		int r=JOptionPane.showConfirmDialog(this, "Desea guardar la conversación?", "Guardar conversación", JOptionPane.YES_NO_OPTION);
		contacto.close(r==JOptionPane.YES_OPTION);
		
		super.dispose();
		contacto.setWindow(null);
	}

	public static void main(String[] args) {
		(new DiagChat(null)).setVisible(true);
	}

	public void append(String o) {
		txtChat.append(contacto.getUsername() + ": " + o + "\n");
		sp.getVerticalScrollBar().setValue(
				sp.getVerticalScrollBar().getMaximum());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(txtMsg) || e.getSource().equals(btnSend)) {
			String msg = txtMsg.getText();
			if (contacto.sendMsg(msg))
				txtChat.append("Yo: " + msg + "\n");
			txtMsg.setText("");
			sp.getVerticalScrollBar().setValue(
					sp.getVerticalScrollBar().getMaximum());
		}else if (e.getSource().equals(itmSendFile)){
			JFileChooser fc = new JFileChooser();			
			if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
				File f= fc.getSelectedFile();
				contacto.askSendFile(f);
				txtChat.append("//Yo he pedido a "+contacto.getUsername()+" recibir el archivo "+f.getName()+"\n");
				sp.getVerticalScrollBar().setValue(
						sp.getVerticalScrollBar().getMaximum());
			}
		}

	}

	public void askReiciveFile(int fileid, String file) {
		int r = JOptionPane.showConfirmDialog(this, contacto.getUsername()
				+ " quiere mandarte el archivo " + file,
				"Envio de archivo", JOptionPane.YES_NO_OPTION);
		if (r == JOptionPane.YES_OPTION) {
			txtChat.append("//Yo he aceptado el envio del archivo "+file+"\n");
			contacto.reiciveFile(fileid,file,true );
		}else{
			txtChat.append("//Yo no he aceptado el envio del archivo "+file+"\n");
			contacto.reiciveFile(fileid,file ,false);
		}
		sp.getVerticalScrollBar().setValue(
				sp.getVerticalScrollBar().getMaximum());
	}

	public void msg(String string) {
		txtChat.append(string);
		sp.getVerticalScrollBar().setValue(
				sp.getVerticalScrollBar().getMaximum());
		
	}

}
