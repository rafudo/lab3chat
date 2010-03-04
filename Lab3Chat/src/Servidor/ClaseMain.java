package Servidor;

/**
 * Clase main.
 */
public class ClaseMain 
{
	// Atributos
	/**
	 * Unica instancia de la clase servidor.
	 */
	public static Servidor servidor;
	
	/**
	 * Metodo main.
	 */
	public static void main(String[] args)
	{
		servidor = Servidor.getInstance();
		Servidor.run();
	}

}
