import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
		//super.paintComponent(g);
		//if (init) {
			for (int i = 0; i < 9; i++) {
				g.drawLine(51, 51*(i+1), 459, 51*(i+1));
				g.drawLine(51*(i+1), 51, 51*(i+1), 459);
			}
			for (int c = 0; c < 8; c++) {
				g.drawString("" + (char) (c + 97), 51*(c+1) + 25, 40);
				g.drawString("" + (char) (c + 97), 51*(c+1) + 25, 480);
				g.drawString("" + (9 - (c + 1)), 35, 51*(c+1) + 30);
				g.drawString("" + (9 - (c + 1)), 470, 51*(c+1) + 30);
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
		
		int rI = mov.getxInitial();
		int cI = mov.getyInitial();
		int rF = mov.getxFinal();
		int cF = mov.getyFinal();
		
		System.out.println("rI: " + rI + "cI: " + cI + "rF: " + rF + "cF: " + cF);
		//Call vars directly from Move?
		
		if ((pos.getSquare(rI, cI) == 1 && rI == 6 && rF == 4)||(pos.getSquare(rI, cI) == 7 && rI == 1 && rF == 3)) {
			pos.setEnPassantColumn(cF);
		} else {
			pos.setEnPassantColumn(-1);
		}
		
		if (mov.getPromotionID() != 0) {
			pos.setSquare(rF, cF, mov.getPromotionID());
		} else {
			if ((pos.getSquare(rI, cI) == 1 && rI == 3 && rF == 2 && cI != cF && pos.getSquare(rF, cF) == 0)||(pos.getSquare(rI, cI) == 7 && rI == 4 && rF == 5 && cI != cF && pos.getSquare(rF, cF) == 0)) {
				pos.setSquare(rI, cF, (byte) 0);
				squares[rI][cF].repaint();
				revalidate();
			}
			pos.setSquare(rF, cF, pos.getSquare(rI, cI));
		}
		pos.setSquare(rI, cI, (byte) 0);
		
		//TODO detect en passant move and remove captured pawn
		pos.setBlackToMove(!pos.isBlackToMove());
		System.out.println("Black to move now " + pos.isBlackToMove());
		//refresh();

		squares[rI][cI].repaint();
		revalidate();
		squares[rF][cF].repaint();
		revalidate();
		//TODO also repaint the square for the captured en passant pawn
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
		if (mov.getPromotionID() != 0) {
			pieces[rF][cF].promote(mov.getPromotionID());
		}
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
		if (pos.getAllLegalMoves().size() == 0) {
			boolean checkmate = false;
			pos.setBlackToMove(!pos.isBlackToMove());
			int[] kingLocation = pos.findKing(pos);
    		int kingR = kingLocation[0];
    		int kingC = kingLocation[1];
    		ArrayList<Move> potentialLegalMoves = pos.getAllLegalMovesNoCheck();
    		for (Move potentialNextMove: potentialLegalMoves) {
    			if (potentialNextMove.getxFinal() == kingR && potentialNextMove.getyFinal() == kingC) {
    				checkmate = true;
    				break;
    			}
    		}
			if (checkmate) {
	    		String win;
				if (pos.isBlackToMove()) {
					win = "Black";
				} else {
					win = "White";
				}
				JOptionPane.showMessageDialog(frame, "Checkmate! " + win + " wins.");
			} else {
				JOptionPane.showMessageDialog(frame, "Stalemate! It's a draw.");
			}
		}
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
	public Position getPosition() {
		return pos;
	}
}
