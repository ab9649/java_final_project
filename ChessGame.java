import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import square.*;


public class ChessGame extends JFrame{

    private String currTurn = "white";
    private Square[][] gameArray;

    public ChessGame(){
        setSize(700, 700);
        JPanel board =  new ChessBoard();
        gameArray = ((ChessBoard)board).getChessBoardArray();

        board.addMouseListener(new MouseListener(){

            Square fromSquare = null;
            Square toSquare = null;
            Set<String> possibleMoveLocations = null;
            boolean validPress = true;
            

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                JComponent comp = (JComponent) e.getSource();
                if (comp.getComponentAt(e.getPoint()) instanceof Square){
                    Square chosenSquare = (Square) comp.getComponentAt(e.getPoint());
                    if (chosenSquare.getPiece() != null && chosenSquare.getPiece().getColor().equals(currTurn)){
                        fromSquare = chosenSquare;
                        //validate moves
                        System.out.println(""+fromSquare.getlocX() +" "+ fromSquare.getlocY() +" "+fromSquare.getPiece());
                        possibleMoveLocations = fromSquare.getPiece().possibleMoves(gameArray);
                        validPress = true;
                        return;
                    }
                }
                validPress = false;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                if (validPress){
                    JComponent comp = (JComponent) e.getSource();
                    if (comp.getComponentAt(e.getPoint()) instanceof Square){
                        toSquare = (Square) comp.getComponentAt(e.getPoint());
                        if (fromSquare.getPiece().Move(possibleMoveLocations, toSquare, fromSquare)){
                            System.out.println("true");
                            swapCurTurn();
                        }
                    }
                }
                fromSquare = null;
                toSquare = null;
                possibleMoveLocations = null;
            }

            private void swapCurTurn() {
                if (currTurn.equals("white")){
                    currTurn = "black";
                }
                else{
                    currTurn = "white";
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
        add(board, BorderLayout.CENTER);
    }



    public static void main(String[] args){
        JFrame frame = new ChessGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
