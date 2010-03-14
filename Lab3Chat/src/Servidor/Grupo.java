package Servidor;

import java.io.Serializable;

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
	 * nombre del grupo
	 */	
	private String id;
	/**
	 * IP tipo D del grupo.
	 */
	private String ip;
	
	// Constructor
	/**
	 * Crea una instancia de la clase.
	 */
	public Grupo(String nOwner, String nIp, String nombre)
	{
		owner = nOwner;
		ip = nIp;
		id=nombre;
		
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
	public String getIp()
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

	public String getId() {

		return id;
	}
	
	public String toString(){
		return id;
	}
	
	
	
	
	
}
