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
			if(!Servidor.exist(login)){
				Stream.sendObject(cliente, "OK");
				 pss =(String)Stream.receiveObject(cliente);
				 confpss =(String)Stream.receiveObject(cliente);
				if(pss.equals(confpss)){
					Stream.sendObject(cliente, "OK");
					confpss =(String)Stream.receiveObject(cliente);
					Usuario asd = new Usuario(login, pss, confpss);
					Servidor.addUser(asd);
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
		Usuario user = Servidor.getUsuario((String) Stream.receiveObject(cliente));

		if (user!=null&&!Servidor.isConnected(user.getLog())&&user.getPass().equals((String) Stream.receiveObject(cliente))) {
	
			
			Stream.sendObject(cliente, user.getFrase());

			int n = user.amigos.size();
			Stream.sendObject(cliente, ""+n);
			

			for(int i=0;i<n;i++){
				Usuario amigo = Servidor.getUsuario(user.amigos.get(i));

				String conectado = "NO";

				if (Servidor.isConnected(amigo.getLog()))
					conectado = "SI";
				Stream.sendObject(cliente, amigo.getLog());
				Stream.sendObject(cliente, conectado);
				Stream.sendObject(cliente, amigo.getFrase());
				if(conectado.equals("SI")){
				Stream.sendObject(cliente,amigo.getIP());
				Stream.sendObject(cliente,amigo.getPort());
				}
				else {Stream.sendObject(cliente, "0");
				Stream.sendObject(cliente, 0);}

				
			}

			Stream.sendObject(cliente, "0");

			user.setIP((String) Stream.receiveObject(cliente));
			user.setPuerto(Integer.parseInt((String) Stream.receiveObject(cliente)) );

			Servidor.addConnected(user);
			for(int i=0;i<user.amigos.size();i++)
			{
				(new FriendStatusChanged(user.amigos.get(i),user)).start();
			}
			Monitor moni = new Monitor(user.getLog(), user.getIP(), user.getPort());
			moni.start();
			
			return true;
		} else {
			Stream.sendObject(cliente, "ERROR");
		
			return false;
		}
	}

	/**
	 * En caso que se desee desconectar.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	private void chao() throws IOException, ClassNotFoundException {
		Usuario user = Servidor.getUsuario((String) Stream.receiveObject(cliente));
		Servidor.disconnect(user.getLog());
		Stream.sendObject(cliente, "CHAO");
		

		for(int i=0;i<user.amigos.size();i++)
		{
			(new FriendStatusChanged(user.amigos.get(i),user)).start();
		}
		

	}

	/**
	 * En caso que desee cambiar su pass.
	 * PASS:<LOG>:<PASSACTUAL>:<NUEVAPASS>:<CONF>
	 */
	private void pass() throws Exception {
		Usuario user = Servidor.getUsuario((String)Stream.receiveObject(cliente));
		String ps;
		if (((String)Stream.receiveObject(cliente)).equals(user.getPass())
				&& (ps=(String)Stream.receiveObject(cliente)).equals(((String)Stream.receiveObject(cliente)))) {
			try{Servidor.changePass(user,ps);
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
		Usuario user = Servidor.getUsuario((String)Stream.receiveObject(cliente));
		if(user.getPass().equals((String)Stream.receiveObject(cliente)))
		Servidor.changeFrase(user, (String)Stream.receiveObject(cliente));
		Stream.sendObject(cliente, "OK");
	}

	

	/**
	 * En caso que desee realizar una solicitud de amistad.
	 */
	private void solicitud(String[] partesMensaje, PrintWriter out)
			throws Exception {
		if (Servidor.getUsuario(partesMensaje[1]) != null) {
			PeticionAmigo temp = new PeticionAmigo(partesMensaje[2],
					partesMensaje[1]);
			Servidor.addFriendRequest(temp);
			out.println("OK");
		} else
			out.println("ERROR");
	}
}
