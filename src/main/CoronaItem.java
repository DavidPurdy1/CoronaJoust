package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

/**
 * This class constructs our coronaItem which is picked
 * up in game to receive points. They drop down to the nearest
 * platform after killing a monster if it isn't picked up it turns back to a monster 
 * 
 * @author theneltj & purdydj
 *
 */
public class CoronaItem extends GameObject {
	private int RADIUS = 30;
	private int[] speeds = {-10, -7, -6, -5, 5, 6, 7, 10};
	private boolean isDead = false;
	private boolean isFalling = true;
	private boolean isNowMonster = false;
	private Image img;
	
	
	public CoronaItem(int x, int y) {
		super(x, y);
		Timer recovery = new Timer();
		recovery.schedule(new TurnBack(), 5000);
		try {
			img =  ImageIO.read(new File("Imports/Sprites/Bottle.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR ON IMAGE READ BOTTLE");
		}
	}

	public void drawOn(Graphics2D g) {
		g.setColor(new Color(242, 142, 28));
		g.drawImage(img, this.getX(), this.getY(), 12*2, 40*2, this);
		g.setColor(Color.BLACK);
	}

	@Override
	public void update(Dimension2D size) {
		
		
		if (this.getVelY() <= 10 && isFalling)
			this.setVelY(this.getVelY() + 1);
		this.setX(this.getX() + this.getVelX());
		this.setY(this.getY() + this.getVelY());
		
		if (this.getY() + 90 > 1065 || this.getY() < 0) {
			setIsFalling(false);
			this.setVelY(0);
		}
	}

	public int getRadius() {
		return this.RADIUS;
	}
	
	
	public boolean getIsDead() {
		return this.isDead;
	}
	
	public boolean getIsFalling() {
		return this.isFalling;
	}
	
	public void setIsFalling(boolean fall) {
		this.isFalling = fall;
		this.setVelY(0);
	}
	
	public boolean getIsTurnBack() { //makes it a monster
		return this.isNowMonster;
	}
	
	
	class TurnBack extends TimerTask { //timer tells it to turn to a monster
		public void run() {
			isNowMonster = true;
		}
	}
}
