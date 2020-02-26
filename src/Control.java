import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;

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
	private Evaluation eval;
  
	public Control() {
		this.pos = new Position();
		this.board = new Board(pos);
		this.mouse = new MyMouseListener(this);
		board.getFrame().addMouseListener(mouse);
		this.engine = new Engine();
		this.eval = new Evaluation();
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
				String lastMove = board.getPosition().toHumanNotation(move);
				//System.out.println(lastMove);
				board.move(move);
				displayEvaluation(board.getPosition());
//				String lastMove = move.toRawString();
//				if (lastMove.charAt(4) == '0') {
//					lastMove = lastMove.substring(0, 4);
//				}
				if (engine.isTheory()) {
					boolean moveFound = false;
					int totalRows = engine.getWb().getSheetAt(1).getPhysicalNumberOfRows();
					if (engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()).toString().equals(lastMove)) {
						engine.setWbCol(engine.getWbCol() + 1);
						moveFound = true;
					} else {
						engine.setWbRow(engine.getWbRow() + 1);
						while (engine.getWbRow() < totalRows && !(engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()) == null) && (engine.getWbCol() == 0 || engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol() - 1).toString().equals("-"))) {
							if (engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()).toString().equals(lastMove)) {
								engine.setWbCol(engine.getWbCol() + 1);
								moveFound = true;
								break;
							}
							engine.setWbRow(engine.getWbRow() + 1);
						}
					}
					
					//System.out.println(engine.getWbRow() + " " + engine.getWbCol());
					
					if (!moveFound) {
						engine.setTheory(false);
					} else if (engine.getWb().getSheetAt(1).getRow(engine.getWbRow()).getCell(engine.getWbCol()).toString().equals("-")) {
						engine.setTheory(false);
					}
				}
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
		displayEvaluation(board.getPosition());
		//System.out.println("Engine move: " + mov);
	}
	
	public void displayEvaluation(Position pos) {
		if (engine.isTheory()) {
			System.out.println("This is a book move.");
		} else {
			System.out.println("Net position evaluation: " + eval.evaluate(pos));
			if (eval.isEndgame()) {
				System.out.println("This is an endgame position.");
				System.out.println("Material: " + eval.evaluatePieceValue(pos));
				System.out.println("King Activity: " + eval.evaluateKingActivity(pos));
				System.out.println("Rooks: " + eval.evaluateRooks(pos));
				System.out.println("Pawns: " + eval.evaluatePawnsEndgame(pos));
			} else {
				System.out.println("This is a middlegame position.");
				System.out.println("Material: " + eval.evaluatePieceValue(pos));
				System.out.println("Center Control: " + eval.evaluateCenterControl(pos));
				System.out.println("King Safety: " + eval.evaluateKingSafety(pos));
				System.out.println("Development: " + eval.evaluateDevelopment(pos));
				System.out.println("Rooks: " + eval.evaluateRooks(pos));
				System.out.println("Pawns: " + eval.evaluatePawns(pos));
				System.out.println("Piece-Square Table: " + eval.evaluatePieceSquareTable(pos));
				
			}
		}
	}
}
