package es.ucm.fdi.tp.practica6.responses;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Represents a response that the server sends when a move is ended.
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class MoveEndResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 864775445600045813L;
	private Board board;
	private Piece turn;
	private boolean success;

	public MoveEndResponse(Board board, Piece turn, boolean success) {
		this.board = board;
		this.turn = turn;
		this.success = success;
	}

	@Override
	public void run(GameObserver o) {
		// TODO Auto-generated method stub
		o.onMoveEnd(board, turn, success);
	}

}
