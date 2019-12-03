import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyMouseListener extends MouseAdapter {
   private Control control;
	
   public MyMouseListener(Control control) {
	  //System.out.println("Mouse created");
	  this.control = control;
   }
   
   //Called when the mouse is clicked.
   @Override
   public void mouseClicked(MouseEvent e) {
      //System.out.println("Mouse clicked");
	  int r = (e.getY() - 31)/51 - 1;
	  int c = (e.getX() - 8)/51 - 1;
	  if (r >= 0 && r <= 7 && c >= 0 && c <= 7) {
		  control.squareClicked(r, c);
	  } else {
		  control.engineMove();
	  }
   }
}
