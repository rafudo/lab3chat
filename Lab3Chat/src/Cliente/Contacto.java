package Cliente;

import java.net.Socket;

import conectividad.InputListener;



public class Contacto{
	private String frase;
	private String username;
	private String ip;
	private int port;
	

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

	

}
