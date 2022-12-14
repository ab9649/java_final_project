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
    private JFrame temp = null;

    //networking
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    Socket socket = null;
    NetworkGame client = this;
    King clientColor = null;
    Boolean opponentRematch = false;
    Boolean playerRematch =false;
    

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
              //found opponent
              if (move.equals("connected")){
                temp.dispose();
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setVisible(true);
              }
              //opponent disconnected
              else if (move.equals("disconnected")){
                JOptionPane.showConfirmDialog(this, "Opponent disconnected.\nReturn to main menu", "Return to menu",JOptionPane.DEFAULT_OPTION);
                client.dispose();
                StartGame.main(null);
              }
              //opponent resigned
              else if (move.equals("resign")){
                gameOver(board, moveListener, clientColor.getColor(), clientColor.swapColor().toUpperCase() + " Resigned!");
              }
              //opponent requests rematch
              else if (move.equals("reset")){
                if (playerRematch == true){
                    currTurn = startTurn;
                }
                else{opponentRematch = true;}
              }
              //opponent declined rematch
              else if (move.equals("cancel")){
                JOptionPane.showConfirmDialog(this, "Opponent denied Rematch.\nReturn to main menu", "Return to menu",JOptionPane.DEFAULT_OPTION);
                client.dispose();
                StartGame.main(null);
              }
              //opponent pawn promotion choice
              else if (move.charAt(0) == 'P'){
                pawnPromotion(toSquare.getPiece(), toSquare, move.substring(1));
                currTurn = swapCurTurn();
                fromSquare = null;
                toSquare = null;
                possibleMoveLocations = null;
              }
              //normal opponent move
              else{
                fromSquare = gameArray[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
                toSquare = gameArray[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
                possibleMoveLocations = fromSquare.getPiece().possibleMoves(gameArray);
                performMove(fromSquare, toSquare);
              }
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
                        //send move to opponent client
                        try {
                            toServer = new DataOutputStream(socket.getOutputStream());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            String outSquares = ""+fromSquare.getlocX()+fromSquare.getlocY()+toSquare.getlocX()+toSquare.getlocY();
                            toServer.writeUTF(outSquares);
                            toServer.flush();
                          } catch (IOException ex) {
                            System.err.println(ex);
                        }
                        //perform move in this client
                        performMove(fromSquare, toSquare);
                        
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };
        
        createMenu();
        board.addMouseListener(moveListener);
        add(board, BorderLayout.CENTER);

        //network
        try {
            socket = new Socket("localhost", 9898);
            DataInputStream getColor = new DataInputStream(socket.getInputStream());
            //client is first to join, they are white 
            //need to wait for an opponent to connect
            if (getColor.readUTF().equals("white")){
                clientColor = whiteKing;
                temp = new JFrame();
                temp.setSize(500,500);
                JPanel panel = new JPanel(){
                    @Override
                    protected void paintComponent(Graphics g){
                        super.paintComponent(g);
                        g.drawImage(new ImageIcon("images/GameImage.png").getImage(), 0, 0, this.getWidth(),this.getHeight(),null);
                    }
                };
                JLabel waitingLabel = new JLabel("Waiting for opponent to connect");
                waitingLabel.setFont(new Font("Arial", Font.PLAIN, 22));
                waitingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                temp.setContentPane(panel);
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.add(Box.createGlue());
                panel.add(waitingLabel);
                panel.add(Box.createGlue());
                temp.setVisible(true);
            }
            //client connects to awaiting opponent, client is black, game can begin
            else{
                clientColor = blackKing;
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
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
                        gameOver(board, moveListener, currTurn.getColor(), "CheckMate!");
                    }
                }
                //pawn promotion
                if (fromPiece instanceof Pawn && (fromPiece.getY() == 7 || fromPiece.getY() == 0)){
                    if (fromPiece.getColor().equals(clientColor.getColor())){
                        pawnPromotion(fromPiece, toSquare, null);
                    }
                    else{
                        return;
                    }
                }
                currTurn = swapCurTurn();
            }
        }
        fromSquare = null;
        toSquare = null;
        possibleMoveLocations = null;
     }

    //swaps turn after successful move
    private King swapCurTurn() {
        if (currTurn == whiteKing){
            return blackKing;
        }
        return whiteKing;
    }

    //check is king is checkmated
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

    //undo previous move
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
    
    //displays game over screen
    public void gameOver(JPanel board, MouseListener moveListener, String winner, String winCondition){
        String[] options = {"Rematch", "Exit"};
        int choice = JOptionPane.showOptionDialog(this, winCondition+" "+winner.toUpperCase()+" wins!", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
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
                toServer.writeUTF("cancel");
                client.dispose();
                StartGame.main(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //dsplays promotion choice screen
    public void pawnPromotion(Piece piece, Square square, String input){
        String[] options = {"Queen", "Rook","Knight", "Bishop"};
        String choice;
        if (input == null){
            choice = (String) JOptionPane.showInputDialog(this, "Choose piece to promote to", "Promote", JOptionPane.QUESTION_MESSAGE, null, options, "Queen");
        }
        else{
            choice = input;
        }
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
            case "Bishop":
                newPiece= new Bishop(piece.getX(), piece.getY(), piece.getColor());
                break;
            default:
                newPiece= new Bishop(piece.getX(), piece.getY(), piece.getColor());
                break;
        }
        //write choice to opponent client
        if (input == null){
            try {
                toServer.writeUTF("P"+choice);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (piece.getColor() == "white"){
            whitePieces.set(whitePieces.indexOf(piece),newPiece);
        }
        else{
            blackPieces.set(blackPieces.indexOf(piece),newPiece);
        }
        square.setPiece(newPiece);

    }

    private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Options");
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener((e) -> System.exit(0));
		JMenuItem connectItem = new JMenuItem("Resign");
		connectItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    toServer = new DataOutputStream(socket.getOutputStream());
                    toServer.writeUTF("resign");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                gameOver(board, moveListener, clientColor.swapColor(),clientColor.getColor().toUpperCase()+ " Resigned!");
            }
        });
		menu.add(connectItem);
		menu.add(exitItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}

    //reset board for new game
    public void resetBoard(){
        for (int i = 0; i < 8; i++){
            gameArray[i][0].setPiece(blackPieces.get(i));
        }
        for (int i = 0; i <8; i++){
            gameArray[i][1].setPiece(blackPieces.get(i+8));
        }
        for (int i = 2; i < 6; i++){
            for (int j = 0; j < 8; j++){
                gameArray[j][i].setPiece(null);
            }
        }
        for (int i = 0; i <8; i++){
            gameArray[i][6].setPiece(whitePieces.get(i+8));
        }
        for (int i = 0; i < 8; i++){
            gameArray[i][7].setPiece(whitePieces.get(i));
        }
        fromSquare = null;
        toSquare = null;
        possibleMoveLocations = null;
        currTurn = startTurn;
    }

    public static void main(String[] args){
        JFrame frame = new NetworkGame();

    }
    
}
