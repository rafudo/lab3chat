package Servidor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		servidor = Servidor.getInstance();
		Servidor.run();
		/*Usuario user = new Usuario("asd", "123", "Soy tesso");
		user.amigos.add("qwe");
		Usuario user2 = new Usuario("qwe", "123", "Soy tessonQWE");
		user2.amigos.add("asd");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./data/accounts/asd"));
		oos.writeObject(user);
		oos.close();
		oos = new ObjectOutputStream(new FileOutputStream("./data/accounts/qwe"));
		oos.writeObject(user2);
		oos.close();*/
		
	}

}
