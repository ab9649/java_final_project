import javax.swing.*;
import java.awt.*;


public class ChessGame extends JFrame{
    public ChessGame(){
        setSize(700, 700);
        JPanel board =  new ChessBoard();
        add(board, BorderLayout.CENTER);
    }



    public static void main(String[] args){
        JFrame frame = new ChessGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
