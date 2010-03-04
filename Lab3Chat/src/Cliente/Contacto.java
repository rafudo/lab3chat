package Cliente;

import java.net.Socket;

import conectividad.InputListener;



public class Contacto implements InputListener{
	private String frase;
	private String username;
	private String ip;
	private boolean connected;
	
	public Contacto(String username, String frase, String ip, boolean connected) {
		
		this.frase = frase;
		this.username = username;
		this.ip = ip;
		this.connected = connected;
	}

	public Contacto	(){
		
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
	
	public static Contacto crearContacto(String log, String ips, String frase, String con){
		boolean connected  = con.equals("SI");		
		System.out.println(ips);
		return new Contacto(log,frase,ips, connected);
		
	}

	@Override
	public void disconnected(Socket arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Object arg0) {
		// TODO Auto-generated method stub
		
	}

}
