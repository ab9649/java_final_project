package square;
import pieces.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pieces.Piece;

public class Square extends JPanel {
    private int locX;
    private int locY;
	private Piece currPiece;
	
	//public Square(String img) {
		//this(new ImageIcon(img).getImage());
	//}

    public Square(int x, int y){
        this.locX = x;
        this.locY = y;
        this.currPiece = null;
    }
    public Square(Piece piece){
        this.locX = piece.getX();
        this.locY = piece.getY();
        this.currPiece = piece;
    }
    public void setPiece(Piece piece){
        currPiece = piece;
	    if (piece != null){
	        piece.setX(locX);
	        piece.setY(locY);
	    }
        repaint();
    }
    public void setPieceNoRepaint(Piece piece){
        currPiece = piece;
    }
    public Piece getPiece(){return this.currPiece;}
    public int getlocX(){return this.locX;}
    public int getlocY(){return this.locY;}


	//public Square(Image img) {
		//this.img = img;
        //Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        //setPreferredSize(size);
        //setLayout(null);
	//}
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.currPiece != null){
            Image img = currPiece.getImage();
            g.drawImage(img, (this.getWidth() - img.getWidth(null)) / 2, (this.getHeight() - img.getHeight(null)) / 2, null);
        }
    }
    //public void setImage(String newimg){
        //img = new ImageIcon(newimg).getImage();
        //repaint();
    //}
	public boolean isCheckSpot(String color, Square[][]gameArray){
	    String currTurnColor = color;
        int x = locX;
        int y = locY;
        //along axes
        for (int d = 0; d < 4; d++){
            x = locX;
            y = locY;
            while (x >= 0 && y >= 0 && x <= 7 && y <= 7 && gameArray[x][y].getPiece() == null){
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
            if (x >= 0 && x <= 7 && y >= 0 && y <= 7){
                if (gameArray[x][y].getPiece() != null && !gameArray[x][y].getPiece().getColor().equals(currTurnColor) && (gameArray[x][y].getPiece() instanceof Rook || gameArray[x][y].getPiece() instanceof Queen || gameArray[x][y].getPiece() instanceof King)){
                    return true;
                }
            }
        }
        //along diagonal
        int[][] pawnCheckSquares = {{x-1,y-1},{x-1,y+1},{x+1,y+1}, {x+1,y-1}};
        for (int[] pair : pawnCheckSquares){
            if (pair[0] >= 0 && pair[0] <=7 && pair[1] > 0 && pair[1] <=7){
                if (gameArray[pair[0]][pair[1]].getPiece() instanceof Pawn && !gameArray[pair[0]][pair[1]].getPiece().getColor().equals(currTurnColor)){
                    return true;
                    }
                }
            }
        for (int d = 0; d < 4; d++){
            x = locX;
            y = locY;
            while (x > 0 && y >= 0 && x <= 7 && y < 7 && gameArray[x][y].getPiece() == null){
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
            if (x>= 0 && x <= 7 && y <= 7 && y >= 0){
                if (gameArray[x][y].getPiece() != null && !gameArray[x][y].getPiece().getColor().equals(currTurnColor) && (gameArray[x][y].getPiece() instanceof Bishop || gameArray[x][y].getPiece() instanceof Queen || gameArray[x][y].getPiece() instanceof King)){
                    return true;
                }
            }

        }
        //knight
        x = locX;
        y = locY;
        int[][] knightCheckSquares = new int[][]{{x+2,y+1},{x+2,y-1},{x-2,y-1},{x-2,y+1},{x-1,y+2},{x+1,y+2},{x-1,y-2},{x+1,y-2}};
        for (int[] pair:knightCheckSquares){
            if (pair[0] >= 0 && pair[0] <=7 && pair[1] > 0 && pair[1] <=7){
                if (gameArray[pair[0]][pair[1]].getPiece() instanceof Knight && !gameArray[pair[0]][pair[1]].getPiece().getColor().equals(currTurnColor)){
                    return true;
                }
            }
        }
        
        return false;
    }

}
