import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main extends JFrame implements ActionListener, ChangeListener, MouseListener, MouseMotionListener {
	private ImageIcon image1, imagePlay, imagePause, imageStop, sliderBgImage;
	private JLabel labelBackgroundImage, sliderBackground;
	private JTextArea areaTitle;
	private JSlider musicSlider, volumeSlider;
	private JButton playButton, pauseButton, stopButton, muteButton;
	private AudioInputStream audioIn;
	private Clip clip;
	private boolean musicIsStopped, mousePressed, muted, volumeSliderIsPressed, mouseIsOverVolumeSlider;
	private Timer timer;
	private FloatControl volumeControl;
	private BooleanControl volumeMute;
	int volumeSliderOriginalValue;

	Main() {
		musicIsStopped = true;
		volumeSliderIsPressed = false;
		mouseIsOverVolumeSlider = false;
		setLayout(null);
		setSize(630,650);
		setResizable(false);
		muted = false;

		
		image1 = new ImageIcon(getClass().getResource("pepe.jpg"));
		labelBackgroundImage = new JLabel(image1);
		labelBackgroundImage.setBounds(10, 10, 600, 600);
		add(labelBackgroundImage);
		
		areaTitle = new JTextArea("/pol/ exclusive - P.E.P.E.- Shadilay\n[Italian Version]");
		areaTitle.setBounds(50,20,500,100);
		areaTitle.setEditable(false);
		areaTitle.setOpaque(false);
		areaTitle.setFont(new Font("Times New Roman",Font.BOLD,30));
		labelBackgroundImage.add(areaTitle);
		
		imageStop = new ImageIcon(getClass().getResource("stop_button.png"));
		stopButton = new JButton(imageStop);
		stopButton.setBounds(50, 540, 25, 25);
		stopButton.setContentAreaFilled(false);
		stopButton.setBorderPainted(false);
		stopButton.setOpaque(false);
		stopButton.setFocusPainted(false);
		stopButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		labelBackgroundImage.add(stopButton);
		stopButton.addActionListener(this);
		stopButton.setVisible(true);
		
		imagePlay = new ImageIcon(getClass().getResource("play_button.png"));
		playButton = new JButton(imagePlay);
		playButton.setBounds(8, 545, 50, 50);
		playButton.setContentAreaFilled(false);
		playButton.setBorderPainted(false);
		playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		labelBackgroundImage.add(playButton);
		playButton.addActionListener(this);
		
		imagePause = new ImageIcon(getClass().getResource("pause_button.png"));
		pauseButton = new JButton(imagePause);
		pauseButton.setBounds(8, 545, 50, 50);
		pauseButton.setContentAreaFilled(false);
		pauseButton.setBorderPainted(false);
		pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		labelBackgroundImage.add(pauseButton);
		pauseButton.addActionListener(this);
		pauseButton.setVisible(false);
		
		muteButton = new JButton();
		muteButton.setBounds(75, 550, 30, 30);
		muteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		labelBackgroundImage.add(muteButton);
		muteButton.addMouseListener(this);
		muteButton.addActionListener(this);
		muteButton.setForeground(Color.BLACK);
		muteButton.setBackground(Color.BLACK);
		
		timer = new Timer(100, this);
		
		try {
			audioIn = AudioSystem.getAudioInputStream(Main.class.getResource("shadilay.wav"));
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (LineUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		sliderBgImage = new ImageIcon(getClass().getResource("slider_background.png"));
		sliderBackground = new JLabel(sliderBgImage);
		sliderBackground.setBounds(10, 563, 600, 20);
		labelBackgroundImage.add(sliderBackground);
		musicSlider = new JSlider(0,clip.getFrameLength(),0);
		musicSlider.setBounds(100, 0, 455, 20);
		musicSlider.addChangeListener(this);
		musicSlider.addMouseListener(this);
		sliderBackground.add(musicSlider);
		musicSlider.setBackground(Color.BLACK);
		musicSlider.setForeground(Color.DARK_GRAY);	
		musicSlider.addMouseMotionListener(this);
		
		volumeSlider = new JSlider(JSlider.VERTICAL,-25,0,0);
		volumeSlider.setBounds(80, 480, 20, 75);
		volumeSlider.setSnapToTicks(true);
		labelBackgroundImage.add(volumeSlider);
		volumeSlider.setBackground(Color.BLACK);
		volumeSlider.addChangeListener(this);
		volumeSlider.setVisible(false);
		volumeSlider.addMouseListener(this);
		volumeSlider.addMouseMotionListener(this);
		volumeSlider.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		volumeMute = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
		
	}
	
	public static void main(String[] args) {
		Main app = new Main();
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object z = e.getSource();
		if (z == playButton) {
			clip.start();
			musicIsStopped = false;
			timer.start();
			playButton.setVisible(false);
			pauseButton.setVisible(true);
		}
		else if (z == pauseButton) {
			clip.stop();
			musicIsStopped = true;
			timer.stop();
			playButton.setVisible(true);
			pauseButton.setVisible(false);
		}
		else if (z == stopButton) {
			timer.stop();
			musicIsStopped = true;
			clip.stop();
			musicSlider.setValue(0);
			clip.setFramePosition(0);
			playButton.setVisible(true);
			pauseButton.setVisible(false);
		}
		else if (z == muteButton) {
			if (muted == true) {
				volumeSlider.setValue(volumeSliderOriginalValue);
				muted = false;
			}
			else if (muted == false) {
				volumeSliderOriginalValue = volumeSlider.getValue();
				volumeSlider.setValue(-25);
				muted = true;
			}
			
		}
		else if (z == timer) {
			if (mousePressed == false)
				musicSlider.setValue(clip.getFramePosition());
			if (clip.getFramePosition() == clip.getFrameLength()) {
				playButton.setVisible(true);
				pauseButton.setVisible(false);
				clip.stop();
				musicIsStopped = true;
				clip.setFramePosition(0);
			}
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object z = e.getSource();
		if (z == musicSlider && mousePressed == true) 
			clip.setFramePosition(musicSlider.getValue());
		else if (z == volumeSlider) {
			muted = false;
			if (volumeSlider.getValue() == -25)
				volumeMute.setValue(true);
			else {
				volumeControl.setValue(volumeSlider.getValue());
				volumeMute.setValue(false);
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Object z = e.getSource();
		if (z == muteButton || z == volumeSlider) {
			volumeSlider.setVisible(true);
			volumeSlider.requestFocus();
			mouseIsOverVolumeSlider = true;
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Object z = e.getSource();
		if (z == muteButton || z == volumeSlider) {
			mouseIsOverVolumeSlider = false;
			if (volumeSliderIsPressed == false)
				volumeSlider.setVisible(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Object z = e.getSource();
		if (z == musicSlider) {
			mousePressed = true;	
			clip.stop();
			timer.stop();
			int value = (int) (musicSlider.getMousePosition().x * (clip.getFrameLength()/455.0));
			musicSlider.setValue(value);
			//clip.setFramePosition(value);
		}
		else if (z == volumeSlider) {
			int value = (int) (volumeSlider.getMousePosition().y * (-25.0/75.0));
			volumeSlider.setValue(value);
			volumeSliderIsPressed = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Object z = e.getSource();
		if (z == musicSlider) {
			mousePressed = false;
			if (musicIsStopped == false) {
				clip.start();
				timer.start();
			}
		}
		else if (z == volumeSlider) {
			volumeSliderIsPressed = false;
			if (mouseIsOverVolumeSlider == false)
				volumeSlider.setVisible(false);
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Object z = e.getSource();
		if (z == musicSlider) {
			int value = (int) ((getMousePosition().x - 122) * (clip.getFrameLength()/455.0));
			musicSlider.setValue(value);
		}
		else if (z == volumeSlider) {
			int value = (int) ((getMousePosition().y - 512) * (-25.0/75.0));
			volumeSlider.setValue(value);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
