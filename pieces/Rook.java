package pieces;

import java.util.*;
import java.util.Set;

import javax.swing.ImageIcon;

import square.Square;

public class Rook extends Piece{

    public Rook(int x, int y, String color){
        super(x,y,color,new ImageIcon("images/rook_"+color+".png").getImage());
    }

    @Override
    public Set<String> possibleMoves(Square[][] gameArray) {
        int x = super.getX();
        int y = super.getY();
        Set<String> retList = new HashSet<String>();
        
        checkX = x-1;
        checkY = y;
      //check left
        while(checkX >=0 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX --;
        }
      if (checkX >=0 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
      }
      //check right
      while(checkX <=7 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX ++;
        }
      if (checkX <=7 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
      }
      //check up
      while(checkY >= 0 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkY --;
        }
      if (checkY >= 0 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
      }
      //check down
      while(checkY <= 7 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkY ++;
        }
      if (checkY <= 7 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
      }
      
        return retList;
    }

    
}
