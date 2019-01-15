package es.ucm.fdi.tp.practica5.views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * Class SettingsPanel
 * 
 * A class that represents the settings panel for a board game.
 * 
 * It contains: -A status area where status updates are shown. -A table that
 * contains information about the players. -A color selection area where players
 * can change piece colors. -A mode selection area where players can change its
 * playing modes. -An automatic moves section where players can make an only
 * random/intlligent move. -Quit/Restart buttons.
 * 
 * <p>
 * Una clase que representa el panel de opciones de un juego de mesa
 * 
 * Contiene: -Un area de estado que muestra los cambios. -Una tabla que contiene
 * la información sobre los jugadores. -Un area de seleccion de color que
 * permite cambiar los colores de las piezas. -Un area de seleccion de modo que
 * permite cambiar los modos de juego de los jugadores. -Un panel de movimientos
 * automaticos en el que los jugadors pueden crear un unico movimiento
 * random/intelligent. -Botones de quit, restart.
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón.
 */
public class SettingsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -535005026182013434L;
	private Board board;
	private List<Piece> pieces;
	private PieceColorMap colorMap;
	private Piece thisView;
	private Player random;
	private Player intelligent;
	private HashMap<Piece, String> swingPlayers;

	// Status management area components
	private JScrollPane jspStatus;
	private JTextArea jtaStatus;

	// Player Information table components
	private JScrollPane jspPlayerInfo;
	private PlayerInfoTable jtPlayerInfo;
	private String[][] tableData;

	// Color selection area components
	private JPanel jpSelectColor;
	private JComboBox<Piece> jcbColors;
	private JButton jbSelectColor;

	// Player modes selection area components
	private JPanel jpPlayerModes;
	private JComboBox<Piece> jcbPlayer;
	private JComboBox<String> jcbPlayerMode;
	private JButton jbSetPlayerMode;

	// Automatic moves area components
	private JPanel jpAutomaticMoves;
	private JButton jbAutomatic;
	private JButton jbIntelligent;

	// Timeout control panel
	private int timeout;
	private JPanel jpTimeout;
	private JSpinner jsTimeout;
	private JButton jbSetTimeout;

	// Quit-restart area components
	private JPanel jpQuitRestart;
	private JButton jbQuit;
	private JButton jbRestart;

	/**
	 * The class that implements the interface SettingsControlsListener
	 */
	private SettingsControlsListener controlsListener;

	/**
	 * An interface that provides the methods that will dictate the actions to
	 * be performed when a button in this settings panel is pressed.
	 * 
	 * <p>
	 * Una interfaz que proporiona los metodos que elegirán las actiones que se
	 * realizarán cuando un botón de este panel de configuración es pulsado.
	 * clase.
	 * 
	 * @author David Fdez Alcoba, Manuel Sánchez Torrón.
	 *
	 */
	public interface SettingsControlsListener {
		void selectColorPressed(Piece p);

		void setPlayerModePressed(Piece p, String string);

		void automaticPressed();

		void intelligentPressed();

		void quitPressed();

		void restartPressed();
	}

	public SettingsPanel(HashMap<Piece, String> swingPlayers,
			Board board, List<Piece> pieces, Piece thisView, Player random,
			Player intelligent, PieceColorMap colorMap,
			SettingsControlsListener controlsListener) {
		this.swingPlayers = swingPlayers;
		this.board = board;
		this.pieces = pieces;
		this.thisView = thisView;
		this.random = random;
		this.intelligent = intelligent;
		this.colorMap = colorMap;
		this.controlsListener = controlsListener;
		initComponents();
	}

	/**
	 * Initializes all the components included in the settings panel.
	 * 
	 * <p>
	 * Inicializa todas las componentes incluídas en el panel de configuración.
	 */
	public void initComponents() {

		// Status panel
		jtaStatus = new JTextArea();
		jtaStatus.setEditable(false);
		DefaultCaret caret = (DefaultCaret) jtaStatus.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		jspStatus = new JScrollPane(jtaStatus);
		TitledBorder border = BorderFactory
				.createTitledBorder("Status management");
		jspStatus.setBorder(border);
		jspStatus.setPreferredSize(new Dimension(250, 700));

		// Player Information table
		initTableData();
		String[] columnas = { "Player", "Mode", "#Pieces" };
		DefaultTableModel model = new DefaultTableModel(tableData, columnas);
		jtPlayerInfo = new PlayerInfoTable(model, colorMap, pieces,
				swingPlayers, random, intelligent);
		jtPlayerInfo.setEnabled(false);
		jspPlayerInfo = new JScrollPane(jtPlayerInfo);
		border = BorderFactory.createTitledBorder("Player Information");
		jspPlayerInfo.setBorder(border);

		// Select Color panel
		Piece[] piecesNames = new Piece[pieces.size()];
		int i = 0;
		for (Piece p : pieces) {
			piecesNames[i] = p;
			i++;
		}
		jcbColors = new JComboBox<Piece>(piecesNames);
		jbSelectColor = new JButton("Select Color");
		jbSelectColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlsListener.selectColorPressed((Piece) jcbColors
						.getSelectedItem());
			}
		});
		jpSelectColor = new JPanel();
		jpSelectColor.add(jcbColors);
		jpSelectColor.add(jbSelectColor);
		border = BorderFactory.createTitledBorder("Select Color");
		jpSelectColor.setBorder(border);

		// Select Player Modes panel
		jcbPlayer = setPlayerModesComboBox1();
		jcbPlayerMode = setPlayerModesComboBox2();
		jbSetPlayerMode = new JButton("Set");
		jbSetPlayerMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlsListener.setPlayerModePressed(
						(Piece) jcbPlayer.getSelectedItem(),
						(String) jcbPlayerMode.getSelectedItem());
			}
		});
		jpPlayerModes = new JPanel();
		jpPlayerModes.add(jcbPlayer);
		jpPlayerModes.add(jcbPlayerMode);
		jpPlayerModes.add(jbSetPlayerMode);
		border = BorderFactory.createTitledBorder("Player Modes");
		jpPlayerModes.setBorder(border);

		// Automatic Moves panel
		jbAutomatic = new JButton("Random");
		if (random == null)
			jbAutomatic.setEnabled(false);
		jbAutomatic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlsListener.automaticPressed();
			}
		});
		jbIntelligent = new JButton("Intelligent");
		if (intelligent == null)
			jbIntelligent.setEnabled(false);
		jbIntelligent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlsListener.intelligentPressed();
			}
		});
		jpAutomaticMoves = new JPanel();
		jpAutomaticMoves.add(jbAutomatic);
		jpAutomaticMoves.add(jbIntelligent);
		border = BorderFactory.createTitledBorder("Automatic Moves");
		jpAutomaticMoves.setBorder(border);

		// Timeout panel
		timeout = 1;
		SpinnerNumberModel m = new SpinnerNumberModel(1, 1, 6, 1);
		jsTimeout = new JSpinner(m);
		jbSetTimeout = new JButton("Set");
		jbSetTimeout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				timeout = (int) jsTimeout.getValue();
			}

		});

		jpTimeout = new JPanel();
		jpTimeout.add(jsTimeout);
		jpTimeout.add(jbSetTimeout);
		border = BorderFactory.createTitledBorder("Timeout");
		jpTimeout.setBorder(border);

		// Quit Restart panel
		jpQuitRestart = new JPanel();
		jpQuitRestart.setLayout(new FlowLayout());
		jbQuit = new JButton("Quit");
		jbQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlsListener.quitPressed();
			}
		});
		jpQuitRestart.add(jbQuit);
		jbRestart = new JButton("Restart");
		jbRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlsListener.restartPressed();
			}
		});
		if (thisView != null)
			jbRestart.setVisible(false);
		jpQuitRestart.add(jbRestart);

		// Add all previous components
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(jspStatus);
		add(jspPlayerInfo);
		add(jpSelectColor);
		add(jpPlayerModes);
		add(jpAutomaticMoves);
		if (thisView == null)
			add(jpTimeout);
		add(jpQuitRestart);
		setToolTips();

		this.setPreferredSize(new Dimension(250, 500));
	}

	/**
	 * Writes in the status management area an update given in the text
	 * parameter
	 * 
	 * <p>
	 * Escribe en el area de estado una actualización dada en el parametro text
	 * 
	 * @param text
	 *            The update we want to make.
	 * 
	 *            <p>
	 *            La actualización.
	 */
	public void statusUpdate(String text) {
		jtaStatus.append("* " + text + '\n');
	}

	/**
	 * Sets the JComboBox used in PlayerModes according to the pieces in game
	 * and to who this specific view belongs to.
	 * 
	 * <p>
	 * Configura el JComboBox usado en PlayerModes según las piezas en juego y a
	 * quien pertenece esta vista.
	 * 
	 * @return The initialized JComboBox
	 */
	private JComboBox<Piece> setPlayerModesComboBox1() {
		Piece[] players;
		if (thisView == null) {
			players = new Piece[pieces.size()];
			int i = 0;
			for (Piece p : pieces) {
				players[i] = p;
				i++;
			}
		} else {
			players = new Piece[1];
			players[0] = thisView;
		}
		return new JComboBox<Piece>(players);
	}

	/**
	 * Sets the JComboBox used in PlayerModes according to the player modes
	 * allowed in this game.
	 * 
	 * <p>
	 * Configura la JComboBox usada en PlayerModes segun los modos de los
	 * jugadores permitidos en el juego.
	 * 
	 * @return
	 */
	private JComboBox<String> setPlayerModesComboBox2() {
		if (random == null && intelligent == null) {
			String[] modes = { "Manual" };
			return new JComboBox<String>(modes);
		} else if (random == null) {
			String[] modes = { "Manual", "Intelligent" };
			return new JComboBox<String>(modes);
		} else if (intelligent == null) {
			String[] modes = { "Manual", "Random" };
			return new JComboBox<String>(modes);
		} else {
			String[] modes = { "Manual", "Random", "Intelligent" };
			return new JComboBox<String>(modes);
		}
	}

	/**
	 * Initializes the Player Information table data.
	 * 
	 * <p>
	 * Inicializa la tabla Player Information
	 */
	private void initTableData() {
		tableData = new String[pieces.size()][3];
		int i = 0;
		for (Piece p : pieces) {
			tableData[i][0] = p.toString();
			if (thisView == null || thisView.equals(p))
				tableData[i][1] = "Manual";
			else
				tableData[i][1] = "";
			if (board.getPieceCount(p) != null)
				tableData[i][2] = board.getPieceCount(p).toString();
			else
				tableData[i][2] = "";
			i++;
		}
	}

	/**
	 * Updates the information shown in the Player information table
	 * 
	 * <p>
	 * Actuaaliza la informacion mostrada en la tabla Player Information
	 * 
	 * @param board
	 *            The updated board that contains information about #piece.
	 *            <p>
	 *            El tablero actualizado que contiene la información sobre
	 *            #piezas.
	 * @param colorMap
	 *            The updated colorMap that tells the color to paint a piece
	 *            <p>
	 *            El colorMap actualizado que contiene la información acerca del
	 *            color de las piezas
	 * @param swingPlayers
	 *            The updated players corresponding to the pieces.
	 *            <p>
	 *            Los modos de juego actualizados para cada pieza.
	 */
	public void update(Board board, PieceColorMap colorMap,
			HashMap<Piece, String> swingPlayers) {
		this.board = board;
		this.colorMap = colorMap;
		this.swingPlayers = swingPlayers;
		jtPlayerInfo.update(board, colorMap, swingPlayers, thisView);
	}

	/**
	 * Sets the Random button enabled according to the parameter b
	 * 
	 * <p>
	 * Configura el boton Random como activo o no segun el parametro b
	 * 
	 * @param b
	 *            Indicates whether Random button should be enabled or not.
	 *            <p>
	 *            Indica si el boton Random debería estar activo.
	 */
	public void setRandomButtonEnabled(Boolean b) {
		this.jbAutomatic.setEnabled(b);
	}

	/**
	 * Sets the Intelligent button enabled according to the parameter b
	 * 
	 * <p>
	 * Configura el boton Intelligent como activo o no segun el parametro b
	 * 
	 * @param b
	 *            Indicates whether Intelligent button should be enabled or not.
	 *            <p>
	 *            Indica si el boton Intelligent debería estar activo.
	 * 
	 */
	public void setIntelligentButtonEnabled(Boolean b) {
		this.jbIntelligent.setEnabled(b);
	}

	/**
	 * Sets the Restart button enabled according to the parameter b
	 * 
	 * <p>
	 * Configura el boton Restart como activo o no segun el parametro b
	 * 
	 * @param b
	 *            Indicates whether Restart button should be enabled or not.
	 *            <p>
	 *            Indica si el boton Restart debería estar activo.
	 * 
	 */
	public void setRestartButtonEnabled(Boolean b) {
		this.jbRestart.setEnabled(b);
	}

	/**
	 * Sets the Quit button enabled according to the parameter b
	 * 
	 * <p>
	 * Configura el boton Quit como activo o no segun el parametro b
	 * 
	 * @param b
	 *            Indicates whether Quit button should be enabled or not.
	 *            <p>
	 *            Indica si el boton Quit debería estar activo.
	 * 
	 */
	public void setQuitButtonEnabled(boolean b) {
		// TODO Auto-generated method stub
		this.jbQuit.setEnabled(b);
	}

	/**
	 * Sets the SetPlayerMode button enabled according to the parameter b
	 * 
	 * <p>
	 * Configura el boton SetPlayerMode como activo o no segun el parametro b
	 * 
	 * @param b
	 *            Indicates whether SetPlayerMode button should be enabled or
	 *            not.
	 *            <p>
	 *            Indica si el boton SetPlayerMode debería estar activo.
	 * 
	 */
	public void setSetPlayerModeButtonEnabled(boolean b) {
		this.jbSetPlayerMode.setEnabled(b);
	}

	public int getTimeout() {
		return timeout;
	}

	/**
	 * Sets the tooptips for the componenets in this panel
	 * 
	 * <p>
	 * Configura los tooltips para las componenetes del panel.
	 */
	private void setToolTips() {
		this.jtaStatus
				.setToolTipText("<html>Shows any change that may occur during the game");
		this.jtPlayerInfo
				.setToolTipText("<html>Shows the information related to the pieces in game");
		this.jcbColors
				.setToolTipText("<html>Choose the piece to set the color to");
		this.jbSelectColor
				.setToolTipText("<html>Click to select a new color to the piece");
		this.jcbPlayer
				.setToolTipText("<html>Choose the piece to set the mode to");
		this.jcbPlayerMode
				.setToolTipText("<html>Choose the new mode for the piece");
		this.jbSetPlayerMode.setToolTipText("<html>Click to set the new mode");
		this.jbAutomatic.setToolTipText("<html>Click to make a random move");
		this.jbIntelligent
				.setToolTipText("<html>Click to make an intelligent move");
		this.jbQuit.setToolTipText("<html>Click to exit the game");
		this.jbRestart.setToolTipText("<html>Click to restart the game");
	}

}
