package Cliente;

import java.io.IOException;
import java.net.Socket;

import conectividad.Stream;

public class ThreadNotificaciones extends Thread {
	private Cliente me;
	private Socket in;

	public ThreadNotificaciones(Cliente me, Socket in) {
		this.in = in;
		this.me = me;

	}

	@Override
	public void run() {
		try {
			String line = (String) Stream.receiveObject(in);

			if (line.equals("CHARLA")) {
				assignChat();
			} else if (line.equals("CAMBIO")) {
				friendChangedStatus();

			} else if (line.equals("CONFAMIGO")) {

			}
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

	}

	private void assignChat() {
		try {
		
			Contacto c=me.getContacts().get((String) Stream.receiveObject(in));
			c.assingIn(in);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private void friendChangedStatus() {

		try {
			String log = (String) Stream.receiveObject(in);
			String con = (String) Stream.receiveObject(in);
			String frasec = (String) Stream.receiveObject(in);
			String ips = (String) Stream.receiveObject(in);
			int porto = (Integer) Stream.receiveObject(in);
			Contacto c = Contacto.crearContacto(log, ips, frasec, con, porto);
			in.close();
			me.modifyList(c);
			
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

	}

}
