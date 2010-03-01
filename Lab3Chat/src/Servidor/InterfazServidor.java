package Servidor;


import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import conectividad.Stream;

public class InterfazServidor {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		ServerSocket ss = new ServerSocket(2245);
		Socket s = ss.accept();

		

		String line = (String) Stream.receiveObject(s);
		line = (String) Stream.receiveObject(s);
		line = (String) Stream.receiveObject(s);

		Stream.sendObject(s, "HOLA AMIGUITO");
		Stream.sendObject(s, "3");
		Stream.sendObject(s, "andres");
		Stream.sendObject(s, "SI");
		Stream.sendObject(s, "Soy andres teses");
		Stream.sendObject(s, "127.0.0.1");
		Stream.sendObject(s, "kulimbis");
		Stream.sendObject(s, "NO");
		Stream.sendObject(s, "KULIMBISAD");
		Stream.sendObject(s, "127.0.0.1");
		Stream.sendObject(s, "chicha");
		Stream.sendObject(s, "SI");
		Stream.sendObject(s, "AFREINASS");
		Stream.sendObject(s, "127.0.0.1");
		Stream.sendObject(s, "0");

	}

}
