package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.javafx.geom.Dimension2D;

/**
 * This class generates our platforms for the Hero to jump
 * around on and for our CoronaItem to land on. Monsters do
 * not interact with platforms because covid is immune to everything
 * but our lysol spear.
 * 
 * @author theneltj & purdydj
 *
 */
public class Platform extends GameObject {

	private int width;
	private int height;

	public Platform(int x, int y, int width, int height ) {
		super(x, y);
		this.width = width;
		this.height = height;

	}
	
	public void drawOn(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.fill(new Rectangle(this.getX(), this.getY(), this.width, this.height));

	}
	public int getWidth() {
		return width;
	}

}
