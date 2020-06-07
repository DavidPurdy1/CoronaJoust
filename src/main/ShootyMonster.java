package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Just like monster except a different color, a different movement path, and
 * shoots bullets. This also includes the inner class Bullet. Bullet follows
 * game object and updates, it has a method for collision with the hero but
 * cannot be protected if the spear is active
 * 
 * @author purdydj & theneltj
 *
 */

public class ShootyMonster extends Monster {

	private boolean isShoot = false;
	private Image shootyTexture;
	
	public ShootyMonster(int x, int y) {
		super(x, y);
		Random r = new Random();
		setVelX(r.nextInt(10) - 5);
		setVelY(r.nextInt(10) - 5);
		
		try {
			shootyTexture = ImageIO.read(new File("Imports/Sprites/CovidShooty.png"));
		}
		catch (IOException e) {}
	}

	@Override
	public void drawOn(Graphics2D g) {
		
		g.drawImage(shootyTexture, getX(), getY(), getRadius(), getRadius(), this);

	}

	public Bullet shoot() {
		setIsShoot(false);
		return new Bullet(this.getX(), this.getY(), this.getVelX(), this.getVelY());
	}

	public void setIsShoot(boolean b) {
		isShoot = b;
	}

	public boolean getIsShoot() {
		return isShoot;
	}
	
	public void changeDirection() {
		Random r = new Random();
		setVelX(r.nextInt(10) - 5);
		setVelY(r.nextInt(10) - 5);
	}
	
	public void update(Dimension2D size) {
		this.setX(this.getX() + this.getVelX());
		this.setY(this.getY() + this.getVelY());
		if (this.getX() + 70 > 1905 || this.getX() < 0)
			this.setVelX(-this.getVelX());
		if (this.getY() + 70 > 1065 || this.getY() < 0)
			this.setVelY(-this.getVelY());
		
		Random r = new Random();
		
		if (r.nextInt(70) == 35) {
			isShoot = true;
		}
		
		if (r.nextInt(50) == 25) {
			changeDirection();
		}

	}


}
