public class EvaluationCenter () {
	//takes position of piece 

	//public int PawnV = 1;			/*///////////////////////*/
	//public int RookV = 5;		    /*	Values of pieces	 */
	//public int KnightV = 3.05;	/*						 */
	//public int BishopV = 3.45;	/*						 */
	//public int QueenV = 9;		/*						 */
	//public int KingV = 200;		/*///////////////////////*/

	public int PawnCOV = 0.1; //value for pawns in 2,2 to 2,5, 5,2 to 5,5
	public int PawnCIV = 0.2; //value for pawns in center four squares
	public int KnightCOV = 0.4; 
	public int KnightCIV = 0.5; 

	/* just in case if we would need to check for value of this figures in the center
	public int RookCOV = 0.5;
	public int RookCIV = 0.6;
	public int BishopCOV = 0.5;
	public int BishopCIV = 0.6;
	public int QueenCOV = 1;
	public int QueenCIV = 2;
	*/

	//private int value1 = 0;
	private int value2 = 0;

	public int evaluateBasedOnMaterial(final Position p) {
		p.position[][];

	}
	
	/*for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			if (p.position[i][j] == 7) {
				value1 = value1 - PawnV;

			} else if (p.position[i][j] == 1) {
				value1 = value1 + PawnV;

			} else if (p.position[i][j] == 10) {
				value1 = value1 - RookV;

			} else if (p.position[i][j] == 4) {
				value1 = value1 + RookV;

			} else if (p.position[i][j] == 8) {
				value1 = value1 - KnightV;

			} else if (p.position[i][j] == 2) {
				value1 = value1 + KnightV;

			} else if (p.position[i][j] == 9) {
				value1 = value1 - BishopV;

			} else if (p.position[i][j] == 3) {
				value1 = value1 + BishopV;

			} else if (p.position[i][j] == 11) {
				value1 = value1 - QueenV;

			} else if (p.position[i][j] == 5) {
				value1 = value1 + QueenV;

			} else if (p.position[i][j] == 12) {
				value1 = value1 - KingV;

			} else if (p.position[i][j] == 6) {
				value1 = value1 + KingV;

			}

		}

	}

	return value1;*/

	for (i = 2; i < 6; i++) {
		for (j = 2; j < 6; j++) {
			if (i == 3 && j == 3 || i == 3 && j == 4 || i == 4 && j == 3 || i == 4 && j == 4) {
				 if (p.position[i][j] == 1) {
				 	 value2 = value2 + PawnCIV;

				 } else if (p.position[i][j] == 7) {
				 	 value2 = value2 - PawnCIV;

				 } else if (p.position[i][j] == 2) {
				 	 value2 = value2 + KnightCIV;

				 } else if (p.position[i][j] == 8) {
				 	 value2 = value2 - KnightCIV;

				 }

			} else {
				if (p.position[i][j] == 1) {
				 	 value2 = value2 + PawnCOV;

				} else if (p.position[i][j] == 7) {
				 	 value2 = value2 - PawnCOV;

				} else if (p.position[i][j] == 2) {
				 	 value2 = value2 + KnightCOV;

				} else if (p.position[i][j] == 8) {
				 	 value2 = value2 - KnightCOV;

				}

			}

		}

	}

	return value2;

}