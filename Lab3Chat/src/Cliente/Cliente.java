package Cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import conectividad.Stream;

public class Cliente {
	private Vector<Contacto> contactos;
	
	public Cliente() throws UnknownHostException, IOException, InterruptedException{
		
		Socket s = new Socket("localhost",2245);
	/*	PrintWriter pw = new PrintWriter(s.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));*/
		
Stream.sendObject(s, "LOGIN");
		//pw.println("LOGIN\n");
	
		/*pw.println("Camilo");
		pw.println("123123123");*/
	
	/*	String line =br.readLine();
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
		}*/



		
		
	}

	public void disconnect() {
		
	}

	public boolean isConnected() {

		return true;
	}

	public Vector<Contacto> getContacts() {		
		Vector<Contacto> contacts = new Vector<Contacto>();
        ;
        contacts.add(new Contacto("Camilo","123123",new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255},true));
        contacts.add(new Contacto("Camilo","sdgsgsdg",new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255},false));
        contacts.add(new Contacto("Camilo","hj,hj,hj",new byte[]{(byte) 128,(byte) 255,(byte) 255,(byte) 255},true));
        contacts.add(new Contacto("Camilo","tyutyutyu",new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255},false));
        contacts.add(new Contacto("Camilo","yumyumyu",new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255},true));
        contacts.add(new Contacto("Camilo","iiuiyui",new byte[]{(byte) 255,(byte) 255,(byte) 255,(byte) 255},false));
		return contacts;
	}

}
