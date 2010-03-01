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
				
				PeticionAmigo peticion = Servidor.siguientePeticionPendiente();
				
				while(peticion != null)
				{
					if(Servidor.estaConectado(peticion.para))
					{
						Usuario userPara = Servidor.darUsuario(peticion.para);
						Usuario userDe = Servidor.darUsuario(peticion.de);
						
						Socket canal = new Socket(userPara.darIP(), 2010);
						PrintWriter out = new PrintWriter(canal.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(canal.getInputStream()));
						
						out.println(userDe.darLog()+":"+userDe.darFrase());
						
						String respuesta = in.readLine();
						
						if(respuesta.equals("OK"))
						{
							Servidor.agregarleUnAmigo(userPara, userDe);
							Servidor.agregarleUnAmigo(userDe, userPara);
							
							int n = userPara.amigos.size();
							out.println("NUEVALISTA:" + n);
							n--;
							
							while(n >= 0)
							{
								Usuario amigo = Servidor.darUsuario(userPara.amigos.get(n));
								
								String conectado = "NO";
								
								if(Servidor.estaConectado(amigo.darLog()))
									conectado = "SI";
								
								out.println(amigo.darLog()+":"+conectado+":"+amigo.darFrase());
								
								n--;
							}
							
							canal.close();
							
							if(Servidor.estaConectado(userDe.darLog()))
							{
								canal = new Socket(userDe.darIP(), 2010);
								out = new PrintWriter(canal.getOutputStream(), true);
								in = new BufferedReader(new InputStreamReader(canal.getInputStream()));
								
								n = userDe.amigos.size();
								out.println("NUEVALISTA:" + n);
								n--;
								
								while(n >= 0)
								{
									Usuario amigo = Servidor.darUsuario(userDe.amigos.get(n));
									
									String conectado = "NO";
									
									if(Servidor.estaConectado(amigo.darLog()))
										conectado = "SI";
									
									out.println(amigo.darLog()+":"+conectado+":"+amigo.darFrase());
									
									n--;
								}
								
								canal.close();
							}
						}
					}
					
					peticion = Servidor.siguientePeticionPendiente();
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
