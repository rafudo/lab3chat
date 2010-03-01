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
		
		
		
		/*
		 * line=br.readLine(); System.out.println(line); line=br.readLine();
		 * System.out.println(line);
		 */

		/*
		 * pw.println("HOLA AMIGUITO"); pw.println("3"); pw.println("andres");
		 * pw.println("SI"); pw.println("Soy andres teses"); pw.println("asdf");
		 * pw.println("kulimbis"); pw.println("NO"); pw.println("KULIMBISAD");
		 * pw.println("asdf"); pw.println("chicha"); pw.println("SI");
		 * pw.println("AFREINASS"); pw.println("asdf"); pw.println("0");
		 */

	}

}
