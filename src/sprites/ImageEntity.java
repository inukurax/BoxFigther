package sprites;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.imageio.ImageIO;
import run.BoxFigther;

/**
 * Holds all the accessor and mutator methods for the game entities.
 * @author Hjalte Skjold Jørgensen
 * Contact: https://skjoldcode.com - https://github.com/inukurax
 * Email: skjoldborg94@edb.dk - hjalte94@gmail.com
/*********************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * ImageEntity class
 **********************************************************/ 
public class ImageEntity extends BaseGameEntity {
	
	protected Image image;
	protected AffineTransform at;
	protected Graphics2D g2d;
	
	// default constructor
	ImageEntity() {
		setImage(null);
		setAlive(true);
	}
	
	public Image getImage() { return image; }
	
	public void setImage(Image image) {
		this.image = image;
		double x = BoxFigther.WIDTH / 2 - width() / 2;
		double y = BoxFigther.HEIGHT / 2 - height() / 2;
		at = AffineTransform.getTranslateInstance(x, y);
	}

	public int height() {
		if (image != null)
			return image.getWidth(null);
		return 0;
	}

	public int width() {
		if (image != null)
			return image.getWidth(null);
		return 0;
	}
	
	public double getCenterX() {
		return getX() + width() / 2;
	}
	
	public double getCenterY() {
		return getY() + height() / 2;
	}
	
	public void setGraphics(Graphics2D g) {
		g2d = g;
	}
	
	/**
	 * Loads an Image from the resources 
	 * @param filename should be something like "resources/filename.png"
	 */
	public void load(String filename) {
		try {
			image = ImageIO.read(getClass().
					getClassLoader().getResourceAsStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (getImage().getWidth(null) <= 0); // stays here if this is true.
		double x = BoxFigther.WIDTH / 2 - width() / 2;
		double y = BoxFigther.HEIGHT / 2 - height() / 2;
		at = AffineTransform.getTranslateInstance(x, y);
	}
	
	public void transform() {
		at.setToIdentity();
		at.translate((int) getX() + width() / 2, (int) getY() + height() / 2);
		at.rotate(Math.toRadians(getFaceAngle()));
		at.translate(-width() / 2, -height() / 2);
	}
	
	public void draw() {
		g2d.drawImage(image, at, null);
	}
	
	// bounding rectangle
	public Rectangle getBounds() {
		return new Rectangle((int) getX(), (int) getY(), width(), height());
	}	
}