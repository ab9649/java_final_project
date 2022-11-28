import javax.swing.*;

import pieces.*;

import java.awt.*;

public class ChessBoard extends JPanel{

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
                //pawns
                if (i == 2){temp = new Square(new Pawn(i,j,"black"));}
                else if (i == 7){temp = new Square(new Pawn(i, j, "white"));}

                //back row
                /*else if(i == 1){
                    if (j == 1 || j == 8){temp = new Square(new Rook(i,j,"black"));}
                }*/
                else {temp = new Square();}
                if ((i % 2 != 0 &&j % 2 == 0) || (i % 2 == 0 && j % 2 != 0)){
                        temp.setBackground(Color.GRAY);
                    }
                else{
                    temp.setBackground(Color.LIGHT_GRAY);
                }
                add(temp);
                }
            }
        }
}
