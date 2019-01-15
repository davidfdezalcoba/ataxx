package es.ucm.fdi.tp.practica6.net;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A new controller for the net-mode game. Includes methods to perform the
 * actions needed by chat commands
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public abstract class NetGameController extends Controller {

	public NetGameController(Game game, List<Piece> pieces) {
		super(game, pieces);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Shows the currently connected players in the "p" view.
	 * 
	 * @param p
	 *            Represents the player that wants to see the players' list
	 */
	public abstract void showPlayers(Piece p);

	/**
	 * Dictates what to do when a chat message is sent.
	 * 
	 * @param p
	 *            The piece who sends the message
	 * @param text
	 *            The message itself.
	 */
	public abstract void handleTextEntry(Piece p, String text);

	/**
	 * Displays a list of the possible commands. This method should only be used
	 * by a client.
	 */
	public abstract void showHelp();

}
