package run;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
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
	public static final int JUMP_SPEED = 7; 
	public static GameState GAME_STATE = GameState.GAME_PAUSE;

	// screen size
	public static int SCR_HEIGHT = 800;
	public static int SCR_WIDTH = 1200;
	private Thread gameloop; // main thread
	private BufferedImage backbuffer; // double buffer
	private Graphics2D g2d; // main drawing object for the backbuffer
	private AnimatedSprite sprite;
	private AnimatedSprite sprite2;
	private AnimatedSprite dummy;
	
	private int xOnScreen;
	private int yOnScreen;
	private BufferedImage background;
	private boolean dummyCollision;
	private int dummyHitCount;
	private Camera camera;
	
	//SETTINGS//
	//private boolean drawBounds = true;
	
	// initializes the applet
	public void init() {
		this.setSize(SCR_WIDTH, SCR_HEIGHT);
		camera = new Camera (2000, 800);
		backbuffer = new BufferedImage(SCR_WIDTH,SCR_HEIGHT, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		
		//dummy
		dummy = new AnimatedSprite();
		dummy.load("resources/TargetDummy500x500af3x2.png", 3 , 6 ,500 ,500);
		dummy.frameDelay = 0;
		dummy.position = new Point ((SCR_WIDTH - dummy.frameWidth) / 2, 
				SCR_HEIGHT - BOTTOM_LINE - dummy.frameHeight);
		//dummy.testAnimation = true;
		dummy.animation = new Animation (dummy, 0, 6, 4);
		dummy.camera = camera;
		// player
		sprite = new AnimatedSprite();
		camera.setPlayer(sprite);
		sprite.load("resources/SpriteSmiley350x350af6x10.png", 6 , 60 ,350 ,350);
		sprite.frameDelay = 1;
		sprite.position = new Point (0, SCR_HEIGHT - BOTTOM_LINE - sprite.frameHeight);
		sprite.velocity = new Point(0, 0);
		sprite.rotationRate = 0.0;
		sprite.camera = camera;
		int kickDelay = 3;
		int hitDelay = 1;
		sprite.animationWalkRight = new Animation(sprite, 0, 12, 2);
		sprite.animationWalkLeft = new Animation (sprite, 12, 24, 2);
		sprite.stanceRight = 24;

		sprite.animationHitRight = new Animation(sprite, 25, 32, hitDelay);
		sprite.animationKickRight = new Animation(sprite, 32, 36, kickDelay);
		sprite.stanceLeft = 36;

		sprite.animationHitLeft = new Animation(sprite, 37,  44, hitDelay);
		sprite.animationKickLeft = new Animation(sprite, 44, 48, kickDelay);
		sprite.aniJumpRight = new Animation(sprite, 48, 52, 1);
		sprite.aniJumpLeft = new Animation(sprite, 54, 58, 1);
		
		
		sprite2 = new AnimatedSprite();
		sprite2.camera = camera;
		sprite2.load("resources/SpriteSmiley350x350af6x10.png", 6 , 60 ,350 ,350);
		sprite2.frameDelay = 1;
		sprite2.position = new Point (SCR_WIDTH - sprite2.frameWidth, SCR_HEIGHT - BOTTOM_LINE - sprite2.frameHeight);
		sprite2.velocity = new Point(0, 0);
		sprite2.rotationRate = 0.0;
		sprite2.animationDirection = -1;
		
		sprite2.animationWalkRight = new Animation(sprite2, 0, 12, 2);
		sprite2.animationWalkLeft = new Animation (sprite2, 12, 24, 2);
		sprite2.stanceRight = 24;

		sprite2.animationHitRight = new Animation(sprite2, 25, 32, hitDelay);
		sprite2.animationKickRight = new Animation(sprite2, 32, 36, kickDelay);
		sprite2.stanceLeft = 36;

		sprite2.animationHitLeft = new Animation(sprite2, 37,  44, hitDelay);
		sprite2.animationKickLeft = new Animation(sprite2, 44, 48, kickDelay);
		sprite2.aniJumpRight = new Animation(sprite2, 48, 52, 1);
		sprite2.aniJumpLeft = new Animation(sprite2, 54, 58, 1);
				
		try {
			background = ImageIO.read(getClass().
					getClassLoader().getResourceAsStream("resources/background.png"));
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
			g2d.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			drawBackground();

			switch (GAME_STATE) {
			case GAME_OVER:
				g2d.drawString("GAME_OVER", SCR_WIDTH / 2, SCR_HEIGHT / 2);
				break;
			case GAME_PAUSE:
				g2d.setFont(new Font("Verdana", Font.BOLD, 36));
				g2d.setColor(Color.RED);
				g2d.drawString("PAUSE", SCR_WIDTH / 2, SCR_HEIGHT / 2);
				break;
			case GAME_RUNNING:
				gameUpdate();
				break;
			default:
				break;
			}
            repaint();
		}
	}
	
	private void drawBackground() {
		// clears the background
				g2d.fill(new Rectangle(0, 0, SCR_WIDTH, SCR_HEIGHT));
				// background
				Point pos = camera.getPlayerPoint();
				g2d.drawImage(background, 0, 0, SCR_WIDTH, SCR_HEIGHT,pos.x, pos.y, pos.x+SCR_WIDTH , pos.y+SCR_HEIGHT , null);
				g2d.setColor(Color.RED);

				g2d.drawString("Player1 controls: LEFT-UP-RIGHT-M-N", SCR_WIDTH - 400, 28);

				g2d.drawString("Player2 controls: A-W-D-F-G", SCR_WIDTH - 400, 40);
				g2d.drawString("Pause: ESCAPE", SCR_WIDTH - 400, 12);
	}

	private void gameUpdate() {
		sprite.drawBounds(Color.RED, g2d);
		dummy.drawBounds(Color.BLUE, g2d);
		sprite2.drawBounds(Color.GREEN, g2d);

		//debug
		g2d.setColor(Color.RED);

		g2d.drawString("Mouse: " + xOnScreen + "," + yOnScreen, 10, 28);
		g2d.drawString("Position: P1-" + sprite.position + "; P2-" + sprite2.position, 10, 40);
		g2d.drawString("Velocity: P1-" + sprite.velocity + "; P2-" + sprite2.velocity, 10, 52);
		g2d.drawString("Animation: P1-" + sprite.getCurrentFrame() + "; P2-" + sprite2.getCurrentFrame(), 10, 64);
		g2d.drawString("Direction: P1-" + sprite.animationDirection + "; P2-" + sprite2.animationDirection, 10, 76);
		g2d.drawString("Dummy collision: " + dummyCollision, 10, 88);
		g2d.drawString("Dummy hits: " + dummyHitCount / 2, 10, 100);
		g2d.drawString("Screen Position: P1-" + sprite.getScrPosition() + "; P2-" + sprite2.getScrPosition(), 10, 112);
		
		g2d.drawString("Player1", sprite.getScrPosition().x + (sprite.frameWidth / 2),
				sprite.getScrPosition().y );
		g2d.drawString("Player2", sprite2.getScrPosition().x + (sprite2.frameWidth / 2),
				sprite2.getScrPosition().y);
		dummy.draw(g2d);
		sprite.draw(g2d);
		sprite2.draw(g2d);
		checkCollision();
	}

	private void checkCollision() {
		if ((sprite.collidesWith(dummy) && sprite.getCurrentFrame() == 35 
				&& sprite.animationDirection == 1 && dummy.position.x > sprite.position.x) || 
				(sprite.collidesWith(dummy) && sprite.getCurrentFrame() == 47 
						&& sprite.animationDirection == -1 && dummy.position.x < sprite.position.x)) {
			dummyCollision = true;
			dummyHitCount++;
		}
		else
			dummyCollision = false;
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
	
	public enum GameState { 
		GAME_RUNNING, GAME_OVER, GAME_PAUSE;
	}
}
