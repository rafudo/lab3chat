package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class InterfazServidor {
	
	public static void main(String[] args) throws IOException{
		ServerSocket ss = new ServerSocket(2249);
		Socket s = ss.accept();
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

	}

}
