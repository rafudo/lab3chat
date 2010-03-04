package threads;

import java.net.Socket;

import Servidor.Servidor;
import Servidor.Usuario;
import conectividad.Stream;

public class NewFriendList extends Thread {

	private String log;

	public NewFriendList(String string) {
		log=string;
	}

	public void run() {
		if (Servidor.isConnected(log)) {
			try {
				Usuario user = Servidor.getUsuario(log);
				Socket cliente = new Socket(user.getIP(), user.getPort());

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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
