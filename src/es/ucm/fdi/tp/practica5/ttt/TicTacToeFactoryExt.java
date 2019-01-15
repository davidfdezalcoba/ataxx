package es.ucm.fdi.tp.practica5.ttt;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.ttt.TicTacToeFactory;
import es.ucm.fdi.tp.practica5.views.SwingViewFrame;

/**
 * An extended factory for the game TicTacToe in order to support Swing Views.
 * 
 * <p>
 * Una factoría extendida para el juego TicTacToe para soportar Swing Views.
 * 
 * @author David Fdez Alcoba
 *
 */
public class TicTacToeFactoryExt extends TicTacToeFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3386484560165275033L;

	public TicTacToeFactoryExt() {
		super();
	}

	public void createSwingView(final Observable<GameObserver> g,
			final Controller c, final Piece viewPiece, final Player random, final Player ai) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new SwingViewFrame(
							"Board Games: TicTacToe"
									+ (viewPiece != null ? " (" + viewPiece
											+ ")" : ""),
							new TicTacToeSwingView(g, c, viewPiece, random, ai,
									new TicTacToeSwingPlayer()));
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			throw new GameError("");
		}
	}
}
