package es.ucm.fdi.tp.practica6.ChatCommands;

import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.practica6.net.NetGameController;

/**
 * Represents the /quit command. Stops the connection to the server
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class ChatQuitCommand extends ChatCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7303496104038928394L;

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "/quit: Ends the connection with the server and exits the game";
	}

	@Override
	public Command parse(String line) {
		// TODO Auto-generated method stub
		if (line.trim().equalsIgnoreCase("quit")) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public void execute(NetGameController c) {
		// TODO Auto-generated method stub
		c.stop();
	}

}
