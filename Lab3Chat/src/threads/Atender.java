package threads;

import java.io.*;

import java.net.Socket;

import java.util.Hashtable;

import conectividad.Stream;

import Servidor.Grupo;


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
			} else if (line.equals("FRIEND")) {
				solicitud();
			} else if (line.equals("NEW")) {
				newAccount();
			} else if (line.equals("JOINGROUP")) {
				unirGrupo();
			} else if (line.equals("LEAVEGROUP")) {
				salirGrupo();
			} else if (line.equals("NEWGROUP")) {
				crearGrupo();
			} else if (line.equals("LISTGROUP")) {
				listaGrupos();
			}

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
			String login = (String) Stream.receiveObject(cliente);

			if (!Servidor.exists(login)) {
				Stream.sendObject(cliente, "OK");
				String pss = (String) Stream.receiveObject(cliente);

				Usuario asd = new Usuario(login, pss, "");
				Servidor.addUser(asd);
			} else {
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
		Usuario user = Servidor.getUsuario((String) Stream
				.receiveObject(cliente));

		if (user != null
				&& !Servidor.isConnected(user.getLog())
				&& user.getPass()
						.equals((String) Stream.receiveObject(cliente))) {

			Stream.sendObject(cliente, user.getFrase());

			int n = user.amigos.size();
			Stream.sendObject(cliente, "" + n);

			for (int i = 0; i < n; i++) {
				Usuario amigo = Servidor.getUsuario(user.amigos.get(i));

				String conectado = "NO";

				if (Servidor.isConnected(amigo.getLog()))
					conectado = "SI";
				Stream.sendObject(cliente, amigo.getLog());
				Stream.sendObject(cliente, conectado);
				Stream.sendObject(cliente, amigo.getFrase());
				if (conectado.equals("SI")) {
					Stream.sendObject(cliente, amigo.getIP());
					Stream.sendObject(cliente, amigo.getPort());
				} else {
					Stream.sendObject(cliente, "0");
					Stream.sendObject(cliente, 0);
				}

			}

			n = user.getGrupos().size();
			Stream.sendObject(cliente, "" + n);
			for (int i = 0; i < n; i++) {
				String ip = user.getGrupos().get(i);
				Grupo g = Servidor.getGrupos().get(ip);
				if(g!=null){
					Stream.sendObject(cliente, g.getOwner());
					Stream.sendObject(cliente, ip);
					Stream.sendObject(cliente, g.getId());
				}else{
					Stream.sendObject(cliente, "");
					Stream.sendObject(cliente, "D"+ip);
					Stream.sendObject(cliente, "");
				}
				
			}

			user.setIP((String) Stream.receiveObject(cliente));
			user.setPuerto(Integer.parseInt((String) Stream
					.receiveObject(cliente)));

			Servidor.addConnected(user);

			for (int i = 0; i < user.amigos.size(); i++) {
				informar(user.amigos.get(i), user);
			}
			

			return true;
		} else {
			Stream.sendObject(cliente, "ERROR");
			return false;
		}
	}
	private void informar(String destiny, Usuario source){
		if (Servidor.isConnected(destiny)) {
			System.out.println("Esta enviando las notificaciones");
			try {
				Usuario user = Servidor.getUsuario(destiny);
				Socket cliente = new Socket(user.getIP(), user.getPort());
				Stream.sendObject(cliente, "CAMBIO");

				String conectado = "NO";

				if (Servidor.isConnected(source.getLog()))
					conectado = "SI";
				Stream.sendObject(cliente, source.getLog());
				Stream.sendObject(cliente, conectado);
				Stream.sendObject(cliente, source.getFrase());
				if (conectado.equals("SI")) {
					Stream.sendObject(cliente, source.getIP());
					Stream.sendObject(cliente, source.getPort());
				} else {
					Stream.sendObject(cliente, "0");
					Stream.sendObject(cliente, 0);
				}

				cliente.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * En caso que se desee desconectar.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void chao() throws IOException, ClassNotFoundException {
		Usuario user = Servidor.getUsuario((String) Stream
				.receiveObject(cliente));
		Servidor.disconnect(user.getLog());
		Stream.sendObject(cliente, "CHAO");

		for (int i = 0; i < user.amigos.size(); i++) {
			informar(user.amigos.get(i), user);
		}

	}

	/**
	 * En caso que desee cambiar su pass.
	 * PASS:<LOG>:<PASSACTUAL>:<NUEVAPASS>:<CONF>
	 */
	private void pass() throws Exception {
		Usuario user = Servidor.getUsuario((String) Stream
				.receiveObject(cliente));

		if (((String) Stream.receiveObject(cliente)).equals(user.getPass())) {
			try {
				Servidor.changePass(user, (String) Stream
						.receiveObject(cliente));
			} catch (Exception e) {
				Stream.sendObject(cliente, "ERROR");
			}

			Stream.sendObject(cliente, "OK");
		} else
			Stream.sendObject(cliente, "ERROR");

	}

	/**
	 * En caso que desee cambiar su frase. FRASE:<LOG>:<PASS>:<NUEVAFRASE>
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void frase() throws IOException, ClassNotFoundException {
		Usuario user = Servidor.getUsuario((String) Stream
				.receiveObject(cliente));
		if (user != null
				&& user.getPass()
						.equals((String) Stream.receiveObject(cliente))) {
			Servidor.changeFrase(user, (String) Stream.receiveObject(cliente));
			Stream.sendObject(cliente, "OK");
		} else
			Stream.sendObject(cliente, "ERROR");
		for (int i = 0; i < user.amigos.size(); i++) {
			informar(user.amigos.get(i), user);
		}
	}

	/**
	 * En caso que desee realizar una solicitud de amistad.
	 */
	private void solicitud() throws Exception {

		Usuario user = Servidor.getUsuario((String) Stream
				.receiveObject(cliente));
		Usuario amigo = Servidor.getUsuario((String) Stream
				.receiveObject(cliente));

		if (amigo != null && user != null) {
			Servidor.addFriendToUser(user, amigo);
			Stream.sendObject(cliente, "OK");
			if (Servidor.isConnected(user.getLog())) {
				try {

					Socket cliente = new Socket(user.getIP(), user.getPort());
					Stream.sendObject(cliente, "CAMBIO");

					String conectado = "NO";

					if (Servidor.isConnected(amigo.getLog()))
						conectado = "SI";
					Stream.sendObject(cliente, amigo.getLog());
					Stream.sendObject(cliente, conectado);
					Stream.sendObject(cliente, amigo.getFrase());
					if (conectado.equals("SI")) {
						Stream.sendObject(cliente, amigo.getIP());
						Stream.sendObject(cliente, amigo.getPort());
					} else {
						Stream.sendObject(cliente, "0");
						Stream.sendObject(cliente, 0);
					}

					cliente.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (Servidor.isConnected(amigo.getLog())) {
					try {

						Socket cliente = new Socket(amigo.getIP(), amigo
								.getPort());
						Stream.sendObject(cliente, "CAMBIO");

						String conectado = "NO";

						if (Servidor.isConnected(user.getLog()))
							conectado = "SI";
						Stream.sendObject(cliente, user.getLog());
						Stream.sendObject(cliente, conectado);
						Stream.sendObject(cliente, user.getFrase());
						if (conectado.equals("SI")) {
							Stream.sendObject(cliente, user.getIP());
							Stream.sendObject(cliente, user.getPort());
						} else {
							Stream.sendObject(cliente, "0");
							Stream.sendObject(cliente, 0);
						}

						cliente.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else
			Stream.sendObject(cliente, "ERROR");
	}

	/**
	 * Desea unirse a un grupo
	 */
	private void unirGrupo() throws IOException, ClassNotFoundException {
		Usuario user = Servidor.getUsuario((String) Stream
				.receiveObject(cliente));
		String ip = (String) Stream.receiveObject(cliente);		
		if(Servidor.joinGroup(user, ip))
		Stream.sendObject(cliente, "OK");
		else
			Stream.sendObject(cliente, "ERROR");	
	}

	/**
	 * Desea crear un grupo.
	 */
	private void crearGrupo() throws IOException, ClassNotFoundException {
		Usuario user = Servidor.getUsuario((String) Stream
				.receiveObject(cliente));
		String nombre  = (String) Stream.receiveObject(cliente);

		Hashtable<String, Grupo> list = Servidor.getGrupos();
		
			int num1 = (int) Math.random() * 256;
			int num2 = (int) Math.random() * 256;
			while(list.containsKey("239.233."+num1+"."+num2)){
				num1 = (int) Math.random() * 256;
				num2 = (int) Math.random() * 256;
			}
			Stream.sendObject(cliente, "OK");
			String ip="239.233."+num1+"."+num2;
			Stream.sendObject(cliente, ip);
			
			Grupo group = new Grupo(user.getLog(), ip,nombre);
			list.put(ip, group);
			user.addGrupo(ip);
			
			Stream.sendObject(cliente, "OK");
			Servidor.joinGroup(user, ip);
			Servidor.saveGroups();
				
			
		
	}

	/**
	 * Desea salirse de un grupo.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	private void salirGrupo() throws IOException, ClassNotFoundException {
		Usuario user = Servidor.getUsuario((String)Stream.receiveObject(cliente));
		String ip =(String)Stream.receiveObject(cliente);
		if(Servidor.leaveGroup(user, ip))
			Stream.sendObject(cliente, "OK");
			else
				Stream.sendObject(cliente, "ERROR");	
	}

	/**
	 * Desea un listado de grupos
	 */
	private void listaGrupos() throws IOException{
		Object[] grups= Servidor.getGrupos().values().toArray();
		Stream.sendObject(cliente, ""+grups.length);
		for(int i=0;i<grups.length;i++){
			Grupo g = (Grupo) grups[i];
			Stream.sendObject(cliente, g.getOwner());
			Stream.sendObject(cliente, g.getIp());
			Stream.sendObject(cliente, g.getId());
		}
		
	}

}

