package es.ucm.fdi.tp.practica6;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.control.commands.PlayCommand;
import es.ucm.fdi.tp.basecode.bgame.control.commands.QuitCommand;
import es.ucm.fdi.tp.basecode.bgame.control.commands.RestartCommand;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica6.ChatCommands.ChatCommandSet;
import es.ucm.fdi.tp.practica6.ChatCommands.MessageCommand;
import es.ucm.fdi.tp.practica6.ChatCommands.ShowConnectedPlayersCommand;
import es.ucm.fdi.tp.practica6.responses.ChatMessageResponse;
import es.ucm.fdi.tp.practica6.responses.Response;

public class GameClient extends NetGameController implements
		Observable<GameObserver> {

	private static final Logger log = Logger.getLogger(GameServer.class
			.getSimpleName());

	private int timeout;
	private String host;
	private int port;
	private List<GameObserver> observers;
	private Piece localPiece;
	private GameFactory gameFactory;
	private Connection connectionToServer;
	private boolean gameOver;
	private Lobby lobby;

	public GameClient(Integer serverPort, String serverHost) throws Exception {
		super(null, null);
		timeout = 2000;
		port = serverPort;
		host = serverHost;
		observers = new ArrayList<GameObserver>();
		gameOver = false;
		connect();
		lobby = new Lobby(localPiece, this);
		this.addObserver(lobby);
	}

	public GameFactory getGameFactory() {
		return gameFactory;
	}

	public Piece getPlayerPiece() {
		return localPiece;
	}

	public Connection getConnectionToServer() {
		return this.connectionToServer;
	}

	private void connect() throws Exception {
		try {
			connectionToServer = new Connection(new Socket(host, port), timeout);
			connectionToServer.sendObject("Connect");
			Object response = connectionToServer.getObject();
			if (response instanceof Exception)
				throw (Exception) response;

			gameFactory = (GameFactory) connectionToServer.getObject();
			localPiece = (Piece) connectionToServer.getObject();

		} catch (IOException | ClassNotFoundException e) {
			log.log(Level.SEVERE, "Failed while trying to connect");
			throw new Exception();
		}
	}

	public void start() {
		this.observers.add(new GameObserver() {
			@Override
			public void onGameStart(Board board, String gameDesc,
					List<Piece> pieces, Piece turn) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onGameOver(Board board, State state, Piece winner) {
				// TODO Auto-generated method stub
				gameOver = true;
				try {
					connectionToServer.stop();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

			}

		});

		gameOver = false;
		while (!gameOver) {
			try {
				Object ob = connectionToServer.getObject();
				Response res = (Response) ob; // read a response

				// If it is just a chat message...
				if (res instanceof ChatMessageResponse) {
					((ChatMessageResponse) res).run(lobby);
				} else { // Otherwise it'll be a change on the game
					for (GameObserver o : observers) {
						// execute the response on the observer o
						res.run(o);
					}
				}
			} catch (SocketTimeoutException e) {
				log.log(Level.FINE, "Failed to read; will retry");
			} catch (ClassNotFoundException | IOException e) {
				log.log(Level.WARNING, "Failed to read: bad serialization");
				gameOver = true;
			}
		}
		log.log(Level.INFO, "Client exiting gracefully");

	}

	@Override
	public void addObserver(GameObserver o) {
		// TODO Auto-generated method stub
		observers.add(o);
	}

	@Override
	public void removeObserver(GameObserver o) {
		// TODO Auto-generated method stub
		observers.remove(o);
	}

	@Override
	public void makeMove(Player player) {
		forwardCommand(new PlayCommand(player));
	}

	@Override
	public void stop() {
		forwardCommand(new QuitCommand());
	}

	@Override
	public void restart() {
		forwardCommand(new RestartCommand());
	}

	@Override
	public void showHelp() {
		// TODO Auto-generated method stub
		this.lobby.showText(ChatCommandSet.helpText());
	}

	@Override
	public void showPlayers(Piece p) {
		// TODO Auto-generated method stub
		forwardCommand(new ShowConnectedPlayersCommand(p));
	}

	@Override
	public void handleTextEntry(Piece p, String text) {
		// TODO Auto-generated method stub
		forwardCommand(new MessageCommand(localPiece, text));
	}

	/**
	 * A method used to send a command object to the server
	 * 
	 * @param cmd
	 *            The command we want to send
	 */
	public void forwardCommand(Command cmd) {
		if (!gameOver) {
			try {
				connectionToServer.sendObject(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.log(Level.WARNING,
						"An error occurred, couldn't communicate with server");
				e.printStackTrace();
			}
		}
	}
}
