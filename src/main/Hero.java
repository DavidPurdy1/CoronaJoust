package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Listener.StartTune;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * The hero extends GameObject. It is controlled by key inputs in HeroListener
 * and updated in this class. He will start with 3 lives, can move left, right,
 * and double jump. He also has a spear that is activated with space bar that
 * kills Corona.
 * 
 * @author purdydj and theneltj
 *
 */

public class Hero extends GameObject {

	private int RADIUS = 100;

	private Image chadIdleLeft1, chadIdleLeft2, chadIdleRight1, chadIdleRight2;
	private Image chadJumpLeft, chadJumpRight;
	private Image chadStabLeft, chadStabRight;
	private Image chadJumpStabLeft, chadJumpStabRight;
	private Image chadRunLeft1, chadRunLeft2, chadRunLeft3;
	private Image chadRunRight1, chadRunRight2, chadRunRight3;
	private Image sprayLeft, sprayRight;
	private Image fullHeart, emptyHeart;
	private Image spearActive, spearInactive;
	private Image grave;
	private Image img;

	private int lives = 3;
	private boolean isSpearActive;
	private boolean hasInvincibility = false;
	private boolean isDead = false;
	private boolean isOnGround = false;
	private boolean isGoingRight = true;
	private int numJumps = 2;
	private int imageIndex = 0;
	private boolean isStabReady = true;

	private ArrayList<Image> chadIdleRightAnimation = new ArrayList<Image>();
	private ArrayList<Image> chadIdleLeftAnimation = new ArrayList<Image>();
	private ArrayList<Image> chadRunRightAnimation = new ArrayList<Image>();
	private ArrayList<Image> chadRunLeftAnimation = new ArrayList<Image>();

	public Hero(int x, int y) {
		super(x, y);
		Timer changer = new Timer();
		changer.schedule(new imageCycler(), 1);// cycles through the images in class at the bottom

		try { // this reads the different image files for the hero
			chadIdleLeft1 = ImageIO.read(new File("Imports/Sprites/ChadIdleLeft1.png"));
			chadIdleLeft2 = ImageIO.read(new File("Imports/Sprites/ChadIdleLeft2.png"));
			chadIdleRight1 = ImageIO.read(new File("Imports/Sprites/ChadIdleRight1.png"));
			chadIdleRight2 = ImageIO.read(new File("Imports/Sprites/ChadIdleRight2.png"));

			chadIdleLeftAnimation.add(chadIdleLeft1);
			chadIdleLeftAnimation.add(chadIdleLeft2);
			chadIdleLeftAnimation.add(chadIdleLeft1);
			chadIdleLeftAnimation.add(chadIdleLeft2);

			chadIdleRightAnimation.add(chadIdleRight1);
			chadIdleRightAnimation.add(chadIdleRight2);
			chadIdleRightAnimation.add(chadIdleRight1);
			chadIdleRightAnimation.add(chadIdleRight2);

			chadJumpLeft = ImageIO.read(new File("Imports/Sprites/ChadJumpLeft.png"));
			chadJumpRight = ImageIO.read(new File("Imports/Sprites/ChadJumpRight.png"));

			chadStabLeft = ImageIO.read(new File("Imports/Sprites/ChadStabLeft.png"));
			chadStabRight = ImageIO.read(new File("Imports/Sprites/ChadStabRight.png"));
			chadJumpStabLeft = ImageIO.read(new File("Imports/Sprites/ChadJumpStabLeft.png"));
			chadJumpStabRight = ImageIO.read(new File("Imports/Sprites/ChadJumpStabRight.png"));

			chadRunLeft1 = ImageIO.read(new File("Imports/Sprites/ChadRunLeft1.png"));
			chadRunLeft2 = ImageIO.read(new File("Imports/Sprites/ChadRunLeft2.png"));
			chadRunLeft3 = ImageIO.read(new File("Imports/Sprites/ChadRunLeft3.png"));

			chadRunLeftAnimation.add(chadRunLeft1);
			chadRunLeftAnimation.add(chadRunLeft2);
			chadRunLeftAnimation.add(chadRunLeft3);
			chadRunLeftAnimation.add(chadRunLeft2);

			chadRunRight1 = ImageIO.read(new File("Imports/Sprites/ChadRunRight1.png"));
			chadRunRight2 = ImageIO.read(new File("Imports/Sprites/ChadRunRight2.png"));
			chadRunRight3 = ImageIO.read(new File("Imports/Sprites/ChadRunRight3.png"));

			chadRunRightAnimation.add(chadRunRight1);
			chadRunRightAnimation.add(chadRunRight2);
			chadRunRightAnimation.add(chadRunRight3);
			chadRunRightAnimation.add(chadRunRight2);

			sprayLeft = ImageIO.read(new File("Imports/Sprites/SprayLeft.png"));
			sprayRight = ImageIO.read(new File("Imports/Sprites/SprayRight.png"));
			fullHeart = ImageIO.read(new File("Imports/Sprites/FullHeart.png"));
			emptyHeart = ImageIO.read(new File("Imports/Sprites/EmptyHeart.png"));
			grave = ImageIO.read(new File("Imports/Sprites/Grave.png"));
			
			spearActive = ImageIO.read(new File("Imports/Sprites/SpearActive.png"));
			spearInactive = ImageIO.read(new File("Imports/Sprites/SpearInactive.png"));
			// image reading ends here, above are all the images needed for hero when he
			// moves

			// this imports our custom corona font
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Imports/Fonts/EnglishTowne.ttf")));
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
			System.out.println("ERROR ON IMAGE READ CHAD OR FONT");
		}

	}

