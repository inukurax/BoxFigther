package run;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import controls.Keys;
import shapes.Player;

/**
 * Main class for Box Figther game.
 * @author Hjalte
 *
 */
public class BoxFigther extends Applet implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int JUMP_HEIGHT = 200;
	private static final int BOTTOM_HEIGHT = 100;
	public static final double JUMP_SPEED = 7;
	
	private Thread gameloop; // main thread
	private BufferedImage backbuffer; // double buffer
	private Graphics2D g2d; // main drawing object for the backbuffer
	// identity transform (0,0)
	private AffineTransform identity = new AffineTransform();
	private AffineTransform identity2 = new AffineTransform(); 

	// creates the player
	private Player player1;
	private Player player2;

	// screen size
	private int height = 600;
	private int width = 800;
	private int bottomline;
	private boolean drawBounds = true;
	
	public void init() {
		BufferedImage image;
		try {
			image = ImageIO.read(getClass().
					getClassLoader().getResourceAsStream("resources/shape.png"));
			player1 = new Player(image);
			image = ImageIO.read(getClass().
					getClassLoader().getResourceAsStream("resources/star.png"));
			player2 = new Player(image);
		} catch (IOException e) {
			e.printStackTrace();
			player1 = new Player(null);
			player2 = new Player(null);

		}
		
		this.setSize(width, height);
		backbuffer = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		width = this.getSize().height;
		height = this.getSize().width;
		bottomline = width - BOTTOM_HEIGHT;
		// sets player position to middle of screen
		int startY = bottomline - player1.getHeight();
		player1.setX(width - 100);
		player1.setY(startY);
		player2.setX(100);
		player2.setY(startY);
		
		this.addKeyListener(new Keys(player1, player2, bottomline));

	}
	
	public void update(Graphics g) {
		g2d.setTransform(identity);
		// fills background
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, height , width);
		
		// draws the earth (bottom bounds)
		g2d.setPaint(Color.GRAY);
		g2d.fillRect(0, bottomline, height, bottomline);

		//draws x,y cord for player
		g2d.setColor(Color.WHITE);
		g2d.drawString(String.format("P1: %d,%d - VelX: %f - VelY: %f", 
				Math.round(player1.getX()),Math.round(player1.getY()),
				player1.getVelY(), player1.getVelX()), 20, 28);
		g2d.drawString(String.format("P2: %d,%d - VelX: %f - VelY: %f", 
				Math.round(player2.getX()), Math.round(player2.getY()),
				player2.getVelY(), player2.getVelX()), 20, 40);
		g2d.drawString("Use arrow keys to control p1", 20, 52);
		g2d.drawString("a-d-w p2", 20, 64);

		//draws object
		this.drawPlayer();
		paint(g);
	}
	
	public void paint(Graphics g) {
		 g.drawImage(backbuffer, 0, 0, this);
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
				gameUpdate();
				// target framerate is 50fps
				Thread.sleep(20);
			} catch (InterruptedException e) {
					e.printStackTrace();
			}
			repaint();
		}
	}
	
	public void stop() {
		gameloop = null;
	}

	private void gameUpdate() {
		updatePlayer(player1);
		updatePlayer(player2);
		checkCollision();
	}
	
	private void checkCollision() {
		if (player1.getBounds().contains(player2.getBounds()))
			System.out.println("touch");
		
	}

	/**
	 * Draws the player.
	 */
	 public void drawPlayer() {
		g2d.setTransform(identity);
		g2d.translate(player1.getX(), player1.getY());
		g2d.setColor(Color.ORANGE);
		g2d.fill(player1.getShape());
		if (drawBounds ) {
			g2d.setColor(Color.RED);
			g2d.draw(player1.getBounds());
		}
		g2d.setTransform(identity2);
		g2d.translate(player2.getX(), player2.getY());
		g2d.setColor(Color.CYAN);
		g2d.fill(player2.getShape());
		
		if (drawBounds ) {
			g2d.setColor(Color.RED);
			g2d.draw(player2.getBounds());
		}
	}

	/**
	 * Updates the players position
	 */
	private void updatePlayer(Player player) {
		player.incX(player.getVelX());
		player.incY(player.getVelY());
		// jump height, gravity.
		if (player.getY() >= bottomline - player.getHeight()) {
			player.setVelY(0);
		}
		else if (player.getY() <= bottomline - player.getHeight() - JUMP_HEIGHT ) {
			player.setMoveAngle(0); // down
			player.incVelY(calcAngleMoveY(player.getMoveAngle()) * JUMP_SPEED);
		}
		// wrap for screen left & right
		if (player.getX() < - player.getWidth())
			player.setX(height + player.getWidth());
		else if (player.getX() > height + player.getWidth())
			player.setX(-player.getWidth());
		// wrap for screen top bottom
		if (player.getY() <  0) {
			player.setY(bottomline - player.getHeight());
		}
		else if (player.getY() > bottomline - player.getHeight())
			player.setY(bottomline - player.getHeight());
	}
	
	public static double calcAngleMoveY(double moveAngle) {
		return (double) (Math.cos(moveAngle * Math.PI / 180));
	}
}
