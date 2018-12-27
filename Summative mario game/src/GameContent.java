import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;
/* Jan 10th: Made game work with backgrounds moving. Made powerups spawn. Need to work on making hitboxes
 * Jan 11th: Made speed change the same for powerups and background. Work on enemies next and start/end screen.
 * Jan 12th: Attempted to create enemies array, ask for help as to how to work it. Why is array out of bounds?. Keep working on enemies and sound for jetpack.
 * 			 Remove backgrounds of images in order to finish start screen. Work on creating end screen.
 * Jan 14th: Created an array of coins for each level. Game now prints coins collected and time elapsed to screen.
 * 			 Made the mini mario power up function correctly and added the start and end screens. Game now has solid base.
 * 			 Add gravity next. 
 * Jan 16th: Added gravity and acceleration! Make more enemies.
 * Jan 17th: Added sound to the jetpack. Next finish the enemies and powerups, and add a theme song.
 * Jan 18th: Added Bowser enemy and changed faulty koopa gif. Commented all code. Created functioning star powerup. Last step is get fire flower powerup
 * 			 to work and add theme song. Also fixed start screen.
 * Jan 19th: Added fireballs for fire flower powerup. Next step is make hitbox work for those.
 * Jan 20th: Made hitboxes work for fireballs and added sound effects for coins and star. Also added theme song and game over sound. Last step, remove backgrounds if possible.
 * 
 */
public class GameContent extends JPanel implements ActionListener{

	// Timer for animation purposes
	private Timer timer;

	//Keeps track of milliseconds
	private int milliseconds = 0;

	//Keeps track of seconds
	private int seconds = 0;

	//Keeps track of seconds without resetting for game speed purposes
	public int secondsNoResets = 0;

	//Theme song is created.
	private Sound themeSong = new Sound("Super Mario Bros Official Theme Song.wav");

	//Game over sound is created.
	private Sound gameOverSound = new Sound("Game Over.wav");

	//Coin sound created.
	private Sound coinSound = new Sound("super mario bros coin sound FX.wav");

	private Sound starSound = new Sound("Super Mario Galaxy Rainbow Star Invincibility Music.wav");

	//Controls spawn intervals for powerups
	private int powerupTimeSpawnMinimized;
	private int powerupTimeSpawnFireFlower;
	private int powerupTimeSpawnStar;

	//Controls the number of mushroom enemies per level and the spawn time for the mushrooms
	EnemyMushroom [] mushrooms;
	private int mushroomNum = 20;
	private int mushroomSpawn;
	private int mushroomCount = 0;

	//Controls the number of koopa enemies per level and the spawn time for the mushrooms
	EnemyTurtles [] koopas;
	private int koopaNum = 10;
	private int koopaSpawn;
	private int koopaCount = 0;
	
	//Controls the number of fireball enemies per level and the spawn time for the mushrooms
	EnemyFireBalls [] fireballs;
	private int fireballNum = 20;
	private int fireballSpawn;
	private int fireballCount = 0;
	
	//Controls the number of Bowser enemies per level and the spawn time for the mushrooms
	EnemyBowser [] bowsers;
	private int bowserNum = 10;
	private int bowserSpawn;
	private int bowserCount = 0;
	
	//Our main character
	private Character player;

	//The coin object.
	private Coin [] coin;
	private int coinCount = 0;
	public int coinsCollected = 0;

	//Controls game speed
	public double gameSpeed = 0.035;

	//Fire flower powerup
	private PowerupFireFlower fireFlower;
	public boolean fireFlowerOn = false;
	private int fireFlowerTime = 0;
	
	//Minimized powerup
	private PowerupMinimized minimized;
	public boolean minimizedOn = false;
	private int minimizedTime = 0;

	//Star powerup
	private PowerupStar star;
	private boolean starOn = false;
	private int starCount = 0;

	//The level which the game is on
	public int level = 1;

	//Keeps track of level changes
	private boolean levelChange = true;

	//Stores the values for all backgrounds for them to appear to be moving
	private int backgroundOneX = 1365;

	private int backgroundTwoX = 2730;
	
	private int startScreenX = 0;
	
	//Keeps track of if the game is over or not.
	private boolean gameOver = false;

