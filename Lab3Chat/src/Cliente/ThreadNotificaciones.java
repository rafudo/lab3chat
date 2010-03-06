package Cliente;

import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;


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
		System.out.println("Estoy recibiendolas");
	
		try {			
				String log = (String) Stream.receiveObject(in);
				String con = (String) Stream.receiveObject(in);
				String frasec = (String) Stream.receiveObject(in);
				String ips = (String) Stream.receiveObject(in);
				int porto = (Integer) Stream.receiveObject(in);
				Contacto c=Contacto.crearContacto(log, ips, frasec, con, porto);			
			me.modifyList(c);			

		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		
		
	}
	

}
