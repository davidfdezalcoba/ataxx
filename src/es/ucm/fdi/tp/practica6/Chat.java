package es.ucm.fdi.tp.practica6;

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

public class Chat extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3231009277522235399L;

	private final static Logger log = Logger.getLogger(GameServer.class
			.getSimpleName());

	private JTextArea jtaChat;
	private JScrollPane jspChat;
	private JTextField jtfChatEntry;
	private JButton jbSend;
	private GameClient c;
	private Piece localPiece;

	private JPanel jpEntry;

	public Chat(GameClient gameClient) {
		initComponents();
		this.c = gameClient;
		this.localPiece = c.getPlayerPiece();
		initializeCommandSet();
	}

	public JTextArea getTextArea() {
		return this.jtaChat;
	}

	private void initializeCommandSet() {
		// TODO Auto-generated method stub
		ChatCommandSet.addCommand(new ShowConnectedPlayersCommand(localPiece));
		ChatCommandSet.addCommand(new ChatQuitCommand());
		ChatCommandSet.addCommand(new ChatHelpCommand());
	}

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
				handleTextEntry(text);
				jtfChatEntry.setText("");
			}
		}
	}

	public void showText(String text) {
		jtaChat.append(text + "\n");
	}

	private void handleTextEntry(String text) {
		c.handleTextEntry(null, text);
	}

}
