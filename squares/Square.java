package squares;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Square extends JPanel {
	private Image img;
	
	public Square(String img) {
		this(new ImageIcon(img).getImage());
	}
    public Square(){
        this.img = null;
    }
	
	public Square(Image img) {
		this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setLayout(null);
	}
    public void paintComponent(Graphics g) {
        g.drawImage(img, 50, 0, null);
    }
    public void setImage(String newimg){
        img = new ImageIcon(newimg).getImage();
        repaint();
    }

}