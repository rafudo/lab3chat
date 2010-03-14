package Cliente;



import java.awt.Component;



import javax.swing.Box;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;


import javax.swing.JFrame;



public class InterfazCliente extends JFrame {
	

	
	public InterfazCliente() {

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Mazinger");
		setSize(500, 500);
		setResizable(false);
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
		ConnectedPane p = new ConnectedPane(this,cliente);
		cliente.setPanel(p);
		add(p);
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

	

	public static void main(String[] args) {
		
	    SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	    	  
	    	 (new InterfazCliente()).setVisible(true);	        
	      }
	    });
	}
	

}
