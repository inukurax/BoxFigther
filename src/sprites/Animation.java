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
		if (as.getCurrentFrame() < startFrame || as.getCurrentFrame() > endFrame) {
			if (as.animationDirection == 1)
				as.setCurrentFrame(startFrame);
			else // expecting animationDirection == -1
				as.setCurrentFrame(endFrame - 1);
		}

        //update animation
        if (endFrame - startFrame > 1) {
        	as.frameCount++;
            if (as.frameCount > frameDelay) {
            	as.frameCount = 0;
            	as.setCurrentFrame(as.getCurrentFrame()
						+ as.animationDirection);
                if (as.getCurrentFrame() > endFrame - 1) {
                	as.setCurrentFrame(startFrame);
                }
                else if (as.getCurrentFrame() < startFrame) {
                	as.setCurrentFrame(endFrame - 1);
                }
            }
        }
	}
}
