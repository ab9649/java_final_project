import javax.swing.*;
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
                JPanel temp = new JPanel();
                if ((i % 2 != 0 &&j % 2 == 0) || (i % 2 == 0 && j % 2 != 0)){
                        temp.setBackground(Color.BLACK);
                    }
                else{
                    temp.setBackground(Color.WHITE);
                }
                add(temp);
                }
            }
        }
}
