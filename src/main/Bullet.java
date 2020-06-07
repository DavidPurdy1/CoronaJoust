package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.Random;

/**
 * This class is responsible for our shootymonsters bullets. It creates the
 * bullet as a green circle to be shot at the player.
 * 
 * @author theneltj & purdydj
 *
 */
public class Bullet extends GameObject {

	private int RADIUS = 40;
	private boolean deleteMe = false;

	public Bullet(int x, int y, int velX, int velY) {
		super(x, y);
		setVelX(velX);
		setVelY(velY);
	}

	public void drawOn(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillOval(getX(), getY(), RADIUS, RADIUS);
		g.setColor(Color.BLACK);
	}

	@Override
	public void update(Dimension2D size) {
		this.setX(this.getX() + this.getVelX());
		this.setY(this.getY() + this.getVelY());
		if (this.getX() + 70 > 1905 || this.getX() < 0) {
			this.setVelX(-this.getVelX());
			deleteMe = true;
		}
		if (this.getY() + 70 > 1065 || this.getY() < 0) {
			this.setVelY(-this.getVelY());
			deleteMe = true;
		}
	}

	public void bounce(Monster other) {
		this.setVelX(this.getX() - other.getX());
		this.setVelY(this.getY() - other.getY());
		this.normalizeVelocity();
	}

	public int getRadius() {
		return this.RADIUS;
	}

	public boolean getDeleteMe() {
		return this.deleteMe;
	}
}