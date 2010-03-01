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
				pass(null, null);
			}

			else if (line.equals("FRASE")) {
				frase(null, null);
			}

			else if (line.equals("CHARLA")) {
				charla(null, null);
			} else if (line.equals("AMIGO")) {
				solicitud(null, null);
			}
			// TODO Grupo

			cliente.close();
		} catch (Exception e) {
			System.out.println("Error de comunicacion con el usuario.");
			e.printStackTrace();
		}

	}

	/**
	 * En caso que el usuario desee hacer login
	 */
	private boolean login() throws Exception {
		Usuario user = Servidor.darUsuario((String) Stream
				.receiveObject(cliente));

		if (user.darPass().equals((String) Stream.receiveObject(cliente))) {
			Stream.sendObject(cliente, user.darFrase());

			int n = user.amigos.size();
			Stream.sendObject(cliente, n);
			n--;

			while (n >= 0) {
				Usuario amigo = Servidor.darUsuario(user.amigos.get(n));

				String conectado = "NO";

				if (Servidor.estaConectado(amigo.darLog()))
					conectado = "SI";
				Stream.sendObject(cliente, amigo.darLog());
				Stream.sendObject(cliente, conectado);
				Stream.sendObject(cliente, amigo.darFrase());

				n--;
			}

			// TODO Grupo Pendiente
			Stream.sendObject(cliente, "0");

			user.setIP((String) Stream.receiveObject(cliente));
			user.setPuerto(Integer.parseInt((String) Stream
					.receiveObject(cliente)));

			Servidor.agregarConectado(user);

			Monitor moni = new Monitor(user.darLog(), user.darIP(), user
					.darPuerto());
			moni.start();

			return true;
		} else {
			Stream.sendObject(cliente, "ERROR");
			return false;
		}
	}

	/**
	 * En caso que se desee desconectar.
	 */
	private void chao() throws Exception {
		Usuario user = Servidor.darUsuario((String) Stream.receiveObject(cliente));
		Servidor.desconectar(user.darLog());
		Stream.sendObject(cliente, "CHAO");
		

		int n = user.amigos.size();
		n--;

		while (n >= 0) {
			if (Servidor.estaConectado(user.amigos.get(n))) {
				Usuario amigo = Servidor.darUsuario(user.amigos.get(n));
				Socket canal = new Socket(amigo.darIP(), amigo.darPuerto());
				

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
	 */
	private void pass(String[] partesMensaje, PrintWriter out) throws Exception {
		Usuario user = Servidor.darUsuario(partesMensaje[4]);

		if (partesMensaje[1].equals(user.darPass())
				&& partesMensaje[2].equals(partesMensaje[3])) {
			Servidor.cambioPass(user, partesMensaje[2]);
			out.println("OK");
		} else
			out.println("ERROR");

	}

	/**
	 * En caso que desee cambiar su frase.
	 */
	private void frase(String[] partesMensaje, PrintWriter out)
			throws Exception {
		Usuario user = Servidor.darUsuario(partesMensaje[4]);
		Servidor.cambioFrase(user, partesMensaje[1]);
		out.println("OK");
	}

	/**
	 * En caso que desee conversar con un amigo.
	 */
	private void charla(String[] partesMensaje, PrintWriter out)
			throws Exception {
		Usuario user = Servidor.darUsuario(partesMensaje[1]);

		if (Servidor.estaConectado(user.darLog()))
			out.println(user.darIP() + ":" + user.darPuerto());
		else
			out.println("ERROR");
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
