package es.ucm.fdi.tp.practica6;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {

	private Socket s;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public Connection(Socket s, int timeout) throws IOException {
		this.s = s;
		//this.s.setSoTimeout(timeout);
		this.out = new ObjectOutputStream(s.getOutputStream());
		this.in = new ObjectInputStream(s.getInputStream());
	}

	public void sendObject(Object r) throws IOException {
		out.writeObject(r);
		out.flush();
		out.reset();
	}

	public Object getObject() throws ClassNotFoundException, IOException {
		return in.readObject();
	}

	public void stop() throws IOException {
		s.close();
	}
}
