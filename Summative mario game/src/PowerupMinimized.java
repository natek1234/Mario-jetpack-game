import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class PowerupMinimized {

	// The name of the image representing the PowerupMinimized.
	// This file must be in the same folder as the Java source files.
	private String imgName = "MiniMushroom.png";
	private Image image;

	// Movement variables
	private int x;
	private int y;
	
	// Visibility variable.
	private boolean isVisible;

	// Represents the width and height of the PowerupMinimized. This will be
	// used later...
	private int width;
	private int height;
			
	// Constructor for the PowerupMinimized.
	public PowerupMinimized() {
		// Creates the image representing the PowerupMinimized.
		ImageIcon ii = new ImageIcon(this.getClass().getResource(imgName));
		this.image = ii.getImage();

		// Calculates the size of the PowerupMinimized based on the image.
		this.width = this.image.getWidth(null);
		this.height = this.image.getHeight(null);
		
		// Sets the initial position of the PowerupMinimized.
		this.x = Game.W_WIDTH;
		this.y = (int)(Math.random() * Game.W_HEIGHT) + 50;
		
		// Default setting to true.
		this.isVisible = true;
	}

	// Returns the current visibility of the PowerupMinimized.
	public boolean getVisibility() {
		
		return this.isVisible;
	}
	
	// Sets the visibility to the incoming parameter.
	public void setVisible(boolean visOrNot) {
		this.isVisible = visOrNot;
	}
	
	// Moves the PowerupMinimized. 
	// The width and height of the screen must be passed so that it cannot go out of bounds.
	public void move(double gameSpeed, int secondsNoResets) {
		// Updates the PowerupMinimized's location based on its dx and dy values.
		this.x = this.x - ((int) (Math.round(secondsNoResets * gameSpeed)));

	}

	// Returns the x-coordinate of the PowerupMinimized.
	// This is primarily so that the content pane knows where to draw it on screen.
	public int getX() {
		return this.x;
	}

	// Returns the y-coordinate of the PowerupMinimized.
	public int getY() {
		return this.y;
	}

	// Returns the image of the PowerupMinimized.
	public Image getImage() {
		return this.image;
	}

	// Returns the hitbox associated with the PowerupMinimized.
	public Rectangle getHitbox() {
		return new Rectangle(this.x, this.y, this.width, this.height);
	}
	
}
