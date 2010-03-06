package Cliente;



import java.awt.Component;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.Box;

import javax.swing.JFrame;


public class InterfazCliente extends JFrame {

	


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
			Component c = getContentPane().getComponent(0);
			if(c instanceof ConnectedPane)
			((ConnectedPane)c).disconnect();
			
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

	

	public void connectedScreen(Cliente cliente) {
		getContentPane().removeAll();
		add(new ConnectedPane(this,cliente));
		paintAll(getGraphics())	;
		
		
	}

	

}
