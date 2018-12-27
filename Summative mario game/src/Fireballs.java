import java.awt.Image;

import java.awt.Rectangle;

import javax.swing.ImageIcon;

// Class representing a Fireballs. 
public class Fireballs {

	// Location and image settings.
	private int x, y;
	private Image image;
	boolean visible;
	private int width, height;

	// Constant that specifies the Fireball's speed.
	private final int Fireballs_SPEED = 4;

	public Fireballs(int x, int y) {

		ImageIcon ii = new ImageIcon(this.getClass().getResource("Fire-bar.gif"));
		image = ii.getImage();
		visible = true;
		width = image.getWidth(null);
		height = image.getHeight(null);
		this.x = x;
		this.y = y;
	}


	public Image getImage() {
		return image;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean getVisibility() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	// Returns the hitbox associated with the EnemyMushroom.
	public Rectangle getHitbox() {
		return new Rectangle(x, y, width, height);
	}

	public void move() {
		x = x + Fireballs_SPEED;
		if (x > Game.W_WIDTH)
			visible = false;
	}
}
