import java.awt.Color;
import java.awt.Graphics;
import java.security.SecureRandom;
import java.util.Random;

public class CarAI extends Car{
	
	int followAngle = 80;
	double rotation = 2.5;
	
	static int avoid = 100;
	static int hAvoid = avoid/2;
	static int tAvoid = avoid/10;
	
	Line mid;
	Line left;
	Line right;
	
	Line front;
	
	Random rnd = new SecureRandom();
	
	public CarAI() {
		super();
		setLines();
		
		rotation = rnd.nextDouble()+2;
		followAngle = rnd.nextInt(30)+65;
	}
	
	public CarAI(int color) {
		super(color);
		setLines();
		
		rotation = rnd.nextDouble()+2;
		followAngle = rnd.nextInt(30)+65;
	}

	public CarAI(double x, double y, int w, int h, int angle) {
		super(x, y, w, h, angle);
		setLines();
		
		rotation = rnd.nextDouble()+2;
		followAngle = rnd.nextInt(30)+65;
	}

	public CarAI(double x, double y, int w, int h, int angle, int color) {
		super(x, y, w, h, angle, color);
		setLines();
		
		rotation = rnd.nextDouble()+2;
		followAngle = rnd.nextInt(30)+65;
	}
	
	public void followPath(TileGrid tG) {
		Rect followRect = new Rect(0, 0, 0, 0);
		
		for(int i = 1; i <= tG.pathCount; i++) {
			if(this.overlaps(tG.rect[i])) {
				followRect = tG.rect[(i%(tG.pathCount))+1];
			}
		}
		
		int rndOffset = 100;
		Point rndPoint = new Point(
				followRect.midPoint.x + rnd.nextInt(rndOffset)-rndOffset/2, 
				followRect.midPoint.y + rnd.nextInt(rndOffset)-rndOffset/2, 
				followRect
		);
		
		double d = c1.signedDistance(rndPoint);
		
		accelerate();
		
		boolean ltPressed = false;
		boolean rtPressed = false;
		
		if(d > followAngle) ltPressed = true;
		if(d < -followAngle) rtPressed = true;
		
		rotateIfMoving(ltPressed, rtPressed, rotation);
		
	}
	
	public void setLines() {
		between0And360();
		double cosA = Lookup.cos[angle];
		double sinA = Lookup.sin[angle];
		
		front = new Line((int)(x - hAvoid*sinA), (int)(y + hAvoid*cosA), (int)(x + hAvoid*sinA), (int)(y - hAvoid*cosA));
		
		mid = new Line((int)x, (int)y, (int)(x + avoid*cosA), (int)(y + avoid*sinA));
		left = new Line((int)(x + tAvoid*sinA), (int)(y - tAvoid*cosA), (int)(x + avoid*cosA + tAvoid*sinA), (int)(y + avoid*sinA - tAvoid*cosA));
		right = new Line((int)(x - tAvoid*sinA), (int)(y + tAvoid*cosA), (int)(x + avoid*cosA - tAvoid*sinA), (int)(y + avoid*sinA + tAvoid*cosA));
	}
	
	public void avoidWallsAndCars(Car[] cars) {
		avoidCars(cars);
	}
	
	private void avoidCars(Car[] cars) {	
		for(int i = 0; i < cars.length; i++) {
			Circle c = cars[i].c2;
			
			if(cars[i] != this) {
				cars[i].isDetected = false;
				if(front.signedDistanceTo(c) > 0 && front.signedDistanceTo(c) < avoid) {
					if(mid.signedDistanceTo(c) < c.r && mid.signedDistanceTo(c) > -c.r) {						
						if(mid.signedDistanceTo(c) > 0) {
							rotateIfMoving(true, false, rotation);
						} else {
							rotateIfMoving(false, true, rotation);
						}
					}
				}				
			}
		}
	}
	
	@Override
	public void drawHitbox(Graphics g) {
		super.drawHitbox(g);
		
		setLines();
		front.draw(g);
		mid.draw(g);
		left.draw(g);
		right.draw(g);
	}

}
