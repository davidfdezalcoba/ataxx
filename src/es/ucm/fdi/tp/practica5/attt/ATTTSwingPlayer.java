package es.ucm.fdi.tp.practica5.attt;

import java.util.List;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTMove;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.SwingPlayer;

public class ATTTSwingPlayer extends SwingPlayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9202132855878662981L;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces,
			GameRules rules) {
		// TODO Auto-generated method stub
		return new AdvancedTTTMove(preRow, preCol, row, col, p);
	}

}
