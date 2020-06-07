package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * This class extends game object. It has a random starting velocity and a
 * default radius and it will bounce off of other monsters, go through
 * platforms, and will hurt the hero on contact
 * 
 * @author purdydj and thenelltj
 *
 */
public class Monster extends GameObject {

	private int RADIUS = 70;
	private int[] speeds = {-10, -7, -6, -5, 5, 6, 7, 10};
	private boolean isDead = false;
	public Monster(int x, int y) {
		super(x, y);
		Random r = new Random();
		setVelX(speeds[r.nextInt(7)]);
		setVelY(speeds[r.nextInt(7)]);
	}

	public void drawOn(Graphics2D g) {
		Image img;

		try {
			img = ImageIO.read(new File("Imports/Sprites/Covid.png"));
			g.drawImage(img, this.getX(), this.getY(), RADIUS, RADIUS, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Dimension2D size) {
		this.setX(this.getX() + this.getVelX());
		this.setY(this.getY() + this.getVelY());
		if (this.getX() + 70 > 1905 || this.getX() < 0)
			this.setVelX(-this.getVelX());
		if (this.getY() + 70 > 1065 || this.getY() < 0)
			this.setVelY(-this.getVelY());

	}

	public void bounce(Monster other) {
		this.setVelX(this.getX() - other.getX());
		this.setVelY(this.getY() - other.getY());
		this.normalizeVelocity();
	}

	public boolean overlapsWith(Monster other) {
		int xDiff = (this.getX() + this.RADIUS / 2) - (other.getX() + other.RADIUS / 2);
		int yDiff = (this.getY() + this.RADIUS / 2) - (other.getY() + other.RADIUS / 2);
		double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
		return this.RADIUS + other.RADIUS >= 2 * distance;
	}

	public int getRadius() {
		return this.RADIUS;
	}
	
	public void setIsDead(boolean dead) {
		this.isDead = dead;
	}
	
	public boolean getIsDead() {
		return this.isDead;
	}
}
