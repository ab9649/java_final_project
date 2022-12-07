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
                        possibleMoveLocations = fromSquare.getPiece().possibleMoves(gameArray);
                        //highlight squares
                        for (String pair:possibleMoveLocations){
                            if (!(pair.equals("KC") || pair.equals("QC"))){
                                int x = Character.getNumericValue(pair.charAt(0));
                                int y = Character.getNumericValue(pair.charAt(2));
                                gameArray[x][y].highlightSquare(true);
                            }
                        }
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
                    for (String pair:possibleMoveLocations){
                        if (!(pair.equals("KC") || pair.equals("QC"))){
                            int x = Character.getNumericValue(pair.charAt(0));
                            int y = Character.getNumericValue(pair.charAt(2));
                            gameArray[x][y].highlightSquare(false);
                        }
                    }
                    JComponent comp = (JComponent) e.getSource();
                    if (comp.getComponentAt(e.getPoint()) instanceof Square){
                        toSquare = (Square) comp.getComponentAt(e.getPoint());
                        Piece fromPiece = fromSquare.getPiece();
                        Piece toPiece = toSquare.getPiece();
                        if (fromSquare.getPiece().Move(gameArray, possibleMoveLocations, toSquare, fromSquare)){
                            //move would place own king in check
                            if (currTurn.isChecked(gameArray)){
                                undo(fromPiece, toPiece, fromSquare, toSquare);
                                System.out.println("Invalid move: Would leave king in check");
                            }
                            else {
                                //introduce new check
                                if (swapCurTurn().isChecked(gameArray)){
                                    System.out.println("Check");
                                    if (isCheckmate(swapCurTurn())){
                                        gameOver(board, this, currTurn.getColor());
                                    }
                                }
                                currTurn = swapCurTurn();
                            }
                            //pawn promotion
                            if (fromPiece instanceof Pawn && (fromPiece.getY() == 7 || fromPiece.getY() == 0)){
                                pawnPromotion(fromPiece, toSquare);
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
            if(!piece.isCaptured()){
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
    
    public void gameOver(JPanel board, MouseListener moveListener, String winner){
        board.removeMouseListener(moveListener);
        String[] options = {"New Game", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this, "CHECKMATE. "+winner.toUpperCase()+" wins!", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
        if (choice == 0){
            this.dispose();
            JFrame frame = new ChessGame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    public void pawnPromotion(Piece piece, Square square){
        String[] options = {"Queen", "Rook","Knight", "Bishop"};
        String choice = (String) JOptionPane.showInputDialog(this, "Choose piece to promote to", "Promote", JOptionPane.QUESTION_MESSAGE, null, options, "Queen");
        Piece newPiece;
        switch(choice){
            case "Queen":
                newPiece= new Queen(piece.getX(), piece.getY(), piece.getColor());
                break;
            case "Knight":
                newPiece= new Knight(piece.getX(), piece.getY(), piece.getColor());
                break;
            case "Rook":
                newPiece= new Rook(piece.getX(), piece.getY(), piece.getColor());
                ((Rook)newPiece).setCanCastle(false);
                break;
            default:
                newPiece= new Bishop(piece.getX(), piece.getY(), piece.getColor());
                break;
        }
        if (piece.getColor() == "white"){
            whitePieces.set(whitePieces.indexOf(piece),newPiece);
        }
        else{
            blackPieces.set(whitePieces.indexOf(piece),newPiece);
        }
        square.setPiece(newPiece);
    }

    public static void main(String[] args){
        JFrame frame = new ChessGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
