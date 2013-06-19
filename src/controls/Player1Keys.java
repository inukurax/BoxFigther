package controls;
import java.awt.event.KeyEvent;
import run.BoxFigther;
import java.awt.event.KeyListener;
import shapes.Player;

public class Player1Keys implements KeyListener {
	private static final double START_SPEED = 3;
	private Player player;
	private int bottomline;
	
	public Player1Keys(Player player, int bottomline) {
		this.player = player;
		this.bottomline = bottomline;
	}

	@Override
	public void keyPressed(KeyEvent k) {
		int keyCode = k.getKeyCode();

		switch (keyCode) {
		case KeyEvent.VK_LEFT :
			player.setMoveAngle(player.getFaceAngle() - 90);
			if (player.getVelX() >= -1)
				player.setVelX(calcAngleMoveX(player.getMoveAngle()) * START_SPEED);
			player.incVelX(calcAngleMoveX(player.getMoveAngle()) * 0.1);			
			break;
		case KeyEvent.VK_RIGHT : 
			player.setMoveAngle(player.getFaceAngle() + 90);
			if (player.getVelX() <= 1)
				player.setVelX(calcAngleMoveX(player.getMoveAngle()) * START_SPEED);
			player.incVelX(calcAngleMoveX(player.getMoveAngle()) * 0.1);
			break;
		case KeyEvent.VK_UP : // for jumping
			if (player.getY() >= bottomline - player.getHeight()) {
				player.setMoveAngle(player.getFaceAngle() + 180);
				player.incVelY(calcAngleMoveY(player.getMoveAngle()) * BoxFigther.JUMP_SPEED);
			}
			break;
		case KeyEvent.VK_SPACE :
			break;
		}
	}

	public static double calcAngleMoveY(double moveAngle) {
		return (double) (Math.cos(moveAngle * Math.PI / 180));
	}

	public double calcAngleMoveX(double moveAngle) {
		return (double) (Math.sin(moveAngle * Math.PI / 180));
	}

	@Override
	public void keyReleased(KeyEvent k) {
		int keyCode = k.getKeyCode();
		// makes sure we dont keep moving when key is released.
		switch (keyCode) {
		case KeyEvent.VK_LEFT :
			if (player.getVelX() < 0)
				player.setVelX(0);
			break;
		case KeyEvent.VK_RIGHT : 
			if (player.getVelX() > 0)
				player.setVelX(0);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent k) {
	}
}
