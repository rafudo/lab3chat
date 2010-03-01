package Servidor;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import conectividad.Stream;

public class InterfazServidor {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		ServerSocket ss = new ServerSocket(2245);
		Socket s = ss.accept();

		/*
		 * PrintWriter pw = new PrintWriter(s.getOutputStream()); BufferedReader
		 * br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		 */

		String line = (String) Stream.receiveObject(s);
		line = (String) Stream.receiveObject(s);
		line = (String) Stream.receiveObject(s);

		Stream.sendObject(s, "HOLA AMIGUITO");
		Stream.sendObject(s, "3");
		Stream.sendObject(s, "andres");
		Stream.sendObject(s, "SI");
		Stream.sendObject(s, "Soy andres teses");
		Stream.sendObject(s, "asdf");
		Stream.sendObject(s, "kulimbis");
		Stream.sendObject(s, "NO");
		Stream.sendObject(s, "KULIMBISAD");
		Stream.sendObject(s, "asdf");
		Stream.sendObject(s, "chicha");
		Stream.sendObject(s, "SI");
		Stream.sendObject(s, "AFREINASS");
		Stream.sendObject(s, "asdf");
		Stream.sendObject(s, "0");

	}

}
