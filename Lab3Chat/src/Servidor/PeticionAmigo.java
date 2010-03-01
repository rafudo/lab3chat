package Servidor;

/**
 * Representa la peticion de agregar a un usuario como amigo de otro.
 */
public class PeticionAmigo 
{
	// Atributos
	
	/**
	 * Quien hace la peticion
	 */
	public String de;
	
	/**
	 * Hacia quien es
	 */
	public String para;
	
	
	// Constructor
	/**
	 * Retorna una instancia de la clase.
	 */
	public PeticionAmigo(String nDe, String nPara)
	{
		de = nDe;
		para = nPara;
	}
	
	// Metodos
	/**
	 * Retorna una representacion string de la clase.
	 */
	public String toString()
	{
		return de+":"+para;
	}
}
