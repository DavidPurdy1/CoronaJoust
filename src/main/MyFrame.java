package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * This class creates the frame used for the arcade game
 * to display our objects and play our game on. It also
 * sets the background image.
 * 
 * @author theneltj & purdydj
 *
 */
public class MyFrame extends JFrame {
	private JPanel contentPane;

	public MyFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel() {
			
			public void paintComponent(Graphics g) {
				Image img;
				
				try {
					
					img = ImageIO.read(new File("Imports/Sprites/PixelBeach.png"));
					g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
}