	// Constructor for the game content
	public GameContent(int width, int height) {
		// Common settings for JPanel
		this.setFocusable(true);
		this.setBackground(Color.WHITE);
		this.setDoubleBuffered(true);
		this.setSize(width, height);
		
		//Loops the theme song for the whole game.
		themeSong.loop();

		// Creates an instance of the player's character.
		this.player = new Character();
		//Makes so that levelChange is set to true at the start
		levelChange = true;
		// Creates the Coin object array.
		this.coin = new Coin[45];
		//Creates the first enemy mushrooms array.
		mushrooms = new EnemyMushroom[mushroomNum];
		
		//Creates first enemy turtle array.
		koopas = new EnemyTurtles[koopaNum];
		
		//Creates first fireball array
		fireballs = new EnemyFireBalls[fireballNum];
		
		//Creates first bowser array
		bowsers = new EnemyBowser[bowserNum];
		
		// Adds a KeyListener to this JPanel object. 
		// A key listener is an object that intercepts keystrokes allowing
		// you to process them. The code for this key listener is at the 
		// bottom of this file.
		this.addKeyListener(new KeyListener());		

		// Creates a new Timer. The timer "goes off" every 5ms and calls
		// the actionPerformed() method. This allows us to update game content
		// rapidly.
		this.timer = new Timer(5, this);
		this.timer.start();
	}

	// The following method must be included so that the board is displayed on screen.
	// It's a required method that ensures the window is actively redrawn.
	public void addNotify() {
		super.addNotify(); 
	}

	// This method instructs Java how to "paint" the game content.
	// It uses the Graphics parameter g which is automatically sent to the method  
	// (don't worry about where it comes from) to draw on the screen.
	// The Graphics object is converted to a Graphics2d object which 
	// allows us to draw images.
	public void paint(Graphics g) {
		// Tells the JPanel class to paint itself. DO NOT MODIFY!
		super.paint(g);

		// Grabs the Graphics object to draw images. DO NOT MODIFY!
		Graphics2D g2d = (Graphics2D)g;

		//Draws start screen 
		if(startScreenX > -1366)
		{
			ImageIcon startScreen = new ImageIcon(this.getClass().getResource("mariobackgroundstartscreen.gif"));
			g2d.drawImage(startScreen.getImage(),  startScreenX,  0, this);
		}	
			
		//Draws the game to the screen if the game is not over.
		if (gameOver == false)
		{
			//Paints background to the screen.
			ImageIcon ii = new ImageIcon(this.getClass().getResource("mariobackground.gif"));
			g2d.drawImage(ii.getImage(),  backgroundOneX,  0, this);
	
			ii = new ImageIcon(this.getClass().getResource("mariobackground2.gif"));
			g2d.drawImage(ii.getImage(),  backgroundTwoX,  0, this);
	
			// Draws the character. 
			g2d.drawImage(this.player.getImage(minimizedOn), (int) Math.round(this.player.getX()), (int) Math.round(this.player.getY()), this);
	
			// Draws the coins.
			for (int count = 0; count < coinCount; count++)
			{
				if (this.coin[count].getVisibility()) {
					g2d.drawImage(this.coin[count].getImage(),  this.coin[count].getX(),  this.coin[count].getY(), this);
				}
			}
			
			//Draws the fire flower powerup if it has been created.
			if (fireFlower != null && this.fireFlower.getVisibility()) {
				g2d.drawImage(this.fireFlower.getImage(),  this.fireFlower.getX(),  this.fireFlower.getY(), this);
			}
	
			//Draws the minimized powerup if it has been created.
			if (minimized != null && this.minimized.getVisibility()) {
				g2d.drawImage(this.minimized.getImage(),  this.minimized.getX(),  this.minimized.getY(), this);
			}
	
			//Draws the star powerup if it has been created.
			if (star != null && this.star.getVisibility()) {
				g2d.drawImage(this.star.getImage(),  this.star.getX(),  this.star.getY(), this);
			}
			
			//Draws the enemy mushrooms that have been created.
			if (mushrooms != null)
			{
				for (int count = 0; count < mushroomCount; count++)
				{
					if (mushrooms[count] != null && this.mushrooms[count].getVisibility()) {
						g2d.drawImage(this.mushrooms[count].getImage(),  this.mushrooms[count].getX(),  this.mushrooms[count].getY(), this);
					}
				}
			}	
			
			//Draws the koopas that have been created.
			if (koopas != null)
			{
				for (int count = 0; count < koopaCount; count++)
				{
					if (koopas[count] != null && this.koopas[count].getVisibility()) {
						g2d.drawImage(this.koopas[count].getImage(),  this.koopas[count].getX(),  this.koopas[count].getY(), this);
					}
				}
			}
			
			//Draws the fireballs that have been created.
			if (fireballs != null)
			{
				for (int count = 0; count < fireballCount; count++)
				{
					if (fireballs[count] != null && this.fireballs[count].getVisibility()) {
						g2d.drawImage(this.fireballs[count].getImage(),  this.fireballs[count].getX(),  this.fireballs[count].getY(), this);
					}
				}
			}
			
			//Draws the bowsers that have been created.
			if (bowsers != null)
			{
				for (int count = 0; count < bowserCount; count++)
				{
					if (bowsers[count] != null && this.bowsers[count].getVisibility()) {
						g2d.drawImage(this.bowsers[count].getImage(),  this.bowsers[count].getX(),  this.bowsers[count].getY(), this);
					}
				}
			}	
			// Draws the missiles on screen.
			ArrayList<Fireballs> firedFireballs = this.player.getFiredFireballs();
			for (int i = 0; i < firedFireballs.size(); i++ ) {
				if (firedFireballs.get(i).getVisibility() == true) {
					g2d.drawImage(firedFireballs.get(i).getImage(), firedFireballs.get(i).getX(), firedFireballs.get(i).getY(), this);
				}
			}

		}
		//If game is over and user has lost, only the game over screen is painted and game over sound played.
		if (gameOver == true)
		{
			themeSong.stop();
			gameOverSound.loop();
			ImageIcon endScreen = new ImageIcon(this.getClass().getResource("gameover.jpg"));
			g2d.drawImage(endScreen.getImage(),  0,  0, this);
		}
		
		//Keeps constant track of the coins collected and seconds elapsed in the top left corner.
		g.setFont(new Font("Times New Roman", Font.BOLD, 26));
		g2d.drawString("Coins collected: " + coinsCollected, 5, 20);
		g2d.drawString("Seconds Elapsed: " + secondsNoResets, 5, 50);

		// Synchronizes the graphics state. DO NOT MODIFY!
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}


