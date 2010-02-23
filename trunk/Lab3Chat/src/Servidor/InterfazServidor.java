package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InterfazServidor {
	
	public static void main(String[] args) throws IOException{
		ServerSocket ss = new ServerSocket(2249);
		Socket s = ss.accept();
		
		System.out.println(s.getInputStream().read());
		s.getOutputStream().write(4);
	}

}
