package es.ucm.fdi.tp.practica6;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.responses.ChangeTurnResponse;
import es.ucm.fdi.tp.practica6.responses.ChatMessageResponse;
import es.ucm.fdi.tp.practica6.responses.ErrorResponse;
import es.ucm.fdi.tp.practica6.responses.GameOverResponse;
import es.ucm.fdi.tp.practica6.responses.GameStartResponse;
import es.ucm.fdi.tp.practica6.responses.MoveEndResponse;
import es.ucm.fdi.tp.practica6.responses.MoveStartResponse;
import es.ucm.fdi.tp.practica6.responses.Response;

public class GameServer extends NetGameController implements GameObserver {

	private static final Logger log = Logger.getLogger(GameServer.class
			.getSimpleName());

	private int timeout;
	private int port;
	private int numPlayers;
	private int numOfConnectedPlayers;
	private GameFactory gameFactory;
	private List<Connection> clients;
	private JTextArea infoArea;
	private boolean firstTime;

	volatile private ServerSocket server;
	volatile private boolean stopped;
	volatile private boolean gameOver;

	public GameServer(GameFactory gameFactory, List<Piece> pieces,
			int serverPort, int timeout) {
		super(new Game(gameFactory.gameRules()), pieces);
		this.port = serverPort;
		this.numPlayers = pieces.size();
		this.gameFactory = gameFactory;
		this.numOfConnectedPlayers = 0;
		this.clients = new ArrayList<Connection>();
		this.firstTime = true;
		this.timeout = timeout;
		game.addObserver(this);
	}

	public static Logger getLogger() {
		return log;
	}

	private void controlGUI() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					constructGUI();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			throw new GameError(
					"Something went wrong when constructing the GUI");
		}
	}

	private void constructGUI() {
		JFrame window = new JFrame("Game Server");
		infoArea = new JTextArea();
		infoArea.setEditable(false);
		TextHandler h = new TextHandler(infoArea);
		h.setFormatter(new SimpleFormatter());
		log.addHandler(h);
		DefaultCaret caret = (DefaultCaret) infoArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane infoPanel = new JScrollPane(infoArea);
		JButton quitButton = new JButton("Stop server");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					stopped = true;
					if (game.getState().equals(Game.State.InPlay))
						stop();
					for (Connection c : clients)
						c.stop();
					server.close();
					window.dispose();
				} catch (IOException | NullPointerException e1) {
					// TODO Auto-generated catch block
					System.exit(0);
				}
			}
		});
		BorderLayout border = new BorderLayout();
		window.setLayout(border);
		window.add(infoPanel, BorderLayout.CENTER);
		window.add(quitButton, BorderLayout.SOUTH);
		window.setPreferredSize(new Dimension(400, 300));
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}

	// private void log(String s) {
	// SwingUtilities.invokeLater(new Runnable() {
	// @Override
	// public void run() {
	// infoArea.append(">" + s + '\n');
	// }
	// });
	// }

	private void forwardNotification(Response r) {
		for (Connection c : clients) {
			try {
				c.sendObject(r);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void startServer() {
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(timeout);
			log.info("GameServer waiting for connections");

			stopped = false;
			while (!stopped) {
				Socket s = null;
				try {
					s = server.accept();
				} catch (SocketTimeoutException ste) {
					log.log(Level.FINE,
							"Timeout; checking stop flag and re-accepting");
					continue;
				}
				handleRequest(s);
			}
		} catch (IOException e) {
			if (!stopped) {
				log.log(Level.WARNING, "error while waiting for a connection: "
						+ e.getMessage());
			}
		}
	}

	private void handleRequest(Socket s) {
		try {
			Connection c = new Connection(s, timeout);
			Object clientRequest = c.getObject();
			if (!(clientRequest instanceof String)
					&& !((String) clientRequest).equalsIgnoreCase("Connect")) {
				c.sendObject(new GameError("Invalid Request"));
				c.stop();
				return;
			}

			if (numOfConnectedPlayers == numPlayers) {
				throw new GameError("El servidor no admite mas jugadores");
			}

			numOfConnectedPlayers++;
			clients.add(c);
			c.sendObject("Ok");
			c.sendObject(gameFactory);
			c.sendObject(pieces.get(clients.indexOf(c)));

			log.log(Level.INFO, "Ok, link to client " + numOfConnectedPlayers
					+ " seems to be up and running");

			if (numOfConnectedPlayers == pieces.size()) {
				if (firstTime) {
					super.start();
					firstTime = false;
				} else {
					restart();
				}
			}

			startClientListener(c);
		} catch (IOException | ClassNotFoundException _e) {
			log.log(Level.WARNING,
					"Failed to read: could not create object input stream");
		}
	}

	private class ClientListener implements Runnable {

		private Connection c;

		public ClientListener(Connection c) {
			this.c = c;
		}

		@Override
		public void run() {
			while (!stopped && !gameOver) {
				try {
					Command cmd;
					cmd = (Command) c.getObject();
					cmd.execute(GameServer.this);
				} catch (ClassNotFoundException | IOException e) {
					if (!stopped && !gameOver) {
						if (game.getState().equals(Game.State.InPlay))
							stop();
						gameOver = true;
						for (Connection c : clients) {
							try {
								c.stop();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	private void startClientListener(Connection c) {
		gameOver = false;
		Thread t = new Thread(new ClientListener(c));
		t.start();
	}

	// Overridden methods from controller

	@Override
	public synchronized void makeMove(Player player) {
		try {
			super.makeMove(player);
		} catch (GameError e) {

		}
	}

	@Override
	public synchronized void stop() {
		try {
			super.stop();
		} catch (GameError e) {

		}
	}

	@Override
	public synchronized void restart() {
		try {
			super.restart();
		} catch (GameError e) {

		}
	}

	@Override
	public void start() {
		controlGUI();
		startServer();
	}

	@Override
	public synchronized void handleTextEntry(Piece p, String text) {
		forwardNotification(new ChatMessageResponse(p, text));
	}

	@Override
	public synchronized void showPlayers(Piece p) {
		// TODO Auto-generated method stub
		String o = "The list of players is: " + '\n';
		for (int i = 0; i < clients.size(); i++) {
			o += (i + 1) + ".- " + pieces.get(i) + '\n';
		}
		try {
			clients.get(pieces.indexOf(p)).sendObject(
					new ChatMessageResponse(null, o));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void showHelp() {
	}

	// Implemented GameObserver methods

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
			Piece turn) {
		forwardNotification(new GameStartResponse(board, gameDesc, pieces, turn));
		log.log(Level.INFO, "Server is full, game is about to start...");
		log.log(Level.INFO, "Starting game: " + gameDesc + ". Turn for: "
				+ turn);
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		forwardNotification(new GameOverResponse(board, state, winner));
		// stop the game
		if (game.getState().equals(Game.State.InPlay))
			stop();
		gameOver = true;
		for (Connection c : clients) {
			try {
				c.stop();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		clients.clear();
		numOfConnectedPlayers = 0;
		log.log(Level.INFO, "Game is over, disconnecting players...");
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		forwardNotification(new MoveStartResponse(board, turn));
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		forwardNotification(new MoveEndResponse(board, turn, success));
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		forwardNotification(new ChangeTurnResponse(board, turn));
		log.log(Level.INFO, "Turn for player" + turn);
	}

	@Override
	public void onError(String msg) {
		forwardNotification(new ErrorResponse(msg));
		log.log(Level.WARNING, "An error has occurred: " + msg);
	}

}
