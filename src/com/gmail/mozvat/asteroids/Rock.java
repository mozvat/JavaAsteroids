package com.gmail.mozvat.asteroids;

//Leveraging the existing polygon class
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
//Extending the Polygon class because I'm drawing Polygons
@SuppressWarnings("serial")
public class Rock extends Polygon {
	
	// Upper left hand corner of the Polygon
	int uLeftXPos, uLeftYPos;
	// Used to change the direction of the asteroid when 
	// it hits something and determines how fast it moves	
	int xDirection = 1;
	int yDirection = 1;
	// Get the board width and height
	int width = GameBoard.boardWidth;
	int height = GameBoard.boardHeight;
	// Will hold the x & y coordinates for the Polygons	
	int[] polyXArray, polyYArray;
	//Rock Dimensions
	int rockWidth = 26;
	int rockHeight = 31;
	// Used to change the direction of the asteroid when
	// it hits something and determines how fast it moves
	static ArrayList<Rock> rocks = new ArrayList<Rock>();

	// x & y positions available for other methods
	// There will be more Polygon points available later	
	//public static int[] sPolyXArray = {6,18,36,48,54,54,48,36,18,6,0,0,6};
	//public static int[] sPolyYArray = {6,0,0,6,18,36,48,54,54,48,36,18,6};
	public static int[] sPolyXArray = {10,17,26,34,27,36,26,14,8,1,5,1,10};
	public static int[] sPolyYArray = {0,5,1,8,13,20,31,28,31,22,16,7,0};

	// Constructor - Creates a new asteroid 
	public Rock(int[] polyXArray, int[] polyYArray, int pointsInPoly, int randomStartXPos, int randomStartYPos){
		// Creates a Polygon by calling the super or parent class of Rock Polygon
		super(polyXArray, polyYArray, pointsInPoly);
		// Randomly generate a speed for the Polygon
		this.xDirection = (int) (Math.random() * 4 + 1);
		this.yDirection = (int) (Math.random() * 4 + 1);
		//this.xDirection = (int) (Math.random() * 2 + 1);
		//this.yDirection = (int) (Math.random() * 2 + 1);
		// Holds the starting x & y position for the Rock	
		this.uLeftXPos = randomStartXPos;
		this.uLeftYPos = randomStartYPos;
	}
	
	public Rectangle getBounds(){
		return new Rectangle(super.xpoints[0], super.ypoints[0],rockWidth, rockHeight);
	}
		public void move(){
			//Get this rocks bounds
			Rectangle rockToCheck = this.getBounds();
			
			for(Rock rock : rocks){
				Rectangle otherRock = rock.getBounds();
				//Check that we are not checking 'this' rock to itself
				//Then check other rock to 'this' rock if there is an intersection/collision
				if(rock != this && otherRock.intersects(rockToCheck)){
					//Get 'this' rocks x,y direction
					int tempXDirection = this.xDirection;					
					int tempYDirection = this.yDirection;
					//set the other rocks x,y direction to this rocks direction
					this.xDirection = rock.xDirection;
					this.yDirection = rock.yDirection;
					//Set 'this' rocks original x,y direction to the other rock's direction
					rock.xDirection = tempXDirection;
					rock.yDirection = tempYDirection;
				}
			}
			// Get the upper left and top most point for the Polygon
			// This will be dynamic later on		
			int uLeftXPos = super.xpoints[0]; 		
			int uLeftYPos = super.ypoints[0];
			
			// If the Rock hits a wall it will go in the opposite direction
			if (uLeftXPos < 0 || (uLeftXPos + 25) > width) xDirection = -xDirection; 
			if (uLeftYPos < 0 || (uLeftYPos + 50) > height) yDirection = -yDirection; 
			
			// Change the values of the points for the Polygon
			for (int i = 0; i < super.xpoints.length; i++){
				super.xpoints[i] += xDirection;
				super.ypoints[i] += yDirection;
			}
		}
		
		// public method available for creating Polygon x point arrays, it needs the start position as a parameter to give the exact initial coordinates
		public static int[] getpolyXArray(int randomStartXPos){
			// Clones the array so that the original shape isn't changed for the asteroid
			int[] tempPolyXArray = (int[])sPolyXArray.clone();
			
			for (int i = 0; i < tempPolyXArray.length; i++){
				tempPolyXArray[i] += randomStartXPos;
			}
			return tempPolyXArray;
		}
		
		// public method available for creating Polygon y point arrays, it needs the start position as a parameter to give the exact initial coordinates
		public static int[] getpolyYArray(int randomStartYPos){
			// Clones the array so that the original shape isn't changed for the asteroid
			int[] tempPolyYArray = (int[])sPolyYArray.clone();
	
			for (int i = 0; i < tempPolyYArray.length; i++){
				tempPolyYArray[i] += randomStartYPos;
			}		
			return tempPolyYArray;
		}
}