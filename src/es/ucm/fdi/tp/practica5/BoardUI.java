package es.ucm.fdi.tp.practica5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;

import es.ucm.fdi.tp.basecode.bgame.model.Board;

public class BoardUI extends JPanel {

	private Board board;
	private PieceColorMap colorMap;
	private JLabel[][] squares;
	private PlayControlsListener playControlsListener;
	
	public interface PlayControlsListener {
		
		void squareWasLeftClicked(int i, int j);
		
		void squareWasRightClicked();
		
	}

	public BoardUI(PlayControlsListener playControlsListener) {
		setPreferredSize(new Dimension(500, 500));
		this.playControlsListener = playControlsListener;
	}

	public void setBoard(Board board, PieceColorMap colorMap) {
		
		this.board = board;
		this.colorMap = colorMap;

		setLayout(new GridLayout(board.getRows(), board.getCols()));

		squares = new JLabel[board.getRows()][board.getCols()];

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				squares[i][j] = new Square(i, j , playControlsListener);
				squares[i][j].setBackground(Color.GRAY);
				squares[i][j].setOpaque(true);
				squares[i][j].setBorder(BorderFactory
						.createLineBorder(Color.WHITE));
				add(squares[i][j]);
			}
		}

		paintPieces();

	}
	
	public void paintPieces(){
		for(int i = 0; i < board.getRows(); i++)
			for(int j = 0; j < board.getCols(); j++){
				if(board.getPosition(i, j) != null){
					if(colorMap.containsKey(board.getPosition(i, j)))
						squares[i][j].setBackground(colorMap.getColorFor(board.getPosition(i, j)));
					else
						squares[i][j].setBackground(Color.BLACK);
				}
				else{
					squares[i][j].setBackground(Color.GRAY);
				}
			}
	}

	public void update(Board board, PieceColorMap colorMap) {
		this.board = board;
		this.colorMap = colorMap;
		paintPieces();
	}

}
