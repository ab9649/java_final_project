import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import square.*;
import pieces.*;


public class NetworkGame extends JFrame implements Runnable{

    private Square[][] gameArray;
    private King startTurn;
    private King whiteKing;
    private King blackKing;
    private final ArrayList<Piece> blackPieces = new ArrayList<Piece>();
    private final ArrayList<Piece> whitePieces = new ArrayList<Piece>();
    private JPanel board;
    private JFrame frame = this;

    //networking
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    Socket socket = null;
    NetworkGame client = this;
    King clientColor = null;
    

    //moving
    Square fromSquare = null;
    Square toSquare = null;
    Set<String> possibleMoveLocations = null;
    boolean validPress = true;
    King currTurn;
    MouseListener moveListener = null;

    @Override
    public void run() {
        try {
            fromServer = new DataInputStream(socket.getInputStream());
    
            while (true) {
              String move = fromServer.readUTF();
              Square fromSquare = gameArray[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
              Square toSquare = gameArray[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
              possibleMoveLocations = fromSquare.getPiece().possibleMoves(gameArray);
              performMove(fromSquare, toSquare);
            }
          }
          catch(IOException ex) {
            ex.printStackTrace();
          } 
        
    }
    public NetworkGame(){
        setSize(700, 700);
        board =  new ChessBoard();
        gameArray = ((ChessBoard)board).getChessBoardArray();
        whiteKing= (King) gameArray[4][7].getPiece();
        blackKing = (King) gameArray[4][0].getPiece();
        startTurn= whiteKing;
        currTurn = startTurn;

        for (int j = 0, i = 0, k = 0; i < 16; i ++, j++){
            if (i == 8){j = 0; k =1;}
            blackPieces.add(gameArray[j][k].getPiece());
        }
        for (int j = 0, i = 0, k = 7; i < 16; i ++, j++){
            if (i == 8){j = 0; k =6;}
            whitePieces.add(gameArray[j][k].getPiece());
        }

        moveListener = new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                JComponent comp = (JComponent) e.getSource();
                if (comp.getComponentAt(e.getPoint()) instanceof Square){
                    Square chosenSquare = (Square) comp.getComponentAt(e.getPoint());
                    if (chosenSquare.getPiece() != null && chosenSquare.getPiece().getColor().equals(clientColor.getColor()) && chosenSquare.getPiece().getColor().equals(currTurn.getColor())){
                        fromSquare = chosenSquare;
                        //validate moves
                        possibleMoveLocations = fromSquare.getPiece().possibleMoves(gameArray);
                        validPress = true;
                        return;
                       }
                }
                validPress = false;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (validPress){
                    JComponent comp = (JComponent) e.getSource();
                    if (comp.getComponentAt(e.getPoint()) instanceof Square){
                        toSquare = (Square) comp.getComponentAt(e.getPoint());
                        performMove(fromSquare, toSquare);
                        try {
                            toServer = new DataOutputStream(socket.getOutputStream());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            String outSquares = ""+fromSquare.getlocX()+fromSquare.getlocY()+toSquare.getlocX()+toSquare.getlocY();
                            toServer.writeUTF(outSquares);
                            toServer.flush();
                          }
                          catch (IOException ex) {
                            System.err.println(ex);
                          }
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };
        

        board.addMouseListener(moveListener);
        createMenu();
        add(board, BorderLayout.CENTER);

        //network
        try {
            socket = new Socket("localhost", 9898);
            DataInputStream getColor = new DataInputStream(socket.getInputStream());
            if (getColor.readUTF().equals("white")){
                clientColor = whiteKing;
            }
            else{
                clientColor = blackKing;
            }
            Thread thread = new Thread(this);
            thread.start();
        } catch (IOException e1) {
            e1.printStackTrace();
        } 
    }
    

    public void performMove(Square fromSquare, Square toSquare){
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
                        gameOver(board, moveListener, currTurn.getColor());
                    }
                }
                //pawn promotion
                if (fromPiece instanceof Pawn && (fromPiece.getY() == 7 || fromPiece.getY() == 0)){
                    pawnPromotion(fromPiece, toSquare);
                }
                currTurn = swapCurTurn();
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
        String[] options = {"Rematch", "Exit"};
        int choice = JOptionPane.showOptionDialog(this, "CHECKMATE. "+winner.toUpperCase()+" wins!", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
        if (choice == 0){
            resetBoard();
            try {
                toServer.writeUTF("reset");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                toServer.writeUTF("Cancel");
                client.dispose();
                StartGame.main(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Options");
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener((e) -> System.exit(0));
		JMenuItem connectItem = new JMenuItem("Return To Menu");
		connectItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                StartGame.main(null);
                frame.dispose();
            }
        });
		menu.add(connectItem);
		menu.add(exitItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}

    public void resetBoard(){
        for (int i = 0; i < 8; i++){
            gameArray[i][0].setPiece(blackPieces.get(i));
        }
        for (int i = 0; i <8; i++){
            gameArray[i][1].setPiece(blackPieces.get(i+8));
        }
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                gameArray[i][j].setPiece(null);
            }
        }
        for (int i = 0; i <8; i++){
            gameArray[i][1].setPiece(whitePieces.get(i+8));
        }
        for (int i = 0; i < 8; i++){
            gameArray[i][0].setPiece(whitePieces.get(i));
        }
    }

    public static void main(String[] args){
        JFrame frame = new NetworkGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    
}
