
import java.awt.*;

public class Rect
{
	int x;
	int y;
	
	int w;
	int h;
	
	Point midPoint;
	
	public Rect(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
				
		this.w = w;
		this.h = h;
		
		midPoint = new Point(x+w/2, y+h/2, this);
	}
	
	public Rect(double x, double y, double w, double h)
	{
		this.x = (int)x;
		this.y = (int)y;
				
		this.w = (int)w;
		this.h = (int)h;

		midPoint = new Point((int)(x+w/2), (int)(y+h/2), this);
	}
	
	public boolean overlaps(Rect r)
	{
		return (y+h >= r.y) && (r.y+r.h >= y) && (x+w >= r.x) && (r.x+r.w >= x);
	}
	
	public boolean contains(int mx, int my)
	{
		return 	(mx >= x)      &&
				(mx <= x + w)  &&
				(my >= y)      &&
				(my <= y + h);
	}
	
	public void addPointsToQuadTree(QuadTree qT) {
		qT.insert(midPoint);
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
	
	public void draw(Graphics g)
	{
		g.drawRect(x, y, w, h);
	}

}