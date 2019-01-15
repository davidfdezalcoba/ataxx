package es.ucm.fdi.tp.practica5.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.views.BoardUI.PlayControlsListener;

/**
 * Class GenericSwingView
 * 
 * A class that represents the SwingView for any board game. It contains the
 * board where the game is taking place and a settings panel to change different
 * options
 * 
 * <p>
 * Una clase que representa la SwingView para un juego de tablero. Contiene el
 * tablero en el que se está jugando así como un panel de configuración para
 * cambiar diferentes opciones.
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón.
 */
public abstract class GenericSwingView extends JPanel implements GameObserver,
		SettingsPanel.SettingsControlsListener, PlayControlsListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7502438577475338602L;

	/**
	 * The list of pieces involved in the game. It is stored when the game
	 * starts and used when the state is printed.
	 * 
	 * <p>
	 * La lista de piezas en juego. Se almacena cuando el juego empieza y se usa
	 * cuando se actualiza la tabla
	 */
	protected List<Piece> pieces;

	/**
	 * The board user interface
	 * 
	 * <p>
	 * La interfaz de usuario del tablero.
	 */
	protected BoardUI boardUI;

	/**
	 * The settings panel user interface
	 * 
	 * <p>
	 * El panel de opciones.
	 */
	protected SettingsPanel settingsPanel;

	/**
	 * The board where the game is taking place
	 * 
	 * <p>
	 * El tablero en el que se esta jugando
	 */
	protected Board board;

	/**
	 * The piece that this view belongs to. If null, then the view belongs to
	 * everyone.
	 * 
	 * <p>
	 * La pieza a la que pertenece la vista. Null si pertenece a todos los
	 * jugadores.
	 */
	protected Piece thisPiece;

	/**
	 * The Player used to make random moves
	 * 
	 * <p>
	 * El jugador para realizar movimientos aleatorios.
	 */
	protected Player random;

	/**
	 * The Player used to make intelligent moves
	 * 
	 * <p>
	 * El jugador para realizar movimientos inteligentes.
	 */
	protected Player intelligent;

	/**
	 * The piece that is in turn
	 * 
	 * <p>
	 * La pieza a la que le toca el turno.
	 */
	protected Piece turn;

	/**
	 * The controller of the game
	 * 
	 * <p>
	 * El controlador del juego
	 */
	protected Controller control;

	/**
	 * A HashMap that matches Pieces with players. By default, all players are
	 * manual.
	 * 
	 * <p>
	 * Un HashMap que asocia piezas con jugadores. por defecto todos los
	 * jugadores son manual.
	 */
	protected HashMap<Piece, String> swingPlayers;

	/**
	 * A HashMap that matches Pieces with Colors in order to let to know a piece
	 * how to paint itself.
	 * 
	 * <p>
	 * Un HashMap que asocia piezas con colores para hacer saber a una pieza de
	 * que color pintarse.
	 */
	protected PieceColorMap colorMap;

	/**
	 * The default list of colors used for the initial pieces.
	 * 
	 * <p>
	 * La lista de colores por defecto al iniciar el juego.
	 */
	protected List<Color> colors = new ArrayList<Color>();

	/**
	 * Creates and adds the BoardUI and the SettingsPanel
	 * 
	 * <p>
	 * Crea y añade BoardUI y el SettingsPanel
	 */
	public void initComponents() {
		boardUI = new BoardUI(this);
		settingsPanel = new SettingsPanel(swingPlayers, board, pieces,
				thisPiece, random, intelligent, colorMap, this);
		setLayout(new BorderLayout());
		add(boardUI, BorderLayout.CENTER);
		add(settingsPanel, BorderLayout.EAST);
	}

	public GenericSwingView(final Observable<GameObserver> game,
			final Controller ctrl, Piece viewPiece, Player random,
			Player intelligent) {
		game.addObserver(this); // Register as an observer.
		this.control = ctrl;
		this.thisPiece = viewPiece;
		this.random = random;
		this.intelligent = intelligent;
		this.colors.add(Color.RED);
		this.colors.add(Color.BLUE);
		this.colors.add(Color.ORANGE);
		this.colors.add(Color.YELLOW);
	}

	/**
	 * A private class for making moves in a background thread so the GUI
	 * doesn't freeze meanwhile
	 * 
	 * <p>
	 * Una clase privada para realizar movimientos automaticos para que no se
	 * congele la interfaz de usuario mientras tanto.
	 * 
	 * @author David Fdez Alcoba, Manuel Sanchez Torron
	 *
	 */
	private class AutomaticMovesWorker extends SwingWorker<Object, Object> {

		private Player player;

		public AutomaticMovesWorker(Player player) {
			this.player = player;
		}

		@Override
		protected Object doInBackground() throws Exception {
			// TODO Auto-generated method stub
			control.makeMove(player);
			return null;
		}

	}

	private class TimeoutControl extends Thread {

		private AutomaticMovesWorker a;

		public TimeoutControl(AutomaticMovesWorker a) {
			super("Timeout Control thread");
			this.a = a;
		}

		@Override
		public void run() {
			try {
				a.execute();
				a.get(settingsPanel.getTimeout(), TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				settingsPanel.statusUpdate("Timeout!");
				swingPlayers.put(turn, "Manual");
				a.cancel(true);
			} catch (Exception e) {
			}
		}

	}

	/**
	 * Updates the two main components of the view.
	 * 
	 * <p>
	 * Actualiza los dos componentes principales de la vista.
	 */
	public void update() {
		this.boardUI.update(board, colorMap);
		this.settingsPanel.update(board, colorMap, swingPlayers);
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
			Piece turn) {
		// TODO Auto-generated method stub
		this.board = board;
		this.pieces = pieces;
		this.turn = turn;
		this.colorMap = new PieceColorMap(pieces, colors);
		this.swingPlayers = new HashMap<Piece, String>();
		for (Piece p : pieces)
			swingPlayers.put(p, "Manual");
		initComponents();

		this.settingsPanel.statusUpdate("Starting '" + gameDesc + "'");
		this.boardUI.setBoard(board, colorMap);

		this.settingsPanel.statusUpdate("Turn for " + turn
				+ (turn.equals(thisPiece) ? " You!" : ""));

		if (thisPiece == null || thisPiece.equals(turn))
			helpMove();
		else {
			settingsPanel.setRandomButtonEnabled(false);
			settingsPanel.setIntelligentButtonEnabled(false);
		}
		revalidate();
		SwingUtilities.getWindowAncestor(this).setVisible(true);
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		// TODO Auto-generated method stub
		this.board = board;

		turn = null;

		settingsPanel.statusUpdate("Game Over!!");
		settingsPanel.statusUpdate("Game Status: " + state);
		if (state.equals(State.Won))
			settingsPanel.statusUpdate("Winner: " + winner
					+ (winner.equals(thisPiece) ? " You!" : ""));

		settingsPanel.setRandomButtonEnabled(false);
		settingsPanel.setIntelligentButtonEnabled(false);
		settingsPanel.setQuitButtonEnabled(true);
		settingsPanel.setRestartButtonEnabled(true);
		settingsPanel.setSetPlayerModeButtonEnabled(false);
		update();
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		// TODO Auto-generated method stub
		setAllButtonsEnabled(false);
		update();
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {

		this.board = board;
		this.turn = turn;

		settingsPanel.statusUpdate("Turn for " + turn
				+ (turn.equals(thisPiece) ? " You!" : ""));

		if (swingPlayers.get(turn).equals("Manual")
				&& (thisPiece == null || thisPiece.equals(turn))) {
			helpMove();
			setAllButtonsEnabled(true);
		} else {
			setAllButtonsEnabled(false);
		}

		// If the new turn is for a random/intelligent player, we execute a
		// worker so the GUI doesn't freeze
		if (swingPlayers.get(turn).equals("Random")) {
			new TimeoutControl(new AutomaticMovesWorker(random)).start();
		} else if (swingPlayers.get(turn).equals("Intelligent")) {
			new TimeoutControl(new AutomaticMovesWorker(intelligent)).start();
		}

		update();

	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		if (thisPiece == null)
			JOptionPane.showMessageDialog(null, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
		else if (thisPiece.equals(turn))
			JOptionPane.showMessageDialog(this.getParent(), msg, "Error",
					JOptionPane.ERROR_MESSAGE);

		loseSelection();
		update();
	}

	@Override
	public void selectColorPressed(Piece p) {
		// TODO Auto-generated method stub

		Color newColor = JColorChooser.showDialog(null,
				"Selecciona color para la pieza " + p, colorMap.getColorFor(p));

		if (newColor != null) {
			colorMap.setColorFor(p, newColor);
			settingsPanel
					.statusUpdate("Piece " + p + " has changed its color.");
		}

		update();
	}

	@Override
	public void setPlayerModePressed(Piece p, String player) {

		if (!player.equals(swingPlayers.get(p))) {
			swingPlayers.put(p, player);

			if (turn.equals(p) && !player.equals("Manual")) {
				setAllButtonsEnabled(false);
				if (player.equals("Random")) {
					new TimeoutControl(new AutomaticMovesWorker(random))
							.start();
				} else {
					new TimeoutControl(new AutomaticMovesWorker(intelligent))
							.start();
				}
			}

			loseSelection();
			update();
		}
	}

	@Override
	public void automaticPressed() {
		new TimeoutControl(new AutomaticMovesWorker(random)).start();
	}

	@Override
	public void intelligentPressed() {
		new TimeoutControl(new AutomaticMovesWorker(intelligent)).start();
	}

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
	public void squareWasRightClicked() {
	}

	/**
	 * This method is used to print in the status area a hint to make a move
	 * 
	 * <p>
	 * Este metodo es utilizado para escribir en el area de estado una ayuda
	 * para realizar el movimiento
	 */
	public abstract void helpMove();

	/**
	 * This method loses the selection previously made in games where a move is
	 * made by taking a piece to another cell.
	 * 
	 * <p>
	 * Este metodo pierde una seleccion hecha previamente.
	 */
	public void loseSelection() {
	}

	/**
	 * Represents a selected piece in games that require so.
	 * 
	 * <p>
	 * Representa una pieza seleccionada en los juegos que lo requieren.
	 * 
	 * @param i
	 * @param j
	 */
	protected void newSelection(int i, int j) {
		settingsPanel.statusUpdate("New selection: (" + i + ", " + j + ").");
		settingsPanel
				.statusUpdate("Click on an empty cell to complete the move");
		boardUI.setBackgroundFor(i, j, Color.GREEN);
		setAllButtonsEnabled(false);
	}

	/**
	 * A method to perform a manual move
	 *
	 * <p>
	 * Un metodo para realizar movimientos manuales.
	 *
	 * @param preRow
	 * @param preCol
	 * @param row
	 * @param col
	 */
	protected abstract void performMove(int preRow, int preCol, int row, int col);

	/**
	 * Sets all the buttons contained in the settingsPanel enabled/disabled.
	 * 
	 * <p>
	 * Configura todos los botones en el panel de configuracion como
	 * activos/inactivos.
	 * 
	 * @param b
	 *            A boolean indicating whether to enable or disable
	 *            <p>
	 *            Un booleano indicando si acivar o desactivar.
	 */
	protected void setAllButtonsEnabled(boolean b) {
		settingsPanel.setQuitButtonEnabled(b);
		settingsPanel.setRestartButtonEnabled(b);
		settingsPanel.setIntelligentButtonEnabled(b);
		settingsPanel.setRandomButtonEnabled(b);
	}

}
