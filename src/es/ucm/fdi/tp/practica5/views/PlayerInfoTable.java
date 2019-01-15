package es.ucm.fdi.tp.practica5.views;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Class PlayerInfoTable
 * 
 * A class representing the table that contains information about the players
 * 
 * The information displayed in this table is: -Name of the player -Current mode
 * of the player -Current number of that player's pieces in game.
 * 
 * <p>
 * Una clase que representa la tabla que contiene informacion sobre los
 * jugadores.
 * 
 * La informacion mostrada en esta table es: -Nombre del jugador -Modo de juego
 * actual -Numero de piezas actual del jugador.
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón.
 */
public class PlayerInfoTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -696576766927797587L;

	/**
	 * The model used to build the table
	 * 
	 * <p>
	 * El modelo a utilizar al contruir la tabla.
	 */
	private DefaultTableModel model;

	/**
	 * The colorMap used to set the background corresponding to a player's row
	 * 
	 * <p>
	 * El coloRmap utilizado para configurar el fondo correspondiente a la fila
	 * del jugador
	 */
	private PieceColorMap colorMap;

	/**
	 * The list of pieces that indicates the number of rows
	 * 
	 * <p>
	 * La lista de piezas que indica el numero de jugadores
	 */
	private List<Piece> pieces;

	public PlayerInfoTable(DefaultTableModel model,
			PieceColorMap colorMap, List<Piece> pieces,
			HashMap<Piece, String> players, Player random, Player intelligent) {
		super(model);
		this.model = model;
		this.colorMap = colorMap;
		this.pieces = pieces;
	}

	public Component prepareRenderer(TableCellRenderer renderer, int row,
			int column) {
		Component stamp = super.prepareRenderer(renderer, row, column);
		stamp.setBackground(colorMap.getColorFor(pieces.get(row)));
		return stamp;
	}

	/**
	 * Updates the table with new information
	 * 
	 * <p>
	 * Actualiza la informacion de la tabla
	 * 
	 * @param board
	 *            The new board that contains the number of pieces
	 *            <p>
	 *            El nuevo tablero que contiene el numero de piezas
	 * @param colorMap
	 *            New colorMap to paint players' rows.
	 *            <p>
	 *            El nuevo colorMap para pintar las filas de los jugadores.
	 * @param players
	 *            List of players to put the updated mode
	 *            <p>
	 *            La lista de jugadores que contiene los nuevos modos de juego
	 * @param thisView
	 *            This parameter tells which player this view belongs to. If
	 *            null, there is an only view to every player and every player
	 *            mode should be shown. In any other case, the player contained
	 *            in this parameter should only be able to see its own mode.
	 *            <p>
	 *            Este parámetro contiene la información sobre a qué jugador
	 *            pertenece esta vista. Si es null, hay una unica vista para
	 *            todos los jugadores y los modos de todos los jugadores
	 *            deberian mostrarse. En otro caso, únicamente el jugador al que
	 *            pertenece la vista debería poder ver su propio modo.
	 */
	public void update(Board board, PieceColorMap colorMap,
			HashMap<Piece, String> players, Piece thisView) {
		this.colorMap = colorMap;
		int i = 0;
		for (Piece p : pieces) {
			if (thisView == null || thisView.equals(p))
				model.setValueAt(players.get(p), i, 1);
			model.setValueAt(board.getPieceCount(p), i, 2);
			model.fireTableDataChanged();
			i++;
		}
		this.setModel(model);
		this.repaint();
	}

}
