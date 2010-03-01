package Servidor;

import java.util.ArrayList;

/**
 * Representa un usuario.
 */
public class Usuario 
{
	// Atributos
	
	/**
	 * Identificador del usuario.
	 * Debe ser unico para cada usuario.
	 */
	private String log;
	
	/**
	 * Password de autenticacion.
	 */
	private String pass;
	
	/**
	 * Frase personal del usuario.
	 */
	private String frase;
	
	/**
	 * Lista de amigos, tipo usuario.
	 */
	public ArrayList<String> amigos;
	
	/**
	 * Lista de grupos a los que pertenece
	 */
	// Unimplemented
    // public ArrayList<Grupo> grupos.
	
	/**
	 * Direccion IP del usuario.
	 */
	private String ip;
	
	/**
	 * Puerto de escucha del usuario
	 */
	private int puerto;
	
	// Constructor.
	/**
	 * Retora una instancia de la clase, con la IP y el canal en null.
	 */
	public Usuario(String nLog, String nPass, String nFrase)
	{
		log = nLog;
		pass = nPass;
		frase = nFrase;
	}
	
	// Metodos
	/**
	 * Retorna el log.
	 */
	public String darLog()
	{
		return log;
	}
	
	/**
	 * Retorna el pass.
	 */
	public String darPass()
	{
		return pass;
	}
	
	/**
	 * Retorna la frase.
	 */
	public String darFrase()
	{
		return frase;
	}
	
	/**
	 * Retorna el IP
	 */
	public String darIP()
	{
		return ip;
	}
	
	/**
	 * Modifica el pass
	 */
	public void cambiarPass(String nPass)
	{
		pass = nPass;
	}
	
	/**
	 * Modifica la frase
	 */
	public void cambiarFrase(String nFrase)
	{
		frase = nFrase;
	}
	
	/**
	 * Modifica IP
	 */
	public void setIP(String nIP)
	{
		ip = nIP;
	}
	
	/**
	 * Compara 2 Usuarios
	 */
	public boolean equals(Object arg0)
	{
		return log.equals(((Usuario)arg0).darLog());
	}
	
	/**
	 * Retorna el puerto de escucha.
	 */
	public int darPuerto()
	{
		return puerto;
	}
	
	/**
	 * Modifica el puerto de escucha.
	 */
	public void setPuerto(int nPuerto)
	{
		puerto = nPuerto;
	}
}

