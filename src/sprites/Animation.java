package sprites;

public class Animation {

	public int endFrame;
	private AnimatedSprite as;
	public int startFrame;
	private int frameDelay = 1;

	public Animation (AnimatedSprite animatedSprite, int startFrame,
			int endFrame, int frameDelay) {
		as = animatedSprite;
		this.endFrame = endFrame;
		this.startFrame = startFrame;
		this.frameDelay = frameDelay;
	}
	
	public void doAnimation() {			
		if (as.currentFrame < startFrame || as.currentFrame > endFrame) {
			if (as.animationDirection == 1)
				as.currentFrame = startFrame;
			else
				as.currentFrame = endFrame - 1;
		}

        //update animation
        if (endFrame - startFrame > 1) {
        	as.frameCount++;
            if (as.frameCount > frameDelay) {
            	as.frameCount = 0;
            	as.currentFrame += as.animationDirection;
                if (as.currentFrame > endFrame - 1) {
                	as.currentFrame = startFrame;
                }
                else if (as.currentFrame < startFrame) {
                	as.currentFrame = endFrame - 1;
                }
            }
        }
	}
}
