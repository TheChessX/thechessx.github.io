import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Piece extends JPanel {
	private byte id;
	private int c;
	private int r;
	private BufferedImage img;
	//private int a;
	
	public Piece(byte id, int c, int r) {
		this.id = id;
		this.c = c;
		this.r = r;
	    img = null;
	    try {
	    	img = ImageIO.read(new File("./src/img/" + id + ".png"));
	    } catch (IOException e) {
	    	System.out.println("image not found when piece was being initialized");
	    }
	}
	
	@Override
	public void paintComponent(Graphics g) {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			System.out.println("InterruptedException");
//		}
		g.drawImage(img, 51*(c+1) + 1, 51*(r+1) + 1, null);
		System.out.println("Painted piece");
	}
	
	public Piece movePiece(int r, int c) {
		this.c = c;
		this.r = r;
		//this.repaint();
		revalidate();
		return this;
	}
	
	public String toString() {
		return "id: " + id + " (" + r + ", " + c + ")";
	}
	
	public void promote(byte promotionID) {
		this.id = promotionID;
		try {
	    	img = ImageIO.read(new File("./src/img/" + id + ".png"));
	    } catch (IOException e) {
	    	System.out.println("image not found when piece was being initialized");
	    }
	}
	
//	public void changeA(int a) {
//		this.a = a;
//	}
}
