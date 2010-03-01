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
	private ArrayList<Usuario> conectados;
	
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
		conectados = new ArrayList<Usuario>();
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
			servidor.cargarServidor();
			System.out.println("Entro");
		}
		
		PeticionesPendientes hilo = new PeticionesPendientes();
		hilo.start();
		
		while(true)
		{
			try
			{
				System.out.println("Esperando conecciones...");
	            if(servidor.server == null) System.out.println("mierda");
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
	public static Usuario darUsuario(String log)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		
		if(servidor.conectados.contains(log))
		{
			return servidor.conectados.get(servidor.conectados.indexOf(new Usuario(log, "", "")));
		}
		else if(servidor.usuarios.contains(log))
		{
			Usuario user = new Usuario(log, servidor.info.getProperty(log+PASS), servidor.info.getProperty(log+FRASE));
			
			int n = Integer.parseInt(servidor.info.getProperty(log+NAMIGOS));
			ArrayList<String> amigos = new ArrayList<String>();
			n--;
			
			while(n >= 0)
			{
				amigos.add(servidor.info.getProperty(log+AMIGO+"."+n));
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
	public static boolean estaConectado(String log)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		
		return servidor.conectados.contains(new Usuario(log, "", ""));
	}
	
	/**
	 * Agrega un Usuario a la lista de conectados.
	 */
	public static void agregarConectado(Usuario user)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		else
			servidor.conectados.add(user);
	}
	
	/**
	 * Retorna una instancia de esta clase.
	 */
	public static Servidor darInstancia()
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		
		return servidor;
	}
	
	/**
	 * Carga y crea el Servidor
	 */
	private void cargarServidor()
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
	public static void desconectar(String log)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		
		Usuario temp = new Usuario(log, "", "");
		
		if(servidor.conectados.contains(temp))
			servidor.conectados.remove(servidor.conectados.indexOf(temp));
	}
	
	/**
	 * Cambia el password de un usuario
	 */
	public static void cambioPass(Usuario user, String nPass)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		
		user.cambiarPass(nPass);
		servidor.info.setProperty(user.darLog()+PASS, nPass);
	}
	
	/**
	 * Cambia la frase de un usuario
	 */
	public static void cambioFrase(Usuario user, String nFrase)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		
		user.cambiarFrase(nFrase);
		servidor.info.setProperty(user.darLog()+FRASE, nFrase);
	}
	
	/**
	 * Agrega un Usuario
	 */
	public static void agregarUsuario(Usuario user)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		
		servidor.usuarios.add(user.darLog());
		
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
		
		servidor.info.setProperty(user.darLog()+PASS, user.darPass());
		servidor.info.setProperty(user.darLog()+FRASE, user.darFrase());
		
		int n = user.amigos.size();
		servidor.info.setProperty(user.darLog()+NAMIGOS, n+"");
		n--;
		
		while(n >= 0)
		{
			servidor.info.setProperty(user.darLog()+AMIGO+n, user.amigos.get(n));
			n--;
		}
	}
	
	/**
	 * Agrega una peticion de amistad.
	 */
	public static void agregarPeticionAmistad(PeticionAmigo peticion)
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		
		servidor.peticionesAmistad.add(peticion);
	}
	
	/**
	 * Retorna una solicitud pendiente, null si no hay mas solicitudes pendientes.
	 */
	public static PeticionAmigo siguientePeticionPendiente()
	{
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		
		if(servidor.peticionesAmistad.isEmpty())
			return null;
		else
			return servidor.peticionesAmistad.get(0);
	}
	
	/**
	 * Le agrega un amigo a un usuario.
	 */
	public static void agregarleUnAmigo(Usuario user, Usuario amigo)
	{
		user.amigos.add(amigo.darLog());
		
		if(servidor == null)
		{
			servidor = new Servidor();
			servidor.cargarServidor();
		}
		
		int n = Integer.parseInt(servidor.info.getProperty(user.darLog()+NAMIGOS));
		servidor.info.setProperty(user.darLog()+AMIGO+n, amigo.darLog());
		n++;
		servidor.info.setProperty(user.darLog()+NAMIGOS, n+"");
	}
}
