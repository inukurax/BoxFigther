package run;
import java.awt.Point;

import sprites.AnimatedSprite;



public class Camera {

	private int offsetMaxX;
	private int offsetMaxY;
	private int offsetMinX;
	private int offsetMinY;
	private int playerX;
	private AnimatedSprite player;
	private int playerY;
	private int worldWidth;
	public int getWorldWidth() {
		return worldWidth;
	}



	public int getWorldHeight() {
		return worldHeight;
	}

	private int worldHeight;

	public void setPlayer(AnimatedSprite player) {
		this.player = player;
	}



	public Camera(int WORLD_SIZE_X, int WORLD_SIZE_Y) {
		worldWidth = WORLD_SIZE_X;
		worldHeight = WORLD_SIZE_Y;
		offsetMaxX = WORLD_SIZE_X - BoxFigther.SCR_WIDTH;
		offsetMaxY = WORLD_SIZE_Y - BoxFigther.SCR_HEIGHT;
		offsetMinX = 0;
		offsetMinY = 0;
		this.playerX = 0;
		this.playerY = 0;
	}
	
	public Point getPlayerPoint() {
		int camX = getPlayerX() - BoxFigther.SCR_WIDTH / 2;
		int camY = getPlayerY() - BoxFigther.SCR_HEIGHT / 2;
		if (camX > offsetMaxX)
		    camX = offsetMaxX;
		else if (camX < offsetMinX)
		    camX = offsetMinX;
		
		if (camY > offsetMaxY)
		    camY = offsetMaxY;
		else if (camY < offsetMinY)
		    camY = offsetMinY;
		return new Point(camX, camY);
	}
	public int getPlayerX() {
		return player.position.x;
	}

	public int getPlayerY() {
		return player.position.y;
	}
}
