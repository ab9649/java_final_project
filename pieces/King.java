package pieces;

import java.util.*;
import java.util.Set;

import javax.swing.ImageIcon;

import square.Square;

public class King extends Piece{

    public King(int x, int y, String color){
        super(x,y,color,new ImageIcon("images/king_"+color+".png").getImage());
    }

    @Override
    public Set<String> possibleMoves(Square[][] gameArray) {
        int x = super.getX();
        int y = super.getY();
        Set<String> retList = new HashSet<String>();
        int[][] checkList = new int[][]{{x+1,y},{x-1,y},{x,y+1},{x,y-1},{x-1,y+1},{x+1,y+1},{x-1,y-1},{x+1,y-1}};
        for (int[] pair:checkList){
            if (pair[0] >= 0 && pair[0] <=7 && pair[1] > 0 && pair[1] <=7){
                if (gameArray[pair[0]][pair[1]].getPiece() == null || gameArray[pair[0]][pair[1]].getPiece().getColor() != super.getColor())
                retList.add(pair[0]+","+pair[1]);
            }
        }
        return retList;
    }

    
}
