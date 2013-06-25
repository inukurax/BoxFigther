package controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import sprites.AnimatedSprite;


public class Keys implements KeyListener {

	public static final int JUMP_SPEED = 3;
	private static final double START_SPEED = 6;
    private final Set<Integer> pressed;
	private AnimatedSprite player1;
	
	public Keys(AnimatedSprite player1) {
		this.player1 = player1;
		this.pressed = new HashSet<Integer>();
	}

	@Override
	public void keyPressed(KeyEvent k) {
        pressed.add(k.getKeyCode());
        for (Integer key : pressed)
        	checkKeys(key);
	}
	
	private void checkKeys(Integer key) {
    	switch (key) {
    	
		case KeyEvent.VK_LEFT :
			if (player1.isHitting()) {
				return;
			}
			// changes direction 
			if (player1.animationDirection == 1) {
				player1.animationDirection = -1;
				break;
			}
				
			player1.animation = player1.aniLeft;
			player1.moveAngle = (player1.faceAngle - 90);
			if (player1.velocity.x >= -1)
				player1.velocity.x += calcAngleMoveX(player1.moveAngle) * START_SPEED;
			player1.velocity.x += calcAngleMoveX(player1.moveAngle) * 0.1;
			break;
		case KeyEvent.VK_RIGHT : 
			if (player1.isHitting()) {
				return;
			}
			// changes direction 
			if (player1.animationDirection == -1) {
				player1.animationDirection = 1;
				break;
			}
				
			player1.animation = player1.aniRight;
			player1.moveAngle = (player1.faceAngle + 90);
			if (player1.velocity.x <= 1)
				player1.velocity.x += calcAngleMoveX(player1.moveAngle) * START_SPEED;
			player1.velocity.x += calcAngleMoveX(player1.moveAngle * 0.1) ;
			break;
		case KeyEvent.VK_SPACE : // for hitting
			player1.velocity.x = 0; 
			player1.doHit = true;
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
			if (player1.velocity.x < 0)
				player1.velocity.x = 0;
			if (player1.isHitting())
				pressed.add(KeyEvent.VK_LEFT);

			break;
		case KeyEvent.VK_RIGHT : 
			if (player1.velocity.x > 0) 
				player1.velocity.x = 0;
			if (player1.isHitting())
				pressed.add(KeyEvent.VK_RIGHT);
			break;
		
		case KeyEvent.VK_SPACE :
			player1.animation = null;
			break;
		}
    }

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
