package run;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import sprites.Sprite;

/**
 * Main class for Box Figther game.
 * @author Hjalte
 *
 */
public class BoxFigther extends Applet implements Runnable {

	private static final long serialVersionUID = 1L;
	// screen size
	public static int HEIGHT = 600;
	public static int WIDTH = 800;
	private Thread gameloop; // main thread
	private BufferedImage backbuffer; // double buffer
	private Graphics2D g2d; // main drawing object for the backbuffer
	private Sprite dragon;
	//SETTINGS//
	//private boolean drawBounds = true;

	public void init() {
		this.setSize(WIDTH, HEIGHT);
		backbuffer = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		loadSprites();
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
            repaint();
		}
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
    
    /**
     * Loads the Sprites.
     */
	private void loadSprites() {
		//load the dragon sprite
        dragon = new Sprite(g2d);
        dragon.load("resources/dragon.png");
		//this.addKeyListener(new Keys(player1, player2, bottomline));
		int width = WIDTH / 2;
        int height = HEIGHT / 2 - dragon.imageHeight();
        Point point = new Point(width, height);
        drawPlayer(dragon, point);		
	}
    
	/*
	 * Draws a Sprite at a specific point
	 */
    private void drawPlayer(Sprite player, Point point) {
        player.setPosition(point);
        player.transform();
        player.draw();
    }
}
