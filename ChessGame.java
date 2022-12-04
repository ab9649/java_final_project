import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import square.*;
import pieces.*;


public class ChessGame extends JFrame{

    private Square[][] gameArray;
    private King startTurn;
    private King whiteKing;
    private King blackKing;
    private final ArrayList<Piece> blackPieces = new ArrayList<Piece>();
    private final ArrayList<Piece> whitePieces = new ArrayList<Piece>();

    public ChessGame(){
        setSize(700, 700);
        JPanel board =  new ChessBoard();
        gameArray = ((ChessBoard)board).getChessBoardArray();
        whiteKing= (King) gameArray[4][7].getPiece();
        blackKing = (King) gameArray[4][0].getPiece();
        startTurn= whiteKing;

        for (int j = 0, i = 0, k = 0; i < 16; i ++, j++){
            if (i == 8){j = 0; k =1;}
            blackPieces.add(gameArray[j][k].getPiece());
        }
        for (int j = 0, i = 0, k = 7; i < 16; i ++, j++){
            if (i == 8){j = 0; k =6;}
            whitePieces.add(gameArray[j][k].getPiece());
        }

        MouseListener moveListener = new MouseListener(){

            Square fromSquare = null;
            Square toSquare = null;
            Set<String> possibleMoveLocations = null;
            boolean validPress = true;
            King currTurn = startTurn;

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
                    if (chosenSquare.getPiece() != null && chosenSquare.getPiece().getColor().equals(currTurn.getColor())){
                        fromSquare = chosenSquare;
                        //validate moves
                        System.out.println(""+fromSquare.getlocX() +" "+ fromSquare.getlocY() +" "+fromSquare.getPiece() + " "+fromSquare.getPiece().getX() +" "+fromSquare.getPiece().getY());
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
                        Piece fromPiece = fromSquare.getPiece();
                        Piece toPiece = toSquare.getPiece();
                        if (fromSquare.getPiece().Move(gameArray, possibleMoveLocations, toSquare, fromSquare)){
                            //move would place own king in check
                            if (currTurn.isChecked(gameArray)){
                                undo(fromPiece, toPiece, fromSquare, toSquare);
                            }
                            else {
                                //introduce new check
                                if (swapCurTurn().isChecked(gameArray)){
                                    if (isCheckmate(swapCurTurn())){
                                        gameOver(board, this);
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
            
        };
        
        
        board.addMouseListener(moveListener);
        add(board, BorderLayout.CENTER);
    }
    public boolean isCheckmate(King currTurn){
        ArrayList<Piece> pieceList;
        if (currTurn.getColor() == "white"){
            pieceList = whitePieces;
        }
        else{
            pieceList = blackPieces;
        }
        for (Piece piece:pieceList){
            Set<String> moveList = piece.possibleMoves(gameArray);
            for (String move : moveList){
                int x = Character.getNumericValue(move.charAt(0));
                int y = Character.getNumericValue(move.charAt(2));
                Piece toPiece = gameArray[x][y].getPiece();
                Square fromSquare = gameArray[piece.getX()][piece.getY()];
                Square toSquare = gameArray[x][y];
                if (piece.Move(gameArray, moveList, toSquare, fromSquare)){
                    if (!currTurn.isChecked(gameArray)){
                        undo(piece, toPiece, fromSquare, toSquare);
                        return false;
                    }
                    else{
                    undo(piece, toPiece, fromSquare, toSquare);
                    }
                }
            }
        }
        return true;
    }
    
    public void undo(Piece fromPiece, Piece toPiece, Square fromSquare, Square toSquare){
        fromSquare.setPiece(fromPiece);
        toSquare.setPiece(toPiece);
        fromPiece.undoTimesMoved();
        if (toPiece != null){
            toPiece.setCapture(false);
            toPiece.undoTimesMoved();
        }
        //preserve castle ability after test move
        if (fromPiece instanceof King){
            if (fromPiece.getTimesMoved() == 1){
                ((King)fromPiece).setCanCastle(true);
            }
        }
    }
    
    public void gameOver(JPanel board, MouseListener moveListener){
        System.out.println("CHECKMATE");
        JLabel overSign = new JLabel("GAME OVER");
        add(overSign,BorderLayout.CENTER);
        board.removeMouseListener(moveListener);
        
    }

    public static void main(String[] args){
        JFrame frame = new ChessGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
