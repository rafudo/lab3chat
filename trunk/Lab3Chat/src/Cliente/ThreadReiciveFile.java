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
				if (f.exists()) {
					f = newName(f);
				}
				FileOutputStream fos = new FileOutputStream(f);
				InputStream is = in.getInputStream();
				try {
					int n;
					do {
						n = is.read();
						fos.write(n);
					} while (n != -1);
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
		// TODO Auto-generated method stub
		return null;
	}

}
