package es.ucm.fdi.tp.practica5.ttt;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;
import es.ucm.fdi.tp.practica5.SwingPlayer;

public class TicTacToeSwingPlayer extends SwingPlayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7623934607240599971L;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces,
			GameRules rules) {
		// TODO Auto-generated method stub
		return new ConnectNMove(row, col, p);
	}

}
