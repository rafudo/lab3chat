package Cliente;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import conectividad.Stream;

public class ThreadSendFile extends Thread {

	private File f;
	private Contacto c;

	public ThreadSendFile(Contacto c, File f) {

		this.f = f;
		this.c = c;
	}

	@Override
	public void run() {
		try {
			Socket send = new Socket(c.getIp(), c.getPort());
			Stream.sendObject(send, "FILE");
			Stream.sendObject(send, System.getProperty("username"));
			if (Stream.receiveObject(send).equals("OK")) {
				
				Stream.sendObject(send, f.getName());
				FileInputStream fis = new FileInputStream(f);
				OutputStream os = send.getOutputStream();
				try {
					int n=fis.read();
					while(n!=-1) {						
						os.write(n);
						n= fis.read();
					}
					os.close();
					fis.close();
				} catch (Exception e) {
					
					os.close();
					fis.close();
				}
			}
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

	}

}
