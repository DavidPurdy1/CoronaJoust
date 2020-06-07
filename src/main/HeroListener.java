package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;

/**
 * This class does 2 things: allows you to control the hero chad with arrows and
 * spacebar and also allows you to change levels on U and D keys
 * 
 * @author purdydj & theneltj
 *
 */
public class HeroListener implements KeyListener {

	public static final int RADIUS = 200;
	private Hero chad;
	private LevelConstructor ourLevel;

	public HeroListener(Hero chad, LevelConstructor ourLevel) {
		this.chad = chad;
		this.ourLevel = ourLevel;
	}

	public void actionPerformed(ActionEvent e) {// updates and moves the hero
		chad.setX(chad.getX() + chad.getVelX());
		chad.setY(chad.getY() + chad.getVelY());
		if (chad.getX() > 1920 - RADIUS) {// stops the hero from going off the edge of the map
			chad.setX((int) (1920 - RADIUS));
		} else if (chad.getX() < 0) {
			chad.setX(0);
		}
		if (chad.getY() > 1080 - RADIUS) {
			chad.setY((int) (1080 - RADIUS));
		} else if (chad.getY() < 0) {
			chad.setY(0);
		}
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_UP && chad.getNumJumps() >= 1) {
			chad.setVelY(-35);
			chad.setIsOnGroundFalse();
			chad.decreaseNumJumps();
		}
		if (key.getKeyCode() == KeyEvent.VK_DOWN) {
		}
		if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			chad.setVelX(-15);
		}
		if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			chad.setVelX(15);
		}
		if (key.getKeyCode() == KeyEvent.VK_SPACE) {
			chad.setIsSpearActiveTrue();
		}
		if (key.getKeyCode() == KeyEvent.VK_U) {
			if (ourLevel.getLevel() < 10) {
				ourLevel.setIsChanged(true);
				ourLevel.setLevel(ourLevel.getLevel() + 1);
			}
		}
		if (key.getKeyCode() == KeyEvent.VK_D) {
			if (ourLevel.getLevel() > 1) {
				ourLevel.setIsChanged(true);
				ourLevel.setLevel(ourLevel.getLevel() - 1);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {// no movement when no keypress
		if (key.getKeyCode() == KeyEvent.VK_UP || key.getKeyCode() == KeyEvent.VK_DOWN) {
			chad.setVelY(0);
		}

		if (key.getKeyCode() == KeyEvent.VK_LEFT || key.getKeyCode() == KeyEvent.VK_RIGHT) {
			chad.setVelX(0);
		}
		if (key.getKeyCode() == KeyEvent.VK_U) {
			ourLevel.setIsChanged(false);
		}
		if (key.getKeyCode() == KeyEvent.VK_D) {
			ourLevel.setIsChanged(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//required for the keyListener
	}

}
