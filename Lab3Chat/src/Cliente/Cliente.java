package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import conectividad.Stream;

public class Cliente {
	private Vector<Contacto> contactos;
	
	public Cliente() throws UnknownHostException, IOException,
			ClassNotFoundException {
		contactos = new Vector<Contacto>();
		Socket s = new Socket("localhost", 2245);
		

		Stream.sendObject(s, "LOGIN");
		Stream.sendObject(s, "Camilo");

		Stream.sendObject(s, "123123123");

		String line = (String) Stream.receiveObject(s);
		if (!line.equals("ERROR")) {
			line = (String) Stream.receiveObject(s);

			int n = Integer.parseInt(line);
			for (int i = 0; i < n; i++) {
				String log = (String) Stream.receiveObject(s);
				String con = (String) Stream.receiveObject(s);
				String frase = (String) Stream.receiveObject(s);
				String ips = (String) Stream.receiveObject(s);
				contactos.add(Contacto.crearContacto(log, ips, frase, con));
			}		
			
			line = (String) Stream.receiveObject(s);

			n = Integer.parseInt(line);
			for (int i = 0; i < n; i++) {
				String log = (String) Stream.receiveObject(s);
				String con = (String) Stream.receiveObject(s);
				String frase = (String) Stream.receiveObject(s);
				String ips = (String) Stream.receiveObject(s);
				contactos.add(Contacto.crearContacto(log, ips, frase, con));
			}
			line = (String) Stream.receiveObject(s);
		}

	}

	public void disconnect() {
		try {
			Socket s = new Socket("localhost", 2245);
			Stream.sendObject(s, "CHAO");
		} catch (UnknownHostException e) {
			
		} catch (IOException e) {
			
		}
		
	}

	public boolean isConnected() {

		return true;
	}

	public Vector<Contacto> getContacts() {
		
		return contactos;
	}

}
