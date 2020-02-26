package chessLogic;

private boolean IsDrawPossible = false;
private int PiecesLeft;
private int BlackPawns;
private int WhitePawns;
private int BlackKnights;
private int WhiteKnights;
private int BlackBishops;
private int WhiteBishops;
private int Repeated;
int MoveArray[][]; //[saved position] [piece value] 
/*
{00, 01, 02, 03, 04, 05, 06, 07}
{08, 09, 10, 11, 12, 13, 14, 15}
{16, 17, 18, 19, 20, 21, 22, 23}
{24, 25, 26, 27, 28, 29, 30, 31}
{32, 33, 34, 35, 36, 37, 38, 39}
{40, 41, 42, 43, 44, 45, 46, 47}
{48, 49, 50, 51, 52, 53, 54, 55}
{56, 57, 58, 59, 60, 61, 62, 63}
*/
int MoveArrayCopy[][];
int NumberofMoves = 0;
MoveArray[][] = new int[NumberofMoves][64];

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

public void SaveTheBoard() {
	NumberofMoves++;
	MoveArrayCopy = MoveArray;
	int v; //c
	int t; //r
	MoveArray[][] = new int[NumberofMoves][64];
	
	for (int i = 0; i < 64; i++) {
		if (i == 0) {
			v = 0;
			t = 0;
			
		}
		
		if (i == 8) {
			v = 1;
			t = 0;
			
		} else if (i == 16) {
			v = 2;
			t = 0;
			
		} else if (i == 24) {
			v = 3;
			t = 0;
			
		} else if (i == 32) {
			v = 4;
			t = 0;
			
		} else if (i == 40) {
			v = 5;
			t = 0;
			
		} else if (i == 48) {
			v = 6;
			t = 0;
			
		} else if (i == 56) {
			v = 7;
			t = 0;
			
		} else if (i != 0) {
			t++;
			
		}
		
		MoveArray[NumberofMoves-1][i] = pos[v][t];
		
	}
	
	//After first move
	//MoveArray[0] - no data
	//MoveArray[1 (NumberofMoves-1)] - saved board
	
	if (NumberofMoves > 1) {
		for (int p = 0; p == NumberofMoves-1; p++) {
			MoveArray[p] = MoveArrayCopy[p];
			
		}
		
		MoveArrayCopy = null;
		
	}
	
}

public void ThreeFoldRep() { 
	int sim = 0;
	if (NumberofMoves > 1) {	
		for (int y = 0; y < NumberofMoves; y++) {
			for (int u = 0; u < NumberofMoves; u++) {
				for (int g = 0; g < 64; g++) {
					if (MoveArray[y][g] === MoveArray[u][g]) {
						sim++;
					
					} else {
						sim = sim;
					
					}
				
					if (sim == 64) {
						Repeated++
					
					}
					
					if (g == 63) {
						sim = 0;
						
					}
					
				}	
			
			}
			
			if (y == NumberofMoves-1 && Repeated != 3) {
				Repeated == 0;
				
			} else if (Repeated === 3) {
				IsDrawPossible = true;
					
			}
			
			
		}
		
	}
	
}

public void MoveRule() {
	
	
}
	
public void DRAW() {
	if (IsDrawPossible) {
		System.out.println("You can claim draw.");
		
	}	
	
}