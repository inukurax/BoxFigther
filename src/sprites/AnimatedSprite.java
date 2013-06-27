package sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import run.BoxFigther;
import run.Camera;

/**
 * @author Hjalte Skjold J�rgensen
 * Contact: https://skjoldcode.com - https://github.com/inukurax
 * Email: skjoldborg94@edb.dk - hjalte94@gmail.com
 */
public class AnimatedSprite {
	
	protected Graphics2D g2d;
	public Image image;
	public boolean alive;
	public Point position;
	public Point velocity;
	public double rotationRate;
	public double moveAngle, faceAngle;
	private int currentFrame;
	public int totalFrames, animationDirection;
	public int frameCount, frameDelay;
	public int frameWidth, frameHeight, columns;
	public int startFrame;
	public Animation animation;
	public Animation aniLeft;
	public Animation aniRight;
	public Animation aniHitLeft;
	public Animation aniHitRight;
	public Animation aniKickRight;
	public Animation aniKickLeft;
	public int stanceLeft;
	public int stanceRight;
	public boolean doHit = false;
	public boolean doKick = false;
	private boolean debug = true;
	public boolean doJump;
	public Animation aniJumpRight;
	public Animation aniJumpLeft;
	public boolean testAnimation = false;
	private Area area;
	public Camera camera;
	
	public AnimatedSprite(Graphics2D g2d) {
		this.g2d = g2d;
		image = null;
		alive = true;
		position = new Point(0, 0);
		velocity = new Point(0, 0);
		rotationRate = 0.0;
		setCurrentFrame(0);
		totalFrames = 1;
		animationDirection = 1;
		frameCount = 0;
		frameDelay = 0;
		frameWidth = 0;
		frameHeight = 0;
		columns = 1;
		moveAngle = 0.0;
		faceAngle = 0.0;
		startFrame = 0;
		animation = new Animation(this, 0, totalFrames, 2);
	}
	
	public Graphics2D getGraphics() { 
		return g2d;
	}
	
