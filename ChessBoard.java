import javax.swing.*;

import pieces.*;


import java.awt.*;
import square.*;

public class ChessBoard extends JPanel{

    private Square[][] chessBoardArray = new Square[8][8];

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
                switch(i){
                    case 1:
                        if (j == 2 || j == 7){temp = new Square(new Knight(j-1,i-1,"black"));}
                        else{temp = new Square(j-1, i-1);} //commment this out after making other peices
                        break;
                    case 2:
                        temp = new Square(new Pawn(j-1,i-1,"black"));
                        break;
                    case 7:
                        temp = new Square(new Pawn(j-1, i-1, "white"));
                        break;
                    case 8:
                        if (j == 2 || j == 7){temp = new Square(new Knight(j-1,i-1,"white"));}
                        else{temp = new Square(j-1, i-1);} //comment this our after making other pieces
                        break;
                    default:
                        temp = new Square(j-1, i-1);
                }

                if ((i % 2 != 0 &&j % 2 == 0) || (i % 2 == 0 && j % 2 != 0)){
                        temp.setBackground(Color.GRAY);
                    }
                else{
                    temp.setBackground(Color.LIGHT_GRAY);
                }
                chessBoardArray[j-1][i-1] = temp;
                add(temp);
                }
            }
        }
    public Square[][] getChessBoardArray(){return this.chessBoardArray;}
}
