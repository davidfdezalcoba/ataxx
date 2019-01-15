package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A random player for Ataxx.
 * 
 * <p>
 * Un jugador aleatorio para Ataxx.
 *
 */
public class AtaxxRandomPlayer extends Player {

	private static final long serialVersionUID = 1L;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces,
			GameRules rules) {

		List<GameMove> moves = new ArrayList<GameMove>();
		moves = rules.validMoves(board, pieces, p);

		if (moves.isEmpty())
			throw new GameError(
					"There are no available moves left, couldn't make a random move!!");

		return moves.get(Utils.randomInt(moves.size()));
	}

	/**
	 * Creates the actual move to be returned by the player. Separating this
	 * method from {@link #requestMove(Piece, Board, List, GameRules)} allows us
	 * to reuse it for other similar games by overriding this method.
	 * 
	 * <p>
	 * Crea el movimiento concreto que sera devuelto por el jugador. Se separa
	 * este metodo de {@link #requestMove(Piece, Board, List, GameRules)} para
	 * permitir la reutilizacion de esta clase en otros juegos similares,
	 * sobrescribiendo este metodo.
	 * 
	 * @param preRow
	 *            previous row number
	 *            <p>
	 *            Numero de la fila previa
	 * 
	 * @param preCol
	 *            Previous column number.
	 *            <p>
	 *            Numero de la columna previa.
	 * 
	 * @param row
	 *            Row number.
	 * @param col
	 *            Column number.
	 * @param p
	 *            Piece at {@code preRow},{@code preCol} to place at ({@code row}
	 *            ,{@code col}).
	 * @return
	 */
	protected GameMove createMove(int preRow, int preCol, int row, int col,
			Piece p) {
		return new AtaxxMove(preRow, preCol, row, col, p);
	}

}