	public void setGraphics(Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	public void setImage(Image image) { 
		this.image = image;
	}
	
    public int getWidth() {
        if (image != null)
            return image.getWidth(null);
        else
            return 0;
    }
    
    public int getHeight() {
        if (image != null)
            return image.getHeight(null);
        else
            return 0;
    }
    
    public Point getCenter() {
        Point pos = getScrPosition();

        int x = pos.x + frameWidth / 2;
        int y = pos.y + frameHeight / 2;
        return(new Point(x,y));
    }
    
    public Area getArea() {
    	if (area != null)
    		return this.area;
    	BufferedImage buff = (BufferedImage) this.image;
        int frameX = (getCurrentFrame() % columns) * frameWidth;
        int frameY = (getCurrentFrame() / columns) * frameHeight;
        //draw the frame 
    	BufferedImage currentImage = buff.getSubimage(frameX, frameY, frameWidth, frameHeight);
    	this.area = this.getOutline(Color.black, currentImage);
    	return this.area;
    }

    public Rectangle getBounds() {
        Point pos = getScrPosition();
		Rectangle r = getArea().getBounds();

        r = new Rectangle((int) (getCenter().x - (r.width / 2)), position.y, r.width, frameHeight);
        return (r);
    }
    
    public Point getScrPosition() {
        Point cam = camera.getPlayerPoint();
        Point pos = new Point(position.x - cam.x, position.y - cam.y);
        return pos;
    }

    public void load(String filename, int _columns, int _totalFrames,
        int _width, int _height)
    {
		try {
			image = ImageIO.read(getClass().
					getClassLoader().getResourceAsStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
        while(image.getWidth(null) <= 0);
        columns = _columns;
        totalFrames = _totalFrames;
        frameWidth = _width;
        frameHeight = _height;
    }

    protected void update() {

        //update position
        position.x += velocity.x;
        position.y += velocity.y;
    	if (position.x < 0 || position.x > camera.getWorldWidth() - frameWidth) {
   		 position.x -= velocity.x;
   	}
        //update rotation
        if (rotationRate > 0.0) {
            faceAngle += rotationRate;
            if (faceAngle < 0)
                faceAngle = 360 - rotationRate;
            else if (faceAngle > 360)
                faceAngle = rotationRate;
        }  
        
        if (testAnimation ) {
        	animation.doAnimation();
        	return;
        }
        
        int line = BoxFigther.SCR_HEIGHT - BoxFigther.BOTTOM_LINE - frameHeight;
        if (position.y <= line - BoxFigther.JUMP_HEIGHT)
        	velocity.y *= -1; // makes player "fall" down with same speed.
        if (position.y >= line - 1)
        	velocity.y = 0;

        // hitting animation
        if ((aniHitRight != null || aniHitLeft != null) && isHitting()) {
        	doHitAnimation();
        }
        // kicking animation
        else if ((aniKickRight != null || aniKickLeft != null) && isKicking() ) {
        	doKickAnimation();
        }
        // jump
        else if ((aniJumpRight != null || aniJumpRight != null) && isJumping() 
        		&& position.y == line) {
        	doJumpAnimation(); 
        }
        // walking animation 
        else if (velocity.x != 0 || velocity.y != 0) {
        	if (velocity.y != 0)
        		return; // no air walking
        	if (animation != null)
        		animation.doAnimation();
        }
        else if (animationDirection == -1)
        	setCurrentFrame(stanceLeft);
        else if (animationDirection == 1)
        	setCurrentFrame(stanceRight);
    }

    private void doJumpAnimation() {
    	switch (animationDirection) {
    	case 1 :
        	if (getCurrentFrame() == aniJumpRight.endFrame - 1) {
        		this.doJump = false;
        		velocity.y = -BoxFigther.JUMP_SPEED;
        	}
        	aniJumpRight.doAnimation();
    		break;
    	case -1:
        	if (getCurrentFrame() == aniJumpLeft.startFrame) {
        		this.doJump = false;
        		velocity.y = -BoxFigther.JUMP_SPEED;
        	}
        	aniJumpLeft.doAnimation();
    		break;
    	default :
    		break;
    	}
	}

	private void doHitAnimation() {
    	switch (animationDirection) {
    	case 1 :
        	if (getCurrentFrame() == aniHitRight.endFrame - 1)
        		this.doHit = false;
        	aniHitRight.doAnimation();
    		break;
    	case -1:
        	if (getCurrentFrame() == aniHitLeft.startFrame)
        		this.doHit = false;
        	aniHitLeft.doAnimation();
    		break;
    	default :
    		break;
    	}
	}

	private void doKickAnimation() {
    	switch (animationDirection) {
    	case 1 :
        	if (getCurrentFrame() == aniKickRight.endFrame - 1)
        		this.doKick= false;
        	aniKickRight.doAnimation();
    		break;
    	case -1:
        	if (getCurrentFrame() == aniKickLeft.startFrame)
        		this.doKick = false;
        	aniKickLeft.doAnimation();
    		break;
    	default :
    		break;
    	}
	}

	public boolean isKicking() {
		return doKick;
	}

	public boolean isHitting() {
		return doHit;
	}
	
	public boolean isJumping() {
		return this.doJump;
	}

	//draw bounding rectangle around sprite
    public void drawBounds(Color c) {
        g2d.setColor(c);
        g2d.draw(getBounds());
    }

    public void draw() {
        update();
        if (!isOnScreen())
        	return;
        //get the current frame
        int frameX = (getCurrentFrame() % columns) * frameWidth;
        int frameY = (getCurrentFrame() / columns) * frameHeight;
        //draw the frame 
        Point pos = getScrPosition();
        g2d.drawImage(image, pos.x, pos.y, pos.x+frameWidth , pos.y+frameHeight, 
            frameX, frameY, frameX+frameWidth, frameY+frameHeight, null);
    }

    private boolean isOnScreen() {
		return getScrPosition().x >= -frameWidth && 
				getScrPosition().x <= BoxFigther.SCR_WIDTH + frameWidth;
	}

	//check for collision with a rectangular shape
    public boolean collidesWith(Rectangle rect) {
        return (rect.intersects(getBounds()));
    }
    //check for collision with another sprite
    public boolean collidesWith(AnimatedSprite sprite) {
        return (getBounds().intersects(sprite.getBounds()));
    }
    //check for collision with a point
    public boolean collidesWith(Point point) {
        return (getBounds().contains(point.x, point.y));
    }
    
    /**
     * Andrew Thompson stackoverflow
     * @param target
     * @param bi
     * @param xStart 
     * @param yStart 
     * @param imgWidth 
     * @param imgHeight 
     * @return
     */
    public Area getOutline(Color target, BufferedImage bi) {
        // construct the GeneralPath
        GeneralPath gp = new GeneralPath();

        boolean cont = false;
        int targetRGB = target.getRGB();
        for (int xx=0; xx< bi.getWidth(); xx++) {
            for (int yy=0; yy< bi.getHeight(); yy++) {
                if (bi.getRGB(xx,yy)==targetRGB) {
                    if (cont) {
                        gp.lineTo(xx,yy);
                        gp.lineTo(xx,yy+1);
                        gp.lineTo(xx+1,yy+1);
                        gp.lineTo(xx+1,yy);
                        gp.lineTo(xx,yy);
                    } else {
                        gp.moveTo(xx,yy);
                    }
                    cont = true;
                } else {
                    cont = false;
                }
            }
            cont = false;
        }
        gp.closePath();

        // construct the Area from the GP & return it
        return new Area(gp);
    }

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
}
