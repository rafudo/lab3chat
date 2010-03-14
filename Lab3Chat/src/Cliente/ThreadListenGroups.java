package Cliente;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import Servidor.Grupo;

public class ThreadListenGroups extends Thread {

	private Cliente me;
	private MulticastSocket in;

	public ThreadListenGroups(Cliente c) throws IOException {
		me = c;
		in = new MulticastSocket(2015);
	}

	@Override
	public void run() {

		Object[] gs = (Object[]) me.getGrupos().values().toArray();
		for (int i = 0; i < gs.length; i++) {
			try {
				in.joinGroup(InetAddress.getByName(((Grupo) gs[i]).getIp()));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		while (true) {
			try {
				byte[] rcv = new byte[1024];
				DatagramPacket dp = new DatagramPacket(rcv, 1024);

				in.receive(dp);

				String s = new String(dp.getData(), dp.getOffset(), dp
						.getLength());
				me.groupsMsg(s);
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

	public void joinGroup(String ip) throws UnknownHostException, IOException {
		in.joinGroup(InetAddress.getByName(ip));		
	}

	public void leaveGroup(String ip) throws UnknownHostException, IOException {
		in.leaveGroup(InetAddress.getByName(ip));		
		
	}

}
