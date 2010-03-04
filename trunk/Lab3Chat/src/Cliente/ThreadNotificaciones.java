package Cliente;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import conectividad.Stream;

public class ThreadNotificaciones extends Thread{
	private Cliente me;
	private Socket in;
	public ThreadNotificaciones(Cliente me, Socket in) {
		this.in=in;
		this.me=me;
		
	}
	
	
	@Override
	public void run() {
		try {
			String line = (String) Stream.receiveObject(in);
			
			if(line.equals("CHARLA")){
				
			}else if (line.equals("NUEVALISTA")){
				newFriendList();
				
			}else if (line.equals("CONFAMIGO")){
				
			}
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		
		
	}


	private void newFriendList() {
		Vector<Contacto> contactos=me.getContacts();
		contactos.clear()		;
		String line;
		try {
			line = (String) Stream.receiveObject(in);
			int n = Integer.parseInt(line);
			for (int i = 0; i < n; i++) {
				String log = (String) Stream.receiveObject(in);
				String con = (String) Stream.receiveObject(in);
				String frasec = (String) Stream.receiveObject(in);
				String ips = (String) Stream.receiveObject(in);
				int porto = (Integer) Stream.receiveObject(in);
				contactos.add(Contacto.crearContacto(log, ips, frasec, con, porto));
			}
			line = (String) Stream.receiveObject(in);
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		
		
	}
	

}
