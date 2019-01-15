package es.ucm.fdi.tp.practica5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.BoardUI.PlayControlsListener;
import es.ucm.fdi.tp.practica5.SettingsPanel.SettingsControlsListener;

public abstract class GenericSwingView extends JPanel implements GameObserver,
		SettingsControlsListener, PlayControlsListener {

	/**
	 * The list of pieces involved in the game. It is stored when the game
	 * starts and used when the state is printed.
	 */
	protected List<Piece> pieces;

	protected BoardUI boardUI;
	protected SettingsPanel settingsPanel;

	protected Board board;
	protected Piece thisPiece;
	protected Player random;
	protected Player intelligent;
	protected SwingCtrlMVC control;
	protected HashMap<Piece, String> players;
	protected PieceColorMap colorMap;
	protected List<Color> colors = new ArrayList<Color>();

	public void initComponents() {
		boardUI = new BoardUI(this);
		settingsPanel = new SettingsPanel(board, pieces, control, thisPiece,
				random, intelligent, colorMap, this);
		setLayout(new BorderLayout());
		add(boardUI, BorderLayout.CENTER);
		add(settingsPanel, BorderLayout.EAST);
	}

	public GenericSwingView(final Observable<GameObserver> game,
			final Controller ctrl, Piece viewPiece, Player random,
			Player intelligent) {
		game.addObserver(this);
		this.control = (SwingCtrlMVC) ctrl;
		this.thisPiece = viewPiece;
		this.random = random;
		this.intelligent = intelligent;
		this.colors.add(Color.RED);
		this.colors.add(Color.BLUE);
		this.colors.add(Color.GREEN);
		this.colors.add(Color.YELLOW);
	}

	public void update() {
		this.boardUI.update(board, colorMap);
		this.settingsPanel.update(board, colorMap, players);
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
			Piece turn) {
		// TODO Auto-generated method stub
		this.board = board;
		this.pieces = pieces;
		this.colorMap = new PieceColorMap(pieces, colors);
		this.players = new HashMap<Piece, String>();
		for(Piece p : pieces)
			players.put(p, "Manual");
		initComponents();
		this.settingsPanel.statusUpdate("Starting '" + gameDesc + "'");
		this.boardUI.setBoard(board, colorMap);
		this.settingsPanel.statusUpdate("Turn for " + turn
				+ (turn == thisPiece ? " You!" : ""));
		
		if(thisPiece == null || thisPiece == turn)
			helpMove();
		else{
			settingsPanel.setRandomButtonEnabled(false);
			settingsPanel.setIntelligentButtonEnabled(false);
		} 
		
		this.revalidate();
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		// TODO Auto-generated method stub
		this.settingsPanel.statusUpdate("Game Over!!");
		this.settingsPanel.statusUpdate("Game Status: " + state);
		if (state == State.Won)
			this.settingsPanel.statusUpdate("Winner: " + winner
					+ (winner == thisPiece ? " You!" : ""));

		settingsPanel.setRandomButtonEnabled(false);
		settingsPanel.setIntelligentButtonEnabled(false);
		settingsPanel.setQuitButtonEnabled(true);
		settingsPanel.setRestartButtonEnabled(true);
		update();
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		// TODO Auto-generated method stub
		settingsPanel.setRandomButtonEnabled(false);
		settingsPanel.setIntelligentButtonEnabled(false);
		settingsPanel.setQuitButtonEnabled(false);
		update();
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		// TODO Auto-generated method stub
		this.board = board;
		settingsPanel.restoreButtons();
		update();
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		// TODO Auto-generated method stub
		settingsPanel.statusUpdate("Turn for " + turn
				+ (turn == thisPiece ? " You!" : ""));
		
		if(players.get(turn) != "Manual"){
			settingsPanel.setQuitButtonEnabled(false);
			settingsPanel.setRestartButtonEnabled(false);
			settingsPanel.setIntelligentButtonEnabled(false);
			settingsPanel.setRandomButtonEnabled(false);
		}
		else{
			settingsPanel.setQuitButtonEnabled(true);
			settingsPanel.setRestartButtonEnabled(true);
			settingsPanel.setIntelligentButtonEnabled(true);
			settingsPanel.setRandomButtonEnabled(true);
		}
		if(players.get(turn) == "Manual" && (thisPiece == null || thisPiece == turn)){
			helpMove();
		}else{
			settingsPanel.setRandomButtonEnabled(false);
			settingsPanel.setIntelligentButtonEnabled(false);
			settingsPanel.setQuitButtonEnabled(false);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
		     public void run() {
		    	 if(players.get(turn) == "Random"){
		 			Utils.sleep(1000);
		 			control.makeMove(random);
		 		}
		 		else if(players.get(turn) == "Intelligent"){
		 			Utils.sleep(1000);
		 			control.makeMove(intelligent);
		 		}
		    }
		});
		
		update();
		repaint();
		revalidate();
		
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		if(thisPiece == null)
			JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
		else if(thisPiece.equals(control.getTurn()))
			JOptionPane.showMessageDialog(this.getParent(), msg, "Error", JOptionPane.ERROR_MESSAGE);
		loseSelection();
	}

	@Override
	public void selectColorPressed(Piece p) {
		// TODO Auto-generated method stub
		
		Color newColor = JColorChooser.showDialog(null,
				"Selecciona color para la pieza "
						+ p,
				colorMap.getColorFor(p));

		if (newColor != null){
			colorMap.setColorFor(p, newColor);
			settingsPanel.statusUpdate("Piece " + p + " has changed its color.");
		}
		
		update();
	}

	@Override
	public void setPlayerModePressed(Piece p, String player) {
		// TODO Auto-generated method stub
		this.players.put(p, player);
		if(control.getTurn() == p && player != "Manual")
			this.settingsPanel.setQuitButtonEnabled(false);
		else
			this.settingsPanel.setQuitButtonEnabled(true);
		if(player == "Random" && p.equals(this.control.getTurn()))
			control.makeMove(random);
		else if(player == "Intelligent" && p.equals(this.control.getTurn()))
			control.makeMove(intelligent);
		loseSelection();
		update();
	}

	@Override
	public abstract void automaticPressed();

	@Override
	public abstract void intelligentPressed();

	@Override
	public void quitPressed() {
		// TODO Auto-generated method stub
		if (JOptionPane.showConfirmDialog(null,
				"Are you sure you want to exit the game?", "Quit",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			control.stop();
			System.exit(0);
		}
	}

	@Override
	public void restartPressed() {
		// TODO Auto-generated method stub
		boardUI.setVisible(false);
		settingsPanel.setVisible(false);
		control.restart();
	}

	@Override
	public abstract void squareWasLeftClicked(int i, int j);

	@Override
	public abstract void squareWasRightClicked();
	
	public abstract void helpMove();
	
	public abstract void loseSelection();

}
