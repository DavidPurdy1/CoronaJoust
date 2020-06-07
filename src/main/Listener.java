package main;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.CoronaItem.TurnBack;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * This is the class for the start button of the game. The main thing this class
 * does is when the start button is pressed a new levelConstructor is made
 * 
 * @author purdydj and thenelltj
 *
 */
public class Listener implements ActionListener {
	private JButton b;
	private JFrame frame;
	private JPanel northPanel;
	private JPanel southPanel;
	private AudioStream music;

	public Listener(JButton b, JFrame frame, JPanel northPanel, JPanel southPanel, AudioStream music) {
		this.b = b;
		this.frame = frame;
		this.northPanel = northPanel;
		this.southPanel = southPanel;
		this.music = music;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Game Started");
		frame.remove(northPanel);
		frame.remove(southPanel);
		AudioPlayer.player.stop(music);
		
		try {
			InputStream sound = new FileInputStream(new File("Imports/Sounds/NextLevel.wav"));
			AudioStream startsound = new AudioStream(sound);
			AudioPlayer.player.start(startsound);
		}
		
		catch (IOException ex) {}
		
		new LevelConstructor(frame, "Imports/Levels/level_");

		frame.pack();
		frame.setVisible(true);
		Timer waiter = new Timer();
		waiter.schedule(new StartTune(), 1000);
	}
	
	class StartTune extends TimerTask {
		public void run() {
			try {
				Random which = new Random();
				boolean whichSong = which.nextBoolean();
				
				if (whichSong) {
					InputStream sound = new FileInputStream(new File("Imports/Sounds/BitSurf.wav"));
					AudioStream firstLevelMusic = new AudioStream(sound);
					AudioPlayer.player.start(firstLevelMusic);
				}
				
				if (!whichSong) {
					InputStream sound = new FileInputStream(new File("Imports/Sounds/Albatross.wav"));
					music = new AudioStream(sound);
					AudioPlayer.player.start(music);
				}
			}
			
			catch (IOException ex) {}
		}
	}

}