	public void actionPerformed(ActionEvent e) {
		// This method is called every 5ms. We will use it to tell
		// classes that they should update their images, locations, and
		// other similar content. For now, it is empty.

		//Keeps track of milliseconds
		milliseconds = milliseconds + 5;

		//Runs at the start of every level.
		if (levelChange)
		{
			//Makes so that the number of enemy mushrooms increases per level and resets other related variables.
			mushroomNum = mushroomNum + 5;
			mushroomSpawn = 1;
			mushrooms = new EnemyMushroom[mushroomNum];
			mushroomCount = 0;
			
			//Makes so that the number of koopas increases per level and resets other related variables.
			koopaNum = koopaNum + 5;
			koopaSpawn = 2;
			koopas = new EnemyTurtles[koopaNum];
			koopaCount = 0;
			
			//Makes so that the number of fire balls increases per level and resets other related variables.
			fireballNum = fireballNum + 5;
			fireballSpawn = 3;
			fireballs = new EnemyFireBalls[fireballNum];
			fireballCount = 0;
			
			//Makes so that the number of bowsers increases per level and resets other related variables.
			bowserNum = bowserNum + 5;
			bowserSpawn = 3;
			bowsers = new EnemyBowser[bowserNum];
			bowserCount = 0;
			
			//Sets the spawn times randomly for the level for each powerup.
			powerupTimeSpawnMinimized = ((int) (Math.random() * 30)+1);
			powerupTimeSpawnFireFlower =((int) (Math.random() * 30)+1);
			powerupTimeSpawnStar =((int) (Math.random() * 30)+1);
			
			//Changes level change trigger back to false.
			levelChange= false;

		}

		//Every second, the int seconds and int secondsNorResets are updated, and milliseconds is reset to 0.
		if (milliseconds == 1000 && gameOver == false)
		{
			seconds = seconds + 1;
			secondsNoResets = secondsNoResets + 1;
			milliseconds = 0;
			
			//Every second, if the minimized powerup is activated, one second is added to the minimized time count.
			if (minimizedOn == true)
			{
				minimizedTime++;
			}
			
			//Every second, if the star powerup is activated, one second is added to the minimized time count.
			if (starOn == true)
			{
				starCount++;
			}
			
			//Every second, if the fire flower powerup is activated, one second is added to the fire flower time count.			
			if (fireFlowerOn == true)
			{
				fireFlowerTime++;
			}
			//A new coin is created every second.
			coin[coinCount] = new Coin();
			coinCount++;
		}

		//Makes the backgrounds increase in speed moving to the left at a constantly increasing rate
		startScreenX = (int) (startScreenX - Math.round(secondsNoResets * gameSpeed));
		backgroundOneX = (int) (backgroundOneX - Math.round(secondsNoResets * gameSpeed));
		backgroundTwoX = (int) (backgroundTwoX - Math.round(secondsNoResets * gameSpeed));

		//Ensures that the backgrounds keep refreshing to the front
		if (backgroundOneX <= -1365)
		{
			backgroundOneX = backgroundTwoX + 1365;
		}
		if (backgroundTwoX <= -1365)
		{
			backgroundTwoX = backgroundOneX + 1365;
		}

		//Every 45 seconds, the level increases in the game
		if (seconds == 45)
		{
			level = level + 1;
			seconds = 0;
			//The level change trigger is set to true.
			levelChange = true;
			//Coin count is reset and a new coin array is created.
			coinCount = 0;
			coin = new Coin [45];
		}

		//If the mushroom spawn time is reached and the level is higher than 1, then this code runs.
		if (mushroomSpawn == seconds && level != 1)
		{		
			//Makes sure count is smaller than array size.
			if (mushroomCount < mushroomNum) {
				//Creates a new enemy mushroom and determines the next spawn time.
				mushrooms[mushroomCount] = new EnemyMushroom();
				mushroomCount++;
				mushroomSpawn = mushroomSpawn + (int) Math.round(45/mushroomNum);
				
			}
		}
		
		//If the koopa spawn time is reached and the level is higher than 2, then this code runs.
		if (koopaSpawn == seconds && level > 2)
		{		
			//Makes sure count is smaller than array size.
			if (koopaCount < koopaNum) {
				//Creates a new koopa array and determines the next spawn time.
				koopas[koopaCount] = new EnemyTurtles();
				koopaCount++;
				koopaSpawn = koopaSpawn + (int) Math.round(45/koopaNum);
				
			}
		}
		
		//If the fireball spawn time is reached and the level is higher than 3 then this code runs.
		if (fireballSpawn == seconds && level > 3)
		{		
			//Makes sure count is smaller than array size.
			if (fireballCount < fireballNum) {
				fireballs[fireballCount] = new EnemyFireBalls();
				fireballCount++;
				fireballSpawn = fireballSpawn + (int) Math.round(45/fireballNum);
				
			}
		}
		
		//If Bowser spawn time is reached and the level is higher than 3 then this code runs.
		if (bowserSpawn == seconds && level > 3)
		{		
			//Makes sure count is smaller than array size.
			if (bowserCount < bowserNum) {
				bowsers[bowserCount] = new EnemyBowser();
				bowserCount++;
				bowserSpawn = bowserSpawn + (int) Math.round(45/bowserNum);		
			}
		}

		//Updates the powerups based on the level.
		//Creates minimized powerup level 2 onwards.
		if (level >= 2)
		{
			//Creates powerup if spawn time reached.
			if (seconds == powerupTimeSpawnMinimized)
			{
				this.minimized = new PowerupMinimized();
			}
			//Moves the minimized powerup.
			if (minimized != null)
			{
				this.minimized.move(gameSpeed, secondsNoResets);
			}
		}
		//Once minimized is active for 15 seconds, it is turned off.
		if (minimizedTime >= 15)
		{
			minimizedOn = false;
			minimizedTime = 0;
		}
		
		//Creates fire flower powerup level 3 onwards.
		if (level >= 3)
		{
			//Creates powerup if spawn time reached.
			if (seconds == powerupTimeSpawnFireFlower)
			{
				this.fireFlower = new PowerupFireFlower();
			}
			//Moves fire flower powerup.
			if (fireFlower != null)
			{
				this.fireFlower.move(gameSpeed, secondsNoResets);
			}
		}
		//Once fire flower has been active for 15 seconds, it is turned off.
		if (fireFlowerTime>= 15)
		{
			fireFlowerOn = false;
			fireFlowerTime = 0;
		}
		//Creates star powerup level 4 onwards
		if (level >= 4)
		{
			//Creates powerup if spawn time reached.
			if (seconds == powerupTimeSpawnStar)
			{
				this.star = new PowerupStar();
			}
			//Moves star powerup.
			if (star != null)
			{
				this.star.move(gameSpeed, secondsNoResets);
			}
		}
		//Once star has been active for 15 seconds, it is turned off.
		if (starCount >= 15)
		{
			starOn = false;
			starCount = 0;
			starSound.stop();
			themeSong.loop();
		}

		
		// Updates the player's position.
		player.move();
		
		// Update the coins' locations.
		for (int count = 0; count < coinCount; count++)
		{
			this.coin[count].move(gameSpeed, secondsNoResets);
		}
		
		//Updates the mushrooms' locations.
		for (int count = 0; count < mushroomCount; count++)
		{
			mushrooms[count].move(gameSpeed, secondsNoResets);
		}
		
		//Updates the koopas' locations.
		for (int count = 0; count < koopaCount; count++)
		{
			koopas[count].move(gameSpeed, secondsNoResets);
		}
		
		//Updates the fireballs' locations.
		for (int count = 0; count < fireballCount; count++)
		{
			fireballs[count].move(gameSpeed, secondsNoResets);
		}
		
		//Updates the bowsers' locations.
		for (int count = 0; count < bowserCount; count++)
		{
			bowsers[count].move(gameSpeed, secondsNoResets);
		}
		
		// Check for collisions
		checkForCollisions();

		// Tells the JPanel to repaint itself since object positions
		// may have changed.
		repaint();  
	}

