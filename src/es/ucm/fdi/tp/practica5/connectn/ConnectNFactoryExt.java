package es.ucm.fdi.tp.practica5.connectn;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNFactory;
import es.ucm.fdi.tp.practica5.views.SwingViewFrame;

/**
 * An extended factory for the game Connect-N in order to support swing views.
 * 
 * <p>
 * Una factoría extendida para el juego Connect-N para soportar swing views.
 * 
 * @author David Fdez Alcoba
 *
 */
public class ConnectNFactoryExt extends ConnectNFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4339532802084028880L;

	public ConnectNFactoryExt() {
		super();
	}

	public ConnectNFactoryExt(int dim) {
		super(dim);
	}

	public void createSwingView(final Observable<GameObserver> g,
			final Controller c, final Piece viewPiece, final Player random, final Player ai) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new SwingViewFrame(
							"Board Games: Connect N"
									+ (viewPiece != null ? " (" + viewPiece
											+ ")" : ""), new ConnectNSwingView(
									g, c, viewPiece, random, ai,
									new ConnectNSwingPlayer()));
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			throw new GameError("");
		}
	}
}
