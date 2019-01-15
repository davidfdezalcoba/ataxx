package es.ucm.fdi.tp.practica6.responses;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Represents a response that the server sends when the game is starting.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class GameStartResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4385162296609333318L;
	private Board board;
	private String gameDesc;
	private List<Piece> pieces;
	private Piece turn;

	public GameStartResponse(Board board, String gameDesc, List<Piece> pieces,
			Piece turn) {
		this.board = board;
		this.gameDesc = gameDesc;
		this.pieces = pieces;
		this.turn = turn;
	}

	@Override
	public void run(GameObserver o) {
		o.onGameStart(board, gameDesc, pieces, turn);
	}

}
