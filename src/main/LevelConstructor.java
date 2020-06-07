package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * This class reads the level from the text file and turn it into the level. It
 * will run through the string made and place the monsters and heroes in the
 * spots.
 * 
 * @author purdydj & theneltj
 *
 */
public class LevelConstructor {

	private JFrame frame;
	private String txtFile;
	private double xstretch;
	private double ystretch;
	private ArrayList<Platform> rects = new ArrayList<Platform>();
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	private ArrayList<ShootyMonster> shootymonsters = new ArrayList<ShootyMonster>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private Hero chad;
	private int levelnumber = 1;
	private HeroListener herolistener;
	private boolean isChanged = false;
	public final GameComponent gameComp;
	private int counter = 0;

	public LevelConstructor(JFrame frame, String txtFile) {
		this.frame = frame;
		this.txtFile = txtFile;
		chad = new Hero(0, 0);
		readFile(this.txtFile);
		gameComp = new GameComponent(rects, chad, monsters, bullets, shootymonsters, this);
		frame.add(gameComp);
		startUpdate(gameComp);
	}

	/**
	 * Takes in a filename to fetch the text file to create the level generation off of.
	 * 
	 * @param fileName
	 */
	private void readFile(String fileName) {
		Scanner scanner;
		System.out.println("MAKING LEVEL:  " + levelnumber);
		ArrayList<String> level = new ArrayList<String>();
		try {
			scanner = new Scanner(new File(fileName + levelnumber + ".txt"));
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				System.out.println(line);
				level.add(line);
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not Found....Sorry!!");
			e.printStackTrace();
			return;
		}
		xstretch = 1920 / level.get(1).length();
		ystretch = 1080 / level.size();

		for (int i = 0; i < level.size(); i++) {
			for (int j = 0; j < level.get(i).length(); j++) {
				if (level.get(i).charAt(j) == 'M')
					makeMonster(i, j);
				if (level.get(i).charAt(j) == 'H')
					makeHero(i, j);
				if (j > 0)
					if (level.get(i).charAt(j) == '_' && level.get(i).charAt(j - 1) == '.')
						makePlatform(level, i, j);
				if (j == 0)
					if (level.get(i).charAt(j) == '_')
						makePlatform(level, i, j);
			}

		}
		scanner.close();
	}

	/**
	 * Creates a monster at the location specified in the txt file
	 * There is a 1/4 chance of making a shooter
	 * @param i
	 * @param j
	 */
	public void makeMonster(int i, int j) {
		Random r = new Random();
		int isShootyType = r.nextInt(4);

		if (isShootyType == 0)
			shootymonsters.add(new ShootyMonster((int) (j * xstretch), (int) (i * ystretch)));
		else
			monsters.add(new Monster((int) (j * xstretch), (int) (i * ystretch)));
	}

	/**
	 * Makes our hero at the specidied location
	 * @param i
	 * @param j
	 */
	public void makeHero(int i, int j) {
		chad.setX((int) (j * xstretch));
		chad.setY((int) (i * ystretch));
	}

	/**
	 * Makes the platform at the given location
	 * 
	 * @param level
	 * @param i
	 * @param j
	 */
	public void makePlatform(ArrayList<String> level, int i, int j) {
		int startj = j;
		int finalj = j;
		while (true) {
			if (finalj == level.get(i).length())
				break;
			if (level.get(i).charAt(finalj) != '_')
				break;
			if (level.get(i).charAt(finalj) == '_')
				finalj++;
		}

		rects.add(new Platform((int) (startj * xstretch), (int) ((i + 0.9) * ystretch - 10),
				(int) ((finalj - startj) * xstretch), 10));

	}

	/**
	 * Starts updating the game
	 * @param gameComp
	 */
	public void startUpdate(GameComponent gameComp) {
		frame.setFocusable(true);
		frame.requestFocus();
		herolistener = new HeroListener(chad, this);

		frame.addKeyListener(herolistener);
		Timer t = new Timer(0, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				gameComp.update();
				gameComp.repaint();
				frame.repaint();
				if (chad.getVelY() < 15) {
					chad.setVelY(chad.getVelY() + 1);
				}
				if (isChanged) {
					rects.clear();
					monsters.clear();
					shootymonsters.clear();
					bullets.clear();
					isChanged = false;
					readFile(txtFile);
				}

				if (chad.getIsDead() && counter ==0) {// counter insures that it only happens once
					makeEndGame();
					counter++;
				}
			}
		});

		t.start();

	}

	/**
	 * Creates the end schene for losing
	 */
	public void makeEndGame() { // makes the ending restart button
		JButton restart = new JButton();
		JPanel endPanel = new JPanel();
		JLabel text = new JLabel("<HTML>Restart Game", SwingConstants.CENTER);
		text.setFont(new Font("English Towne", Font.PLAIN, 36));
		text.setForeground(new Color(0, 90, 156));
		text.setOpaque(false);
		restart.add(BorderLayout.SOUTH, text);
		restart.setBackground(new Color(255, 203, 5));
		restart.setPreferredSize(new Dimension(400, 80));
		restart.setBorderPainted(false);
		restart.setOpaque(false);

		text.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		endPanel.setOpaque(false);

		// removes the game comp and makes level constructor to restart the game
		ActionListener endListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.remove(gameComp);
				frame.remove(text);
				frame.remove(restart);
				frame.remove(endPanel);
				new LevelConstructor(frame, txtFile);
				frame.pack();
			}
		};
		restart.addActionListener(endListener);
		endPanel.add(restart, BorderLayout.SOUTH);

		frame.add(endPanel, BorderLayout.SOUTH);

		frame.repaint();
		frame.pack();

	}

	public int getLevel() {
		return levelnumber;
	}
	public void levelPack() {
		frame.pack();
	}
	public JFrame getFrame() {
		return frame; 
	}
	public void setLevel(int levelnum) {
		this.levelnumber = levelnum;
	}

	public void setIsChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}
}
