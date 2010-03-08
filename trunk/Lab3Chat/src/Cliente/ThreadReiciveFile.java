package Cliente;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import conectividad.Stream;

public class ThreadReiciveFile extends Thread {
	private Socket in;
	private Cliente me;

	public ThreadReiciveFile(Socket in, Cliente me) {

		this.in = in;
		this.me = me;
	}

	@Override
	public void run() {
		try {
			String con = (String) Stream.receiveObject(in);
			if (me.getContacts().get(con) != null) {
				Stream.sendObject(in, "OK");
				con = (String) Stream.receiveObject(in);
				File f = new File(con);
				
					f = newName(f);
				
				FileOutputStream fos = new FileOutputStream(f);
				InputStream is = in.getInputStream();
				try {
					int n = is.read();
					while(n!=-1) {
						fos.write(n);
						n= is.read();
					} 
					fos.close();
					in.close();
				} catch (Exception e) {
					fos.close();
					in.close();
				}
			} else {
				Stream.sendObject(in, "ERROR");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private File newName(File f) {
		if(f.exists()){
			return newName(new File("N"+f.getName()));
		}else{
			return f;
		}
		
	}

}
