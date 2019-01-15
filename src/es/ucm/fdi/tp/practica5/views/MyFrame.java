package es.ucm.fdi.tp.practica5.views;

import javax.swing.JFrame;

/**
 * A class that represents the JFrame hosting the application.
 * 
 * It contains as title the name of the game in play.
 * 
 * <p>
 * La clase que representa el JFrame que hostea la aplicacion.
 * 
 * Contiene como título el nombre del juego al que se esta jugando.
 * 
 * @author David Fdez Alcoba, Manuel Sánchez Torrón.
 */
public class MyFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3936761206623828698L;

	public MyFrame(String gamedesc, GenericSwingView v) {
		super(gamedesc);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(v);
		setSize(600, 400);
		setVisible(false);
	}

}
