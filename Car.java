
import java.security.SecureRandom;
import java.util.Random;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


public class Car {
	
	boolean isDetected;
	
	double x;
	double y;
	
	int w;
	int h;
	
	double halfw;
	double halfh;
	
	Circle c1;
	Circle c2;
	
	int angle;
	
	double speed = 0;
	static double topSpeed = 6;
	static double acceleration = 0.1 ;
	
	boolean stunned = false;
	static int stunCount = 20;
	int stunTime = stunCount;
	Line stunLine;
	
	int lap = 0;
	boolean isCrossing;
	String place;
	boolean finished;
	
	static Random rnd = new SecureRandom();
	
	static String[] pose = {"base", "left", "right", "idle", "stun"};
	Sprite carSprite;
	int color = -1;
	
	public static final String[] colors = {
			"black",
			"blue",
			"brown",
			"darkRed",
			"gold",
			"green",
			"indigo",
			"lightblue",
			"navy",
			"orange",
			"pink",
			"purple",
			"red",
			"silver",
			"white",
			"yellow",
	};
	
	public static final int BLACK = 0;
	public static final int BLUE = 1;
	public static final int BROWN = 2;
	public static final int DARKRED = 3;
	public static final int GOLD = 4;
	public static final int GREEN = 5;
	public static final int INDIGO = 6;
	public static final int LIGHTBLUE = 7;
	public static final int NAVY = 8;
	public static final int ORANGE = 9;
	public static final int PINK = 10;
	public static final int PURPLE = 11;
	public static final int RED = 12;
	public static final int SILVER = 13;
	public static final int WHITE = 14;
	public static final int YELLOW = 15;
	
	public static final int WIDTH = 22;
	public static final int HEIGHT = 37;
	
	public Car() {
		x = 0;
		y = 0;
		
		w = WIDTH;
		h = HEIGHT;
		
		halfw = w/2.0;
		halfh = h/2.0;
		
		c1 = new Circle(x, y, (int)(halfw * 0.85), 0, this);
		c2 = new Circle(x, y, (int)(halfw * 1.15), 0, this);
		
		allignCircles();
		
		angle = 0;
		
		carSprite = new Sprite("car", colors[rnd.nextInt(colors.length)], pose, 3, ".png", 2);
	}
	
	public Car(int color) {
		x = 0;
		y = 0;
		
		w = WIDTH;
		h = HEIGHT;
		
		halfw = w/2.0;
		halfh = h/2.0;
		
		c1 = new Circle(x, y, (int)(halfw * 0.85), 0, this);
		c2 = new Circle(x, y, (int)(halfw * 1.15), 0, this);
		
		allignCircles();
		
		angle = 0;
		
		carSprite = new Sprite("car", colors[color], pose, 3, ".png", 2);
	}
	
	public Car(double x, double y, int w, int h, int angle) {
		this.x = x;
		this.y = y;
		
		this.w = w;
		this.h = h;
		
		halfw = w/2.0;
		halfh = h/2.0;
		
		c1 = new Circle(x, y, (int)(halfw * 0.85), 0, this);
		c2 = new Circle(x, y, (int)(halfw * 1.15), 0, this);
		
		allignCircles();
		
		this.angle = angle;
		
		carSprite = new Sprite("car", colors[rnd.nextInt(colors.length)], pose, 3, ".png", 2);
	}
	
	public Car(double x, double y, int w, int h, int angle, int color) {
		this.x = x;
		this.y = y;
		
		this.w = w;
		this.h = h;
		
		halfw = w/2.0;
		halfh = h/2.0;
		
		c1 = new Circle(x, y, (int)(halfw * 0.85), 0, this);
		c2 = new Circle(x, y, (int)(halfw * 1.15), 0, this);
		
		allignCircles();
		
		this.angle = angle;
		
		carSprite = new Sprite("car", colors[color], pose, 3, ".png", 2);
	}
	
	public void startRace() {
		place = null;
		carSprite.idle();
		speed = 0;
		finished = false;
		lap = 0;
	}
	
	public boolean overlaps(Rect r) {
		return(c1.overlaps(r) || c2.overlaps(r));
	}
	
	public boolean overlaps(Line L) {
		return(c1.overlaps(L) || c2.overlaps(L));
	}
	
	public boolean overlaps(Car c) {
		return(c1.overlaps(c.c1) || c1.overlaps(c.c2) || c2.overlaps(c.c1) || c2.overlaps(c.c2));
	}
	
	public void moveAwayFromEachOther(Car c) {
		
		double dx = (c.x - x);
		double dy = (c.y - y);
		
		double d = Math.sqrt(dx * dx + dy * dy);
		
		dx /= d/3;
		dy /= d/3;
		
		c.x += dx;
		c.y += dy;

		x -= dx;
		y -= dy;
	}
	
	public void checkCarCollision(Car other) {
		if(this.overlaps(other)) {
			moveAwayFromEachOther(other);
		}
	}
	
