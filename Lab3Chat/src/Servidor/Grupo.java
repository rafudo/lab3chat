package Servidor;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Representa un grupo de chat.
 */
public class Grupo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -65142632930191503L;

	// Atributos
	
	/**
	 * El usuario dueno y creador del grupo
	 */
	private String owner;
	
	
	
	/**
	 * IP tipo D del grupo.
	 */
	private InetAddress ip;
	
	// Constructor
	/**
	 * Crea una instancia de la clase.
	 */
	public Grupo(String nOwner, InetAddress nIp)
	{
		owner = nOwner;
		ip = nIp;
		
	}
	
	// Metodos
	
	/**
	 * Retorna el dueno.
	 */
	public String getOwner()
	{
		return owner;
	}
	
	/**
	 * Retorna la ip del grupo
	 */
	public InetAddress getIp()
	{
		return ip;
	}
	
	
	/**
	 * Compara 2 grupos
	 */
	public boolean equals(Object arg0)
	{
		return ip.equals(((Grupo)arg0).getIp());
	}
	
	
}
