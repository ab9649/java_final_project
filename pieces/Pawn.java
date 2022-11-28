package pieces;

import javax.swing.ImageIcon;

public class Pawn extends Piece{

    public Pawn(int x, int y, String color){super(x,y,color,new ImageIcon("pawn_"+color+".png").getImage());}
    @Override
    public void Move(int x, int y) {
        
    }
    
}
