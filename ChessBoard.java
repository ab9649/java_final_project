import squares.Square;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class ChessBoard extends JPanel{
    ArrayList<ArrayList<Square>> boardArr = new ArrayList<ArrayList<Square>>();
    String[] blackList = {"rook_black.png","knight_black.png","bishop_black.png","queen_black.png","king_black.png","bishop_black.png","knight_black.png","rook_black.png"};
    String[] whiteList = {"rook_white.png", "knight_white.png", "bishop_white.png", "queen_white.png","king_white.png","bishop_white.png","knight_white.png","rook_white.png"};
    public ChessBoard(){
        setLayout(new GridLayout(9,9));
        add(new JLabel(""));
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        for (char c = 'A'; c <'I'; c++){
            add(new JLabel(""+c, SwingConstants.CENTER));
        }
        for (int i = 1; i < 9; i++){
            add(new JLabel(""+i, SwingConstants.CENTER));
            for (int j = 1; j < 9; j++){
                Square temp;
                if (i == 1){temp = new Square(blackList[j-1]);}
                else if (i == 2){temp = new Square("pawn_black.png");}
                else if (i == 8){temp = new Square(whiteList[j-1]);}
                else if (i == 7){temp = new Square("white_pawn.png");}
                else{temp = new Square();}
                ArrayList<Square> row = new ArrayList<Square>();
                if ((i % 2 != 0 &&j % 2 == 0) || (i % 2 == 0 && j % 2 != 0)){
                        temp.setBackground(Color.BLACK);
                    }
                else{
                    temp.setBackground(Color.WHITE);
                }
                row.add(temp);
                boardArr.add(row);

                add(temp);
                }
            }
        }
}
