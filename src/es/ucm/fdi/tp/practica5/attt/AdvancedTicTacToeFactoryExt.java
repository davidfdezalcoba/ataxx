package es.ucm.fdi.tp.practica5.attt;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTFactory;
import es.ucm.fdi.tp.basecode.attt.AdvancedTTTRules;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.SwingViewFrame;

public class AdvancedTicTacToeFactoryExt extends AdvancedTTTFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -159316878614371655L;

	@Override
	public GameRules gameRules() {
		return new AdvancedTTTRules();
	}

	public void createSwingView(final Observable<GameObserver> g,
			final Controller c, final Piece viewPiece, final Player random, final Player ai) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new SwingViewFrame(
							"Board Games: Advanced TicTacToe"
									+ (viewPiece != null ? " (" + viewPiece
											+ ")" : ""), new ATTTSwingView(g,
									c, viewPiece, random, ai,
									new ATTTSwingPlayer()));
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			throw new GameError("");
		}
	}
}