	public void moveForwardBy(int d) {
		between0And360();
		double cosA = Lookup.cos[angle];
		double sinA = Lookup.sin[angle];
		
		double dx = d * cosA;
		double dy = d * sinA;
		
		x += dx;
		y += dy;
		
		allignCircles();
	}
	
	public void handleStun() {
		carSprite.stunned();
		if(stunTime == 0) {
			stunned = false;
			stunTime = stunCount;
			stunLine = null;
		} else {
			stunTime -= 1;
			collides(stunLine, c1);
		}
	}
	
	public void accelerate() {
		between0And360();
		carSprite.goStraight();
		
		double cosA = Lookup.cos[angle];
		double sinA = Lookup.sin[angle];
		
		if (speed < topSpeed) {
			if (speed >= 0) speed += acceleration;
			if (speed < 0) speed += 3 * acceleration;
		}
		
		double dx = speed * cosA;
		double dy = speed * sinA;
		
		x += dx;
		y += dy;
		
		allignCircles();
	}
	
	public void reverse() {
		between0And360();
		carSprite.goStraight();
		
		double cosA = Lookup.cos[angle];
		double sinA = Lookup.sin[angle];
		
		if (speed > -topSpeed) {
			if (speed <= 0) speed -= acceleration;
			if (speed > 0) speed -= 3 * acceleration;
		}
		
		double dx = speed * cosA;
		double dy = speed * sinA;
		
		x += dx;
		y += dy;
		
		allignCircles();
	}
	
	public void decelerate() {
		between0And360();
		carSprite.goStraight();
		
		double cosA = Lookup.cos[angle];
		double sinA = Lookup.sin[angle];
		
		if (speed > 0) speed -= acceleration;
		if (speed < 0) speed += acceleration;
		if (Math.abs(speed) < 0.1) speed = 0;
		
		if(speed == 0) carSprite.idle();
		
		double dx = speed * cosA;
		double dy = speed * sinA;
		
		x += dx;
		y += dy;
		
		allignCircles();
	}
	
	public void rotateIfMoving(boolean ltPressed, boolean rtPressed, double rotMultiplier) {
		// Rotation angle based on speed
		int rotSpeed = (int)(rotMultiplier * Math.abs(speed) / topSpeed)+1;
		
		if(ltPressed) {
			carSprite.turnLeft();
			if(speed > 0) rotateLeftBy(rotSpeed);
			if(speed < 0) rotateRightBy(rotSpeed);
		}
		if(rtPressed) {
			carSprite.turnRight();
			if(speed > 0) rotateRightBy(rotSpeed);
			if(speed < 0) rotateLeftBy(rotSpeed);
		}
		
		if(rtPressed && ltPressed) carSprite.goStraight();
		
		allignCircles();
	}
	
	public void rotateLeftBy(int a) {
		angle -= a;

		angle = between0And360(angle);
	}
	
	public void rotateRightBy(int a) {
		angle += a;

		angle = between0And360(angle);
	}
	
	public void handleOverlaps(Line L) {
		if (c1.overlaps(L)) {
			if(Math.abs(speed) > topSpeed * 0.5) {
				stunned = true;
				speed = 0;
			}
			stunLine = L;
			collides(L, c1);
		} else if  (c2.overlaps(L)) {
			if(Math.abs(speed) > topSpeed * 0.5) {
				stunned = true;
				speed = 0;
			}
			stunLine = L;
			collides(L, c2);
		}
	}
	
	public void collides(Line L, Circle ci) {
		ci.moveBackFrom(L, this, 0.5);
		
		allignCircles();
	}
	
	public void allignCircles() {
		angle = between0And360(angle);
		double cosA = Lookup.cos[angle];
		double sinA = Lookup.sin[angle];
		
		c1.x = x + w * 0.6 * cosA;
		c1.y = y + w * 0.6 * sinA;
		c1.angle = angle;
		
		c2.x = x - w * 0.4 * cosA;
		c2.y = y - w * 0.4 * sinA;
	}
	
	public int between0And360(int a) {
		while(a >= 360) a -= 360;
		while(a < 0)    a += 360;
		return a;
	}
	
	public void between0And360() {
		while(angle >= 360) angle -= 360;
		while(angle < 0)    angle += 360;
	}
	
	public void changeColor() {
		carSprite = new Sprite("car", colors[rnd.nextInt(colors.length)], pose, 3, ".png", 2);
	}
	
	public void changeColor(String color) {
		carSprite = new Sprite("car", color, pose, 3, ".png", 2);
	}
	
	public void changeColor(int color) {
		carSprite = new Sprite("car", colors[color], pose, 3, ".png", 2);
	}

	public void drawHitbox(Graphics g) {
		c1.draw(g);
		c2.draw(g);
	}
	
	public void draw(Graphics g) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Lookup.degsToRads[between0And360(angle+90)]);
		transform.translate(-halfw, -halfh);
		transform.scale(0.1, 0.1);
		
		AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

		BufferedImage image = carSprite.getImage((speed/topSpeed));//ImageIO.read(new File("Images/f1_base.png"));
		
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(image, transformOp, (int)x, (int)y);
	}

}
