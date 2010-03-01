package Cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class Cliente {
	private Vector<Contacto> contactos;
	
	public Cliente() throws UnknownHostException, IOException{
		
		Socket s = new Socket("localhost",2249);
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		

		pw.println("LOGIN");
		pw.println("Camilo");
		pw.println("123123123");
	
		String line =br.readLine();
		if(!line.equals("ERROR")){
			line=br.readLine();
			
			int n = Integer.parseInt(line);
			for(int i=0;i<n;i++){
				String log = br.readLine();
				String con = br.readLine();
				String frase = br.readLine();
				String ips= br.readLine();
				contactos.add(Contacto.crearContacto(log, ips, frase, con));
			}
			line=br.readLine();
		}



		
		
	}

	public void disconnect() {
		
	}

	public boolean isConnected() {

		return true;
	}

	public Vector<Contacto> getContacts() {		
		
		return contactos;
	}

}
