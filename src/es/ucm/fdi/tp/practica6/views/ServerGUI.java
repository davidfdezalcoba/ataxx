package es.ucm.fdi.tp.practica6.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.net.GameServer;

/**
 * Represents the GUI for a game server.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron.
 *
 */
public class ServerGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3861233806929356412L;

	/**
	 * A logger to print admin messages on the server gui
	 */
	private static final Logger log = Logger.getLogger(GameServer.class
			.getSimpleName());

	/**
	 * The server that this GUI represents.
	 */
	private GameServer s;

	/**
	 * The list of players for the server.
	 */
	private List<Piece> pieces;

	// Swing components
	private JTextArea infoArea;
	private JButton quitButton;
	private JTextArea players;
	private JScrollPane jspPlayers;

	public ServerGUI(final GameServer server, final List<Piece> pieces) {
		super("Game server");
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					s = server;
					ServerGUI.this.pieces = pieces;
					initComponents();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			throw new GameError(
					"Something went wrong when constructing the GUI");
		}

	}

	/**
	 * Initializes the swing components.
	 */
	private void initComponents() {

		infoArea = new JTextArea();
		infoArea.setEditable(false);

		TextHandler h = new TextHandler(infoArea);
		h.setFormatter(new SimpleFormatter());
		log.addHandler(h);

		// AutoScroll when something is written in the infoarea
		DefaultCaret caret = (DefaultCaret) infoArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane infoPanel = new JScrollPane(infoArea);
		quitButton = new JButton("Stop server");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				s.quitPressed();
				dispose();
			}
		});

		players = new JTextArea();
		players.setEditable(false);
		DefaultCaret caret2 = (DefaultCaret) players.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		jspPlayers = new JScrollPane(players);
		TitledBorder bplayers = BorderFactory
				.createTitledBorder("Online players");
		jspPlayers.setBorder(bplayers);
		jspPlayers.setPreferredSize(new Dimension(100, 700));

		BorderLayout border = new BorderLayout();
		setLayout(border);
		add(infoPanel, BorderLayout.CENTER);
		add(jspPlayers, BorderLayout.EAST);
		add(quitButton, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(400, 300));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
		revalidate();
	}

	/**
	 * Updates the list of connected players.
	 * 
	 * @param numOfConnectedPlayers
	 */
	public void updatePlayers(int numOfConnectedPlayers) {
		players.setText("");
		for (int i = 0; i < numOfConnectedPlayers; i++) {
			players.append(i + 1 + ".- " + pieces.get(i) + "" + '\n');
		}
	}

}
