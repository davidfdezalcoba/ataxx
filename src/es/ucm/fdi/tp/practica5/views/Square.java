package es.ucm.fdi.tp.practica5.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.practica5.views.BoardUI.PlayControlsListener;

/**
 * Class Square
 * 
 * Represents a cell in the gaming board.
 * 
 * <p>
 * Representa una casilla en el tablero de juego.
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón.
 *
 */
public class Square extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2825044895066638189L;
	/**
	 * The color to be used when painting a piece
	 * 
	 * <p>
	 * El color a usar al pintar una pieza.
	 */
	private Color color;
	/**
	 * A boolean indicating whether this cell contains a piece.
	 * 
	 * <p>
	 * Un booleano indicando si la casilla contiene una pieza.
	 */
	private boolean hayPieza;

	public Square(final int row, final int col, final PlayControlsListener playControlsListener) {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e))
					try {
						playControlsListener.squareWasLeftClicked(row, col);
					} catch (Exception exc) {
						exc.getMessage();
					}
				else if (SwingUtilities.isRightMouseButton(e))
					playControlsListener.squareWasRightClicked();
			}
		});
	}

	/**
	 * Sets the color we want to paint a piece placed in this cell with.
	 * 
	 * <p>
	 * Configura el color con el que queremos pintar una pieza en esta casilla.
	 * 
	 * @param color
	 *            The color to be used.
	 * 
	 *            <p>
	 *            El color a usar.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Sets the hayPieza field with a boolean indicating whether or not a piece
	 * is placed in this cell.
	 * 
	 * <p>
	 * Configura el campo hayPieza con un booleano que indica si hay o no una
	 * pieza es esta casilla
	 * 
	 * @param b
	 *            The boolean indicating so.
	 * 
	 *            <p>
	 *            El booleano que lo indica.
	 */
	public void setHayPieza(boolean b) {
		hayPieza = b;
	}

	/**
	 * Paints itself circle-shaped only if a piece occupies the (row, col)
	 * position
	 * 
	 * <p>
	 * Se pinta con forma de circulo si una pieza está situada en esta casilla.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (hayPieza)
			paintCircle(g, color);
	}

	/**
	 * Paints a circle filled in the color {@code color}
	 * 
	 * <p>
	 * Pinta un circulo relleno del color {@code color}
	 * 
	 * @param g
	 * @param color
	 *            The color to be used when filling the circle
	 *            <p>
	 *            El color a usar al pintar el circulo
	 */
	public void paintCircle(Graphics graphics, Color color) {

		int margen1 = getWidth() / 6;
		int margen2 = getHeight() / 6;
		
		Graphics2D g2d = (Graphics2D)graphics;
        g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);

		Ellipse2D.Double circle = new Ellipse2D.Double(this.getAlignmentX()
				+ margen1 / 2, this.getAlignmentY() + margen2 / 2, getWidth()
				- margen1, getHeight() - margen2);

		g2d.setPaint(color);
		g2d.fill(circle);

	}

}
