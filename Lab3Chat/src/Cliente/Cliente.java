package Cliente;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Vector;

import conectividad.Stream;

public class Cliente extends Observable{
	private Vector<Contacto> contactos;
	private Vector<Grupo> grupos;
	private String frase;
	private String username;

	private Cliente(String username, Vector<Contacto> contacts, Vector<Grupo> grupos,
			String frase)  {
		this.username=username;
		contactos = contacts;
		this.frase = frase;
		this.grupos=grupos;
	}

	public void disconnect() {
		try {
			Socket s = new Socket("localhost", 2245);
			Stream.sendObject(s, "CHAO");
			Stream.sendObject(s, username);
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

	public static Cliente createClient(String username, String password) throws UnknownHostException,
			IOException, ClassNotFoundException {
		Vector<Contacto> contactos = new Vector<Contacto>();
		Vector<Grupo> grupos = new Vector<Grupo>();
		String frase = "";
	
		Socket s = new Socket("localhost", 2245);
		Stream.sendObject(s, "LOGIN");
		Stream.sendObject(s, username);
		Stream.sendObject(s, password);

		String line = (String) Stream.receiveObject(s);
		if (!line.equals("ERROR")) {
			frase = line;
			line = (String) Stream.receiveObject(s);

			int n = Integer.parseInt(line);
			for (int i = 0; i < n; i++) {
				String log = (String) Stream.receiveObject(s);
				String con = (String) Stream.receiveObject(s);
				String frasec = (String) Stream.receiveObject(s);
				String ips = (String) Stream.receiveObject(s);
				contactos.add(Contacto.crearContacto(log, ips, frasec, con));
			}
			line = (String) Stream.receiveObject(s);
			n = Integer.parseInt(line);
			
			for (int i = 0; i < n; i++) {
				
			}
			
			try
			{
			 InetAddress addr = InetAddress.getLocalHost();
			 Stream.sendObject(s,addr.getHostAddress());
			 
			}
			catch (UnknownHostException e) { 
				Stream.sendObject(s,"0.0.0.0");
			} 
			return new Cliente(username,contactos, grupos, frase);
		} else {
			return null;
		}
	
	}

	public String getUsername() {

		return username;
	}

	public String getFrase() {

		return frase;
	}

	public void cambiarFrase(String text) {
		// TODO Auto-generated method stub
		
	}

	

}
