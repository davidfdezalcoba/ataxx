package es.ucm.fdi.tp.practica6.responses;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Represents a response that the server sends when the game is over.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class GameOverResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9034490930793792064L;
	private Board board;
	private State state;
	private Piece winner;

	public GameOverResponse(Board board, State state, Piece winner) {
		this.board = board;
		this.state = state;
		this.winner = winner;
	}

	@Override
	public void run(GameObserver o) {
		// TODO Auto-generated method stub
		o.onGameOver(board, state, winner);
	}

}
