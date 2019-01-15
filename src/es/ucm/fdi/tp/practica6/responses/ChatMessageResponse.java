package es.ucm.fdi.tp.practica6.responses;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.views.Lobby;

/**
 * Represents a chat message response.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class ChatMessageResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6962016538256024462L;
	private String text;
	private Piece p;

	public ChatMessageResponse(Piece p, String text) {
		this.text = text;
		this.p = p;
	}

	@Override
	public void run(GameObserver o) {
		// TODO Auto-generated method stub

	}

	public void run(Lobby lobby) {
		lobby.getChat().showText((p != null ? "[" + p + "]: " : "") + text);
	}
}
