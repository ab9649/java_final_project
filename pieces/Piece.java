package pieces;
import java.awt.Image;
import java.util.*;
import square.*;


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
    public abstract Set<String> possibleMoves(Square[][] gameArray);
    public boolean isvalidSpot(Square[][] gameArray, int checkX, int checkY){
        if (gameArray[x][y].getPiece() == null){return true;}
        else {
            if (!gameArray[x][y].getPiece().getColor().equals(this.color)){return true;}
        }
        return false;
    }




    public abstract boolean Move(Set<String> moveList,Square toSquare, Square fromSquare);


    

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
    public String getColor(){return this.color;}
    public boolean isCaptured(){return this.captured;}
    public Image getImage(){return this.img;}
}
