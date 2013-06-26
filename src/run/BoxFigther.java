package run;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import controls.Keys;
import sprites.AnimatedSprite;
import sprites.Animation;

/**
 * Main class for Box Figther game.
 * @author Hjalte
 *
 */
public class BoxFigther extends Applet implements Runnable, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static final int BOTTOM_LINE = 50;
	// jump height in pixels
	public static final int JUMP_HEIGHT = 100;
	private static final int GROUND_HEIGHT = 40;
	public static final int JUMP_SPEED = 7; 
	// screen size
	public static int SCR_HEIGHT = 738;
	public static int SCR_WIDTH = 1024;
	private Thread gameloop; // main thread
	private BufferedImage backbuffer; // double buffer
	private Graphics2D g2d; // main drawing object for the backbuffer
	private AnimatedSprite sprite;
	private AnimatedSprite sprite2;

	private AnimatedSprite dummy;
	private int xOnScreen;
	private int yOnScreen;
	private BufferedImage ground;
	private BufferedImage background;
	
	//SETTINGS//
	//private boolean drawBounds = true;
	
	// initializes the applet
	public void init() {
		this.setSize(SCR_WIDTH, SCR_HEIGHT);
		backbuffer = new BufferedImage(SCR_WIDTH,SCR_HEIGHT, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		
		//dummy
		dummy = new AnimatedSprite(g2d);
		dummy.load("resources/TargetDummy500x500af3x2.png", 3 , 6 ,500 ,500);
		dummy.frameDelay = 0;
		dummy.position = new Point (SCR_WIDTH - dummy.frameWidth, SCR_HEIGHT - BOTTOM_LINE - dummy.frameHeight);
		//dummy.testAnimation = true;
		dummy.animation = new Animation (dummy, 0, 6, 2);

		// player
		sprite = new AnimatedSprite(g2d);
		sprite.load("resources/SmileyMan500x500af6x12.png", 6 , 72 ,500 ,500);
		sprite.frameDelay = 1;
		sprite.position = new Point (300, SCR_HEIGHT - BOTTOM_LINE - sprite.frameHeight);
		sprite.velocity = new Point(0, 0);
		sprite.rotationRate = 0.0;
		
		int kickDelay = 3;
		int hitDelay = 1;
		sprite.aniLeft = new Animation (sprite, 12, 24, 2);
		sprite.aniRight = new Animation(sprite, 0, 12, 2);
		sprite.aniHitRight = new Animation(sprite, 30, 37, hitDelay);
		sprite.aniKickRight = new Animation(sprite, 38, 42, kickDelay);
		sprite.aniJumpRight = new Animation(sprite, 54, 58, 1);
		sprite.aniHitLeft = new Animation(sprite, 42,  49, hitDelay);
		sprite.aniKickLeft = new Animation(sprite, 50, 54, kickDelay);
		sprite.aniJumpLeft = new Animation(sprite, 60, 64, 1);
		
		sprite.stanceLeft = 25;
		sprite.stanceRight = 24;
		
		sprite2 = new AnimatedSprite(g2d);
		sprite2.load("resources/SmileyMan500x500af6x12.png", 6 , 72 ,500 ,500);
		sprite2.frameDelay = 1;
		sprite2.position = new Point (100, SCR_HEIGHT - BOTTOM_LINE - sprite2.frameHeight);
		sprite2.velocity = new Point(0, 0);
		sprite2.rotationRate = 0.0;
		
		sprite2.aniLeft = new Animation (sprite2, 12, 24, 2);
		sprite2.aniRight = new Animation(sprite2, 0, 12, 2);
		sprite2.aniHitRight = new Animation(sprite2, 30, 37, hitDelay);
		sprite2.aniKickRight = new Animation(sprite2, 38, 42, kickDelay);
		sprite2.aniJumpRight = new Animation(sprite2, 54, 58, 1);
		sprite2.aniHitLeft = new Animation(sprite2, 42,  49, hitDelay);
		sprite2.aniKickLeft = new Animation(sprite2, 50, 54, kickDelay);
		sprite2.aniJumpLeft = new Animation(sprite2, 60, 64, 1);
		
		sprite2.stanceLeft = 25;
		sprite2.stanceRight = 24;
		
		try {
			ground = ImageIO.read(getClass().
					getClassLoader().getResourceAsStream("resources/ground.jpg"));
			background = ImageIO.read(getClass().
					getClassLoader().getResourceAsStream("resources/background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.addKeyListener(new Keys(sprite, sprite2));
		this.addMouseMotionListener(this);
	}

	public void start() {
		gameloop = new Thread(this);
		gameloop.start();
	}

	@Override
	public void run() {
		Thread t = Thread.currentThread(); // gets the current thread
		// main game loop
		while (t == gameloop) {
			try {
				// target framerate is 50fps
				Thread.sleep(30);
			} catch (InterruptedException e) {
					e.printStackTrace();
			}
			gameUpdate();
            repaint();
		}
	}
	
	private void gameUpdate() {
		// clears the background
		g2d.setColor(Color.WHITE);
		g2d.fill(new Rectangle(0, 0, SCR_WIDTH, SCR_HEIGHT));
		// draw ground
		g2d.drawImage(ground, 0, SCR_HEIGHT - BOTTOM_LINE - 60 , 
				SCR_WIDTH, SCR_HEIGHT - BOTTOM_LINE - GROUND_HEIGHT, null);
		//g2d.drawImage(background, 0, 0, WIDTH, HEIGHT - BOTTOM_LINE - GROUND_HEIGHT, null);
		sprite.drawBounds(Color.RED);
		//debug
		g2d.drawString("Mouse: " + xOnScreen + "," + yOnScreen, 10, 28);

		g2d.drawString("Position: " + sprite.position, 10, 40);
		g2d.drawString("Velocity: " + sprite.velocity, 10, 52);
		g2d.drawString("Animation: " + sprite.currentFrame, 10, 64);
		g2d.drawString("Direction: " + sprite.animationDirection, 10, 76);
		dummy.draw();
		sprite.draw();
		sprite2.draw();
	}

	public void stop() {
		gameloop = null;
	}

    public void paint(Graphics g) {
        //draw the back buffer to the screen
        g.drawImage(backbuffer, 0, 0, this);
    }
    // fix for dobbelt buffer
    public void update(Graphics g) { 
         paint(g); 
    }

	@Override
	public void mouseDragged(MouseEvent e) {		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.yOnScreen = e.getY();
		this.xOnScreen = e.getX();
	} 
}
