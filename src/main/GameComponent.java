package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * GameComponent is responsible for rendering our in game objects and checking
 * for object to object interaction. It plays sounds and updates all objects.
 * 
 * @author theneltj & purdydj
 *
 */
public class GameComponent extends JComponent {

	private ArrayList<Platform> rects = new ArrayList<Platform>();
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	private ArrayList<ShootyMonster> shootymonsters = new ArrayList<ShootyMonster>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<CoronaItem> bottles = new ArrayList<CoronaItem>();
	private Hero chad;
	private LevelConstructor ourLevel;
	private int score = 0;
	private String HITSOUND = "Hit";
	private String DRINKSOUND = "Drink";
	private String BOTTLESOUND = "BottleLand";
	private String DEATHSOUND = "Death";
	private String SPLASHSOUND = "Splash";
	private String STABSOUND = "Stab";

	private boolean firstSong = true;
	private boolean isWin = false;
	private boolean isTimerSet = false;
	private int endGameCount = 0;

	public GameComponent(ArrayList<Platform> rects, Hero chad, ArrayList<Monster> monsters, ArrayList<Bullet> bullets,
			ArrayList<ShootyMonster> shootymonsters, LevelConstructor ourLevel) {
		this.rects = rects;
		this.monsters = monsters;
		this.chad = chad;
		this.bullets = bullets;
		this.shootymonsters = shootymonsters;
		this.ourLevel = ourLevel;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (Platform t : this.rects) {
			t.drawOn(g2);
		}
		for (Monster m : this.monsters) {
			m.drawOn(g2);
		}
		chad.drawOn(g2);

		for (Bullet b : this.bullets) {
			b.drawOn(g2);
		}
		for (ShootyMonster s : this.shootymonsters) {
			s.drawOn(g2);
		}
		for (CoronaItem c : this.bottles) {
			c.drawOn(g2);
		}

		try { // imports font
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Imports/Fonts/EnglishTowne.ttf")));
		} catch (IOException | FontFormatException e) {
			System.out.println("error in Font in GameComponent");
		}
		g2.drawRect(1500, -10, 420, 100);
		String scoreString = "Corona Drank: " + Integer.toString(score);
		g2.setFont(new Font("English Towne", Font.PLAIN, 30));
		g2.drawString(scoreString, 1520, 50);

		if (isWin) { // condition for it you win
			System.out.println("You have won");
			String win = "You Have Survived The Pandemic !";
			g2.setFont(new Font("English Towne", Font.PLAIN, 40));
			g2.drawString(win, 1920 / 2 - 300, 100);

			String endScoreString = "Score: " + Integer.toString(this.score);
			g2.drawString(endScoreString, 1920 / 2, 150);

			if (!isTimerSet) { // creates a timer which counts down till the game resets
				isTimerSet = true;
				Timer wait = new Timer();
				wait.schedule(new endTime(), 1000);
			}
			String timeDisplay = "Time Left: " + Integer.toString(10 - endGameCount);
			g2.drawString(timeDisplay, 1920 / 2, 200);
		}
	}

