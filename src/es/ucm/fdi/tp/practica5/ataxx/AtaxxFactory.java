package es.ucm.fdi.tp.practica5.ataxx;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.basecode.bgame.control.AIPlayer;
import es.ucm.fdi.tp.basecode.bgame.control.ConsolePlayer;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.views.GenericConsoleView;

/**
 * A factory for creating Ataxx games. See {@link AtaxxRules} for the
 * description of the game.
 * 
 * 
 * <p>
 * Factoria para la creacion de juegos Ataxx. Vease {@link AtaxxRules} para la
 * descripcion del juego.
 */
public class AtaxxFactory implements GameFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7837072299247931345L;

	protected int dim;

	protected int obstacles;

	public AtaxxFactory() {
		this(5, 0);
	}

	public AtaxxFactory(int dim) {
		if (dim < 5 || dim % 2 == 0) {
			throw new GameError(
					"Dimension must be an odd number greater than 5: " + dim);
		} else {
			this.dim = dim;
			this.obstacles = 0;
		}
	}

	public AtaxxFactory(int dim, int obstacles) {
		if (dim < 5 || dim % 2 == 0) {
			throw new GameError(
					"Dimension must be an odd number greater than 5: " + dim);
		} else if (obstacles > dim * 3 || obstacles % 4 != 0) {
			throw new GameError(
					"The number of obstacles must be a multiple of 4 between 0 and dim * 3");
		} else {
			this.dim = dim;
			this.obstacles = obstacles;
		}
	}

	@Override
	public GameRules gameRules() {
		return new AtaxxRules(dim, obstacles);
	}

	@Override
	public Player createConsolePlayer() {
		ArrayList<GameMove> possibleMoves = new ArrayList<GameMove>();
		possibleMoves.add(new AtaxxMove());
		return new ConsolePlayer(new Scanner(System.in), possibleMoves);
	}

	@Override
	public Player createRandomPlayer() {
		return new AtaxxRandomPlayer();
	}

	@Override
	public Player createAIPlayer(AIAlgorithm alg) {
		return new AIPlayer(alg);
	}

	/**
	 * By default, we have two players, X and O.
	 * <p>
	 * Por defecto, hay dos jugadores, X y O.
	 */
	@Override
	public List<Piece> createDefaultPieces() {
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece("X"));
		pieces.add(new Piece("O"));
		return pieces;
	}

	@Override
	public void createConsoleView(Observable<GameObserver> g, Controller c) {
		new GenericConsoleView(g, c);
	}

	@Override
	public void createSwingView(final Observable<GameObserver> g,
			final Controller c, final Piece viewPiece, Player random, Player ai) {
		throw new UnsupportedOperationException("There is no swing view");
	}

}
