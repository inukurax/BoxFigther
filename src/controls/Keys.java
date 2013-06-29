package controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import run.BoxFigther;
import run.BoxFigther.GameState;

import sprites.AnimatedSprite;


public class Keys implements KeyListener {

	public static final int JUMP_SPEED = 3;
	private static final double START_SPEED = 6;
    private final Set<Integer> pressed;
	private AnimatedSprite player1;
	private AnimatedSprite player2;
	
	public Keys(AnimatedSprite player1, AnimatedSprite player2) {
		this.player1 = player1;
		this.player2 = player2;
		this.pressed = new HashSet<Integer>();
	}

	@Override
	public void keyPressed(KeyEvent k)  {
		if (BoxFigther.GAME_STATE != GameState.GAME_PAUSE && k.getKeyCode() == KeyEvent.VK_ESCAPE)
			BoxFigther.GAME_STATE = GameState.GAME_PAUSE;
		else if (BoxFigther.GAME_STATE == GameState.GAME_PAUSE && k.getKeyCode() == KeyEvent.VK_ESCAPE)
			BoxFigther.GAME_STATE = GameState.GAME_RUNNING;

		if (BoxFigther.GAME_STATE != GameState.GAME_RUNNING)
			return;
        pressed.add(k.getKeyCode());
        for (Integer key : pressed)
        	checkKeys(key);
	}
	
	private void checkKeys(Integer key) {
    	switch (key) {
    	
		case KeyEvent.VK_LEFT :
			if (player1.isHitting() || player1.isKicking()) {
				return;
			}
			// changes direction 
			if (player1.animationDirection == 1) {
				player1.animationDirection = -1;
				break;
			}
			player1.animation = player1.animationWalkLeft;
			player1.moveAngle = (player1.faceAngle - 90);
			if (player1.velocity.x >= -1)
				player1.velocity.x += calcAngleMoveX(player1.moveAngle) * START_SPEED;
			player1.velocity.x += calcAngleMoveX(player1.moveAngle) * 0.1;
			break;
		case KeyEvent.VK_RIGHT : 
			if (player1.isHitting() || player1.isKicking()) {
				return;
			}
			// changes direction 
			if (player1.animationDirection == -1) {
				player1.animationDirection = 1;
				break;
			}
				
			player1.animation = player1.animationWalkRight;
			player1.moveAngle = (player1.faceAngle + 90);
			if (player1.velocity.x <= 1)
				player1.velocity.x += calcAngleMoveX(player1.moveAngle) * START_SPEED;
			player1.velocity.x += calcAngleMoveX(player1.moveAngle * 0.1) ;
			break;
		case KeyEvent.VK_M : // for hitting
			player1.velocity.x = 0; 
			player1.doHit = true;
			break;
		case KeyEvent.VK_N : //kicking
			player1.velocity.x = 0; 
			player1.doKick = true;
			break;
		case KeyEvent.VK_UP :
			player1.doJump = true;
			player1.velocity.x = 0;
			break;
			//player2
		case KeyEvent.VK_A :
			if (player2.isHitting() || player2.isKicking()) {
				return;
			}
			// changes direction 
			if (player2.animationDirection == 1) {
				player2.animationDirection = -1;
				break;
			}
				
			player2.animation = player2.animationWalkLeft;
			player2.moveAngle = (player2.faceAngle - 90);
			if (player2.velocity.x >= -1)
				player2.velocity.x += calcAngleMoveX(player2.moveAngle) * START_SPEED;
			player2.velocity.x += calcAngleMoveX(player2.moveAngle) * 0.1;
			break;
		case KeyEvent.VK_D : 
			if (player2.isHitting() || player2.isKicking()) {
				return;
			}
			// changes direction 
			if (player2.animationDirection == -1) {
				player2.animationDirection = 1;
				break;
			}
				
			player2.animation = player2.animationWalkRight;
			player2.moveAngle = (player2.faceAngle + 90);
			if (player2.velocity.x <= 1)
				player2.velocity.x += calcAngleMoveX(player2.moveAngle) * START_SPEED;
			player2.velocity.x += calcAngleMoveX(player2.moveAngle * 0.1) ;
			break;
		case KeyEvent.VK_F : // for hitting
			player2.velocity.x = 0; 
			player2.doHit = true;
			break;
		case KeyEvent.VK_G : //kicking
			player2.velocity.x = 0; 
			player2.doKick = true;
			break;
		case KeyEvent.VK_W :
			player2.doJump = true;
			player2.velocity.x = 0;
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
			break;
		case KeyEvent.VK_RIGHT : 
			if (player1.velocity.x > 0) 
				player1.velocity.x = 0;
			break;
		
		case KeyEvent.VK_M :
			player1.animation = null;
			break;
		case KeyEvent.VK_N :
			player1.animation = null;
			break;
		case KeyEvent.VK_A :
			if (player2.velocity.x < 0)
				player2.velocity.x = 0;
			break;
		case KeyEvent.VK_D : 
			if (player2.velocity.x > 0) 
				player2.velocity.x = 0;
			break;
		case KeyEvent.VK_F :
			player2.animation = null;
			break;
		case KeyEvent.VK_G :
			player2.animation = null;
			break;
		}
    }
    
	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
