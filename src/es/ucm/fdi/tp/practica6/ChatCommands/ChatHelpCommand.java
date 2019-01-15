package es.ucm.fdi.tp.practica6.ChatCommands;

import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.practica6.net.NetGameController;

/**
 * Represents the /help command. Shows a list of the available commands and some
 * usage information
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class ChatHelpCommand extends ChatCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4782740536160594812L;

	@Override
	public String helpText() {
		// TODO Auto-generated method stub
		return "/help: Shows a list with all possible commands";
	}

	@Override
	public Command parse(String line) {
		// TODO Auto-generated method stub
		if (line.trim().equalsIgnoreCase("help")) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public void execute(NetGameController c) {
		// TODO Auto-generated method stub
		c.showHelp();
	}

}
