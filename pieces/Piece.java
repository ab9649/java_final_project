package pieces;
import java.awt.Image;
public abstract class Piece{
    private int x;
    private int y;
    private boolean captured = false;
    private String color;
    private Image img;
    

    public Piece(int x, int y, String color, Image img){
        this.x = x;
        this.y = y;
        this.color = color;
        this.img = img;
    }
    public abstract void Move(int x, int y);
    public void Take(Piece piece){
        this.x = piece.getX();
        this.y = piece.getY();
        piece.capture();
    }
    public void capture(){
        this.captured = true;
    }
    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public boolean isCaptured(){return this.captured;}
    public Image getImage(){return this.img;}
}
