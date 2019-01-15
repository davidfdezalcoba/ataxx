package es.ucm.fdi.tp.practica5;

import javax.swing.JFrame;

public class MyFrame extends JFrame {

	public MyFrame(String gamedesc, GenericSwingView v){
		super(gamedesc);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(v);
		setSize(600, 400);
		setVisible(true);
	}
	
}
