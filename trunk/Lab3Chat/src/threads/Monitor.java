package threads;

import java.net.*;
import Servidor.Servidor;

/**
 * Thread que verifica constantemente si el usuario se encuentra conectado.
 */
public class Monitor extends Thread
{
	// Atributos
	/**
	 * Log del Usuario
	 */
	private String log;
	
	/**
	 * IP del Usuario
	 */
	private String ip;
	
	/**
	 * Puerto de escucha del usuario.
	 */
	private int puerto;
	
	// Constructor
	/**
	 * Crea una instancia del thread.
	 */
	public Monitor(String nLog, String nIP, int nPuerto)
	{
		super();
		log = nLog;
		ip = nIP;
		puerto = nPuerto;
	}
	
	/**
	 * Envia una solicitud de coneccion al usuario para verificar si este esta conectado.
	 */
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.yield();
				Thread.sleep(1000000);
			}
			catch(Exception e)
			{}
			
			try
			{
				Socket temp = new Socket(ip, puerto);
				temp.close();
			}
			catch(UnknownHostException x)
			{
				Servidor.desconectar(log);
				break;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				break;
			}
		}
	}

}
