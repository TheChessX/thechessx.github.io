import javax.swing.JOptionPane;

import data.Position;
import data.Move;

public class Control {
	private Position pos;
	private Board board;
	private MyMouseListener mouse;
	private int mouseRowI = -1; //-1 indicates empty selection
	private int mouseColI = -1;
	private int mouseRowF = -1;
	private int mouseColF = -1;
	private Engine engine;
  
	public Control() {
		this.pos = new Position();
		this.board = new Board(pos);
		this.mouse = new MyMouseListener(this);
		board.getFrame().addMouseListener(mouse);
		this.engine = new Engine();
		//Initialize vars here
	}

	public void squareClicked(int r, int c) {
		//Resets user move when the same square is clicked twice
		if (r == mouseRowI && c == mouseColI) {
			mouseRowI = -1;
			mouseColI = -1;
			//System.out.println("HELLO");
		} else if (mouseRowI == -1) {
			mouseRowI = r;
			mouseColI = c;
			//System.out.println("HELLO2");
		} else {
			//System.out.println("HELLO3");
			mouseRowF = r;
			mouseColF = c;
			Move move = new Move(mouseRowI, mouseColI, mouseRowF, mouseColF);
			if (board.getPosition().isLegalMove(move) && (mouseRowF == 0 && (board.getPosition().getSquare(mouseRowI, mouseColI) == 1) || (mouseRowF == 7 && board.getPosition().getSquare(mouseRowI, mouseColI) == 7))) {
				Object[] options = {"Knight",
				                    "Bishop",
				                    "Rook",
				                    "Queen"};
				int n = JOptionPane.showOptionDialog(board.getFrame(),
				    "Promote to",
				    "Promotion",
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[3]);
				n += 2;
				if (board.getPosition().isBlackToMove()) {
					n += 6;
				}
				if (n == 1 || n == 7) { //User closes promotion dialog box
					mouseRowI = -1; //Treats as illegal move
				}
				move = new Move(mouseRowI, mouseColI, mouseRowF, mouseColF, (byte) n);
			}
			//System.out.println(move);
			//System.out.println("HELLO4");
			if (board.getPosition().isLegalMove(move)) {
				board.move(move);
			} else {
				System.out.println("Illegal move");
//				ArrayList<Move> legalMoves = board.getPosition().getAllLegalMoves();
//				for (Move mov: legalMoves) {
//					System.out.println(mov);
//				}
			}
			//board.move(move);
			mouseRowI = -1;
			mouseColI = -1;
			mouseRowF = -1;
			mouseColF = -1;
		}
	}
	
	public void engineMove() {
//		double kingSafetyScore = evaluation.evaluateKingSafety(board.getPosition());
//		System.out.println("King Safety: " + kingSafetyScore);
//		
//		double pieceValueScore = evaluation.evaluatePieceValue(board.getPosition());
//		System.out.println("Piece Value: " + pieceValueScore);
//		
//		double centerControlScore = evaluation.evaluateCenterControl(board.getPosition());
//		System.out.println("Center Control: " + centerControlScore);
		Move mov = engine.play(board.getPosition());
		board.move(mov);
		//System.out.println("Engine move: " + mov);
	}
}
