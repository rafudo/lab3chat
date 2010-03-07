package Cliente;



import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.Box;
import javax.swing.JMenuBar;

import javax.swing.JFrame;


public class InterfazCliente extends JFrame implements ActionListener{

	
	

	
	public InterfazCliente() {

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Mazinger");
		setSize(500, 500);
		setJMenuBar(new JMenuBar());
		
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

	

	

	public void connectedScreen(Cliente cliente) {
		System.setProperty("username", cliente.getUsername());
		getContentPane().removeAll();
		
		System.gc();
		add(new ConnectedPane(this,cliente));
		paintAll(getGraphics())	;
		
		
	}

	public void loginScreen() {
		getContentPane().removeAll();
		System.gc();
		LoginPane loginPane = new LoginPane(this);
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalGlue());
		box.add(loginPane);
		box.add(Box.createVerticalGlue());
		add(box);
		paintAll(getGraphics())	;
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		(new InterfazCliente()).setVisible(true);
	}
	

}
