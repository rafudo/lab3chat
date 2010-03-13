package Cliente;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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

	
	

	private Cliente(String username, String password,Hashtable<String,Contacto> contacts, Hashtable<String,Grupo> grupos,
			String frase, int port)  {
		this.username=username;
		contactos = contacts;
		this.password=password;
		this.frase = frase;
		this.grupos=grupos;
		this.port=port;
		escucha=new ThreadEscucha(this);
		escucha.start();
		
		this.grupos=grupos;
		
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
				grupos.put(g.getIp(),g);
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
	/*
	public String darListadoGruposTodos()
	{
		try
		{
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "LISTA GRUPO");
			gruposTodos = (ArrayList<Grupo>)Stream.receiveObject(s);
			
			int i = 0;
			
			String lista = gruposTodos.size() + " grupos./n/n";
			
			while(i < gruposTodos.size())
			{
				lista += "Grupo de " + gruposTodos.get(i).getOwner() + ":/n";
				for(String inscrito: gruposTodos.get(i).darGente())
					lista += "- " + inscrito + "/n";
				lista += "/n";
				i++;
			}
			
			return lista;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Error al comunicarse con el servidor.";
		}
	}*/
	
	/**
	 * Crea un grupo.
	 */
	public String createGroup(String nombre)
	{
		try
		{
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "NEWGRUPO");
			Stream.sendObject(s, username);
			Stream.sendObject(s, nombre);
			
			String respuesta = (String)Stream.receiveObject(s);
			
			if(respuesta.equals("ERROR")){
				s.close();
				return "No se pudo crear el grupo "+nombre;
			}else
			{
				String ip = (String)Stream.receiveObject(s);
				Grupo g = new Grupo(username, ip, nombre);
				grupos.put(ip,g);
				s.close();
				return "Se creo su grupo de forma exitosa.";
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			return "Error al comunicarse con el servidor.";
		}
	}
	
	/**
	 * Salirse de un grupo, en caso que el usuario sea el dueno del grupo, se elimina el grupo.
	 */
	public String salirGrupo(String idGrupo)
	{
		int j = 0;
		Grupo g = null;
		
		while(j < grupos.size())
		{
			if(grupos.get(j).getOwner().equals(idGrupo))
				g = grupos.get(j);
			
			j++;
		}
		
		if(g == null)
			return "Usted no hace parte del grupo especificado.";
		else
		{
			try
			{
				Socket s = new Socket(HOST, 2245);
				Stream.sendObject(s, "SALIR GRUPO");
				Stream.sendObject(s, username);
				Stream.sendObject(s, g);
				
				String respuesta = (String)Stream.receiveObject(s);
				
				return respuesta;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return "Error al comunicarse con el servidor.";
			}
		}
	}
	
	/**
	 * Unirse a un grupo.
	 */
	/*
	public String unirseGrupo(String idGrupo)
	{
		if(gruposTodos == null)
			darListadoGruposTodos();
		
		Grupo g = gruposTodos.get(gruposTodos.indexOf(new Grupo(idGrupo, null)));
		
		if(g == null)
			return "El grupo especificado no existe.";
		
		try
		{
			Socket s = new Socket(HOST, 2245);
			Stream.sendObject(s, "UNIR GRUPO");
			Stream.sendObject(s, username);
			Stream.sendObject(s, g);
			
			grupos.add(g);
			return (String)Stream.receiveObject(s);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Error al comunicarse con el servidor.";
		}
	}
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
			Stream.sendObject(s, "AMIGO");
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
	
	
	
	


}
