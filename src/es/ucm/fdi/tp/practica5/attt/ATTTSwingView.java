package es.ucm.fdi.tp.practica5.attt;

import java.awt.Color;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

/**
 * A view for the specific game Advanced Tic Tac Toe
 * 
 * <p>
 * Una vista para el juego ATTT
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón. *
 */
public class ATTTSwingView extends GenericSwingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -953848236093503199L;

	/**
	 * The row where the selected cell is. -1 if no cell is selected
	 */
	private int preRow;

	/**
	 * The col where the selected cell is. -1 is no cell is selected
	 */
	private int preCol;

	private ATTTSwingPlayer manual;

	public ATTTSwingView(Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player random, Player intelligent,
			ATTTSwingPlayer manual) {
		super(game, ctrl, viewPiece, random, intelligent);
		this.manual = manual;
		preRow = -1;
		preCol = -1;
	}

	@Override
	public void squareWasLeftClicked(int i, int j) {
		// TODO Auto-generated method stub

		// Right view and manual player
		if ((thisPiece == null || thisPiece.equals(turn))
				&& swingPlayers.get(turn).equals("Manual")) {

			// Empty cell selected
			if (board.getPosition(i, j) == null) {

				// Remaining pieces to place or a piece already selected
				if (board.getPieceCount(turn) > 0 || preRow != -1)
					performMove(preRow, preCol, i, j);

				// Occupied cell selected and no pieces remaining
			} else if (board.getPieceCount(turn) == 0) {
				// If the selected cell contains a piece in turn -> new
				// selection.
				if (board.getPosition(i, j).equals(turn)) {
					// If a piece was already selected, change it for the new
					// one.
					if (preRow != -1)
						boardUI.setBackgroundFor(preRow, preCol, Color.GRAY);
					newSelection(i, j);
				}
			}
		}
	}

	@Override
	public void squareWasRightClicked() {
		// TODO Auto-generated method stub
		loseSelection();
	}

	@Override
	public void helpMove() {
		// TODO Auto-generated method stub
		if (board.getPieceCount(turn) > 0)
			settingsPanel.statusUpdate("Click on an empty cell");
		else
			settingsPanel.statusUpdate("Click on an origin cell");
	}

	@Override
	public void loseSelection() {
		if (preRow != -1) {
			settingsPanel.statusUpdate("Selection lost");
			boardUI.setBackgroundFor(preRow, preCol, Color.GRAY);
			preRow = -1;
			preCol = -1;
			setAllButtonsEnabled(true);
		}
	}

	@Override
	protected void newSelection(int i, int j) {
		preRow = i;
		preCol = j;
		super.newSelection(i, j);
	}

	@Override
	protected void performMove(int preRow, int preCol, int row, int col)
			throws GameError {
		manual.setMove(preRow, preCol, row, col);
		control.makeMove(manual);
		boardUI.setBackgroundFor(preRow, preCol, Color.GRAY);
		this.preRow = -1;
		this.preCol = -1;
	}

}
