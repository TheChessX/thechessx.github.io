
public class Control {
	private Position pos;
	private Board board;
	private MyMouseListener mouse;
	private int mouseRowI = -1; //-1 indicates empty selection
	private int mouseColI = -1;
	private int mouseRowF = -1;
	private int mouseColF = -1;
	private Evaluation evaluation;
	
	public Control() {
		this.pos = new Position();
		this.board = new Board(pos);
		this.mouse = new MyMouseListener(this);
		board.getFrame().addMouseListener(mouse);
		this.evaluation = new Evaluation();
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
			//System.out.println(move);
			//System.out.println("HELLO4");
			board.move(move);
			mouseRowI = -1;
			mouseColI = -1;
			mouseRowF = -1;
			mouseColF = -1;
		}
	}
	
	public void evaluate() {
		double kingSafetyScore = evaluation.evaluateKingSafety(board.getPosition());
		System.out.println("King Safety (white): " + kingSafetyScore);
		
		double pieceValueScore = evaluation.evaluatePieceValue(board.getPosition());
		System.out.println("Piece Value: " + pieceValueScore);
	}
}
