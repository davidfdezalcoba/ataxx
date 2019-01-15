package es.ucm.fdi.tp.practica6.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Represents the connection between server and client. This class allows us to
 * send/receive objects using the given socket.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class Connection {

	private Socket s;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	/**
	 * Initializes the input and output streams given the socket.
	 * 
	 * @param s
	 *            The socket
	 * @throws IOException
	 */
	public Connection(Socket s) throws IOException {
		this.s = s;
		this.out = new ObjectOutputStream(s.getOutputStream());
		this.in = new ObjectInputStream(s.getInputStream());
	}

	/**
	 * Writes an object into this connection's socket output stream
	 * 
	 * @param r
	 *            The object to send
	 * @throws IOException
	 */
	public void sendObject(Object r) throws IOException {
		out.writeObject(r);
		out.flush();
		out.reset();
	}

	/**
	 * Reads an object from this connection's socket input stream
	 * 
	 * @return The object read.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Object getObject() throws ClassNotFoundException, IOException {
		return in.readObject();
	}

	/**
	 * Stops the communication with the server
	 * 
	 * @throws IOException
	 */
	public void stop() throws IOException {
		s.close();
	}

}