	// Method that checks for collisions and reacts accordingly.
	private void checkForCollisions() {
		// Get the hitbox for the Character.
		Rectangle charHitbox = this.player.getHitbox();

		// Get the hitbox for the coins.
		for (int count = 0; count < coinCount; count++)
		{
			Rectangle coinHitbox = this.coin[count].getHitbox();

			// Check to make sure the coin is visible.
			if (this.coin[count].getVisibility() == true) {
				
				// Check if the two objects collided.
				if (charHitbox.intersects(coinHitbox) && gameOver == false) {
					
					// Set the coin to be invisible.
					coinSound.play();
					this.coin[count].setVisible(false);
					coinsCollected++;
				}
			}
		}
		
		//Runs if minimized is created.
		if (minimized != null)
		{
			//Gets hitbox for minimized.
			Rectangle minimizedHitbox = this.minimized.getHitbox();
			
			// Check to make sure the minimized is visible.
			if (this.minimized.getVisibility() == true) {
				
				// Check if the two objects collided.
				if (charHitbox.intersects(minimizedHitbox)) {

					// Set the minimized to be invisible.
					this.minimized.setVisible(false);
					minimizedOn = true;
				}
			}
		}

		if (fireFlower != null)
		{
			Rectangle fireFlowerHitbox = this.fireFlower.getHitbox();
			// Check to make sure the fireFlower is visible.
			if (this.fireFlower.getVisibility() == true) {
				// Check if the two objects collided.

				if (charHitbox.intersects(fireFlowerHitbox)) {

					// Set the fireFlower to be invisible.
					fireFlowerOn = true;
					this.fireFlower.setVisible(false);
				}
			}
		}

		//Runs if star is created.
		if (star != null)
		{
			//Gets the hitbox for the star.
			Rectangle starHitbox = this.star.getHitbox();
			
			// Check to make sure the star is visible.
			if (this.star.getVisibility() == true) {
				
				// Check if the two objects collided.
				if (charHitbox.intersects(starHitbox)) {

					// Set the star to be invisible and turns it on.
					this.star.setVisible(false);
					starOn = true;
					themeSong.stop();
					starSound.loop();
				}
			}
		}
		
		//Loops through all the enemy mushrooms.
		for (int count = 0; count < mushroomCount; count++)
		{
			//Runs if mushroom is created.
			if (mushrooms[count] != null)
			{
				//Gets the hitbox for the mushroom.
				Rectangle mushroomsHitbox = this.mushrooms[count].getHitbox();
				
				// Check to make sure the mushroom is visible.
				if (this.mushrooms[count].getVisibility() == true) {

					// Check if the two objects collided.
					if (charHitbox.intersects(mushroomsHitbox)) {

						// Set the mushroom to be invisible.
						this.mushrooms[count].setVisible(false);
						if (starOn == false)
						{
							//Shows end screen
							gameOver = true;
						}
					}
					//Checks if enemy intersects with a fireball and should be eliminated.
					if (fireFlowerOn == true)
					{
						ArrayList<Fireballs> fired = player.getFiredFireballs();
						for (int i = 0; i < fired.size(); i++) {
							Fireballs f = fired.get(i);
							Rectangle fireballHitbox = f.getHitbox();
							if (mushroomsHitbox.intersects(fireballHitbox))
							{
								mushrooms[count].setVisible(false);
								f.setVisible(false);
							}
						}
					}
				}
			}
		}
		
		//Loops through all the koopas.
		for (int count = 0; count < koopaCount; count++)
		{
			//Runs if koopa is created.
			if (koopas[count] != null)
			{
				//Gets the koopa's hitbox.
				Rectangle koopasHitbox = this.koopas[count].getHitbox();
				
				// Check to make sure the koopa is visible.
				
				if (this.koopas[count].getVisibility() == true) {
					
					// Check if the two objects collided.
					if (charHitbox.intersects(koopasHitbox)) {

						// Set the koopa to be invisible.
						this.koopas[count].setVisible(false);
						if (starOn == false)
						{
							//Shows end screen
							gameOver = true;
						}
					}
					//Checks if enemy intersects with a fireball and should be eliminated.
					if (fireFlowerOn == true)
					{
						ArrayList<Fireballs> fired = player.getFiredFireballs();
						for (int i = 0; i < fired.size(); i++) {
							Fireballs f = fired.get(i);
							Rectangle fireballHitbox = f.getHitbox();
							if (koopasHitbox.intersects(fireballHitbox))
							{
								koopas[count].setVisible(false);
								f.setVisible(false);
							}
						}
					}
				}
			}
		}
		
		//Loops through all of the fire balls.
		for (int count = 0; count < fireballCount; count++)
		{
			//Runs if fireball is created.
			if (fireballs[count] != null)
			{
				//Gets the hitbox for the fireball.
				Rectangle fireballHitbox = this.fireballs[count].getHitbox();
				
				// Check to make sure the fireball is visible.
				if (this.fireballs[count].getVisibility() == true) {

					// Check if the two objects collided.
					if (charHitbox.intersects(fireballHitbox)) {

						// Set the fireball to be invisible.
						this.fireballs[count].setVisible(false);
						if (starOn == false)
						{
							//Shows end screen
							gameOver = true;
						}
					}
					//Checks if enemy intersects with a fireball and should be eliminated.
					if (fireFlowerOn == true)
					{
						ArrayList<Fireballs> fired = player.getFiredFireballs();
						for (int i = 0; i < fired.size(); i++) {
							Fireballs f = fired.get(i);
							Rectangle fireballsHitbox = f.getHitbox();
							if (fireballHitbox.intersects(fireballsHitbox))
							{
								fireballs[count].setVisible(false);
								f.setVisible(false);
							}
						}
					}
				}
			}
		}
		
		//Loops through all the bowsers.
		for (int count = 0; count < bowserCount; count++)
		{
			//Runs if Bowser has been created.
			if (bowsers[count] != null)
			{
				//Gets Bowser's hitbox.
				Rectangle bowserHitbox = this.bowsers[count].getHitbox();
				
				// Check to make sure Bowser is visible.
				if (this.bowsers[count].getVisibility() == true) {

					// Check if the two objects collided.
					if (charHitbox.intersects(bowserHitbox)) {

						// Set Bowser to be invisible.
						this.bowsers[count].setVisible(false);
						if (starOn == false)
						{
							//Shows end screen
							gameOver = true;
						}
					}
				}
				//Checks if enemy intersects with a fireball and should be eliminated.
				if (fireFlowerOn == true)
				{
					ArrayList<Fireballs> fired = player.getFiredFireballs();
					for (int i = 0; i < fired.size(); i++) {
						Fireballs f = fired.get(i);
						Rectangle fireballHitbox = f.getHitbox();
						if (bowserHitbox.intersects(fireballHitbox))
						{
							bowsers[count].setVisible(false);
							f.setVisible(false);
						}
					}
				}
			}
		}


	}


	// Private class -- this is a class that outside Java files
	// cannot see. It's sole purpose is to intercept keystrokes
	// and send them to the player class for processing.
	// DO NOT MODIFY!
	private class KeyListener extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			player.keyPressed(e, fireFlowerOn, gameOver);
		}
	}

}
