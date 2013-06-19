package shapes;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;


public class Player extends BaseShape {
	private int height;
	private int width;

	private Area area;

	public Player(BufferedImage image) {
		area = this.getOutline(Color.BLACK, image);
		this.setShape(area);
		this.height = this.getShape().getBounds().height;
		this.width = this.getShape().getBounds().width;
	}
	
	public Rectangle getBounds() {
		return area.getBounds();
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public Area getOutline(Color target, BufferedImage bi) {
	    // construct the GeneralPath
	    GeneralPath gp = new GeneralPath();

	    boolean cont = false;
	    int targetRGB = target.getRGB();
	    for (int xx=0; xx<bi.getWidth(); xx++) {
	        for (int yy=0; yy<bi.getHeight(); yy++) {
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

}
