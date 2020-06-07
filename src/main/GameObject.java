package main;

import java.awt.geom.Dimension2D;

import javax.swing.JComponent;

/** 
 * This is the superclass to the Monster, Hero, CoronaItem,
 * Platform, and Bullet class. It has all the basic methods
 * for objects to be displayed in the game.
 * 
 * @author theneltj
 *
 */
public class GameObject extends JComponent {
	private int x, y, velX, velY;

	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void update(Dimension2D size) { //this is default update 
		x += getVelX();
		y += getVelY();
		if (this.getX() + 70 > 1905 || this.getX() < 0)
			this.setVelX(-this.getVelX());
		if (this.getY() + 70 > 1065 || this.getY() < 0)
			this.setVelY(-this.getVelY());
	}


	public void normalizeVelocity() {
		// move down, right if velocity is set to zero
		if (getVelX() == 0 && getVelY() == 0) {
			setVelX(1);
			setVelY(1);
		}

		// normalize vector
		double vectorLength = Math.sqrt(getVelX() * getVelX() + getVelY() * getVelY());
		setVelX((int) (getVelX() / vectorLength * 10 * 2));
		setVelY((int) (getVelY() / vectorLength * 10 * 2));
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getVelX() {
		return this.velX;
	}

	public int getVelY() {
		return this.velY;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}
}
