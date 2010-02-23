package Cliente;



public class Contacto {
	private String frase;
	private String username;
	private byte[] ip;
	private boolean connected;
	
	public Contacto(String username, String frase, byte[] ip, boolean connected) {
		
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
	
	public byte[] getIp(){
		return ip;
	}

}
