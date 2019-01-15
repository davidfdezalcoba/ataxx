package es.ucm.fdi.tp.practica5.ataxx;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.SwingViewFrame;

public class AtaxxFactoryExt extends AtaxxFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 421844552614703645L;

	public AtaxxFactoryExt() {
		super();
	}

	public AtaxxFactoryExt(int dim) {
		super(dim);
	}

	public AtaxxFactoryExt(int dim, int obs) {
		super(dim, obs);
	}

	public void createSwingView(final Observable<GameObserver> g,
			final Controller c, final Piece viewPiece, final Player random, final Player ai) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new SwingViewFrame(
							"Board Games: Ataxx"
									+ (viewPiece != null ? " (" + viewPiece
											+ ")" : ""), new AtaxxSwingView(g,
									c, viewPiece, random, ai,
									new AtaxxSwingPlayer()));
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			throw new GameError("");
		}
	}

}
