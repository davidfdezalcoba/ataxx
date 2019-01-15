package es.ucm.fdi.tp.practica6.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import es.ucm.fdi.tp.practica6.views.ServerGUI;

/**
 * The class GameServer gives a server for a board game. This server accepts
 * clients until every piece in the game is taken by one of them. Rejects any
 * other client until the game stops.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class GameServer extends NetGameController implements GameObserver {

	/**
	 * A logger for printing admin messages in the servers console.
	 */
	private static final Logger log = Logger.getLogger(GameServer.class
			.getSimpleName());

	/**
	 * The port
	 */
	private int port;

	/**
	 * Number of clients for the game this server is providing.
	 */
	private int numPlayers;

	/**
	 * Number of currently connected players.
	 */
	private int numOfConnectedPlayers;

	/**
	 * The factory correspondent to the game provided.
	 */
	private GameFactory gameFactory;

	/**
	 * A list of all the clients currently connected.
	 */
	private List<Connection> clients;

	/**
	 * Tells the server if it is the first time being played. If it is, the game
	 * initializes a new game, otherwise it calls the restart controller method.
	 */
	private boolean firstTime;

	/**
	 * The socket for this server.
	 */
	volatile private ServerSocket server;

	/**
	 * Tells whether the server has been stopped.
	 */
	volatile private boolean stopped;

	/**
	 * Tells whether the game provided is in play or it's been stopped or has
	 * ended.
	 */
	volatile private boolean gameOver;

	/**
	 * A User interface for this server.
	 */
	private ServerGUI gui;

	/**
	 * Initializes variables and registers as an observer.
	 * 
	 * @param gameFactory
	 * @param pieces
	 * @param serverPort
	 */
	public GameServer(GameFactory gameFactory, List<Piece> pieces,
			int serverPort) {
		super(new Game(gameFactory.gameRules()), pieces);
		this.port = serverPort;
		this.numPlayers = pieces.size();
		this.gameFactory = gameFactory;
		this.numOfConnectedPlayers = 0;
		this.clients = new ArrayList<Connection>();
		this.firstTime = true;
		this.stopped = false;
		game.addObserver(this);
	}

	/**
	 * Stops the server and the game if it is still in play and disconnects
	 * every client. Closes the server.
	 */
	public void quitPressed() {
		try {
			stopped = true;
			if (game.getState().equals(Game.State.InPlay))
				stop();
			for (Connection c : clients)
				c.stop();
			server.close();
		} catch (IOException | NullPointerException e1) {
			// TODO Auto-generated catch block
			System.exit(0);
		}
	}

	/**
	 * This method is used for notifying the clients of the changes in the game
	 * or the chat messages someone sends.
	 * 
	 * @param r
	 */
	private void forwardNotification(Response r) {
		for (Connection c : clients) {
			try {
				c.sendObject(r);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Starts the server in the given port and waits for incoming connections
	 */
	private void startServer() {
		try {
			server = new ServerSocket(port);
			log.info("GameServer waiting for connections");

			while (!stopped) {
				Socket s = null;
				s = server.accept();
				handleRequest(s);
			}
			server.close();
		} catch (IOException e) {
			if (!stopped) {
				log.log(Level.WARNING, "error while waiting for a connection: "
						+ e.getMessage());
			}
		}
	}

	/**
	 * Handles an incoming connection request as follows. Accepts the client if
	 * there are available pieces and sends it the gamefactory and its
	 * correspondent piece. If there are enough players to start the game it
	 * starts it and rejects every other request until the game is stopped,
	 * ended.
	 * 
	 * @param s
	 */
	private void handleRequest(Socket s) {
		try {
			Connection c = new Connection(s);
			Object clientRequest = c.getObject();
			if (!(clientRequest instanceof String)
					&& !((String) clientRequest).equalsIgnoreCase("Connect")) {
				c.sendObject(new GameError("Invalid Request"));
				c.stop();
				return;
			}

			if (numOfConnectedPlayers == numPlayers) {
				log.log(Level.WARNING, "El servidor no admite mas jugadores");
				throw new GameError("El servidor no admite mas jugadores");
			}

			numOfConnectedPlayers++;
			clients.add(c);
			c.sendObject("Ok");
			c.sendObject(gameFactory);
			c.sendObject(pieces.get(clients.indexOf(c)));

			gui.updatePlayers(numOfConnectedPlayers);

			log.log(Level.INFO, "Ok, link to client " + numOfConnectedPlayers
					+ " seems to be up and running");

			if (numOfConnectedPlayers == numPlayers) {
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

	/**
	 * Represents a client listener. While the server is running and the game is
	 * in play executes any command coming from that client. If an error occurs
	 * every client is automatically disconnected from the server.
	 * 
	 * @author David Fdez Alcoba, Manuel Sanchez Torron
	 *
	 */
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
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Starts the client listener.
	 * 
	 * @param c
	 *            The connection to the client
	 */
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
		gui = new ServerGUI(GameServer.this, pieces);
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
		gui.updatePlayers(0);
		log.log(Level.INFO, "Game is over, disconnecting players..." + '\n'
				+ "Ready to start listening to new clients");
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
