
import java.awt.*;

public class Circle
{
	double x;
	double y;
	
	int r;

	int angle;
	
	Car ofCar;
	
	public Circle(double x, double y, int r, int angle)
	{
		this.x = x;
		this.y = y;
		
		this.r = r;
		
		this.angle = angle;
		
		ofCar = null;
	}
	
	public Circle(double x, double y, int r, int angle, Car ofCar){
		this.x = x;
		this.y = y;
		
		this.r = r;
		
		this.angle = angle;
		
		this.ofCar = ofCar;
	}
	
	public void chase(Circle c)
	{
		turnToward(c);
		
		double dx = c.x - x;
		double dy = c.y - y;
		
		double d2 = dx*dx + dy*dy;
		
		if (d2 > 40000)    moveForwardBy(4);
	}
	
	public void evade(Circle c)
	{
		turnAwayFrom(c);
		
		double dx = c.x - x;
		double dy = c.y - y;
		
		double d2 = dx*dx + dy*dy;
		
		if (d2 < 40000)    moveForwardBy(4);
	}
	
	public void turnToward(Circle c)
	{
		double d = signedDistance(c);
		
		if(d >  10)  rotateLeftBy(3);		
	
		if(d < -10)  rotateRightBy(3);
	}
	
	public void turnAwayFrom(Circle c)
	{
		double d = signedDistance(c);
		
		if(d <  -10)  rotateLeftBy(4);		
	
		if(d >   10)  rotateRightBy(4);
	}
	
	public double signedDistance(Circle c)
	{
		double cosA = Lookup.cos[angle];
		double sinA = Lookup.sin[angle];
		
		return (c.x - x)*sinA - (c.y - y)*cosA;
	}
	
	public double signedDistance(Point p)
	{
		double cosA = Lookup.cos[angle];
		double sinA = Lookup.sin[angle];
		
		return (p.x - x)*sinA - (p.y - y)*cosA;
	}

	public boolean overlaps(Circle c)
	{
		double dx = x - c.x;
		double dy = y - c.y;
		
		double d2 = dx*dx + dy*dy;
		
		return d2 <= (r + c.r) * (r + c.r);
	}
	
	public boolean overlaps(Line L)
	{
		double d = L.signedDistanceTo(this);
		
		return d <= r;
	}
	
	public boolean overlaps(Rect rec) {
		int nearestx = max(rec.x, min((int)x, rec.x + rec.w));
		int nearesty = max(rec.y, min((int)y, rec.y + rec.h));
		
		int dx = (int)x - nearestx;
		int dy = (int)y - nearesty;
		
		return (dx * dx + dy * dy) < (r * r);
	}
	
	public int distanceFrom(Rect rec) {
		int nearestx = max(rec.x, min((int)x, rec.x + rec.w));
		int nearesty = max(rec.y, min((int)y, rec.y + rec.h));
		
		int dx = (int)x - nearestx;
		int dy = (int)y - nearesty;
		
		return (dx * dx + dy * dy);
	}
	
	public void moveBackFrom(Line L)
	{
		double d = L.signedDistanceTo(this);
		
		double mag = r - d;
		
		x += 2 * mag * L.xN;
		y += 2 * mag * L.yN;
	}
	
	public void moveBackFrom(Line L, Car ca, double pushForce)
	{
		
		ca.x += pushForce * L.xN;
		ca.y += pushForce * L.yN;	
		
	}
	
	public void rotateBy(int dangle)
	{
	   angle += dangle;
	   
	   while(angle >= 360) angle -= 360;
  	}

   
	
	public void rotateLeftBy(int dangle)
	{
	   angle -= dangle;
	   
	   while(angle < 0) angle += 360;
  	}

   
	
	public void rotateRightBy(int dangle)
	{
	   angle += dangle;
	   
	   if(angle >= 360)   angle -= 360;
  	}

   
   public void moveForwardBy(int distance)
   {
   	double radians = angle*Math.PI/180;
   	double dx = distance * Math.cos(radians);
   	double dy = distance * Math.sin(radians);
   	
   	x += dx;
   	y += dy;
   }

   
	public void moveLeftBy(int dx)
	{
		x -= dx;
	}

	
	public void moveRightBy(int dx)
	{
		x += dx;
	}

	
	public void moveUpBy(int dy)
	{
		y -= dy;
	}

	
	public void moveDownBy(int dy)
	{
		y += dy;
	}

	
	public void moveBy(int dx, int dy)
	{
	   x += dx;
	   y += dy;
	}
	
	public int min(int a, int b) {
		if (a <= b) return a;
		else return b;
	}
	
	public int max(int a, int b) {
		if (a >= b) return a;
		else return b;
	}

	public void drawLine(Graphics g) {
		double cosA = Lookup.cos[angle];
		double sinA = Lookup.sin[angle];
		
		g.drawLine((int)x, (int)y, (int)(x + r * 20 * cosA), (int)(y + r * 20 * sinA));
	}
	
	public void draw(Graphics g)
	{
//		g.setColor(Color.yellow);
//		g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
//		g.setColor(Color.black);
		g.drawOval((int)(x-r), (int)(y-r), 2*r, 2*r);
		
		double cosA = Lookup.cos[angle];
		double sinA = Lookup.sin[angle];
				
	}
	
}