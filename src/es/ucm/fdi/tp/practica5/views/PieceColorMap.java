package es.ucm.fdi.tp.practica5.views;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Class PieceColorMap
 * 
 * A class that represents a map that tells a piece a color to paint itself.
 * 
 * <p>
 * Una clase que representa un mapa que dice de que color pintar cada pieza.
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón.
 */
public class PieceColorMap extends HashMap<Piece, Color> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1785090815934912736L;

	public PieceColorMap(List<Piece> pieces, List<Color> colors) {
		super(pieces.size());
		for (int i = 0; i < pieces.size(); i++) {
			this.put(pieces.get(i), colors.get(i));
		}
	}

	/**
	 * 
	 * @param p
	 *            the piece we want to know the color for
	 *            <p>
	 *            La pieza sobre la cual queremos saber el color
	 * @return The color used to paint this piece
	 *         <p>
	 *         El color usado para pintar esta pieza.
	 */
	public Color getColorFor(Piece p) {
		return this.get(p);
	}

	/**
	 * Sets the color to paint a piece
	 * 
	 * <p>
	 * Configura el color para pintar una pieza
	 * 
	 * @param p
	 *            The piece which the new color belong to
	 *            <p>
	 *            La pieza a la que pertenece el color
	 * @param color
	 *            The new Color for the p piece
	 *            <p>
	 *            El nevo color para la pieza p
	 */
	public void setColorFor(Piece p, Color color) {
		this.put(p, color);
	}

}
