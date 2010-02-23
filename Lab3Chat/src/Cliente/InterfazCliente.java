package Cliente;



import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.Box;

import javax.swing.JFrame;


public class InterfazCliente extends JFrame {

	private Cliente cliente;


	public InterfazCliente() {

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Mazinger");
		setSize(500, 500);

		LoginPane loginPane = new LoginPane(this);
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalGlue());
		box.add(loginPane);
		box.add(Box.createVerticalGlue());
		add(box);

	}

	@Override
	public void dispose() {
		try {
			cliente.disconnect();
		} catch (Exception e) {
		}
		super.dispose();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		(new InterfazCliente()).setVisible(true);

	}

	public Cliente getCliente() {

		return cliente;
	}

	public void connectedScreen() {
		getContentPane().removeAll();
		add(new PanelConectado(this));
		paintAll(getGraphics())	;
		
		
	}

	public void connect() {
		try {
			cliente = new Cliente();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
