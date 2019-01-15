package es.ucm.fdi.tp.practica6.responses;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Represents the response given by a server when there is a change of turn.
 * 
 * @author David Fdez Alcoba, Manuel Sannchez Torron
 *
 */
public class ChangeTurnResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1958649962175203618L;
	private Board board;
	private Piece turn;

	public ChangeTurnResponse(Board board, Piece turn) {
		this.board = board;
		this.turn = turn;
	}

	@Override
	public void run(GameObserver o) {
		// TODO Auto-generated method stub
		o.onChangeTurn(board, turn);
	}

}
