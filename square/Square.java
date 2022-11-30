package square;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pieces.Piece;

public class Square extends JPanel {
    private int locX;
    private int locY;
	private Piece currPiece;
	
	//public Square(String img) {
		//this(new ImageIcon(img).getImage());
	//}

    public Square(int x, int y){
        this.locX = x;
        this.locY = y;
        this.currPiece = null;
    }
    public Square(Piece piece){
        this.locX = piece.getX();
        this.locY = piece.getY();
        this.currPiece = piece;
    }
    public void setPiece(Piece piece){
        currPiece = piece;
        repaint();
    }
    public Piece getPiece(){return this.currPiece;}
    public int getlocX(){return this.locX;}
    public int getlocY(){return this.locY;}


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