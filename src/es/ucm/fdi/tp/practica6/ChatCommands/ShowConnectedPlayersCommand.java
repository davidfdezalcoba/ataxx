package es.ucm.fdi.tp.practica6.ChatCommands;

import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.net.NetGameController;

/**
 * Represents the /showplayers command. Shows a list of the online players.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class ShowConnectedPlayersCommand extends ChatCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6479899759462325511L;

	/**
	 * Represents the client that called the /showplayers command, so the server
	 * can return the information to this particular client
	 */
	private Piece c;

	/**
	 * 
	 * @param c
	 *            The Client
	 */
	public ShowConnectedPlayersCommand(Piece c) {
		this.c = c;
	}

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "/showplayers: Show a list of all the players currently connected";
	}

	@Override
	public Command parse(String line) {
		// TODO Auto-generated method stub
		if (line.trim().equalsIgnoreCase("showplayers")) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public void execute(NetGameController c) {
		// TODO Auto-generated method stub
		c.showPlayers(this.c);
	}

}
