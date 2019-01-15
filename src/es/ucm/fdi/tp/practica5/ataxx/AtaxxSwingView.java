package es.ucm.fdi.tp.practica5.ataxx;

import java.awt.Color;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.GenericSwingView;

/**
 * A swing view for the specific game Ataxx.
 * 
 * <p>
 * Una vista swing para el juego Ataxx.
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón.
 *
 */
public class AtaxxSwingView extends GenericSwingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2214893070167623972L;

	/**
	 * The row where the selected cell is. -1 if no cell is selected.
	 * 
	 * <p>
	 * La fila en la que se encuentra la casilla seleccionada. -1 si no hay
	 * casilla seleccionada.
	 */
	private int preRow;

	/**
	 * The col where the selected cell is. -1 is no cell is selected
	 * 
	 * <p>
	 * La columna en la que se encuentra la casilla seleccionada. -1 si no hay
	 * casilla seleccionada.
	 */
	private int preCol;

	private AtaxxSwingPlayer manual;

	public AtaxxSwingView(Observable<GameObserver> game, Controller ctrl,
			Piece viewPiece, Player random, Player intelligent,
			AtaxxSwingPlayer manual) {
		super(game, ctrl, viewPiece, random, intelligent);
		this.manual = manual;
		preRow = -1;
		preCol = -1;
	}

	@Override
	public void squareWasLeftClicked(int i, int j) {

		// Right view and manual player in turn
		if ((thisPiece == null || thisPiece.equals(turn))
				&& swingPlayers.get(turn).equals("Manual")) {
			// Empty cell clicked and previous selection already made.
			if (board.getPosition(i, j) == null && preRow != -1 && preCol != -1) {
				performMove(preRow, preCol, i, j);
				// A piece in turn selected -> new selection
			} else if (board.getPosition(i, j) != null
					&& board.getPosition(i, j).equals(turn)) {
				if (preRow != -1)
					boardUI.setBackgroundFor(preRow, preCol, Color.GRAY);
				newSelection(i, j);
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

	protected void performMove(int preRow, int preCol, int row, int col)
			throws GameError {
		manual.setMove(preRow, preCol, row, col);
		control.makeMove(manual);
		boardUI.setBackgroundFor(preRow, preCol, Color.GRAY);
		this.preRow = -1;
		this.preCol = -1;
	}

}
