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
import controls.Keys;
import sprites.AnimatedSprite;
import sprites.Animation;

/**
 * Main class for Box Figther game.
 * @author Hjalte
 *
 */
public class BoxFigther extends Applet implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int BOTTOM_LINE = 50;
	// jump height in pixels
	public static final int JUMP_HEIGHT = 450;
	private static final int GROUND_HEIGHT = 40; 
	// screen size
	public static int HEIGHT = 800;
	public static int WIDTH = 1200;
	private Thread gameloop; // main thread
	private BufferedImage backbuffer; // double buffer
	private Graphics2D g2d; // main drawing object for the backbuffer
	private AnimatedSprite sprite;
	private int xOnScreen;
	private int yOnScreen;
	
	//SETTINGS//
	//private boolean drawBounds = true;
	
	// initializes the applet
	public void init() {
		this.setSize(WIDTH, HEIGHT);
		backbuffer = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();

		sprite = new AnimatedSprite(g2d);
		sprite.load("resources/SmileyMan500x500af6x9.png", 6 , 36 ,500 ,500);
		sprite.frameDelay = 1;
		sprite.position = new Point (300, HEIGHT - BOTTOM_LINE - sprite.frameHeight);
		sprite.velocity = new Point(0, 0);
		sprite.rotationRate = 0.0;
		sprite.aniLeft = new Animation (sprite, 12, 24);
		sprite.aniRight = new Animation(sprite, 0, 12);
		sprite.aniHitRight = new Animation(sprite, 30, 37);
		sprite.aniKickRight = new Animation(sprite, 38, 42);

		sprite.aniHitLeft = new Animation(sprite, 42,  49);
		sprite.aniKickLeft = new Animation(sprite, 50, 54);
		sprite.stanceLeft = 25;
		sprite.stanceRight = 24;
		this.addKeyListener(new Keys(sprite));
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
		g2d.setColor(Color.gray);
		g2d.fill(new Rectangle(0, 0, WIDTH, HEIGHT));

		// draw ground
		g2d.setColor(Color.BLACK);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(0, HEIGHT - BOTTOM_LINE - 60 , WIDTH, HEIGHT - BOTTOM_LINE - GROUND_HEIGHT);
		
		
		// keep sprite in screen box
		sprite.drawBounds(Color.RED);
//		if (sprite.position.x < 0 || sprite.position.x > WIDTH-sprite.frameHeight)
//			sprite.velocity.x *= -2;
//		if (sprite.position.y < BOTTOM_LINE || sprite.position.y > HEIGHT-sprite.frameHeight)
//			sprite.velocity.y *= -2;
		//debug
		g2d.drawString("Mouse: " + xOnScreen + "," + yOnScreen, 10, 28);

		g2d.drawString("Position: " + sprite.position, 10, 40);
		g2d.drawString("Velocity: " + sprite.velocity, 10, 52);
		g2d.drawString("Animation: " + sprite.currentFrame, 10, 64);
		g2d.drawString("Direction: " + sprite.animationDirection, 10, 76);
		sprite.draw();
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

}
