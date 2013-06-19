package shapes;
import java.awt.Shape;

public  abstract class BaseShape {
	private static final double MAX_SPEED = 7;
	private Shape shape;
	private double x,y;
	private double velX, velY; //velocity for x and y
	private double moveAngle, faceAngle;
	
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setVelX(double velX) { this.velX = velX; }
	public void setVelY(double velY) { this.velY = velY; }
	
	public void setShape(Shape shape) { this.shape = shape; }
	public void setMoveAngle(double moveAngle) { this.moveAngle = moveAngle; }
	public void setFaceAngle(double faceAngle) { this.faceAngle = faceAngle; }
	public void incFaceAngle(double i) { this.faceAngle += i; }
	public void incMoveAngle(double i) { this.moveAngle += i; }
	public void incX(double i) { this.x += i; }
	public void incY(double i) { this.y += i; }
	
	public void incVelX(double i) {
		if (this.getVelX() > MAX_SPEED || this.getVelX() <  - MAX_SPEED)
			return;
		this.velX += i;
	}
	public void incVelY(double i) {
		this.velY += i;
	}

	public Shape getShape() { return shape; }
	public double getX() { return x; }
	public double getY() { return y; }
	public double getVelX() { return velX; }
	public double getVelY() { return velY; }
	public double getMoveAngle() { return moveAngle; }
	public double getFaceAngle() { return faceAngle; }

	public double calcAngleMoveX(double moveAngle) {
		return (double) (Math.sin(moveAngle * Math.PI / 180));
	}
}
