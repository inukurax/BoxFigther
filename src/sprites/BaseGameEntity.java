package sprites;

/**
 * Holds all the accessor and mutator methods for the game entities.
 * @author Hjalte Skjold Jørgensen
 * Contact: https://skjoldcode.com - https://github.com/inukurax
 * Email: skjoldborg94@edb.dk - hjalte94@gmail.com
/*********************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * BaseGameEntity class
 **********************************************************/ 
public abstract class BaseGameEntity {
	protected boolean alive;
	protected double x,y;
	protected double velX, velY; //velocity for x and y
	protected double moveAngle, faceAngle;
	
	public void setAlive(boolean alive) { this.alive = alive; }
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setVelX(double velX) { this.velX = velX; }
	public void setVelY(double velY) { this.velY = velY; }
	public void setMoveAngle(double moveAngle) { this.moveAngle = moveAngle; }
	public void setFaceAngle(double faceAngle) { this.faceAngle = faceAngle; }
	
	public void incFaceAngle(double i) { this.faceAngle += i; }
	public void incMoveAngle(double i) { this.moveAngle += i; }
	public void incX(double i) { this.x += i; }
	public void incY(double i) { this.y += i; }
	public void incVelX(double i) { this.velX += i; }
	public void incVelY(double i) { this.velY += i; }
	
	public boolean isAlive() { return alive; };
	public double getX() { return x; }
	public double getY() { return y; }
	public double getVelX() { return velX; }
	public double getVelY() { return velY; }
	public double getMoveAngle() { return moveAngle; }
	public double getFaceAngle() { return faceAngle; }
	
	//default constructor
	BaseGameEntity() {
		setAlive(false);
		setX(0.0);
		setY(0.0);
		setVelX(0.0);
		setVelY(0.0);
		setFaceAngle(0.0);
		setMoveAngle(0.0);
	}
}