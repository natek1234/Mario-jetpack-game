import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Character {

	// The name of the image representing the character.
	// This file must be in the same folder as the Java source files.
	private String imgName = "mario.png";
	private Image image;

	// List of fireballs that have been fired.
	private ArrayList<Fireballs> fired;
	
	//Creates sound for jetpack
	private Sound jetpack = new Sound("Water_Hose_Sound_Effect2.wav");
	
	// Movement variables
	private double x;
	private double y;
	private double dx, dy;
	//Creates the impression of gravity.
	private double gravity = 1;
	//Creates the impression of acceleration.
	private double acceleration = -1.5;

	// Represents the width and height of the character. This will be
	// used later...
	private int width;
	private int height;

	// Constructor for the character.
	public Character() {
		// Creates the image representing the character.
		ImageIcon ii = new ImageIcon(this.getClass().getResource(imgName));
		this.image = ii.getImage();

		// Calculates the size of the character based on the image.
		this.width = this.image.getWidth(null);
		this.height = this.image.getHeight(null);

		// Arraylist of fired missiles.
		this.fired = new ArrayList<Fireballs>();
				
		// Sets the initial position of the character.
		this.x = 0;
		this.y = 365;
	}


	// Moves the character. 
	// The width and height of the screen must be passed so that it cannot go out of bounds.
	public void move() {
		// Updates the character's location based on its dx and dy values.
		//Gravity is incremented and applied.
		gravity = gravity * 1.005;
		this.x = this.x + this.dx;
		this.y = (this.y + this.dy) + gravity;

		// Ensures that it can't move out of bounds.
		if (this.x < 1) {
			this.x = 1;
		}

		if (this.y < 1) {
			this.y = 1;
		}

		if (this.x > Game.W_WIDTH) {
			this.x = Game.W_WIDTH;
		}

		if (this.y > Game.W_HEIGHT) {
			this.y = Game.W_HEIGHT;
		}
		// Instructs the missiles to move.
		for (int i = 0; i < this.fired.size(); i++) {
			if (this.fired.get(i).getVisibility() == true) {
				// Move the missile if it's visible.	
				this.fired.get(i).move();
			}
			else {
				// Remove the missile if it's not visible.
				this.fired.remove(this.fired.get(i));
			}
		}
	}

	// Returns the x-coordinate of the character.
	// This is primarily so that the content pane knows where to draw it on screen.
	public double getX() {
		return this.x;
	}

	// Returns the y-coordinate of the character.
	public double getY() {
		return this.y;
	}

	// Returns the image of the character.
	public Image getImage(boolean minimizedOn) {
		//If minimized is turned on then the image switched with a smaller image.
		if (minimizedOn == true)
		{
			imgName = "MiniMario.png";
			// Creates the image representing the character.
			ImageIcon miniMario = new ImageIcon(this.getClass().getResource(imgName));
			this.image = miniMario.getImage();

			// Calculates the size of the character based on the image.
			this.width = this.image.getWidth(null);
			this.height = this.image.getHeight(null);
			return this.image;

		}
		//Runs normally if minimized is not turned on.
		imgName = "mario.png";
		// Creates the image representing the character.
		ImageIcon ii = new ImageIcon(this.getClass().getResource(imgName));
		this.image = ii.getImage();

		// Calculates the size of the character based on the image.
		this.width = this.image.getWidth(null);
		this.height = this.image.getHeight(null);
		return this.image;

	}
	// Returns the Arraylist of fireballs that have been fired.
	public ArrayList<Fireballs> getFiredFireballs() {
		return this.fired;
	}

	

	// How to react to keys for movement purposes
	public void keyPressed(KeyEvent e, boolean fireFlowerOn, boolean gameOver) {
		// Grabs a code that represents the key that was pressed.
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_SPACE)
		{
			if (fireFlowerOn == true)
			this.fired.add(new Fireballs((int) Math.round(this.x), (int) Math.round(this.y)));
		}
		if (key == KeyEvent.VK_LEFT) {
			this.dx = -3;
		}

		if (key == KeyEvent.VK_RIGHT) {
			this.dx = 3;
		}

		if (key == KeyEvent.VK_UP) {
			//Simulates acceleration while key is pressed.
			acceleration = acceleration * 1.07;
			this.dy = acceleration;
			if(gameOver == false)
			{
				//Plays jetpack sound.
				jetpack.loop();
			}
			gravity = 0;
		}

		if (key == KeyEvent.VK_DOWN) {
			this.dy = 3;
		}
	}
 
	// The following code runs when a key is released.
	// It resets the dx and dy variables.
	public void keyReleased(KeyEvent e) {
		// Grabs a code that represents the key that was pressed.
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			this.dx = 0;
		}

		if (key == KeyEvent.VK_RIGHT) {
			this.dx = 0;
		}

		if (key == KeyEvent.VK_UP) {
			this.dy = 0;
			//Stops jetpack sound.
			jetpack.stop();
			//Gravity is activated.
			gravity = 1;
			//Acceleration is reset to a base value.
			acceleration = -2;
		}

		if (key == KeyEvent.VK_DOWN) {
			this.dy = 0;
		}
	}

	// Returns the hitbox associated with the Character.
	public Rectangle getHitbox() {
		return new Rectangle((int) Math.round(this.x), (int) Math.round(this.y), this.width, this.height);
	}
}
