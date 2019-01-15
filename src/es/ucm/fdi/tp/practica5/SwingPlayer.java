package es.ucm.fdi.tp.practica5;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * This class represents a player for making manual moves in a board game from a
 * Swing View.
 * 
 * <p>
 * Esta clase representa un jugador para hacer movimientos manuales en un juego
 * de mesa desde una Interfaz de usuario Swing
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón
 *
 */
public abstract class SwingPlayer extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5180940869356684093L;

	/**
	 * Integers representing the source row and column and the destination row
	 * and column, respectively.
	 * 
	 * <p>
	 * Enteros que representan la fila y columna origen y la fila y columna
	 * destino, respectivamente.
	 */
	protected int preRow, preCol, row, col;

	/**
	 * Stores a move represented by a string in the field thisMove.
	 * 
	 * <p>
	 * Almacena un movimiento representado por un string en el campo thisMove.
	 * 
	 * @param preRow
	 *            Source row
	 * 
	 *            <p>
	 *            Fila origen
	 * @param preCol
	 *            Source col
	 * 
	 *            <p>
	 *            Columna origen
	 * @param row
	 * @param col
	 */
	public void setMove(int preRow, int preCol, int row, int col) {
		this.preRow = preRow;
		this.preCol = preCol;
		this.row = row;
		this.col = col;
	}

	@Override
	public abstract GameMove requestMove(Piece p, Board board,
			List<Piece> pieces, GameRules rules);

}
