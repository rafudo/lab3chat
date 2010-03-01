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
	
	public static Contacto crearContacto(String log, String ips, String frase, String con){
		boolean connected  = con.equals("SI");
		byte[] ip = new byte[4];
		
		String[] laip=ips.split("\\.");
		
		ip[0]=Byte.parseByte(laip[0]);
		ip[1]=Byte.parseByte(laip[1]);
		ip[2]=Byte.parseByte(laip[2]);
		ip[3]=Byte.parseByte(laip[3]);
		
		return new Contacto(log,frase,ip, connected);
		
	}

}
