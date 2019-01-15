package es.ucm.fdi.tp.practica5.connectn;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;
import es.ucm.fdi.tp.practica5.SwingPlayer;

public class ConnectNSwingPlayer extends SwingPlayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5889978253046493312L;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces,
			GameRules rules) {
		// TODO Auto-generated method stub
		return new ConnectNMove(row, col, p);
	}

}
