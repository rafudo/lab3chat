package Cliente;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Vector;

import conectividad.Stream;

public class Cliente extends Observable{
	private Hashtable<String,Contacto> contactos;
	private Vector<Grupo> grupos;
	private String frase;
	private String username;
	private String password;
	private int port;
private ThreadEscucha escucha;
	private Cliente(String username, String password,Hashtable<String,Contacto> contacts, Vector<Grupo> grupos,
			String frase, int port)  {
		this.username=username;
		contactos = contacts;
		this.password=password;
		this.frase = frase;
		this.grupos=grupos;
		this.port=port;
		escucha=new ThreadEscucha(this);
		escucha.start();
	}

	public void disconnect() {
		try {
			Socket s = new Socket("localhost", 2245);
			Stream.sendObject(s, "CHAO");
			Stream.sendObject(s, username);
			Stream.receiveObject(s);
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

	}

	public boolean isConnected() {

		return true;
	}

	public Hashtable<String,Contacto> getContacts() {

		return contactos;
	}

	public static Cliente createClient(String username, String password) throws UnknownHostException,
			IOException, ClassNotFoundException {
		Hashtable<String,Contacto> contactos = new Hashtable<String,Contacto>();
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
				int porto = (Integer) Stream.receiveObject(s);
				contactos.put(log,Contacto.crearContacto(log, ips, frasec, con, porto));
			}
			line = (String) Stream.receiveObject(s);
			n = Integer.parseInt(line);
			
			for (int i = 0; i < n; i++) {
				
			}
			int port=Stream.findFreePort(1000,2000);
			try
			{
			 InetAddress addr = InetAddress.getLocalHost();
			 
			 Stream.sendObject(s,addr.getHostAddress());
			 
			}
			catch (UnknownHostException e) { 
				Stream.sendObject(s,"0.0.0.0");
			} 
			Stream.sendObject(s, ""+port);
			s.close();
			return new Cliente(username,password,contactos, grupos, frase, port);
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

	public int darPort() {
		return port;
	}

	

	


}
