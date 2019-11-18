import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import data.Position;
import data.Move;

public class Board extends JPanel {
	private final JFrame frame;
	private Position pos;
	private Piece[][] pieces;
	//private boolean init = true;
	private Square[][] squares;

	public Board(Position pos) {
		this.setPreferredSize(new Dimension(510, 510));
		this.frame = new JFrame("ChessX");
		frame.setBackground(new Color(255, 255, 255));
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		this.squares = new Square[8][8];
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				squares[r][c] = new Square(r, c);
				frame.add(squares[r][c]);
				frame.revalidate();
				//squares[r][c].repaint();
			}
		}
		this.pieces = setUp(pos);
		this.pos = pos;

		//frame.add(squares);
	}

	@Override
	public void paintComponent(Graphics g) {

		//if (init) {
			for (int i = 0; i < 9; i++) {
				g.drawLine(51, 51*(i+1), 459, 51*(i+1));
				g.drawLine(51*(i+1), 51, 51*(i+1), 459);
			}
			for (int c = 0; c < 8; c++) {
				g.drawString("" + c, 51*(c+1) + 25, 40);
				g.drawString("" + c, 40, 51*(c+1) + 25);
			}
			/*g.setColor(new Color(150, 0, 200));
			for (int c = 0; c < 8; c++) {
				for (int r = 0; r < 8; r++) {
					if ((c + r)%2 == 1) {
						g.fillRect(51*(c+1) + 1, 51*(r+1) + 1, 50, 50);
					}
				}
			}
			g.setColor(new Color(255, 255, 255));
			for (int c = 0; c < 8; c++) {
				for (int r = 0; r < 8; r++) {
					if ((c + r)%2 != 1) {
						g.fillRect(51*(c+1) + 1, 51*(r+1) + 1, 50, 50);
					}
				}
			}
			g.setColor(Color.BLACK);
			//init = false;
			 */
		//}
	}

	public Piece[][] setUp(Position pos) {
		Piece[][] pieceArray = new Piece[8][8];
		//if (init) {
			//Piece[][] pieces = new Piece[8][8];
			for (int c = 0; c < 8; c++) {
				for (int r = 0; r < 8; r++) {
					if (pos.getSquare(r, c) != 0) {
						//System.out.println("making piece");
						pieceArray[r][c] = new Piece(pos.getSquare(r, c), c, r);
						frame.add(pieceArray[r][c]);
						frame.revalidate();
						//pieceArray[r][c].repaint();
					}
				}
			}
			//init = false;
			//return pieces;

			//init = false;
		//}
		return pieceArray;
	}

	public void move(Move mov) {
		System.out.println();

		int rI = mov.getyInitial();
		int cI = mov.getxInitial();
		int rF = mov.getyFinal();
		int cF = mov.getxFinal();
		//Call vars directly from Move?
		pos.setSquare(rF, cF, pos.getSquare(rI, cI));
		pos.setSquare(rI, cI, (byte) 0);
		//refresh();

		squares[rI][cI].repaint();
		revalidate();
		squares[rF][cF].repaint();
		revalidate();
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			System.out.println("InterruptedException");
//		}
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {


	         }
	      });
		pieces[rI][cI].movePiece(rF, cF);
		pieces[rF][cF] = pieces[rI][cI];
		//System.out.println("Repaint piece about to be called");
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e){
//			System.out.println("InterruptedException");
//		}

		pieces[rI][cI] = null;
		paintPiece(rF, cF);
		revalidate();
		//pieces = setUp(pos);
		//System.out.println("finish set up");
		/* frame.remove(pieces[rI][cI]);

		if (pieces[rF][cF] != null) {
			frame.remove(pieces[rF][cF]);
		}
		pieces[rF][cF] = pieces[rI][cI].movePiece(cF, rF);
		frame.add(pieces[rF][cF]);
		pieces[rI][cI] = null;
		//frame.remove(pieces[rI][cI]);
		refresh();
		*/
		//refresh();

	}

	public void refresh() {
	      frame.revalidate();
	      frame.repaint();
   }

	public Piece[][] getPieces() {
		return pieces;
	}

	public JFrame getFrame() {
		return frame;
	}
	private void paintPiece(final int rF, final int cF) {
		  SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	pieces[rF][cF].repaint();
				revalidate();
		    }
		  });
	}
}
