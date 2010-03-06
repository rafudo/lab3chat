package Cliente;


import javax.swing.JFrame;

public class DiagChat extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Cliente cliente;
	private Contacto contacto;
	public DiagChat (Cliente e, Contacto c){
		super("Conversación con "+c.getUsername());
		cliente=e;
		contacto=c;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(350,375);
		
	}
	
	

}
