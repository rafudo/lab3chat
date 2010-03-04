package Cliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ThreadEscucha extends Thread {
	private Cliente me;
	
	public ThreadEscucha(Cliente c){
		me=c;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket s = new ServerSocket(me.darPort());
			while(true){
				Socket in=s.accept();
				(new ThreadNotificaciones(me,in)).start();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}

}
