import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


public class Square extends JPanel {
	private int r;
	private int c;
	private Color color;
	
	public Square(int r, int c) {
		this.r = r;
		this.c = c;
		if ((r+c)%2 == 0) {
			color = new Color(255, 255, 255);
		} else {
			color = new Color(150, 0, 200); //purple
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//System.out.println("got here");
		g.setColor(color);
		g.fillRect(51*(c+1) + 1, 51*(r+1) + 1, 50, 50);
		//System.out.println("Painted square" + "(" + r + ", " + c + ")");
	}
}
