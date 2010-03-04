package Cliente;

import java.io.IOException;
import java.net.Socket;

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
				
			}else if (line.equals("CONFAMIGO")){
				
			}
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		
		
	}
	

}
