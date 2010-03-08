package Cliente;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;

import conectividad.InputListener;
import conectividad.Stream;

public class Contacto implements InputListener {
	private String frase;
	private String username;
	private String ip;
	private int port;
	private Thread reader;

	private Socket in;
	private Socket out;
	private DiagChat chat;
	private Hashtable<Integer, File> archivos;
	private int fileNumber;
	private boolean connected;

	public Contacto(String username, String frase, String ip,
			boolean connected, int porto) {
		this.fileNumber = 100;
		archivos = new Hashtable<Integer, File>();
		this.frase = frase;
		this.username = username;
		this.ip = ip;
		this.connected = connected;
		this.port = porto;
	}

	public Contacto() {

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

	public String getIp() {
		return ip;
	}

	public static Contacto crearContacto(String log, String ips, String frase,
			String con, int porto) {
		boolean connected = con.equals("SI");
		System.out.println(ips + " / " + log + " / " + frase + " / " + con
				+ " / " + porto);
		return new Contacto(log, frase, ips, connected, porto);

	}

	public void assingIn(Socket ini) {

		if (reader == null || !reader.isAlive()) {
			in = ini;
			reader = Stream.listenSocket(in, this);
			reader.start();
			System.gc();
		}

	}

	@Override
	public void disconnected(Socket s, Throwable e) {
		try {
			s.close();
			System.out.println("socket cerrado con normalidad");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public boolean sendMsg(String msg) {
		try {
			if (out == null || out.isClosed()) {
				out = new Socket(ip, port);
				Stream.sendObject(out, "CHARLA");
				Stream.sendObject(out, System.getProperty("username"));
			}
			Stream.sendObject(out, "C" + msg);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void update(Object o) {
		if (chat == null) {
			chat = new DiagChat(this);
			chat.setVisible(true);
		}
		String msg = (String) o;
		System.out.println(msg);
		if (msg.startsWith("C"))
			chat.append(msg.substring(1));
		else if (msg.startsWith("F")) {
			chat.askReiciveFile(Integer.parseInt(msg.substring(1, 4)), msg
					.substring(4));
		}else if (msg.startsWith("R")){
			
			if(msg.charAt(4)=='Y'){
				int id = Integer.parseInt(msg.substring(1, 4));
				File f = archivos.remove(id);
				chat.msg("//"+username+" ha aceptado el archivo.");
				(new ThreadSendFile(this,f)).start();
			}else{
				chat.msg("//"+username+" ha aceptado el archivo.");
			}
		}

	}

	public void setWindow(DiagChat diagChat) {
		chat = diagChat;
		System.gc();

	}

	public DiagChat getWindow() {
		return chat;

	}

	public void close() {

		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			System.out.println("cambiar por e.printstacjtrac");
		}
	}

	public void openWindow() {
		if (chat == null) {
			chat = new DiagChat(this);
			chat.setVisible(true);
		}

	}

	public void askSendFile(File f) {
		try {
			if (out == null || out.isClosed()) {
				out = new Socket(ip, port);
				Stream.sendObject(out, "CHARLA");
				Stream.sendObject(out, System.getProperty("username"));
			}
			
			archivos.put(fileNumber, f);
			System.out.println(f);
			Stream.sendObject(out, "F" + fileNumber + f.getName());
			fileNumber = (fileNumber == 999) ? 100 : fileNumber + 1;
	
		} catch (IOException e) {
			e.printStackTrace();
		
		}

	}

	public void reiciveFile(int fileid, String file, boolean recibir) {
		
		try {
			if (out == null || out.isClosed()) {
				out = new Socket(ip, port);
				Stream.sendObject(out, "CHARLA");
				Stream.sendObject(out, System.getProperty("username"));
			}
			if (recibir) {
				Stream.sendObject(out, "R" + fileid + "Y");
			} else {
				Stream.sendObject(out, "R" + fileid + "N");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
