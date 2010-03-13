package Servidor;

import java.io.*;
import java.net.*;
import java.util.*;

import threads.Atender;


/**
 * Clase Servidor.
 */
public class Servidor {
	// Constantes

	public final static String PASS = "pass";
	public final static String FRASE = "frase";
	public final static String AMIGO = "a";
	public final static String NAMIGOS = "n";
	public final static String CONFIG = "./data/servidor.properties";
	public final static String LOGS = "./data/accounts/";
	public final static String PETICIONES = "./data/peticiones/";
	public final static String INFO = "./data/info.properties";
	public final static String GRUPOS = "./data/grupos";

	// Atributos

	/**
	 * Socket de escucha.
	 */
	private ServerSocket server;

	/**
	 * Lista de Usuarios Conectados
	 */
	private Hashtable<String, Usuario> conectados;

	
	/**
	 * Instancia unica del servidor
	 */
	private static Servidor servidor;

	/**
	 * Lista de grupos
	 */
	private Hashtable<String, Grupo> grupos;

	// Constructor
	/**
	 * Recibe de parametros el numero del puerto de escucha, y la ruta donde se
	 * encuantra la informacion. Puede lanzar FileNotFound Exception, IO
	 * Exception
	 */
	public Servidor() {

		conectados = new Hashtable<String, Usuario>();
		
		grupos = new Hashtable<String, Grupo>();
	}

	// Metodos
	/**
	 * Recibe conecciones por el puerto especificado.
	 */
	public static void run() {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();

		}

	
		while (true) {
			try {
				System.out.println("Esperando conecciones...");
				Socket cliente = servidor.server.accept();
				Atender thread = new Atender(cliente);
				thread.start();
			} catch (Exception e) {
				System.out.println("Error al recibir una nueva coneccion.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Retorna el usuario identificado por el log parametro. Retorna null si no
	 * existe.
	 */
	public static Usuario getUsuario(String log) {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}

		if (servidor.conectados.containsKey(log)) {
			return servidor.conectados.get(log);
		} else if (exists(log)) {
			Usuario user = null;
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(new FileInputStream(LOGS + log));
				user = (Usuario) ois.readObject();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}

			return user;
		} else
			return null;
	}

	/**
	 * Retorna True si el Usuario esta conectado.
	 */
	public static boolean isConnected(String log) {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}

		return servidor.conectados.containsKey(log);
	}

	/**
	 * Agrega un Usuario a la lista de conectados.
	 */
	public static void addConnected(Usuario user) {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}

		servidor.conectados.put(user.getLog(), user);
	}

	/**
	 * Retorna una instancia de esta clase.
	 */
	public static Servidor getInstance() {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}

		return servidor;
	}

	/**
	 * Carga y crea el Servidor
	 */
	
	private void loadServer() {
		try {

			Properties config = new Properties();
			config.load(new FileInputStream(CONFIG));

			server = new ServerSocket(Integer.parseInt(config
					.getProperty("puerto")));

			// Llena la list de peticiones pendientes
			
			
			// Carga los grupos.
			
			File f = new File(GRUPOS);
			
			if (!f.exists()) 
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				grupos = (Hashtable<String, Grupo>) ois.readObject();
				ois.close();
			}
			else
				grupos = new Hashtable<String, Grupo>();
			

		} catch (Exception e) {
			System.out
					.println("Problemas al leer algun archivo de configuracion.");
			e.printStackTrace();
		}
	}

	/**
	 * Remueve un Usuario de la lista de conectados.
	 */
	public static void disconnect(String log) {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}

		Usuario temp = new Usuario(log, "", "");

		if (servidor.conectados.contains(temp))
			servidor.conectados.remove(log);

	}

	/**
	 * Cambia el password de un usuario
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void changePass(Usuario user, String nPass)
			throws FileNotFoundException, IOException {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}
		try {
			user.setPass(nPass);
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(LOGS + user.getLog()));
			oos.writeObject(user);
			oos.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Cambia la frase de un usuario
	 */
	public static void changeFrase(Usuario user, String nFrase) {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}
		try {
			user.setFrase(nFrase);
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(LOGS + user.getLog()));
			oos.writeObject(user);
			oos.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Agrega un Usuario
	 */
	public static void addUser(Usuario user) {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}

		File f = new File(LOGS + user.getLog());
		try {
			if (f.createNewFile()) {
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(f));
				oos.writeObject(user);
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	

	/**
	 * Le agrega un amigo a un usuario.
	 */
	public static void addFriendToUser(Usuario user, Usuario amigo) {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}
		try {
			if(!user.amigos.contains(amigo.getLog())){
			user.amigos.add(amigo.getLog());
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(LOGS + user.getLog()));
			oos.writeObject(user);
			oos.close();
			}
			if(!amigo.amigos.contains(user.getLog())){
				amigo.amigos.add(user.getLog());
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(LOGS + amigo.getLog()));
				oos.writeObject(amigo);
				oos.close();
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Agrega un Grupo
	 */
	public static void addGrupo(Grupo g)
	{
		if (servidor == null) 
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		servidor.grupos.put(g.getIp(),g);
		
		try
		{	
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GRUPOS));
			oos.writeObject(servidor.grupos);
			oos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Retorna los grupos
	 */
	public static Hashtable<String, Grupo> getGrupos()
	{
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		return servidor.grupos;
	}
	
	
	public static boolean exists(String login) {
		if (servidor == null) {
			servidor = new Servidor();
			servidor.loadServer();
		}
		File f = new File(LOGS + login);
		return f.exists()&&!login.equals("");
	}
	
	/**
	 * Elimina un grupo
	 */
	public static void removerGrupo(Grupo g)
	{
		if (servidor == null) 
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		servidor.grupos.remove(g);
	}
}
