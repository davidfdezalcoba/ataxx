package es.ucm.fdi.tp.practica4.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A Class representing a move for Ataxx.
 * 
 * <p>
 * Clase para representar un movimiento del juego Ataxx.
 * 
 */
public class AtaxxMove extends GameMove {

	private static final long serialVersionUID = 1L;

	/**
	 * preRow indicates the row where the piece we want to move is placed at
	 * first.
	 * 
	 * <p>
	 * preRow indica la fila en la que se encuentra la pieza que queremos mover.
	 */
	protected int preRow;

	/**
	 * preCol indicates the col where the piece we want to move is placed at
	 * first.
	 * 
	 * <p>
	 * preCol indica la columna en la que se encuentra la pieza que queremos
	 * mover.
	 */
	protected int preCol;

	/**
	 * The row where to place the piece returned by {@link GameMove#getPiece()}.
	 * <p>
	 * Fila en la que se coloca la ficha devuelta por
	 * {@link GameMove#getPiece()}.
	 */
	protected int row;

	/**
	 * The column where to place the piece returned by
	 * {@link GameMove#getPiece()} .
	 * <p>
	 * Columna en la que se coloca la ficha devuelta por
	 * {@link GameMove#getPiece()}.
	 */
	protected int col;

	/**
	 * This constructor should be used ONLY to get an instance of
	 * {@link AtaxxMove} to generate game moves from strings by calling
	 * {@link #fromString(String)}
	 * 
	 * <p>
	 * Solo se debe usar este constructor para obtener objetos de
	 * {@link AtaxxMove} para generar movimientos a partir de strings usando el
	 * metodo {@link #fromString(String)}
	 * 
	 */

	public AtaxxMove() {
	}

	/**
	 * Constructs a move for moving a piece of the type referenced by {@code p}
	 * placed at position ({@code preRow},{@code preCol}) to position (
	 * {@code row},{@code col}).
	 * 
	 * <p>
	 * Construye un movimiento para mover una ficha del tipo referenciado por
	 * {@code p} situada en la posicion({@code preRow},{@code preCol}) a la
	 * posicion ({@code row},{@code col}).
	 * 
	 * @param preRow
	 *            Number of previous row.
	 *            <p>
	 *            Numero de fila previa.
	 * 
	 * @param preCol
	 *            Number of previous column.
	 *            <p>
	 *            Numero de la columna previa.
	 * 
	 * @param row
	 *            Number of row.
	 *            <p>
	 *            Numero de fila.
	 * @param col
	 *            Number of column.
	 *            <p>
	 *            Numero de columna.
	 * @param p
	 *            A piece to be place at ({@code row},{@code col}).
	 *            <p>
	 *            Ficha a colocar en ({@code row},{@code col}).
	 */
	public AtaxxMove(int preRow, int preCol, int row, int col, Piece p) {
		super(p);
		this.preRow = preRow;
		this.preCol = preCol;
		this.row = row;
		this.col = col;
	}

	@Override
	public void execute(Board board, List<Piece> pieces) {
		Piece p = getPiece();

		// Look for errors that may occur during the execution of the move
		if (board.getPieceCount(p) <= 0) {
			throw new GameError("There are no pieces of type " + p
					+ " available");
		} else if (board.getPosition(row, col) != null) {
			throw new GameError("Position (" + row + "," + col
					+ ") is already occupied");
		} else if (board.getPosition(preRow, preCol) != p) {
			throw new GameError("Position (" + preRow + ", " + preCol
					+ ") doesn't contain a " + p + " piece available to move");
		} else if ((preRow - row > 2 || row - preRow > 2)
				|| (preCol - col > 2 || col - preCol > 2)) {
			throw new GameError(
					"The position where you are trying to move is too far");
		} else { // No error occurred

			board.setPosition(row, col, p);
			if (preRow - row < 2 && preCol - col < 2 && col - preCol < 2
					&& row - preRow < 2) {
				board.setPieceCount(p, board.getPieceCount(p) + 1);
			} else {
				board.setPosition(preRow, preCol, null);
			}
			// Check other pieces in surrounding positions and convert them into
			// the p piece.
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (row + i > -1 && row + i < board.getRows()
							&& col + j < board.getCols() && col + j > -1) {
						for (Piece q : pieces) {
							if (board.getPosition(row + i, col + j) != null
									&& board.getPosition(row + i, col + j)
											.equals(q)) {
								board.setPosition(row + i, col + j, p);
								board.setPieceCount(q,
										board.getPieceCount(q) - 1);
								board.setPieceCount(p,
										board.getPieceCount(p) + 1);
							}
						}
					}
				}
			}
		}

	}

	/**
	 * This move can be constructed from a string of the form
	 * "preRow SPACE preCol SPACE row SPACE col" where preRow, preCol, row and
	 * col are integers representing two positions.
	 * 
	 * <p>
	 * Se puede construir un movimiento desde un string de la forma
	 * "preRow SPACE preCol SPACE > SPACE row SPACE col" donde preRow, preCol, row y col
	 * son enteros que representan dos casillas.
	 */
	@Override
	public GameMove fromString(Piece p, String str) {
		String[] words = str.split(" ");
		if (words.length != 5) {
			return null;
		}

		try {
			int preRow, preCol, row, col;
			preRow = Integer.parseInt(words[0]);
			preCol = Integer.parseInt(words[1]);
			row = Integer.parseInt(words[3]);
			col = Integer.parseInt(words[4]);
			return createMove(preRow, preCol, row, col, p);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Creates a move that is called from {@link #fromString(Piece, String)}.
	 * Separating it from that method allows us to use this class for other
	 * similar games by overriding this method.
	 * 
	 * <p>
	 * Crea un nuevo movimiento con la misma ficha utilizada en el movimiento
	 * actual. Llamado desde {@link #fromString(Piece, String)}; se separa este
	 * metodo del anterior para permitir utilizar esta clase para otros juegos
	 * similares sobrescribiendo este metodo.
	 * 
	 * @param preRow
	 *            Row where the piece that is going to move is placed.
	 *            <p>
	 *            Fila en la que se encuentra la piece que se va a mover.
	 * 
	 * @param preCol
	 *            Column where the piece that is going to move is placed.
	 *            <p>
	 *            Columna en la que se encuentra la piece que se va a mover.
	 * 
	 * @param row
	 *            Row of the move being created.
	 *            <p>
	 *            Fila del nuevo movimiento.
	 * 
	 * @param col
	 *            Column of the move being created.
	 *            <p>
	 *            Columna del nuevo movimiento.
	 */
	protected GameMove createMove(int preRow, int preCol, int row, int col,
			Piece p) {
		return new AtaxxMove(preRow, preCol, row, col, p);
	}

	@Override
	public String help() {
		return "'preRow preCol row col', to move the piece at (preRow, preCol)"
				+ "to the corresponding position (row, col).";
	}

	@Override
	public String toString() {
		if (getPiece() == null) {
			return help();
		} else {
			return "Place a piece '" + getPiece() + "' in (" + preRow + ","
					+ preCol + ") at (" + row + "," + col + ")";
		}
	}

}
