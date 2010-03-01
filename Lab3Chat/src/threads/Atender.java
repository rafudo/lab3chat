package threads;

import java.io.*;
import java.net.Socket;

import Servidor.PeticionAmigo;
import Servidor.Servidor;
import Servidor.Usuario;

/**
 * Hilo que se encarga de atender un nuevo cliente
 */
public class Atender extends Thread
{
	//Atributos
	/**
	 * Canal de Comunicacion con el cliente.
	 */
	private Socket cliente;
	
	//Constructor
	/**
	 * Retorna una instancia del thread.
	 */
	public Atender(Socket canal)
	{
		super();
		cliente = canal;
	}
	
	/**
	 * Atiende al Usuario segun lo que este requiera.
	 */
	public void run()
	{
		try
		{
			BufferedReader in = new BufferedReader( new InputStreamReader(cliente.getInputStream()));
			PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
		
			String[] partesMensaje = in.readLine().split(":");
			
			if(partesMensaje[0].equals("LOGIN"))
				login(partesMensaje, in, out);	
			
			else if(partesMensaje[0].equals("CHAO"))
				chao(partesMensaje, out);
				
			else if(partesMensaje[0].equals("PASSS"))
					pass(partesMensaje, out);
					
			else if(partesMensaje[0].equals("FRASE"))
				frase(partesMensaje, out);
			
			else if(partesMensaje[0].equals("CHARLA"))
				charla(partesMensaje, out);
			else if(partesMensaje[0].equals("AMIGO"))
				solicitud(partesMensaje, out);
			// TODO Grupo
			
			cliente.close();
		}
		catch(Exception e)
		{
			System.out.println("Error de comunicacion con el usuario.");
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * En caso que el usuario desee hacer login
	 */
	private boolean login(String[] partesMensaje, BufferedReader in, PrintWriter out) throws Exception
	{
		Usuario user = Servidor.darUsuario(partesMensaje[1]);
		
		if(user.darPass().equals(partesMensaje[2]))
		{
			out.println(user.darFrase());
			
			int n = user.amigos.size();
			out.println(n);
			n--;
			
			while(n >= 0)
			{
				Usuario amigo = Servidor.darUsuario(user.amigos.get(n));
				
				String conectado = "NO";
				
				if(Servidor.estaConectado(amigo.darLog()))
					conectado = "SI";
				
				out.println(amigo.darLog()+":"+conectado+":"+amigo.darFrase());
				
				n--;
			}
			
			//TODO Grupo Pendiente
			out.println("0");
			
			partesMensaje = in.readLine().split(":");
			
			user.setIP(partesMensaje[0]);
			user.setPuerto(Integer.parseInt(partesMensaje[1]));
			
			Servidor.agregarConectado(user);
			
			Monitor moni = new Monitor(user.darLog(), user.darIP(), user.darPuerto());
			moni.start();
			
			return true;
		}
		else
		{
			out.println("ERROR");
			return false;
		}
	}
	
	/**
	 * En caso que se desee desconectar.
	 */
	private void chao(String[] partesMensaje, PrintWriter out) throws Exception
	{
		Usuario user = Servidor.darUsuario(partesMensaje[1]);
		Servidor.desconectar(user.darLog());
		out.println("CHAO");
		
		int n = user.amigos.size();
		n--;
		
		while(n >= 0)
		{
			if(Servidor.estaConectado(user.amigos.get(n)))
			{
				Usuario amigo = Servidor.darUsuario(user.amigos.get(n));
				Socket canal = new Socket(amigo.darIP(), amigo.darPuerto());
				PrintWriter outAmigo = new PrintWriter(canal.getOutputStream(), true);
				
				int m = amigo.amigos.size();
				outAmigo.println("NUEVALISTA:" + m);
				m--;
				
				while(m >= 0)
				{
					Usuario amigoAmigo = Servidor.darUsuario(amigo.amigos.get(m));
					
					String conectado = "NO";
					
					if(Servidor.estaConectado(amigoAmigo.darLog()))
						conectado = "SI";
					
					outAmigo.println(amigoAmigo.darLog()+":"+conectado+":"+amigoAmigo.darFrase());
					
					m--;
				}
				
				canal.close();
			}
			
			n--;
		}
		
	}
	
	/**
	 * En caso que desee cambiar su pass.
	 */
	private void pass(String[] partesMensaje, PrintWriter out) throws Exception
	{
		Usuario user = Servidor.darUsuario(partesMensaje[4]);
		
		if(partesMensaje[1].equals(user.darPass()) && partesMensaje[2].equals(partesMensaje[3]))
		{
			Servidor.cambioPass(user, partesMensaje[2]);
			out.println("OK");
		}
		else
			out.println("ERROR");
		
	}
	
	/**
	 * En caso que desee cambiar su frase.
	 */
	private void frase(String[] partesMensaje, PrintWriter out) throws Exception
	{
		Usuario user = Servidor.darUsuario(partesMensaje[4]);
		Servidor.cambioFrase(user, partesMensaje[1]);
		out.println("OK");
	}
	
	/**
	 * En caso que desee conversar con un amigo.
	 */
	private void charla(String[] partesMensaje, PrintWriter out) throws Exception
	{
		Usuario user = Servidor.darUsuario(partesMensaje[1]);
		
		if(Servidor.estaConectado(user.darLog()))
			out.println(user.darIP()+":"+user.darPuerto());
		else
			out.println("ERROR");
	}
	
	/**
	 * En caso que desee realizar una solicitud de amistad.
	 */
	private void solicitud(String[] partesMensaje, PrintWriter out) throws Exception
	{
		if(Servidor.darUsuario(partesMensaje[1]) != null)
		{
			PeticionAmigo temp = new PeticionAmigo(partesMensaje[2], partesMensaje[1]);
			Servidor.agregarPeticionAmistad(temp);
			out.println("OK");
		}
		else
			out.println("ERROR");
	}
}
