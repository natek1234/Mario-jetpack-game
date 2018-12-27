import java.awt.Image;

import java.awt.Rectangle;

import javax.swing.ImageIcon;

// Class representing a missile. 
public class Missile {

	// Location and image settings.
	private int x, y;
	private Image image;
	boolean visible;
	private int width, height;

	// Constant that specifies the missile's speed.
	private final int MISSILE_SPEED = 4;

	public Missile(int x, int y) {

		ImageIcon ii = new ImageIcon(this.getClass().getResource("missile.png"));
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

	public void move() {
		x = x + MISSILE_SPEED;
		if (x > Game.W_WIDTH)
			visible = false;
	}
}
