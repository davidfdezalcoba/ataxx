package es.ucm.fdi.tp.practica6.ChatCommands;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.practica6.net.NetGameController;

public abstract class ChatCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2033729348594823580L;

	/**
	 * We override this method so it can call the NetGameController, with
	 * methods to execute the new commands introduced in the chat
	 */
	@Override
	public void execute(Controller c) {
		execute((NetGameController) c);
	}

	public abstract void execute(NetGameController c);

	public abstract String helpText();

	public abstract Command parse(String line);
}
