package threads;

import java.net.Socket;

import Servidor.Servidor;
import Servidor.Usuario;
import conectividad.Stream;

public class FriendStatusChanged extends Thread {

	private Usuario source;
	private String destiny;

	public FriendStatusChanged(String dst, Usuario src) {
		source = src;
		destiny = dst;
	}

	public void run() {
		if (Servidor.isConnected(destiny)) {
			System.out.println("Esta enviando las notificaciones");
			try {
				Usuario user = Servidor.getUsuario(destiny);
				Socket cliente = new Socket(user.getIP(), user.getPort());
				Stream.sendObject(cliente, "NUEVALISTA");

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

}
