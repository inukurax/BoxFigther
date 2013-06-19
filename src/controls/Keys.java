package controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import run.BoxFigther;
import shapes.Player;

public class Keys implements KeyListener {

	private static final double START_SPEED = 3;
    private final Set<Integer> pressed;
	private Player player1;
	private Player player2;
	private int bottomline;
	
	public Keys(Player player1, Player player2, int bottomline) {
		this.player1 = player1;
		this.player2 = player2;
		this.bottomline = bottomline;
		this.pressed = new HashSet<Integer>();
	}

	@Override
	public void keyPressed(KeyEvent k) {
        pressed.add(k.getKeyCode());
        System.out.println(pressed);
        for (Integer key : pressed)
        	checkKeys(key);
	}
	private void checkKeys(Integer key) {
    	switch (key) {
		case KeyEvent.VK_LEFT :
			player1.setMoveAngle(player1.getFaceAngle() - 90);
			if (player1.getVelX() >= -1)
				player1.setVelX(calcAngleMoveX(player1.getMoveAngle()) * START_SPEED);
			player1.incVelX(calcAngleMoveX(player1.getMoveAngle()) * 0.1);			
			break;
		case KeyEvent.VK_RIGHT : 
			player1.setMoveAngle(player1.getFaceAngle() + 90);
			if (player1.getVelX() <= 1)
				player1.setVelX(calcAngleMoveX(player1.getMoveAngle()) * START_SPEED);
			player1.incVelX(calcAngleMoveX(player1.getMoveAngle()) * 0.1);
			break;
		case KeyEvent.VK_UP : // for jumping
			if (player1.getY() >= bottomline - player1.getHeight()) {
				player1.setMoveAngle(player1.getFaceAngle() + 180);
				player1.incVelY(calcAngleMoveY(player1.getMoveAngle()) * BoxFigther.JUMP_SPEED);
			}
			break;
		case KeyEvent.VK_A :
			player2.setMoveAngle(player2.getFaceAngle() - 90);
			if (player2.getVelX() >= -1)
				player2.setVelX(calcAngleMoveX(player2.getMoveAngle()) * START_SPEED);
			player2.incVelX(calcAngleMoveX(player2.getMoveAngle()) * 0.1);			
			break;
		case KeyEvent.VK_D : 
			player2.setMoveAngle(player2.getFaceAngle() + 90);
			if (player2.getVelX() <= 1)
				player2.setVelX(calcAngleMoveX(player2.getMoveAngle()) * START_SPEED);
			player2.incVelX(calcAngleMoveX(player2.getMoveAngle()) * 0.1);
			break;
		case KeyEvent.VK_W : // for jumping
			if (player2.getY() >= bottomline - player2.getHeight()) {
				player2.setMoveAngle(player2.getFaceAngle() + 180);
				player2.incVelY(calcAngleMoveY(player2.getMoveAngle()) * BoxFigther.JUMP_SPEED);
			}
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
        pressed.remove(keyCode);

		// makes sure we dont keep moving when key is released.
		switch (keyCode) {
		case KeyEvent.VK_LEFT :
			if (player1.getVelX() < 0)
				player1.setVelX(0);
			break;
		case KeyEvent.VK_RIGHT : 
			if (player1.getVelX() > 0)
				player1.setVelX(0);
			break;
		case KeyEvent.VK_A :
			if (player2.getVelX() < 0)
				player2.setVelX(0);
			break;
		case KeyEvent.VK_D : 
			if (player2.getVelX() > 0)
				player2.setVelX(0);
			break;
		}
    }

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
