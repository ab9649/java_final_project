import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


public class GameServer extends JFrame implements Runnable {

	private static int WIDTH = 400;
	private static int HEIGHT = 300;
	private JTextArea ta;
	private int colorNum = 0;
	private HashMap<Socket, Socket> opponents = new HashMap<Socket, Socket>();
	private Queue<Socket> clientQueue = new LinkedList<>();
	
	public GameServer() {
		super("Chat Server");
		ta = new JTextArea();
		ta.append("Game server started at "+ new Date() +"\n");
		this.add(ta);
		this.setSize(GameServer.WIDTH, GameServer.HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		this.setVisible(true);

		Thread t = new Thread(this);
		t.start();
		
	}
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener((e) -> System.exit(0));
		menu.add(exitItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	

	public static void main(String[] args) {
		GameServer gameServer = new GameServer();
	}

	@Override
	public void run() {
			try (ServerSocket serverSocket = new ServerSocket(9898)) {
                while (true){
                	Socket socket = serverSocket.accept();
                	colorNum ++;
                    ta.append("Starting thread for client " + colorNum + " at " + new Date() + '\n');

                	if (clientQueue.isEmpty()){
						clientQueue.add(socket);
					}
					else{
						Socket partner = clientQueue.remove();
						opponents.put(socket, partner);
						opponents.put(partner, socket);
					}
                
                	new Thread(new HandleAClient(socket, colorNum)).start();
                }		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class HandleAClient implements Runnable {
		private Socket socket; 
		private int colorNum;
		
		public HandleAClient(Socket socket, int colorNum) {
		  this.socket = socket;
		  this.colorNum = colorNum;
          try {
            String color;
            if (this.colorNum % 2 == 1){
                color = "white";
            }
            else{
                color = "black";
            }
            ObjectOutputStream assignColor = new ObjectOutputStream(socket.getOutputStream());
            assignColor.writeObject(color);
			assignColor = null;
			
        	} catch (IOException e) {
            	e.printStackTrace();
        	}
		}
	
		public void run() {
		  try {

			ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream outputToClient;
	
			while (true) {
				//if (opponents.get(socket)!= null){
                	int [][]moves =  (int[][]) inputFromClient.readObject();
					Socket opponent = opponents.get(socket);
					outputToClient = new ObjectOutputStream(opponent.getOutputStream());
					outputToClient.writeObject(moves);
				//}
			}
		  }
		  catch(IOException ex) {
			ex.printStackTrace();
		  } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	  }
	
}


