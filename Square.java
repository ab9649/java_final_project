
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pieces.Piece;

public class Square extends JPanel {
	private Piece currPiece;
	
	//public Square(String img) {
		//this(new ImageIcon(img).getImage());
	//}


    public Square(){
        this.currPiece = null;
    }
    public Square(Piece piece){
        this.currPiece = piece;
    }
    public void setPiece(Piece piece){
        currPiece = piece;
        repaint();
    }


	//public Square(Image img) {
		//this.img = img;
        //Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        //setPreferredSize(size);
        //setLayout(null);
	//}
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.currPiece != null){
            Image img = currPiece.getImage();
            g.drawImage(img, (this.getWidth() - img.getWidth(null)) / 2, (this.getHeight() - img.getHeight(null)) / 2, null);
        }
    }
    //public void setImage(String newimg){
        //img = new ImageIcon(newimg).getImage();
        //repaint();
    //}

}