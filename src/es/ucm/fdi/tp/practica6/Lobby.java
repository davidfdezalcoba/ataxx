package es.ucm.fdi.tp.practica6;

import java.awt.BorderLayout;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.*;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class Lobby extends JPanel implements GameObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5562996996495387545L;

	private final static Logger log = Logger.getLogger(GameServer.class
			.getSimpleName());

	protected String name;

	private JFrame window;
	private Chat chat;
	private Piece thisPiece;

	public Lobby(Piece thisPiece, GameClient g) {

		chat = new Chat(g);

		TextHandler h = new TextHandler(chat.getTextArea());
		h.setFormatter(new SimpleFormatter());
		log.addHandler(h);
		this.thisPiece = thisPiece;

		setLayout(new BorderLayout());
		add(chat, BorderLayout.CENTER);

		log.log(Level.INFO,
				"Welcome to the Lobby! Here you can chat with other players, enjoy!");
		window = new JFrame("Lobby: " + thisPiece);
		window.add(this);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setSize(400, 300);
	}

	public Chat getChat() {
		return this.chat;
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
			Piece turn) {
		log.log(Level.INFO, "Game is about to start");
		window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {

		log.log(Level.INFO, "GameOver !!" + '\n' + "Game Status: " + state);
		if (state.equals(State.Won))
			log.log(Level.INFO, "Winner: " + winner
					+ (winner.equals(thisPiece) ? " You!" : ""));

	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub

		log.log(Level.WARNING, "An error occurred.");
		log.log(Level.WARNING, msg);
	}

	public void showText(String text) {
		chat.showText(text);
	}

}
