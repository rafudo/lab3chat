package threads;

import java.io.*;
import java.net.Socket;

import conectividad.Stream;

import Servidor.PeticionAmigo;
import Servidor.Servidor;
import Servidor.Usuario;

/**
 * Hilo que se encarga de atender un nuevo cliente
 */
public class Atender extends Thread {
	// Atributos
	/**
	 * Canal de Comunicacion con el cliente.
	 */
	private Socket cliente;

	// Constructor
	/**
	 * Retorna una instancia del thread.
	 */
	public Atender(Socket canal) {
		super();
		cliente = canal;
	}

	/**
	 * Atiende al Usuario segun lo que este requiera.
	 */
	public void run() {
		try {

			String line = (String) Stream.receiveObject(cliente);

			if (line.equals("LOGIN")) {
				login();
			}

			else if (line.equals("CHAO")) {
				chao();
			}

			else if (line.equals("PASS")) {
				pass();
			}

			else if (line.equals("FRASE")) {
				frase();
			}
			 else if (line.equals("AMIGO")) {
				solicitud(null, null);
			}else if(line.equals("NEW")){
				newAccount();
			}
			// TODO Grupo

			cliente.close();
		} catch (Exception e) {
			System.out.println("Error de comunicacion con el usuario.");
			e.printStackTrace();
		}

	}
/**
 * Crea un nuevo usuario
 */
	private void newAccount() {
		try {
			String login =(String)Stream.receiveObject(cliente);
			String pss ="";
			String confpss="";
			if(!Servidor.existe(login)){
				Stream.sendObject(cliente, "OK");
				 pss =(String)Stream.receiveObject(cliente);
				 confpss =(String)Stream.receiveObject(cliente);
				if(pss.equals(confpss)){
					Stream.sendObject(cliente, "OK");
					confpss =(String)Stream.receiveObject(cliente);
					Usuario asd = new Usuario(login, pss, confpss);
					Servidor.agregarUsuario(asd);
				}else{
					Stream.sendObject(cliente, "ERROR");
				}
			}else{
				Stream.sendObject(cliente, "ERROR");
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		
			e.printStackTrace();
		}
		
	}

	/**
	 * En caso que el usuario desee hacer login
	 */
	private boolean login() throws Exception {
		Usuario user = Servidor.darUsuario((String) Stream.receiveObject(cliente));

		if (user!=null&&user.darPass().equals((String) Stream.receiveObject(cliente))) {
			Stream.sendObject(cliente, user.darFrase());

			int n = user.amigos.size();
			Stream.sendObject(cliente, ""+n);
			n--;

			while (n >= 0) {
				Usuario amigo = Servidor.darUsuario(user.amigos.get(n));

				String conectado = "NO";

				if (Servidor.estaConectado(amigo.darLog()))
					conectado = "SI";
				Stream.sendObject(cliente, amigo.darLog());
				Stream.sendObject(cliente, conectado);
				Stream.sendObject(cliente, amigo.darFrase());
				if(conectado.equals("SI")){
				Stream.sendObject(cliente,amigo.darIP());}
				else Stream.sendObject(cliente, "0");

				n--;
			}

			Stream.sendObject(cliente, "0");

			user.setIP((String) Stream.receiveObject(cliente));
			user.setPuerto(Integer.parseInt((String) Stream.receiveObject(cliente)) );

			Servidor.agregarConectado(user);

			Monitor moni = new Monitor(user.darLog(), user.darIP(), user.darPort());
			moni.start();
			cliente.close();
			return true;
		} else {
			Stream.sendObject(cliente, "ERROR");
			cliente.close();
			return false;
		}
	}

	/**
	 * En caso que se desee desconectar.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	private void chao() throws IOException, ClassNotFoundException {
		Usuario user = Servidor.darUsuario((String) Stream.receiveObject(cliente));
		Servidor.desconectar(user.darLog());
		Stream.sendObject(cliente, "CHAO");
		

		int n = user.amigos.size();
		n--;

		while (n >= 0) {
			if (Servidor.estaConectado(user.amigos.get(n))) {
				Usuario amigo = Servidor.darUsuario(user.amigos.get(n));
				Socket canal = new Socket(amigo.darIP(), 2010);
				

				int m = amigo.amigos.size();
				Stream.sendObject(canal, "NUEVALISTA");
				Stream.sendObject(canal, m);
				
				m--;

				while (m >= 0) {
					Usuario amigoAmigo = Servidor.darUsuario(amigo.amigos
							.get(m));

					String conectado = "NO";

					if (Servidor.estaConectado(amigoAmigo.darLog()))
						conectado = "SI";
					Stream.sendObject(canal,amigoAmigo.darLog());
					Stream.sendObject(canal,conectado);
					Stream.sendObject(canal,amigoAmigo.darFrase());
					

					m--;
				}

				canal.close();
			}

			n--;
		}

	}

	/**
	 * En caso que desee cambiar su pass.
	 * PASS:<LOG>:<PASSACTUAL>:<NUEVAPASS>:<CONF>
	 */
	private void pass() throws Exception {
		Usuario user = Servidor.darUsuario((String)Stream.receiveObject(cliente));
		String ps;
		if (((String)Stream.receiveObject(cliente)).equals(user.darPass())
				&& (ps=(String)Stream.receiveObject(cliente)).equals(((String)Stream.receiveObject(cliente)))) {
			try{Servidor.cambioPass(user,ps);
			}catch(Exception e){
				Stream.sendObject(cliente, "ERROR");
			}
			
			Stream.sendObject(cliente, "OK");
		} else
			Stream.sendObject(cliente, "ERROR");

	}

	/**
	 * En caso que desee cambiar su frase.
	 * FRASE:<LOG>:<PASS>:<NUEVAFRASE>
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	private void frase() throws IOException, ClassNotFoundException
			 {
		Usuario user = Servidor.darUsuario((String)Stream.receiveObject(cliente));
		if(user.darPass().equals((String)Stream.receiveObject(cliente)))
		Servidor.cambioFrase(user, (String)Stream.receiveObject(cliente));
		Stream.sendObject(cliente, "OK");
	}

	

	/**
	 * En caso que desee realizar una solicitud de amistad.
	 */
	private void solicitud(String[] partesMensaje, PrintWriter out)
			throws Exception {
		if (Servidor.darUsuario(partesMensaje[1]) != null) {
			PeticionAmigo temp = new PeticionAmigo(partesMensaje[2],
					partesMensaje[1]);
			Servidor.agregarPeticionAmistad(temp);
			out.println("OK");
		} else
			out.println("ERROR");
	}
}
