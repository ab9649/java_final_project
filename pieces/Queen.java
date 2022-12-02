package pieces;

import java.util.*;
import java.util.Set;

import javax.swing.ImageIcon;

import square.Square;

public class Queen extends Piece{

    public Queen(int x, int y, String color){
        super(x,y,color,new ImageIcon("images/queen_"+color+".png").getImage());
    }

    @Override
    public Set<String> possibleMoves(Square[][] gameArray) {
        int x = super.getX();
        int y = super.getY();
        Set<String> retList = new HashSet<String>();
      
      
      //rook moves
        int checkX = x-1;
        int checkY = y;
      //check left
        while(checkX >=0 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX --;
        }
        if (checkX >=0 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
          retList.add(checkX + "," + checkY);
        }
        checkX = x+1;
      //check right
        while(checkX <=7 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX ++;
        }
        if (checkX <=7 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
        }
        checkX = x;
        checkY = y-1;
      //check up
        while(checkY >= 0 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkY --;
        }
        if (checkY >= 0 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
          retList.add(checkX + "," + checkY);
        }
        checkY = y+1;
      //check down
        while(checkY <= 7 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkY ++;
        }
        if (checkY <= 7 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
        }
        
      
      //bishop moves
        checkX = x-1;
        checkY = y-1;
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
        checkX = x+1;
        checkY = y-1;
        while(checkX <=7 && checkY >= 0 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX ++;
          checkY --;
        }
        if (checkX <=7 && checkY >= 0 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
          retList.add(checkX + "," + checkY);
        }
      //SW
        checkX = x-1;
        checkY = y+1;
        while(checkY <= 7 && checkX >= 0 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX --;
          checkY ++;
        }
        if (checkY <= 7 && checkX >= 0 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
        }
      //SE
        checkX = x+1;
        checkY = y+1;
        while(checkX <= 7 && checkY <= 7 && gameArray[checkX][checkY].getPiece() == null){
          retList.add(checkX + "," + checkY);
          checkX ++;
          checkY ++;
        }
        if (checkX <= 7 && checkY <= 7 && !gameArray[checkX][checkY].getPiece().getColor().equals(super.getColor())){
        retList.add(checkX + "," + checkY);
        }
      
        return retList;
    }    
}
