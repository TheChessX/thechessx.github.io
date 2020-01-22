package chessLogic;

private boolean IsDrawPossible = false;
private int PiecesLeft;
private int BlackPawns;
private int WhitePawns;
private int BlackKnights;
private int WhiteKnights;
private int BlackBishops;
private int WhiteBishops;

public void NumberOfPieces() { //Call it after each move. 
	for (int c = 0; c < 8; c++) {
		for (int r = 0;r < 8; r++) {
			if (pos[c][r] > 0) {
				PiecesLeft++;
				
				if (pos[c][r] == 1) {
					WhitePawns++;
						
				} else if (pos[c][r] == 7) {
					BlackPawns++;
						
				}
					
				if (pos[c][r] == 2) {
					WhiteKnights++;
						
				} else if (pos[c][r] == 8) {
					BlackKnights++;
						
				}
					
				if (pos[c][r] == 3) {
					WhiteBishops++;
						
				} else if (pos[c][r] == 9) {
					BlackBishops++;
						
				}
					
			}
				
		}
			
	}
		
	if (PiecesLeft >= 2 && PiecesLeft <= 4 && WhitePawns == 0 && BlackPawns == 0) {
		if (PiecesLeft == 2) {
			DRAW();
				
		} else if (PiecesLeft == 3) {
			if (WhiteKnights > 0 || WhiteBishops > 0 || BlackKnights > 0 || BlackBishops > 0) {
				DRAW();
					
			} 
				
		} else if (PiecesLeft == 4) {
			if (WhiteKnights > 0) {
				//
					
			}
				
			if (WhiteBishops > 0) {
				//Check square color
					
			}
				
			if (BlackKnights > 0) {
				//
					
			}
				
			if (BlackBishops > 0) {
				//Check square color
					
			} 
				
		} 
			
	} else {
		IsDrawPossible = false;
			
	}	
		
	/* 
	return PiecesLeft;
	return BlackPawns;
	return WhitePawns;
	*/
		
	PiecesLeft = 0;
	BlackPawns = 0;
	WhitePawns = 0;
	WhiteKnights = 0;
	BlackKnights = 0;
	WhiteBishops = 0;
	BlackBishops = 0;
	
}
	
public void DRAW() {
	if (IsDrawPossible) {
		System.out.println("You can claim draw.");
		
	}	
	
}