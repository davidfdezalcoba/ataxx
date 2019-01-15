package es.ucm.fdi.tp.practica5;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.practica5.BoardUI.PlayControlsListener;

public class Square extends JLabel {
	
	public Square(int row, int col, PlayControlsListener playControlsListener){
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e))
						playControlsListener.squareWasLeftClicked(row, col);
					else if(SwingUtilities.isRightMouseButton(e))
						playControlsListener.squareWasRightClicked();
			}
		});
	}

}
