package threads;

import Servidor.Servidor;
import Servidor.Usuario;
import conectividad.Stream;

public class NewFriendList extends Thread{
	
	
	public static void newFriendList(String log) {
		if (isConnected(log)) {
			Usuario user = servidor.conectados.get(log);
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
		}

	}


}
