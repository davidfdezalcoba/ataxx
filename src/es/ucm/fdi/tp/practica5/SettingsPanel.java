package es.ucm.fdi.tp.practica5;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class SettingsPanel extends JPanel {

	private Board board;
	private List<Piece> pieces;
	private PieceColorMap colorMap;
	private Piece thisView;
	private Player random;
	private Player intelligent;
	private HashMap<Piece, String> players;

	private JScrollPane jspStatus;
	private JTextArea jtaStatus;

	private JScrollPane jspPlayerInfo;
	private PlayerInfoTable jtPlayerInfo;
	private String[][] tableData;

	private JPanel jpSelectColor;
	private JComboBox<Piece> jcbColors;
	private JButton jbSelectColor;

	private JPanel jpPlayerModes;
	private JComboBox<Piece> jcbPlayer;
	private JComboBox<String> jcbPlayerMode;
	private JButton jbSetPlayerMode;

	private JPanel jpAutomaticMoves;
	private JButton jbAutomatic;
	private JButton jbIntelligent;

	private JPanel jpQuitRestart;
	private JButton jbQuit;
	private JButton jbRestart;
	
	private SettingsControlsListener controlsListener;

	public interface SettingsControlsListener {
		
		void selectColorPressed(Piece p);

		void setPlayerModePressed(Piece p, String player);

		void automaticPressed();

		void intelligentPressed();

		void quitPressed();

		void restartPressed();
		
	}

	public SettingsPanel(Board board, List<Piece> pieces, SwingCtrlMVC control,
			Piece thisView, Player random, Player intelligent,
			PieceColorMap colorMap, SettingsControlsListener controlsListener) {
		this.board = board;
		this.pieces = pieces;
		this.thisView = thisView;
		this.random = random;
		this.intelligent = intelligent;
		this.colorMap = colorMap;
		this.controlsListener = controlsListener;
		initComponents();
	}

	public void initComponents() {

		// Status panel
		jtaStatus = new JTextArea();
		jtaStatus.setEditable(false);
		DefaultCaret caret = (DefaultCaret)jtaStatus.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		jspStatus = new JScrollPane(jtaStatus);
		TitledBorder border = BorderFactory
				.createTitledBorder("Status management");
		jspStatus.setBorder(border);
		jspStatus.setPreferredSize(new Dimension(250, 700));

		// Player Information table
		initTableData(pieces, board);
		String[] columnas = { "Player", "Mode", "#Pieces" };
		DefaultTableModel model = new DefaultTableModel(tableData, columnas);
		jtPlayerInfo = new PlayerInfoTable(model, colorMap,
				pieces, players);
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
						(Piece)jcbPlayer.getSelectedItem(),
						(String)jcbPlayerMode.getSelectedItem());
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
		if(random == null)
			jbAutomatic.setEnabled(false);
		jbAutomatic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlsListener.automaticPressed();
			}
		});
		jbIntelligent = new JButton("Intelligent");
		if(intelligent == null)
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
		if(thisView != null)
			jbRestart.setVisible(false);
		jpQuitRestart.add(jbRestart);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(jspStatus);
		add(jspPlayerInfo);
		add(jpSelectColor);
		add(jpPlayerModes);
		add(jpAutomaticMoves);
		add(jpQuitRestart);

		this.setPreferredSize(new Dimension(250, 500));
	}

	public void statusUpdate(String text) {
		jtaStatus.append("* " + text + '\n');
	}

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

	private JComboBox<String> setPlayerModesComboBox2() {
		if (random == null && intelligent == null)
			return null;
		else if (random == null) {
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

	private void initTableData(List<Piece> pieces, Board board) {
		tableData = new String[pieces.size()][3];
		int i = 0;
		for (Piece p : pieces) {
			tableData[i][0] = p.toString();
			if(thisView == null || thisView == p)
				tableData[i][1] = "Manual";
			else 
				tableData[i][1] = "";
			if(board.getPieceCount(p) != null)
				tableData[i][2] = board.getPieceCount(p).toString();
			else
				tableData[i][2] = "";
			i++;
		}
	}

	public void update(Board board, PieceColorMap colorMap, HashMap<Piece, String> players) {
		this.board = board;
		this.colorMap = colorMap;
		this.players = players;
		jtPlayerInfo.update(board, colorMap, players, thisView);
	}
	
	public void setRandomButtonEnabled(Boolean b){
		this.jbAutomatic.setEnabled(b);
	}
	
	public void setIntelligentButtonEnabled(Boolean b){
		this.jbIntelligent.setEnabled(b);
	}
	
	public void setRestartButtonEnabled(Boolean b){
		this.jbRestart.setEnabled(b);
	}
	
	public void restoreButtons(){
		if(random != null)
			setRandomButtonEnabled(true);
		if(intelligent != null)
			setIntelligentButtonEnabled(true);
	}

	public void setQuitButtonEnabled(boolean b) {
		// TODO Auto-generated method stub
		this.jbQuit.setEnabled(b);
	}

}
