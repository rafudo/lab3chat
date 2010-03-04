package Servidor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Representa un usuario.
 */
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5814199195098737498L;

	/**
	 * Identificador del usuario. Debe ser unico para cada usuario.
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

	public ArrayList<String> grupos;

	/**
	 * Direccion IP del usuario.
	 */
	private String ip;

	/**
	 * Puerto
	 */
	private int port;

	// Constructor.
	/**
	 * Retora una instancia de la clase, con la IP y el canal en null.
	 */
	public Usuario(String nLog, String nPass, String nFrase) {
		log = nLog;
		pass = nPass;
		frase = nFrase;
		amigos = new ArrayList<String>();
		grupos = new ArrayList<String>();
	}

	// Metodos
	/**
	 * Retorna el log.
	 */
	public String getLog() {
		return log;
	}

	/**
	 * Retorna el pass.
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * Retorna la frase.
	 */
	public String getFrase() {
		return frase;
	}

	/**
	 * Retorna el IP
	 */
	public String getIP() {
		return ip;
	}

	/**
	 * Modifica el pass
	 */
	public void changePass(String nPass) {
		pass = nPass;
	}

	/**
	 * Modifica la frase
	 */
	public void changeFrase(String nFrase) {
		frase = nFrase;
	}

	/**
	 * Modifica IP
	 */
	public void setIP(String nIP) {
		ip = nIP;
	}

	/**
	 * Compara 2 Usuarios
	 */
	public boolean equals(Object arg0) {
		return log.equals(((Usuario) arg0).getLog());
	}

	public int getPort() {
		return port;
	}

	public void setPuerto(int parseInt) {
		port = parseInt;

	}

	public void setPass(String nPass) {
		pass = nPass;

	}

	public void setFrase(String nFrase) {
		frase=nFrase;
		
	}

}
