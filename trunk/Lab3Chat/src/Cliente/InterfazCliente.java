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
			if(cliente!=null)
			cliente.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.dispose();
		System.exit(0);
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
		add(new ConnectedPane(this));
		paintAll(getGraphics())	;
		
		
	}

	public void connect(String username, String password) {
		try {
			cliente = Cliente.createClient(username,password);
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		
	}

}
