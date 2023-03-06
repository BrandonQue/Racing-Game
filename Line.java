
import java.awt.*;

public class Line
{
	int x0;
	int y0;
	
	int x1;
	int y1;
	
	double xu;
	double yu;
	
	double xN;
	double yN;
	
	public Line(int x0, int y0, int x1, int y1)
	{
		this.x0 = x0;
		this.y0 = y0;
		
		this.x1 = x1;
		this.y1 = y1;
		
		double xv = x1 - x0;
		double yv = y1 - y0;
		
		double mag = Math.sqrt(xv*xv + yv*yv);
		
		xu = xv / mag;
		yu = yv / mag;
		
		xN = -yu;
		yN =  xu;
	}
	
	public double signedDistanceTo(Circle c)
	{
		return (c.x - x0)*xN + (c.y - y0)*yN;
	}
	
	public double signedDistanceTo(Car c) {
		return (c.x - x0)*xN + (c.y - y0)*yN;
	}
	
	public boolean overlaps(Circle c)
	{
		double d = signedDistanceTo(c);
		
		return d <= c.r;
	}
	
	public void addPointsToQuadTree(QuadTree qt) {
		double xv = x1 - x0;
		double yv = y1 - y0;
		
		int minx = min(x0, x1);
		int maxx = max(x0, x1);
		
		int miny = min(y0, y1);
		int maxy = max(y0, y1);
		
		if(yv == 0) {
			for(int i = minx; i < maxx; i++) qt.insert(new Point(i, y0, this));
		} else if (xv == 0) {
			for(int i = miny; i < maxy; i++) qt.insert(new Point(x0, i, this));
		}
	}
	
	public int min(int a, int b) {
		if (a <= b) return a;
		else return b;
	}
	
	public int max(int a, int b) {
		if (a >= b) return a;
		else return b;
	}
	
	public void draw(Graphics g)
	{
		g.drawLine(x0, y0, x1, y1);
	}
}