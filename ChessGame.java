import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import square.*;


public class ChessGame extends JFrame{

    private Square[][] gameArray;
    private King currTurn;
    private King whiteKing;
    private King blackKing;

    public ChessGame(){
        setSize(700, 700);
        JPanel board =  new ChessBoard();
        gameArray = ((ChessBoard)board).getChessBoardArray();
        whiteKing= gameArray[3][7].getPiece();
        blackKing = gameArray[3][0].getPiece();
        currTurn = whiteKing;

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
                    if (currTurn.getChecked() == false || chosenSquare.getPiece() == currTurn){    //check if king needs to move
                        if (chosenSquare.getPiece() != null && chosenSquare.getPiece().getColor().equals(currTurn.getColor())){
                            fromSquare = chosenSquare;
                            //validate moves
                            System.out.println(""+fromSquare.getlocX() +" "+ fromSquare.getlocY() +" "+fromSquare.getPiece());
                            possibleMoveLocations = fromSquare.getPiece().possibleMoves(gameArray);
                            validPress = true;
                            return;
                        }
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
                        Piece fromPiece = fromSquare.getPiece();
                        Piece toPiece = toSquare.getPiece();
                        if (fromSquare.getPiece().Move(possibleMoveLocations, toSquare, fromSquare)){
                            //move would place own king in check
                            if (currTurn.isChecked()){
                                undo(fromPiece, toPiece);
                            }
                            else {
                                if (swapCurTurn().isChecked()){
                                    //TODO
                                    swapCurTurn.setChecked() = true;
                                    if (swapCurTurn().possibleMoves() == {}){
                                        gameOver();
                                    }
                                }
                                currTurn = swapCurTurn();
                            }
                        }
                    }
                }
                fromSquare = null;
                toSquare = null;
                possibleMoveLocations = null;
            }

            private King swapCurTurn() {
                if (currTurn == whiteKing){
                    return blackKing;
                }
                return whiteKing;
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
    
    public void undo(Piece fromPiece, Piece toPiece){
        fromSquare.setPiece(fromPiece);
        toSquare.setPiece(toPiece);
        if (toPiece != null){
            toPiece.setCapture(false);
            toPiece.setX(toSquare.getlocX());
            toPiece.setY(toSquare.getlocY());
        }
    }

    public static void main(String[] args){
        JFrame frame = new ChessGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
