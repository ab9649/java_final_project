package pieces;

import java.lang.reflect.Array;
import java.util.*;
import square.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Pawn extends Piece{

    public Pawn(int x, int y, String color){
        super(x,y,color,new ImageIcon("images/pawn_"+color+".png").getImage());
    }
    public void Promote(){
        //TODO
    }

     
    @Override
    public boolean Move(Set<String> moveList, Square toSquare, Square fromSquare) {
       if (super.Move(moveList, toSquare, fromSquare)){
            if (super.getY() == 0){
                this.Promote();
            }
            return true;
        }
        return false;
    }


    @Override
    public Set<String>possibleMoves(Square[][] gameArray) {
        int x = super.getX();
        int y = super.getY();
        Set<String> retList = new HashSet<String>();
        if (super.getColor().equals("white")){
            if (gameArray[x][y-1].getPiece() == null){
                retList.add(x+","+(y-1));
                //first move. checks for double
                if (super.getY() == 6){
                    if (gameArray[x][y-2].getPiece() == null){
                        retList.add(x+","+(y-2));
                    }
                }
            }
            //here
            if (super.getY() > 0){
                if (super.getX() > 0 && gameArray[x-1][y-1].getPiece() != null && !gameArray[x-1][y-1].getPiece().getColor().equals(super.getColor())){
                    retList.add((x-1)+","+(y-1));
                }
                if (super.getX() < 7 && gameArray[x+1][y-1].getPiece() != null && !gameArray[x+1][y-1].getPiece().getColor().equals(super.getColor())){
                    retList.add((x+1)+"," +(y-1));
                }
            }
        }
        else {
            if (gameArray[x][y+1].getPiece() == null){
                retList.add(x+","+(y+1));
                //first move. checks for double
                if (super.getY() == 1){
                    if (gameArray[x][y+2].getPiece() == null){
                        retList.add(x+","+(y+2));
                    }
                }
            }
            if (super.getY() < 7){
                if (super.getX() > 0 && gameArray[x-1][y+1].getPiece() != null && !gameArray[x-1][y+1].getPiece().getColor().equals(super.getColor())){
                    retList.add((x-1)+","+(y+1));
                }
                if (super.getX() < 7 && gameArray[x+1][y+1].getPiece() != null && !gameArray[x+1][y+1].getPiece().getColor().equals(super.getColor())){
                    retList.add((x+1)+","+(y+1));
                }
            }
        }
        System.out.println(retList);
        return retList;
        
    }
    
    
}
