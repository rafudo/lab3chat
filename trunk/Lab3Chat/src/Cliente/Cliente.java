package Cliente;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Vector;

import Servidor.Grupo;

import conectividad.Stream;

public class Cliente extends Observable{
	
	/**
	 * Hostname del servidor
	 */
	public final static String HOST = "localhost";
	
	private Hashtable<String,Contacto> contactos;
	private Hashtable<String,Grupo> grupos;
	private String frase;
	private String username;
	private String password;
	private int port;
	private ThreadEscucha escucha;
	private ThreadListenGroups listenGroups;
	private ConnectedPane panel;
	
	

	private Cliente(String username, String password,Hashtable<String,Contacto> contacts, Hashtable<String,Grupo> grupos,
			String frase, int port) throws IOException  {
		this.username=username;
		contactos = contacts;
		this.password=password;
		this.frase = frase;
		this.grupos=grupos;
		this.port=port;		
		this.grupos=grupos;
		escucha=new ThreadEscucha(this);
		escucha.start();
		listenGroups = new ThreadListenGroups(this)	;
		listenGroups.start();
	}
	
	public void setPanel(ConnectedPane p){
		panel=p;
	}

	public void disconnect() {
		
		try {
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "CHAO");
			Stream.sendObject(s, username);
			Stream.receiveObject(s);
			s.close();
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
		Hashtable<String,Grupo> grupos = new Hashtable<String,Grupo>();
		String frase = "";
	
		Socket s = new Socket(HOST, 2245);
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
			
			for (int i = 0; i < n; i++) 
			{
				String own = (String)Stream.receiveObject(s);
				String ip = (String)Stream.receiveObject(s);
				String id = (String)Stream.receiveObject(s);
				Grupo g = new Grupo(own, ip, id);
				grupos.put(ip,g);
				
			}
			int port=Stream.findFreePort(1024,2048);
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

	public void changeFrase(String text) {
		try {
			Socket s = new Socket("localhost", 2245);
			Stream.sendObject(s, "FRASE");
			Stream.sendObject(s, username);
			Stream.sendObject(s, password);
			Stream.sendObject(s, text);
			Stream.receiveObject(s);
			s.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		
	}

	public int getPort() {
		return port;
	}

	

	public void modifyList(Contacto c) {
		setChanged();
		contactos.remove(c.getUsername());
		contactos.put(c.getUsername(),c);
		notifyObservers(contactos.values().toArray());
		clearChanged();
		
	}

	
	
	/**
	 * Retorna un lista de todos los grupos.
	 */
	
	public ArrayList< Grupo> getGroupList()
	{
		try
		{
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "LISTGROUP");
			int ng = Integer.parseInt((String)Stream.receiveObject(s));
			ArrayList<Grupo>grups = new ArrayList<Grupo>();
			for(int i=0;i<ng;i++){
				String own = (String)Stream.receiveObject(s);
				String ip = (String)Stream.receiveObject(s);
				String id = (String)Stream.receiveObject(s);
				Grupo g = new Grupo(own, ip, id);
				grups.add( g);
			}
			return grups;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		return new ArrayList< Grupo>();
	}
	
	/**
	 * Crea un grupo.
	 */
	public boolean createGroup(String nombre)
	{
		try
		{
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "NEWGROUP");
			Stream.sendObject(s, username);
			Stream.sendObject(s, nombre);
			
			String respuesta = (String)Stream.receiveObject(s);
			
			if(respuesta.equals("ERROR")){
				s.close();
				return false;
			}else
			{
				String ip = (String)Stream.receiveObject(s);
				Grupo g = new Grupo(username, ip, nombre);
				grupos.put(ip,g);
				setChanged();
				notifyObservers();
				clearChanged();
				s.close();
				return true;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Unirse a un grupo.
	 */
	public boolean changePassword(String actual, String nueva) {
		try {
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "PASS");
			Stream.sendObject(s, username);			
			Stream.sendObject(s, actual);
			Stream.sendObject(s, nueva);
			boolean b =Stream.receiveObject(s).equals("OK");
			s.close();
			return b;
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return false;
	}


	public boolean addContact(String con) {
		
		try {
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "FRIEND");
			Stream.sendObject(s, username);			
			Stream.sendObject(s, con);
			boolean b =Stream.receiveObject(s).equals("OK");
			s.close();
			return b;
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return false;
	}


	public static boolean createNewAccount(String username, String contra) {
		try {
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "NEW");
			Stream.sendObject(s, username);		
			if(Stream.receiveObject(s).equals("OK")){
				Stream.sendObject(s, contra);
				s.close();
				return true;
			}else{
				s.close();
				return false;
			}
			
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return false;
	}


	public Hashtable<String, Grupo> getGrupos() {
		
		return grupos;
	}


	public void sendMsg(String text, Grupo g) throws IOException {
		DatagramSocket ds = new DatagramSocket();
		InetAddress ip = InetAddress.getByName(g.getIp());
		text = g.getId()+" ("+username+"): "+text;
		DatagramPacket dp = new DatagramPacket(text.getBytes(), text.length(), ip, 2015);
		ds.send(dp);
		
		
	}


	public void groupsMsg(String s) {
		panel.groupsMsg(s);
		
	}

	public boolean joinGroup(Grupo g) {
		try {
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "JOINGROUP");
			Stream.sendObject(s, username);			
			Stream.sendObject(s, g.getIp());
			boolean b =Stream.receiveObject(s).equals("OK");
			s.close();
			if(b){
				grupos.put(g.getIp(), g);
				listenGroups.joinGroup(g.getIp());
			}
			setChanged();
			notifyObservers();
			clearChanged();
			return b;
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return false;
		
	}
	public boolean leaveGroup(String ip) {
		try {
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "LEAVEGROUP");
			Stream.sendObject(s, username);			
			Stream.sendObject(s, ip);
			boolean b =Stream.receiveObject(s).equals("OK");
			s.close();
			if(b){
				listenGroups.leaveGroup(ip);
				grupos.remove(ip);
				
			}
			setChanged();
			notifyObservers();
			clearChanged();
			return b;
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return false;
		
	}

	
	
	
	
	


}
