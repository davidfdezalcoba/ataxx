package es.ucm.fdi.tp.practica5.ttt;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

/**
 * A specific Swing view for the Game Tic Tac Toe.
 * 
 * <p>
 * Una vista especifica para el juego Tic-Tac-Toe.
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón.
 */
public class TicTacToeSwingView extends GenericSwingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7638249080058812307L;
	private TicTacToeSwingPlayer manual;

	public TicTacToeSwingView(Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player random, Player intelligent,
			TicTacToeSwingPlayer manual) {
		super(game, ctrl, viewPiece, random, intelligent);
		this.manual = manual;
	}

	@Override
	public void squareWasLeftClicked(int i, int j) {
		// TODO Auto-generated method stub
		if (this.swingPlayers.get(turn).equals("Manual")
				&& (thisPiece == null || thisPiece.equals(turn))) {
			performMove(-1, -1, i, j);
		}
	}

	@Override
	protected void performMove(int preRow, int preCol, int row, int col)
			throws GameError {
		manual.setMove(preRow, preCol, row, col);
		control.makeMove(manual);
	}

	@Override
	public void helpMove() {
		// TODO Auto-generated method stub
		this.settingsPanel.statusUpdate("Click on an empty cell");
	}

}
