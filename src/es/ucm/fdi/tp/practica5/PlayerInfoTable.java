package es.ucm.fdi.tp.practica5;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class PlayerInfoTable extends JTable {

	private DefaultTableModel model;
	private PieceColorMap colorMap;
	private List<Piece> pieces;

	public PlayerInfoTable(DefaultTableModel model,
			PieceColorMap colorMap, List<Piece> pieces,
			HashMap<Piece, String> players) {
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

	public void update(Board board, PieceColorMap colorMap, HashMap<Piece, String> players, Piece thisView) {
		this.colorMap = colorMap;
		int i = 0;
		for(Piece p : pieces){
			if(thisView == null || thisView == p)
				model.setValueAt(players.get(p), i, 1);
			model.setValueAt(board.getPieceCount(p), i, 2);
			model.fireTableDataChanged();
			i++;
		}
		this.setModel(model);
		this.repaint();
	}

}
