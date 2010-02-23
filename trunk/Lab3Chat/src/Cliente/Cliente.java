package Cliente;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class Cliente {
	
	public Cliente() throws UnknownHostException, IOException{
		
		Socket s = new Socket("localhost",2249);
		s.getOutputStream().write(2);
		System.out.println(s.getInputStream().read());
		
		
	}

	public void disconnect() {
		
	}

	public boolean isConnected() {

		return true;
	}

	public Vector<Contacto> getContacts() {
		Vector<Contacto> contacts = new Vector<Contacto>();
		;
		contacts.add(new Contacto("Camilo","123123",new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255},true));
		contacts.add(new Contacto("Camilo","sdgsgsdg",new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255},false));
		contacts.add(new Contacto("Camilo","hj,hj,hj",new byte[]{(byte) 128,(byte) 255,(byte) 255,(byte) 255},true));
		contacts.add(new Contacto("Camilo","tyutyutyu",new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255},false));
		contacts.add(new Contacto("Camilo","yumyumyu",new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255},true));
		contacts.add(new Contacto("Camilo","iiuiyui",new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255},false));
		return contacts;
	}

}
