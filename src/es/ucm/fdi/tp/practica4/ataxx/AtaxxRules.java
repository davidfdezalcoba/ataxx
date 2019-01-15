package es.ucm.fdi.tp.practica4.ataxx;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Rules for Ataxx game.
 * <ul>
 * <li>The game is played on an NxN board (with N an odd number>=5). The number
 * of obstacles places in the board must be smaller than N * 3</li>
 * <li>The number of players is between 2 and 4.</li>
 * <li>The player turn in the given order, each placing a piece on an empty
 * cell. The winner is the one who has more pieces in the board when no player
 * has available moves or the board is full.</li>
 * </ul>
 * 
 * <p>
 * Reglas del juego ConnectN.
 * <ul>
 * <li>El juego se juega en un tablero NxN (con N numero impar>=5). El numero de
 * obstaculos en el tablero será menor que N * 3.</li>
 * <li>El numero de jugadores esta entre 2 y 4.</li>
 * <li>Los jugadores juegan en el orden proporcionado, cada uno colocando una
 * ficha en una casilla vacia. El ganador es aquel que tenga mas piezas en el
 * tablero una vez que a nadie le queden movimientos posibles o el tablero este
 * lleno.</li>
 * </ul>
 *
 */
public class AtaxxRules implements GameRules {

	// This object is returned by gameOver to indicate that the game is not
	// over. Just to avoid creating it multiple times, etc.
	//
	protected final Pair<State, Piece> gameInPlayResult = new Pair<State, Piece>(
			State.InPlay, null);

	// The piece reserved for representing an obstacle.("*")
	private Piece obs;

	private int dim;

	private int obstacles;

	public AtaxxRules(int dim, int obstacles) {

		if (dim < 5 || dim % 2 == 0) {
			throw new GameError(
					"Dimension must be an odd number greater than 5: " + dim);
		} else if (obstacles > dim * 3 || obstacles % 4 != 0) {
			throw new GameError(
					"The number of obstacles must be a multiple of 4 between 0 and dim * 3");
		} else {
			this.dim = dim;
			this.obstacles = obstacles;
			this.obs = new Piece("*");
		}
	}

	@Override
	public String gameDesc() {
		return "Ataxx " + dim + "x" + dim + " with " + obstacles
				+ " obstacles.";
	}

	@Override
	public Board createBoard(List<Piece> pieces) {

		Board board = new FiniteRectBoard(dim, dim);

		// Place the initial pieces in the board
		for (int i = 0; i < pieces.size(); i++) {
			if (i == 0) {
				board.setPosition(0, 0, pieces.get(i));
				board.setPosition(dim - 1, dim - 1, pieces.get(i));
				board.setPieceCount(pieces.get(i), 2);
			} else if (i == 1) {
				board.setPosition(0, dim - 1, pieces.get(i));
				board.setPosition(dim - 1, 0, pieces.get(i));
				board.setPieceCount(pieces.get(i), 2);
			} else if (i == 2) {
				board.setPosition(0, dim / 2, pieces.get(i));
				board.setPosition(dim - 1, dim / 2, pieces.get(i));
				board.setPieceCount(pieces.get(i), 2);
			} else {
				board.setPosition(dim / 2, 0, pieces.get(i));
				board.setPosition(dim / 2, dim - 1, pieces.get(i));
				board.setPieceCount(pieces.get(i), 2);
			}
		}

		// Place the number of obstacles provided
		int row = Utils.randomInt(dim / 2);
		int col = Utils.randomInt(dim / 2);
		for (int j = 0; j < this.obstacles / 4; j++) {
			while (board.getPosition(row, col) != null) {
				row = Utils.randomInt(dim / 2);
				col = Utils.randomInt(dim / 2);
			}
			board.setPosition(row, col, obs);
			board.setPosition(row, dim - 1 - col, obs);
			board.setPosition(dim - 1 - row, col, obs);
			board.setPosition(dim - 1 - row, dim - 1 - col, obs);
		}

		board.setPieceCount(obs, this.obstacles);

		return board;
	}

	@Override
	public Piece initialPlayer(Board board, List<Piece> playersPieces) {
		return playersPieces.get(0);
	}

	@Override
	public int minPlayers() {
		return 2;
	}

	@Override
	public int maxPlayers() {
		return 4;
	}

	@Override
	public Pair<State, Piece> updateState(Board board, List<Piece> pieces,
			Piece turn) {

		boolean acabar = true;
		boolean iguales = false;

		// Check if there is only one player left
		for (Piece i : pieces) {
			if (board.getPieceCount(i) != 0) {
				for (Piece j : pieces) {
					if (j != i && board.getPieceCount(j) != 0)
						acabar = false;
				}
				if (acabar)
					return new Pair<State, Piece>(State.Won, i);
			}
		}

		acabar = true;
		Piece max = pieces.get(0);

		// Get in max the player with more pieces and check if there are
		// available moves left.
		for (Piece i : pieces) {
			if (!validMoves(board, pieces, i).isEmpty()) {
				acabar = false;
			}
			if (board.getPieceCount(max) < board.getPieceCount(i))
				max = i;
		}
		// Check if there is more than one player with max number of pieces.
		for (Piece i : pieces) {
			if (max != i
					&& (board.getPieceCount(max) == board.getPieceCount(i)))
				iguales = true;
		}
		if (acabar) {
			if (!iguales) // A nadie le quedan movimientos y solo un jugador
							// tiene el maximo numero de piezas -> Victoria
				return new Pair<State, Piece>(State.Won, max);
			else
				// A nadie le quedan movimientos y hay mas de un jugador con
				// maximo
				// numero de piezas -> Empate
				return new Pair<State, Piece>(State.Draw, null);
		}

		return gameInPlayResult;
	}

	@Override
	public Piece nextPlayer(Board board, List<Piece> playersPieces,
			Piece lastPlayer) {

		List<Piece> pieces = playersPieces;
		int i = pieces.indexOf(lastPlayer);
		Piece next = pieces.get((i + 1) % pieces.size());
		while (validMoves(board, playersPieces, next).isEmpty()) {
			i++;
			System.out.print("Player " + next + " has no available moves left, turn passes to: ");
			next = pieces.get((i + 1) % pieces.size());
			System.out.println(next);
		}
		return next;
	}
	
	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn, Piece p) {
		return 0;
	}

	@Override
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces,
			Piece turn) {

		List<GameMove> moves = new ArrayList<GameMove>();

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				// Find the pieces that can move and add those cells where they
				// can move to.
				if (board.getPosition(i, j) != null
						&& board.getPosition(i, j).equals(turn)) {
					for (int p = -2; p < 3; p++) {
						for (int q = -2; q < 3; q++) {
							if (i + p < board.getRows() && i + p > -1
									&& j + q < board.getCols() && j + q > -1
									&& board.getPosition(i + p, j + q) == null)
								moves.add(new AtaxxMove(i, j, i + p, j + q,
										turn));
						}
					}
				}
			}
		}
		return moves;
	}

}
