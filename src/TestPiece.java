import java.awt.Graphics;

import javax.swing.JPanel;


public class TestPiece extends JPanel {
	public TestPiece() {
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.fillRect(200,  200,  200,  200);
	}
}
