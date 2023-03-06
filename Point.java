import java.awt.Graphics;

public class Point {
	
	int x;
	int y;
	Line lineFrom;
	Car carFrom;
	Rect rectFrom;
	
	public Point(int x, int y, Line lineFrom) {
		this.x = x;
		this.y = y;
		this.lineFrom = lineFrom;
	}
	
	public Point(int x, int y, Car carFrom) {
		this.x = x;
		this.y = y;
		this.carFrom = carFrom;
	}
	
	public Point(int x, int y, Rect rectFrom) {
		this.x = x;
		this.y = y;
		this.rectFrom = rectFrom;
	}

	public void draw(Graphics g) {
		g.fillOval(x-3, y-3, 6, 6);
		//g.drawLine(x, y, x, y);
	}
	
}