	@Override
	public void update(Dimension2D size) { // this is default update
		if (!isDead) {
			setX(this.getX() + getVelX());
			setY(this.getY() + getVelY());

			if (this.getX() + 100 > 1905)
				setX(1905 - 105);
			if (this.getX() < 0)
				setX(5);

			if (this.getY() + 100 > 1300)
				isDead = true;
			if (this.getY() < 0)
				setY(5);
		}
	}

	public void drawOn(Graphics2D g) {
		int xShift = 0;

		//Draws Hearts!
		for (int i = 0; i < this.lives; i++) {
			g.drawImage(fullHeart, 1780 + xShift, 30, 30, 28, this);// runs through the lives displaying them
			xShift = xShift + 40;
		}
		for (int i = 0; i < 3 - this.lives; i++) {
			g.drawImage(emptyHeart, 1780 + xShift, 30, 30, 28, this);
			xShift = xShift + 40;
		}
		
		if (isStabReady) {
			g.drawImage(spearActive, 1780, 100, 96, 96, this);
		}
		
		if (!isStabReady) {
			g.drawImage(spearInactive, 1780, 100, 96, 96, this);
		}
		

		if (!isDead) {// all of this below changes the image based on which direction he is going

			if (this.getVelX() == 0) {
				if (isGoingRight)
					img = chadIdleRightAnimation.get(imageIndex);
				if (!isGoingRight)
					img = chadIdleLeftAnimation.get(imageIndex);
			}

			if (this.getVelX() > 0) {
				img = chadRunRightAnimation.get(imageIndex);
				isGoingRight = true;
			}
			if (this.getVelX() < 0) {
				img = chadRunLeftAnimation.get(imageIndex);
				isGoingRight = false;
			}
			if (!isOnGround && isGoingRight)
				img = chadJumpRight;

			if (!isOnGround && !isGoingRight)
				img = chadJumpLeft;

			if (isSpearActive && isOnGround && isGoingRight) {
				img = chadStabRight;
				g.drawImage(sprayRight, this.getX() + 100, this.getY(), 100, 100, this);
			}

			if (isSpearActive && isOnGround && !isGoingRight) {
				img = chadStabLeft;
				g.drawImage(sprayLeft, this.getX() - 100, this.getY(), 100, 100, this);
			}

			if (isSpearActive && !isOnGround && isGoingRight) {
				img = chadJumpStabRight;
				g.drawImage(sprayRight, this.getX() + 100, this.getY(), 100, 100, this);
			}

			if (isSpearActive && !isOnGround && !isGoingRight) {
				img = chadJumpStabLeft;
				g.drawImage(sprayLeft, this.getX() - 100, this.getY(), 100, 100, this);
			}

			g.drawImage(img, this.getX(), this.getY() + 5, RADIUS, RADIUS, this);// changed getwidth to radius
		}

		if (isDead) {// makes the death animation
			g.drawImage(grave, this.getX(), this.getY(), 100, 100, this);
			String deathString = "You have been infected!!";
			g.setFont(new Font("English Towne", 50, 50));
			g.drawString(deathString, 1920 / 2 - 230, 100);
			g.setFont(new Font("English Towne", 70, 70));
			g.drawString("Game Over", 1920 / 2 - 150, 900);
		}
	}

	public int getRadius() {
		return this.RADIUS;
	}

