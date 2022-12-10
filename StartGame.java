import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class StartGame extends JFrame{
    JFrame frame = this;
    public StartGame(){
        setSize(500,500);
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(new ImageIcon("images/GameImage.png").getImage(), 0, 0, this.getWidth(),this.getHeight(),null);
            }
        };
        setContentPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        

        JButton localButton = new JButton("Local");
        JButton networkButton = new JButton("Network");
        Font FONT = new Font("Arial", Font.PLAIN, 22);
        localButton.setFont(FONT);
        networkButton.setFont(FONT);

        localButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        localButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                ChessGame.main(null);
                frame.dispose();
            }
        });
        networkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        networkButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                NetworkGame.main(null);
                frame.dispose();
            }
        });
        
        panel.add(Box.createGlue());
        panel.add(localButton);
        panel.add(Box.createRigidArea(new Dimension(0,30)));
        panel.add(networkButton);
        panel.add(Box.createGlue());
        
    }

    public static void main(String[] args){
        JFrame frame = new StartGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
