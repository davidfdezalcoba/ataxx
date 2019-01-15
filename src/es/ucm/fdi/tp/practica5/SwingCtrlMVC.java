package es.ucm.fdi.tp.practica5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class SwingCtrlMVC extends Controller{

	public SwingCtrlMVC(Game g, List<Piece> pieces) {
		
		super(g, pieces);
		
	}

	@Override
	public void start(){
		// start the game
		game.start(pieces);
		
	}
	
	public Piece getTurn(){
		return this.game.getTurn();
	}
	
}
