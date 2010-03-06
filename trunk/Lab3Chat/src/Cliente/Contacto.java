package Cliente;

import java.io.IOException;
import java.net.Socket;

import conectividad.InputListener;



public class Contacto implements InputListener{
	private String frase;
	private String username;
	private String ip;
	private int port;
	private Thread in;
	private Socket out;
	private DiagChat chat;

	private boolean connected;
	
	public Contacto(String username, String frase, String ip, boolean connected, int porto) {
		
		this.frase = frase;
		this.username = username;
		this.ip = ip;
		this.connected = connected;
		this.port=porto;
	}

	public Contacto	(){
		
	}
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public String getFrase() {

		return frase;
	}

	public boolean isConnected() {

		return connected;
	}
	
	public String getIp(){
		return ip;
	}
	
	public static Contacto crearContacto(String log, String ips, String frase, String con, int porto){
		boolean connected  = con.equals("SI");		
		System.out.println(ips+" / "+log+" / "+frase+" / "+con+" / "+porto);
		return new Contacto(log,frase,ips, connected, porto);
		
	}

	public void assingIn(Socket ini) {
		
		
		
	}

	@Override
	public void disconnected(Socket s, Throwable e) {
		try {
			s.close();
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	@Override
	public void update(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	

}
