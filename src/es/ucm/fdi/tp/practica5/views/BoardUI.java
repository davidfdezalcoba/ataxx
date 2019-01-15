package es.ucm.fdi.tp.practica5.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;

/**
 * Class BoardUI
 * 
 * A class that represents the gaming table in a graphic interface.
 * 
 * <p>
 * Una clase que representa un tablero de juego en la interfaz grafica
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón
 *
 */
public class BoardUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6165825295578203405L;

	/**
	 * The board where the game is taking place
	 * 
	 * <p>
	 * El tablero en el que se esta jugando.
	 */
	private Board board;

	/**
	 * The colorMap that tells the color to paint a piece
	 * 
	 * <p>
	 * El colorMap para pintar las piezas
	 */
	private PieceColorMap colorMap;

	/**
	 * Each of the cells contained in the board
	 * 
	 * <p>
	 * Las casillas que contiene el tablero
	 */
	private Square[][] squares;

	/**
	 * An interface that dictates the actions to be performed responding to
	 * events
	 * 
	 * <p>
	 * Una interfaz que dictamina como atuar ante los eventos.
	 */
	private PlayControlsListener playControlsListener;

	/**
	 * An interface with methods to perform actions when a cell is clicked
	 * 
	 * <p>
	 * Una interfaz que proporciona metodos para realizar acciones cuando una
	 * casilla es clicada.
	 * 
	 * @author David Fdez Alcoba, Manuel Sánchez Torrón
	 *
	 */
	public interface PlayControlsListener {

		/**
		 * Contains the action to be performed when a left click takes place in
		 * one of the squares
		 * 
		 * <p>
		 * Contiene la accion a realizar cuando se produce un click izquierdo
		 * 
		 * @param i
		 *            row in which the square is.
		 * @param j
		 *            col in which the square is.
		 */
		void squareWasLeftClicked(int i, int j) throws GameError;

		/**
		 * Contains the action to be performed when a right click takes place in
		 * one of the squares
		 * 
		 * <p>
		 * Contiene la accion a realizar cuando se produce un click derecho
		 */
		void squareWasRightClicked();

	}

	public BoardUI(PlayControlsListener playControlsListener) {
		setPreferredSize(new Dimension(500, 500));
		this.playControlsListener = playControlsListener;
	}

	/**
	 * Totally replace the board
	 * 
	 * <p>
	 * Reemplaza el tablero totalment
	 * 
	 * @param board
	 *            the board to be painted
	 * @param colorMap
	 *            the colorMap to paint pieces
	 */
	public void setBoard(Board board, PieceColorMap colorMap) {

		this.board = board;
		this.colorMap = colorMap;

		setLayout(new GridLayout(board.getRows(), board.getCols()));

		squares = new Square[board.getRows()][board.getCols()];

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				Square s = new Square(i, j, playControlsListener);
				
				s.setBackground(Color.GRAY);
				s.setOpaque(true);
				s.setBorder(BorderFactory.createLineBorder(Color.WHITE));
				add(s);
				squares[i][j] = s;
			}
		}

		paintPieces();

	}

	/**
	 * This method goes through the board painting the pieces that it contains.
	 * 
	 * <p>
	 * Este metodo recorre el tablero pintando las piezas que contiene.
	 */
	public void paintPieces() {
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				Square s = squares[i][j];
				if (board.getPosition(i, j) != null) {
					if (colorMap.containsKey(board.getPosition(i, j))) {
						s.setHayPieza(true);
						s.setColor(colorMap.getColorFor(board.getPosition(i, j)));
						s.repaint();
					} else {
						s.setBackground(Color.BLACK);
					}
				} else {
					s.setHayPieza(false);
					s.setBackground(Color.GRAY);
				}
			}
		}
		repaint();
	}

	/**
	 * Sets the background to a specific cell
	 * 
	 * <p>
	 * Configura el fondo de una casilla.
	 * 
	 * @param i
	 *            row of the cell
	 * @param j
	 *            col of the cell
	 * @param color
	 *            The new background color
	 */
	public void setBackgroundFor(int i, int j, Color color) {
		this.squares[i][j].setBackground(color);
	}

	/**
	 * Updates the interface with new board information or color information
	 * 
	 * <p>
	 * Actualiza la interfaz con nueva informacion sobre el tablero o los
	 * colores.
	 * 
	 * @param board
	 *            the updated board
	 * @param colorMap
	 *            the new colorMap
	 */
	public void update(Board board, PieceColorMap colorMap) {
		this.board = board;
		this.colorMap = colorMap;
		paintPieces();
	}

}
