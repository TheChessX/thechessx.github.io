public class Evaluation () {
	//takes position of piece 

	public int PawnV = 1;		/*///////////////////////*/
	public int RookV = 5;		/*	Values of pieces	 */
	public int KnightV = 3.05;	/*						 */
	public int BishopV = 3.45;	/*						 */
	public int QueenV = 9;		/*						 */
	public int KingV = 200;		/*///////////////////////*/

	private int value = 0;

	public int evaluateBasedOnMaterial(final Position p) {
		p.position[][];

	}
	
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			if (p.position[i][j] == 7) {
				value = value - PawnV;

			} else if (p.position[i][j] == 1) {
				value = value + PawnV;

			} else if (p.position[i][j] == 10) {
				value = value - RookV;

			} else if (p.position[i][j] == 4) {
				value = value + RookV;

			} else if (p.position[i][j] == 8) {
				value = value - KnightV;

			} else if (p.position[i][j] == 2) {
				value = value + KnightV;

			} else if (p.position[i][j] == 9) {
				value = value - BishopV;

			} else if (p.position[i][j] == 3) {
				value = value + BishopV;

			} else if (p.position[i][j] == 11) {
				value = value - QueenV;

			} else if (p.position[i][j] == 5) {
				value = value + QueenV;

			} else if (p.position[i][j] == 12) {
				value = value - KingV;

			} else if (p.position[i][j] == 6) {
				value = value + KingV;

			}

		}

	}

	return value;

}
