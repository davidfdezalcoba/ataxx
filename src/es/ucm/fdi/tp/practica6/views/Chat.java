package es.ucm.fdi.tp.practica6.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.ChatCommands.*;
import es.ucm.fdi.tp.practica6.net.GameClient;

/**
 * Represents the chat in the lobby of a client.
 * 
 * @author David Fdez Alcoba, Manuel Sanche Torron
 *
 */
public class Chat extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3231009277522235399L;

	/**
	 * A logger to write admin info in the chat area.
	 */
	private final static Logger log = Logger.getLogger(GameClient.class
			.getSimpleName());

	// Swing components
	private JTextArea jtaChat;
	private JScrollPane jspChat;
	private JTextField jtfChatEntry;
	private JButton jbSend;
	private JPanel jpEntry;

	/**
	 * The client to whom this chat belongs
	 */
	private GameClient c;

	/**
	 * The piece that represents this client in-game.
	 */
	private Piece localPiece;

	public Chat(GameClient gameClient) {
		initComponents();
		this.c = gameClient;
		this.localPiece = c.getPlayerPiece();
		initializeCommandSet();
	}

	public JTextArea getTextArea() {
		return this.jtaChat;
	}

	/**
	 * Initialized the supported chat commands.
	 */
	private void initializeCommandSet() {
		ChatCommandSet.addCommand(new ShowConnectedPlayersCommand(localPiece));
		ChatCommandSet.addCommand(new ChatQuitCommand());
		ChatCommandSet.addCommand(new ChatHelpCommand());
	}

	/**
	 * Initializes the swing components
	 */
	private void initComponents() {
		jtaChat = new JTextArea();
		jtaChat.setEditable(false);
		jspChat = new JScrollPane(jtaChat);
		DefaultCaret d = (DefaultCaret) jtaChat.getCaret();
		d.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		jtfChatEntry = new JTextField();
		jtfChatEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				processTextEvent();
			}
		});

		jbSend = new JButton("Send");
		jbSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				processTextEvent();
			}

		});

		jpEntry = new JPanel();
		jpEntry.setLayout(new BorderLayout());
		jpEntry.add(jtfChatEntry, BorderLayout.CENTER);
		jpEntry.add(jbSend, BorderLayout.EAST);

		this.setLayout(new BorderLayout());
		add(jspChat, BorderLayout.CENTER);
		add(jpEntry, BorderLayout.SOUTH);
		Border border = BorderFactory.createTitledBorder("Chat");
		setBorder(border);

	}

	/**
	 * Processes a chat text entry.
	 */
	private void processTextEvent() {
		String entry = jtfChatEntry.getText();
		if (!entry.equals("")) {
			if (entry.startsWith("/")) {
				showText(entry);
				jtfChatEntry.setText("");
				Command cmd = ChatCommandSet.parse(entry.substring(1,
						entry.length()));
				if (cmd != null) {
					try {
						cmd.execute(c);
					} catch (GameError e) {
						log.log(Level.WARNING,
								"An error occurred: " + e.getMessage());
					}
				}
			} else {
				String text = jtfChatEntry.getText();
				c.handleTextEntry(null, text);
				jtfChatEntry.setText("");
			}
		}
	}

	/**
	 * Shows the given text in the chat area.
	 * 
	 * @param text
	 */
	public void showText(String text) {
		jtaChat.append(text + "\n");
	}

}
