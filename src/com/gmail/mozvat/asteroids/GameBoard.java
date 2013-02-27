package com.gmail.mozvat.asteroids;

//Layout used by the JPanel
import java.awt.BorderLayout;
//Define color of shapes
import java.awt.Color;

import java.awt.geom.AffineTransform;

//Allows me to draw and render shapes on components
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
//Key event handlers
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//Will hold all of my Rock objects
import java.util.ArrayList;
//Runs commands after a given delay
import java.util.concurrent.ScheduledThreadPoolExecutor;
//Defines time units. In this case TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameBoard extends JFrame{
	
	// Height and width of the game board
	public static int boardWidth = 1000;
	public static int boardHeight = 800;
	
	public static boolean keyHeld = false;
	public static int keyHeldCode;
	
	public static void main(String [] args){
         new GameBoard();
	}
	
	public GameBoard()
	{
 	// Define the defaults for the JFrame
     this.setSize(boardWidth, boardHeight);
     this.setTitle("Ozvat's Asteroids");
     this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
     addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==87)
			    {
					System.out.println("Forward");
			    } else if (e.getKeyCode()==83){
			    	System.out.println("Backward");
			    } else if (e.getKeyCode()==68){
			    	System.out.println("Rotate Right");
			    	keyHeldCode = e.getKeyCode();
			    	keyHeld = true;
			    } else if (e.getKeyCode()==65){
			    	System.out.println("Rotate Left");
			    	keyHeldCode = e.getKeyCode();
			    	keyHeld = true;
			    }
			    
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyHeld = false;
			}
     });
     
     GameDrawingPanel gamePanel = new GameDrawingPanel();
     //Make the drawing area take up the rest of the frame
     this.add(gamePanel, BorderLayout.CENTER);
     // Used to execute code after a given delay
     // The attribute is corePoolSize - the number of threads to keep in 
     // the pool, even if they are idle
     ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
     // Method to execute, initial delay, subsequent delay, time unit
     executor.scheduleAtFixedRate(new RepaintTheBoard(this), 0L, 20L, TimeUnit.MILLISECONDS);
     // Show the frame
     this.setVisible(true);
 }
}

//Class implements the runnable interface
//By creating this thread we can continually redraw the screen
//while other code continues to execute
class RepaintTheBoard implements Runnable{

	GameBoard theBoard;	
	public RepaintTheBoard(GameBoard theBoard){
		this.theBoard = theBoard;
	}

	@Override
	public void run() {
		// Redraws the game board		
		theBoard.repaint();
	}
}

//GameDrawingPanel is what we are drawing on
@SuppressWarnings("serial")
class GameDrawingPanel extends JComponent { 
	
	// Holds every Rock I created
	public static ArrayList<Rock> rocks = new ArrayList<Rock>();
	// Get the original x & y points for the polygon
	int[] polyXArray = Rock.sPolyXArray;
	int[] polyYArray = Rock.sPolyYArray;
	// Create a SpaceShip
	SpaceShip theShip = new SpaceShip();
	// Gets the game board height and weight
	int width = GameBoard.boardWidth;
	int height = GameBoard.boardHeight;
	// Creates 50 Rock objects and stores them in the ArrayList
	public GameDrawingPanel() { 
		
		for(int i = 0; i < 2; i++){
			// Find a random x & y starting point
			// The -40 part is on there to keep the Rock on the screen
			int randomStartXPos = (int) (Math.random() * (GameBoard.boardWidth - 40) + 1);
			int randomStartYPos = (int) (Math.random() * (GameBoard.boardHeight - 40) + 1);
			// Add the Rock object to the ArrayList based on the attributes sent
			rocks.add(new Rock(Rock.getpolyXArray(randomStartXPos), Rock.getpolyYArray(randomStartYPos), 13, randomStartXPos, randomStartYPos)); 		
			Rock.rocks = rocks;
		}
	} 
	
	public void paint(Graphics g) { 
		// Allows me to make many settings changes in regards to graphics	
		Graphics2D graphicSettings = (Graphics2D)g; 
		
		AffineTransform identity  = new AffineTransform();
		
		
		// Draw a black background that is as big as the game board
		graphicSettings.setColor(Color.BLACK);
		graphicSettings.fillRect(0, 0, getWidth(), getHeight());
		// Set rendering rules
		graphicSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		// Set the drawing color to white
		graphicSettings.setPaint( Color.WHITE); 
		// Cycle through all of the Rock objects
		for(Rock rock : rocks){
			// Move the Rock polygon 
			rock.move(); 
			// redraw the polygon Rock on the screen
			graphicSettings.draw(rock); 
		} 
	
		if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 68 ){
			SpaceShip.rotationAngle += 10;
		}else if (GameBoard.keyHeld == true && GameBoard.keyHeldCode == 65 ){
			SpaceShip.rotationAngle -= 10;
		} 
			
		theShip.move();
		
		graphicSettings.setTransform(identity);
		graphicSettings.translate(GameBoard.boardWidth/2,GameBoard.boardHeight/2);
		graphicSettings.rotate(Math.toRadians(SpaceShip.rotationAngle));
		
		graphicSettings.draw(theShip);
	} 
	
}