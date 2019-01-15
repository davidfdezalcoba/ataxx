package es.ucm.fdi.tp.practica6.ChatCommands;

import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.net.NetGameController;

/**
 * Represents a message. It's used to send a message in the server-client app.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class MessageCommand extends ChatCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3765698850078656564L;

	/**
	 * The text contained in this message
	 */
	private String text;

	/**
	 * The client who sends this message
	 */
	private Piece p;

	public MessageCommand(Piece p, String text) {
		this.text = text;
		this.p = p;
	}

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Command parse(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(NetGameController c) {
		// TODO Auto-generated method stub
		c.handleTextEntry(p, text);
	}

}
