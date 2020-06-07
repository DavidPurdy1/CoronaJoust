package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 * This class loads many of the fonts and generates our main menu for the arcade
 * game. It then waits for the start button to be pressed before loading in game
 * components.
 * 
 * @author purdydj & theneltj
 *
 */
public class ArcadeGameViewer {
	public static final int FRAME_WIDTH = 1920;
	public static final int FRAME_HEIGHT = 1080;
	public static final Color LIGHT_GRAY = new Color(200, 200, 200);
	private JFrame frame;
	AudioStream music;

	public ArcadeGameViewer() {
		frame = new MyFrame();
		frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		// IMPORT FONT && MUSIC//
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Imports/Fonts/EnglishTowne.ttf")));

			// InputStream sound = new FileInputStream(new File("Imports/titlemusic.wav"));
			InputStream sound = new FileInputStream(new File("Imports/Sounds/Caribbean.wav"));
			music = new AudioStream(sound);
			AudioPlayer.player.start(music);

		} catch (IOException | FontFormatException e) {
			System.out.println("Font or music not recognized");
		}

		// Generate north panel for title
		JPanel northPanel = new JPanel();
		northPanel.setOpaque(false);
		JLabel title = new JLabel("Corona ~ Joust");
		title.setFont(new Font("English Towne", Font.PLAIN, 280));
		title.setForeground(new Color(0, 90, 156));
		northPanel.add(title);
		frame.add(northPanel, BorderLayout.NORTH);

		// Generate south panel game selects
		JPanel southPanel = new JPanel();
		southPanel.setOpaque(false);
		JLabel text = new JLabel("<HTML>Start Game!", SwingConstants.CENTER);
		text.setFont(new Font("English Towne", Font.PLAIN, 72));
		text.setForeground(new Color(0, 90, 156));
		JButton button = new JButton();
		button.add(BorderLayout.CENTER, text);
		button.setBackground(new Color(255, 203, 5));
		button.setPreferredSize(new Dimension(400, 80));
		button.setBorderPainted(false);
		button.setOpaque(false);
		text.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		Listener ear1 = new Listener(button, frame, northPanel, southPanel, music);
		button.addActionListener(ear1);

		southPanel.add(button);
		frame.add(southPanel, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);

	}

}
