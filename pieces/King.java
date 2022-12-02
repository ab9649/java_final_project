package pieces;

import java.util.*;
import java.util.Set;

import javax.swing.ImageIcon;

import square.Square;

public class King extends Piece{
    
    private boolean checked = false;
    private boolean canCastle = true;

    public King(int x, int y, String color){
        super(x,y,color,new ImageIcon("images/king_"+color+".png").getImage());
    }
    public void setChecked(boolean status){this.checked = status;}
    public boolean getChecked(){return this.checked;}

    @Override
    public boolean Move(Set<String> moveList,Square toSquare, Square fromSquare){
        Piece toPiece = toSquare.getPiece();
        //fix this (bad or)
        if (toPiece instanceof Rook && toPiece.getColor() == super.getColor() && (moveList.contains("KC") || moveList.contains("QC")) ){
            performCastle(toPiece);
        }
        else {
            if(super.Move(moveList, toSquare, fromSquare)){
                checked = false;
                canCastle = false;
                return true;
            }
            return false;
        }
    }
    private performCastle(Rook rook){
        
    }
    
    private boolean attemptCastle(Square[][] gameArray, Rook rook){
        if (rook.getX() == 0){
            for (int i = 1; i < 4; i++){
                if (gameArray[i][y].getPiece() != null || (i != 1 && gameArray[i][y].isCheckSpot())){
                    return false;}}}
        else{
            for (int i = 5; i < 7; i++){
                if (gameArray[i][y].getPiece() != null || gameArray[i][y].isCheckSpot()){
                    return false;}}}
        return true;
    }
    
    @Override
    public Set<String> possibleMoves(Square[][] gameArray) {
        int x = super.getX();
        int y = super.getY();
        Set<String> retList = new HashSet<String>();
        //castling
        if (this.canCastle && !this.checked){
            //queenside castle
            if (gameArray[0][y].getPiece() instanceof Rook && gameArray[0][y].getPiece().getCanCastle()){
                if (attempCastle(gameArray[0][y].getPiece())){
                    retList.add("QC");
                }
            }
            //kinside castle
            if (gameArray[7][y].getPiece() instanceof Rook && gameArray[7][y].getPiece().getCanCastle()){
                if (attemptCastle(gameArray[7][y].getPiece())){
                    retList.add("KC");
                }
            }
        }
        
        int[][] checkList = new int[][]{{x+1,y},{x-1,y},{x,y+1},{x,y-1},{x-1,y+1},{x+1,y+1},{x-1,y-1},{x+1,y-1}};
        for (int[] pair:checkList){
            if (pair[0] >= 0 && pair[0] <=7 && pair[1] >= 0 && pair[1] <=7){
                if (gameArray[pair[0]][pair[1]].getPiece() == null || !gameArray[pair[0]][pair[1]].getPiece().getColor().equals(super.getColor())){
                    //if (!gameArray[pair[0]][pair[1]].isCheckSpot(super.getColor(), gameArray)){
                        retList.add(pair[0]+","+pair[1]);
                    //}
                }
            }
        }
        return retList;
    }
    
    public boolean isChecked(Square[][] gameArray){
        int x = super.getX();
        int y = super.getY();
        //along axes
        for (int d = 0; d < 4; d++){
            x = super.getX();
            y = super.getY();
            while (x >= 0 && y >= 0 && x <= 7 && y <= 7 && (gameArray[x][y].getPiece() == null || gameArray[x][y].getPiece() == this)){
                switch(d){
                case 0:
                    x--;
                    break;
                case 1:
                    y--;
                    break;
                case 2:
                    x++;
                    break;
                case 3:
                    y++;
                    break;
                }
            }
            if(x >= 0 && x <= 7 && y >= 0 && y <= 7){
                if (gameArray[x][y].getPiece() != null && !gameArray[x][y].getPiece().getColor().equals(super.getColor()) && (gameArray[x][y].getPiece() instanceof Rook || gameArray[x][y].getPiece() instanceof Queen || gameArray[x][y].getPiece() instanceof King)){
                    return true;
                }
            }
        }
        //along diagonal
        int[][] pawnCheckSquares = {{x-1,y-1},{x-1,y+1},{x+1,y+1}, {x+1,y-1}};
        for (int[] pair : pawnCheckSquares){
            if (pair[0] >= 0 && pair[0] <=7 && pair[1] > 0 && pair[1] <=7){
                if (gameArray[pair[0]][pair[1]].getPiece() instanceof Pawn && !gameArray[pair[0]][pair[1]].getPiece().getColor().equals(super.getColor())){
                    return true;
                    }
                }
            }
        for (int d = 0; d < 4; d++){
            x = super.getX();
            y = super.getY();
            while (x >= 0 && y >= 0 && x <= 7 && y <= 7 && (gameArray[x][y].getPiece() == null || gameArray[x][y].getPiece() == this)){
                switch(d){
                case 0:
                    x--;
                    y--;
                    break;
                case 1:
                    y--;
                    x++;
                    break;
                case 2:
                    x++;
                    y++;
                    break;
                case 3:
                    y++;
                    x--;
                    break;
                }
            }
            if (x >= 0 && x <= 7 && y >= 0 && y <= 7){
                if (gameArray[x][y].getPiece() != null && !gameArray[x][y].getPiece().getColor().equals(super.getColor()) && (gameArray[x][y].getPiece() instanceof Bishop || gameArray[x][y].getPiece() instanceof Queen || gameArray[x][y].getPiece() instanceof King)){
                    return true;
                }
            }
        }
        //knight
        int[][] knightCheckSquares = new int[][]{{x+2,y+1},{x+2,y-1},{x-2,y-1},{x-2,y+1},{x-1,y+2},{x+1,y+2},{x-1,y-2},{x+1,y-2}};
        for (int[] pair: knightCheckSquares){
            if (pair[0] >= 0 && pair[0] <=7 && pair[1] > 0 && pair[1] <=7){
                if (gameArray[pair[0]][pair[1]].getPiece() instanceof Knight && !gameArray[pair[0]][pair[1]].getPiece().getColor().equals(super.getColor())){
                    return true;
                }
            }
        }
        
        return false;
    }

    
}
