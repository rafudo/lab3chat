package Servidor;
import java.io.*;
import java.net.*;
import java.util.*;

import threads.Atender;
import threads.PeticionesPendientes;

/**
 * Clase Servidor.
 */
public class Servidor 
{
	// Constantes
	
	public final static String PASS = ".pass";
	public final static String FRASE = ".frase";
	public final static String AMIGO = ".amigo";
	public final static String NAMIGOS = ".namigos";
	public final static String CONFIG = "./data/servidor.properties";
	public final static String LOGS = "./data/logs";
	public final static String PETICIONES = "./data/peticiones";
	public final static String INFO = "./data/info.properties";
	
	// Atributos
	
	/**
	 * Socket de escucha.
	 */
	private ServerSocket server;
	
	/**
	 * Lista de Usuarios
	 */
	private ArrayList<String> usuarios;
	
	/**
	 * Lista de Usuarios Conectados
	 */
	private Hashtable<String,Usuario> conectados;
	
	/**
	 * Lista de notificaciones pendientes.
	 */
	private ArrayList<PeticionAmigo> peticionesAmistad;
	
	/**
	 * Properties con la informacion de los usuarios.
	 */
	private Properties info;
	
	/**
	 * Instancia unica del servidor
	 */
	private static Servidor servidor;
	
	//TODO Grupos
	
	// Constructor
	/**
	 * Recibe de parametros el numero del puerto de escucha, y la ruta donde se encuantra la informacion.
	 * Puede lanzar FileNotFound Exception, IO Exception
	 */
	public Servidor()
	{
		usuarios = new ArrayList<String>();
		conectados = new Hashtable<String, Usuario>();
		peticionesAmistad = new ArrayList<PeticionAmigo>();
	}
	
	// Metodos
	/**
	 * Recibe conecciones por el puerto especificado.
	 */
	public static void run()
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();

		}
		
		PeticionesPendientes hilo = new PeticionesPendientes();
		hilo.start();
		
		while(true)
		{
			try
			{
				System.out.println("Esperando conecciones...");	           
				Socket cliente = servidor.server.accept();
				Atender thread = new Atender(cliente);
				thread.start();
			}
			catch(Exception e)
			{
				System.out.println("Error al recibir una nueva coneccion.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Retorna el usuario identificado por el log parametro. Retorna null si no existe.
	 */
	public static Usuario getUsuario(String log)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		System.out.println("busca " +log);
		if(servidor.conectados.contains(new Usuario(log,"","")))
		{
			System.out.println("paso");
			return servidor.conectados.get(log);
		}
		else if(servidor.usuarios.contains(log))
		{
			Usuario user = new Usuario(log, servidor.info.getProperty(log+PASS), servidor.info.getProperty(log+FRASE));
			
			int n = Integer.parseInt(servidor.info.getProperty(log+NAMIGOS));
			ArrayList<String> amigos = new ArrayList<String>();
			n--;
			
			while(n >= 0)
			{
				amigos.add(servidor.info.getProperty(log+AMIGO+n));
				n--;
			}
			
			user.amigos = amigos;
			
			return user;
		}
		else
			return null;
	}
	
	
	/**
	 * Retorna True si el Usuario esta conectado.
	 */
	public static boolean isConnected(String log)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		return servidor.conectados.contains(new Usuario(log, "", ""));
	}
	
	/**
	 * Agrega un Usuario a la lista de conectados.
	 */
	public static void addConnected(Usuario user)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		servidor.conectados.put(user.getLog(),user);
	}
	
	/**
	 * Retorna una instancia de esta clase.
	 */
	public static Servidor getInstance()
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		return servidor;
	}
	
	/**
	 * Carga y crea el Servidor
	 */
	private void loadServer()
	{
		try
		{
			
	        info = new Properties( );
	        info.load( new FileInputStream(CONFIG));
	       
	        server = new ServerSocket(Integer.parseInt(info.getProperty("puerto")));
	        
	        // Llena la lista de Usuarios
	        BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(LOGS)));
	        
	        int n = Integer.parseInt(buffer.readLine());
	        
			while(n > 0)
			{
				usuarios.add(buffer.readLine());
				n--;
			}
			
			// Llena la list de peticiones pendientes
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(PETICIONES)));
			
			n = Integer.parseInt(buffer.readLine());
	        
			while(n > 0)
			{
				String[] temp = buffer.readLine().split(":");
				PeticionAmigo peticion = new PeticionAmigo(temp[0], temp[1]);
				peticionesAmistad.add(peticion);
				n--;
			}
			
			// Carga el Properties
			info.load( new FileInputStream(INFO));
			
		}
		catch(Exception e)
		{
			System.out.println("Problemas al leer algun archivo de configuracion.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Remueve un Usuario de la lista de conectados.
	 */
	public static void disconnect(String log)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		Usuario temp = new Usuario(log, "", "");
		
		if(servidor.conectados.contains(temp))
			servidor.conectados.remove(log);
	}
	
	/**
	 * Cambia el password de un usuario
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void changePass(Usuario user, String nPass) throws FileNotFoundException, IOException
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		user.changePass(nPass);
		servidor.info.setProperty(user.getLog()+PASS, nPass);
		servidor.info.store(new FileOutputStream(INFO), null);
	}
	
	/**
	 * Cambia la frase de un usuario
	 */
	public static void changeFrase(Usuario user, String nFrase)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		user.changeFrase(nFrase);
		servidor.info.setProperty(user.getLog()+FRASE, nFrase);
	}
	
	/**
	 * Agrega un Usuario
	 */
	public static void addUser(Usuario user)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		servidor.usuarios.add(user.getLog());
		
		File logs = new File(LOGS);
		
		if(logs.exists())
			logs.delete();
		
		try
		{
			logs.createNewFile();
			PrintWriter in = new PrintWriter(logs);
			
			int n = servidor.usuarios.size();
			in.println(n);
			n--;
			
			while(n >= 0)
			{
				in.println(servidor.usuarios.get(n));
				n--;
			}
			
			in.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		servidor.info.setProperty(user.getLog()+PASS, user.getPass());
		servidor.info.setProperty(user.getLog()+FRASE, user.getFrase());
		
		int n = user.amigos.size();
		servidor.info.setProperty(user.getLog()+NAMIGOS, n+"");
		n--;
		
		while(n >= 0)
		{
			servidor.info.setProperty(user.getLog()+AMIGO+n, user.amigos.get(n));
			n--;
		}
	}
	
	/**
	 * Agrega una peticion de amistad.
	 */
	public static void addFriendRequest(PeticionAmigo peticion)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		servidor.peticionesAmistad.add(peticion);
	}
	
	/**
	 * Retorna una solicitud pendiente, null si no hay mas solicitudes pendientes.
	 */
	public static PeticionAmigo nextPendingRequest()
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		if(servidor.peticionesAmistad.isEmpty())
			return null;
		else
			return servidor.peticionesAmistad.get(0);
	}
	
	/**
	 * Le agrega un amigo a un usuario.
	 */
	public static void addFriendToUser(Usuario user, Usuario amigo)
	{
		user.amigos.add(amigo.getLog());
		
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		int n = Integer.parseInt(servidor.info.getProperty(user.getLog()+NAMIGOS));
		servidor.info.setProperty(user.getLog()+AMIGO+n, amigo.getLog());
		n++;
		servidor.info.setProperty(user.getLog()+NAMIGOS, n+"");
	}

	public static boolean exist(String login) {
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.loadServer();
		}
		
		return servidor.usuarios.contains(login);
	}
}
