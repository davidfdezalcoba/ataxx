package es.ucm.fdi.tp.practica5.connectn;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

/**
 * A view for the specific game Connect-N
 * 
 * <p>
 * Una vista para el juego Connect-N
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón. *
 */
public class ConnectNSwingView extends GenericSwingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4858388106087937698L;

	private ConnectNSwingPlayer manual;

	public ConnectNSwingView(Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player random, Player intelligent,
			ConnectNSwingPlayer manual) {
		super(game, ctrl, viewPiece, random, intelligent);
		this.manual = manual;
	}

	@Override
	public void squareWasLeftClicked(int i, int j) {
		// TODO Auto-generated method stub
		if (swingPlayers.get(turn).equals("Manual")
				&& (thisPiece == null || thisPiece.equals(turn))
				&& board.getPosition(i, j) == null) {
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
		this.settingsPanel.statusUpdate("click on an empty cell");
	}

	@Override
	protected void newSelection(int row, int col) {
		// this game makes no selections

	}
}
