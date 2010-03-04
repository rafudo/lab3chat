package threads;

import java.io.*;
import java.net.Socket;

import Servidor.PeticionAmigo;
import Servidor.Servidor;
import Servidor.Usuario;

/**
 * Hilo que se encarga de solucionar peticiones de amistad pendientes.
 */
public class PeticionesPendientes extends Thread
{
	// Constructor
	/**
	 * Crea una instancia de la clase.
	 */
	public PeticionesPendientes()
	{
		super();
	}
	
	// Metodos
	/**
	 * Revisa si hay solicitudes pendientes y las ejecuta cada 5 segundos.
	 */
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(5000);
				
				PeticionAmigo peticion = Servidor.nextPendingRequest();
				
				while(peticion != null)
				{
					if(Servidor.isConnected(peticion.para))
					{
						Usuario userPara = Servidor.getUsuario(peticion.para);
						Usuario userDe = Servidor.getUsuario(peticion.de);
						
						Socket canal = new Socket(userPara.getIP(), 2010);
						PrintWriter out = new PrintWriter(canal.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(canal.getInputStream()));
						
						out.println(userDe.getLog()+":"+userDe.getFrase());
						
						String respuesta = in.readLine();
						
						if(respuesta.equals("OK"))
						{
							Servidor.addFriendToUser(userPara, userDe);
							Servidor.addFriendToUser(userDe, userPara);
							
							int n = userPara.amigos.size();
							out.println("NUEVALISTA:" + n);
							n--;
							
							while(n >= 0)
							{
								Usuario amigo = Servidor.getUsuario(userPara.amigos.get(n));
								
								String conectado = "NO";
								
								if(Servidor.isConnected(amigo.getLog()))
									conectado = "SI";
								
								out.println(amigo.getLog()+":"+conectado+":"+amigo.getFrase());
								
								n--;
							}
							
							canal.close();
							
							if(Servidor.isConnected(userDe.getLog()))
							{
								canal = new Socket(userDe.getIP(), 2010);
								out = new PrintWriter(canal.getOutputStream(), true);
								in = new BufferedReader(new InputStreamReader(canal.getInputStream()));
								
								n = userDe.amigos.size();
								out.println("NUEVALISTA:" + n);
								n--;
								
								while(n >= 0)
								{
									Usuario amigo = Servidor.getUsuario(userDe.amigos.get(n));
									
									String conectado = "NO";
									
									if(Servidor.isConnected(amigo.getLog()))
										conectado = "SI";
									
									out.println(amigo.getLog()+":"+conectado+":"+amigo.getFrase());
									
									n--;
								}
								
								canal.close();
							}
						}
					}
					
					peticion = Servidor.nextPendingRequest();
				}
			}
			catch(Exception e)
			{
				System.out.println("Error al procesar las peticiones pendientes.");
				e.printStackTrace();
			}
		}
	}
}
