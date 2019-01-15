package es.ucm.fdi.tp.practica5.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.SwingPlayer;

public class AtaxxSwingPlayer extends SwingPlayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7286561242938301351L;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces,
			GameRules rules) {
		// TODO Auto-generated method stub
		return new AtaxxMove(preRow, preCol, row, col, p);
	}

}