	public void update() { //this is going to handle the update of most objects in the game
		try {
			if (shootymonsters.isEmpty() && monsters.isEmpty() && bottles.isEmpty() && ourLevel.getLevel() == 10) {
				isWin = true;
			}

			if (shootymonsters.isEmpty() && monsters.isEmpty() && bottles.isEmpty()) {
				if (ourLevel.getLevel() < 10) {
					ourLevel.setIsChanged(true);
					ourLevel.setLevel(ourLevel.getLevel() + 1);

					try {
						InputStream sound = new FileInputStream(new File("Imports/Sounds/NextLevel.wav"));
						AudioStream startsound = new AudioStream(sound);
						AudioPlayer.player.start(startsound);
					}

					catch (IOException ex) {
					}
				}
			}

			for (Platform p : this.rects) {
				p.update(this.getSize());
			}

			for (Monster m1 : this.monsters) {
				for (Monster m2 : this.monsters) {
					if (m1 != m2 && m1.overlapsWith(m2)) {
						m1.bounce(m2);
					}
				}
				m1.update(this.getSize());
			}

			for (ShootyMonster s1 : this.shootymonsters) {
				for (ShootyMonster s2 : this.shootymonsters) {
					if (s1 != s2 && s1.overlapsWith(s2)) {
						s1.bounce(s2);
					}
				}
				if (chad.overlapsWith(s1) && chad.getIsSpearActive()) {
					bottles.add(new CoronaItem(s1.getX(), s1.getY()));
					playSoundEffect(STABSOUND);
					shootymonsters.remove(s1);
				}

				s1.update(this.getSize());
				if (s1.getIsShoot()) {
					playSoundEffect(SPLASHSOUND);
					Random r = new Random();
					boolean isSprayFire = (r.nextInt(8) == 4);
					if (!isSprayFire) {
						// Creates a unit vector for shot
						double distancex = (chad.getX() + chad.getRadius() / 2) - s1.getX();
						double distancey = (chad.getY() + chad.getRadius() / 2) - s1.getY();
						double dist = Math.sqrt(distancex * distancex + distancey * distancey);

						double velx = (20 * distancex) / dist;
						double vely = (20 * distancey) / dist;
						bullets.add(new Bullet(s1.getX(), s1.getY(), (int) velx, (int) vely));
					} else {
						bullets.add(new Bullet(s1.getX(), s1.getY(), 0, 9));
						bullets.add(new Bullet(s1.getX(), s1.getY(), 7, 7));
						bullets.add(new Bullet(s1.getX(), s1.getY(), 9, 0));
						bullets.add(new Bullet(s1.getX(), s1.getY(), 7, -7));
						bullets.add(new Bullet(s1.getX(), s1.getY(), 0, -9));
						bullets.add(new Bullet(s1.getX(), s1.getY(), -7, -7));
						bullets.add(new Bullet(s1.getX(), s1.getY(), -9, 0));
						bullets.add(new Bullet(s1.getX(), s1.getY(), -7, 7));
					}
					s1.setIsShoot(false);
				}
			}

			chad.update(this.getSize());

			for (Platform p : this.rects) {
				if (chad.overlapsWith(p)) {
					int error = 20;
					if (chad.getY() + 100 < p.getY() + error) {
						chad.setY(p.getY() - 100);
						chad.setVelY(0);
					}

					// Check if under
					if (chad.getY() > p.getY() - error) {
						chad.setY(p.getY() + 10);
						chad.setVelY(0);
					}

					if (chad.getX() + 100 > p.getX() && chad.getX() + 100 < p.getX() + error) {
						chad.setX(p.getX() - 100);
						chad.setVelX(0);
					}

					if (chad.getX() < p.getX() + p.getWidth() && chad.getX() > p.getX() + p.getWidth() - error) {
						chad.setX(p.getX() + p.getWidth());
						chad.setVelX(0);
					}

				}
				// CHECK IF BOTTLES HIT PLATFORMS
				for (CoronaItem c : this.bottles) {
					if (c.getY() + 80 < p.getY() + 24 && c.getY() + 80 > p.getY() - 15 && c.getX() + 30 > p.getX()
							&& c.getX() < p.getX() + p.getWidth()) {
						if (c.getIsFalling())
							playSoundEffect(BOTTLESOUND);
						c.setIsFalling(false);
						c.setY(p.getY() - 80);
					}
				}
			}

			for (Bullet b : this.bullets) {
				b.update(getSize());
				if (b.getDeleteMe())
					bullets.remove(b);

			}

			for (CoronaItem c : this.bottles) {
				c.update(getSize());
				if (c.getIsTurnBack()) {
					monsters.add(new Monster(c.getX(), c.getY() - 20));
					bottles.remove(c);
				}

				if (chad.overlapsWith(c) && !c.getIsFalling()) {
					bottles.remove(c);
					playSoundEffect(DRINKSOUND);
					score += 100; 
				}
			}

			for (Monster m : this.monsters) {
				if (chad.overlapsWith(m) && chad.getIsSpearActive()) {
					playSoundEffect(STABSOUND);
					bottles.add(new CoronaItem(m.getX(), m.getY()));
					this.monsters.remove(m);
					m.setIsDead(true);
				} else if (chad.overlapsWith(m) && !chad.getHasInvincibility()) {
					playSoundEffect(HITSOUND);
					chad.bounce(m);
				}
			}

			for (Bullet b : this.bullets) {
				if (chad.overlapsWith(b) && !chad.getHasInvincibility()) {
					playSoundEffect(HITSOUND);
					chad.bounce(b);
				}
			}
		} catch (ConcurrentModificationException e) {
		};
	}

	public void makeBullet(Bullet b) {
		bullets.add(b);
	}

	public void playSoundEffect(String soundname) {
		try {
			AudioStream sound = new AudioStream(new FileInputStream(new File("Imports/Sounds/" + soundname + ".wav")));
			AudioPlayer.player.start(sound);
		}

		catch (IOException ex) {
		}
	}

	class endTime extends TimerTask { //timer at the end of the game 
		public void run() {
			endGameCount++;

			if (endGameCount == 10) {
				ourLevel.getFrame().dispose();
				new ArcadeGameViewer();
			}
			if (endGameCount != 10) {
				Timer wait = new Timer();
				wait.schedule(new endTime(), 1000);
			}

		}
	}
}