	public void bounce(Monster other) {
		this.setVelX(this.getX() - other.getX());
		this.setVelY(this.getY() - other.getY());
		this.normalizeVelocity();
		if (isSpearActive) {
		} else if (!isDead) {
			subtractLife();
			hasInvincibility = true; // makes him so he doesn't repeatedly get hit and die
			Timer waiter = new Timer();
			waiter.schedule(new waiter(), 3000);
		}

	}

	public void bounce(Bullet other) {
		this.setVelX(this.getX() - other.getX());
		this.setVelY(this.getY() - other.getY());
		this.normalizeVelocity();
		subtractLife();
		if (!isDead) {
			hasInvincibility = true;
			Timer waiter = new Timer();
			waiter.schedule(new waiter(), 3000);
		}
	}

	public boolean overlapsWith(Monster other) {
		int xDiff = 0;
		if (isGoingRight && isSpearActive)
			xDiff = (this.getX() + 100 + this.RADIUS / 2) - (other.getX() + other.getRadius() / 2);
		if (!isGoingRight && isSpearActive)
			xDiff = (this.getX() - 100 + this.RADIUS / 2) - (other.getX() + other.getRadius() / 2);
		if (!isSpearActive)
			xDiff = (this.getX() + this.RADIUS / 2) - (other.getX() + other.getRadius() / 2);
		int yDiff = (this.getY() + this.RADIUS / 2) - (other.getY() + other.getRadius() / 2);
		double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

		return this.RADIUS + other.getRadius() >= 2 * distance;
	}

	public boolean overlapsWith(Bullet other) {
		int xDiff = (this.getX() + this.RADIUS / 2) - (other.getX() + other.getRadius() / 2);
		int yDiff = (this.getY() + this.RADIUS / 2) - (other.getY() + other.getRadius() / 2);
		double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

		return this.RADIUS + other.getRadius() >= 2 * distance;
	}

	public boolean overlapsWith(CoronaItem other) {
		int xDiff = (this.getX() + this.RADIUS / 2) - (other.getX() + other.getRadius() / 2);
		int yDiff = (this.getY() + this.RADIUS / 2) - (other.getY() + other.getRadius() / 2);
		double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

		return this.RADIUS + other.getRadius() >= 2 * distance;
	}

	public boolean overlapsWith(Platform other) {
		boolean result = (this.getX() + 100 >= other.getX() && this.getX() <= other.getX() + other.getWidth()
				&& this.getY() + 100 >= other.getY() && this.getY() <= other.getY() + 10);

		if (result) {//makes it so he can only jump twice 
			numJumps = 2;
			isOnGround = true;
		}
		return result;
	}

	public void subtractLife() {
		if (!isDead) {

			if (lives > 1) {
				this.lives--;
			} else {
				this.lives--;
				dead();
				try {
					AudioStream sound = new AudioStream(new FileInputStream(new File("Imports/Sounds/Death.wav")));
					AudioPlayer.player.start(sound);
				} catch (IOException e) {
				}
				hasInvincibility = true;
			}
		}
	}

	public void dead() {
		isDead = true;
	}

	public boolean getIsSpearActive() {
		return isSpearActive;
	}

	public void setIsSpearActiveTrue() {
		if (isStabReady) {
			isSpearActive = true;
			isStabReady = false;

			Timer wait1 = new Timer(); //has a cooldown time for the spear
			wait1.schedule(new spearReturn(), 400);

			Timer wait2 = new Timer();
			wait2.schedule(new spearCooldown(), 2000);
		}
	}

	public boolean getHasInvincibility() {
		return hasInvincibility;
	}

	public boolean getIsDead() {
		return isDead;
	}

	public int getNumJumps() {
		return numJumps;
	}

	public void decreaseNumJumps() {
		if (numJumps > 0)
			numJumps--;
	}

	public void setIsOnGroundFalse() {
		isOnGround = false;
	}

	class waiter extends TimerTask {
		public void run() {
			hasInvincibility = false;
		}
	}

	class imageCycler extends TimerTask {
		public void run() {
			if (imageIndex < 4)
				imageIndex++;
			if (imageIndex >= 4)
				imageIndex = 0;
			Timer changer = new Timer();
			changer.schedule(new imageCycler(), 200);
		}
	}

	class spearReturn extends TimerTask {
		public void run() {
			isSpearActive = false;
		}
	}

	class spearCooldown extends TimerTask {
		public void run() {
			isStabReady = true;
		}
	}
}