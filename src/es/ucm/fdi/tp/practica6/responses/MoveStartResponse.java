package es.ucm.fdi.tp.practica6.responses;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Represents a response that the server sends when a move is about to start.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class MoveStartResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2066817956600389247L;
	private Board board;
	private Piece turn;

	public MoveStartResponse(Board board, Piece turn) {
		this.board = board;
		this.turn = turn;
	}

	@Override
	public void run(GameObserver o) {
		// TODO Auto-generated method stub
		o.onMoveStart(board, turn);
	}

}
