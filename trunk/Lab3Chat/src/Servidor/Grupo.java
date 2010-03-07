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
	 * Lista de Usuarios inscritos al grupo.
	 */
	private ArrayList<String> gente;
	
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
		gente = new ArrayList<String>();
		gente.add(owner);
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
	 * Agrega un usuario al grupo.
	 */
	public void add(String log)
	{
		gente.add(log);
	}
	
	/**
	 * Retorna la lista de inscritos al grupo.
	 */
	public ArrayList<String> darGente()
	{
		return gente;
	}
	
	/**
	 * Compara 2 grupos
	 */
	public boolean equals(Object arg0)
	{
		return owner.equals(((Grupo)arg0).owner);
	}
	
	/**
	 * Remueve un usuario del grupo
	 */
	public void remover(String log)
	{
		gente.remove(log);
	}
}
