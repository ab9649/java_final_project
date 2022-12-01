package pieces;

import java.util.*;
import java.util.Set;

import javax.swing.ImageIcon;

import square.Square;

public class Bishop extends Piece{

    public Bishop(int x, int y, String color){
        super(x,y,color,new ImageIcon("images/rook_"+color+".png").getImage());
    }

    @Override
    public Set<String> possibleMoves(Square[][] gameArray) {
        int x = super.getX();
        int y = super.getY();
        Set<String> retList = new HashSet<String>();
        
        checkX = x-1;
        checkY = y;
      //check NW
        while(checkX >=0 && checkY >= 0 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX --;
          checkY --;
        }
      if (checkX >=0 && checkY >= 0 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
      }
      //check NE
      while(checkX <=7 && checkY >= 0 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX ++;
          checkY --;
        }
      if (checkX <=7 && checkY >= 0 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
      }
      //SW
      while(checkY <= 7 && checkX >= 0 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX --;
          checkY ++;
        }
      if (checkY >= 0 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
      }
      //SE
      while(check X <= 7 && checkY <= 7 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX ++:
          checkY ++;
        }
      if (checkX <= 7 && checkY <= 7 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
      }
      
        return retList;
    }

    
}
