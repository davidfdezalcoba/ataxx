package es.ucm.fdi.tp.practica5;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

@SuppressWarnings("serial")
public class PieceColorMap extends HashMap<Piece, Color> {
	
	public PieceColorMap(List<Piece> pieces, List<Color> colors){
		super(pieces.size());
		for(int i = 0; i < pieces.size(); i++){
			this.put(pieces.get(i), colors.get(i));
		}
	}

	public Color getColorFor(Piece p){
		return this.get(p);
	}
	
	public void setColorFor(Piece p, Color color){
		this.put(p, color);
	}

}
