package pieces;

import java.util.*;
import java.util.Set;

import javax.swing.ImageIcon;

import square.Square;

public class Knight extends Piece{

    public Knight(int x, int y, String color){
        super(x,y,color,new ImageIcon("images/knight_"+color+".png").getImage());
    }

    @Override
    public Set<String> possibleMoves(Square[][] gameArray) {
        int x = super.getX();
        int y = super.getY();
        Set<String> retList = new HashSet<String>();
        int[][] checkList = new int[][]{{x+2,y+1},{x+2,y-1},{x-2,y-1},{x-2,y+1},{x-1,y+2},{x+1,y+2},{x-1,y-2},{x+1,y-2}};
        for (int[] pair:checkList){
            if (pair[0] >= 0 && pair[0] <=7 && pair[1] >= 0 && pair[1] <=7){
                if (gameArray[pair[0]][pair[1]].getPiece() == null || gameArray[pair[0]][pair[1]].getPiece().getColor() != super.getColor())
                retList.add(pair[0]+","+pair[1]);
            }
        }
        return retList;
    }

    
}
