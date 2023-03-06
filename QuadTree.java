import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class QuadTree {
	
	static int levels = 0;
	
	double x;
	double y;
	double w;
	double h;
	
	Rect rect;
	
	int maxPoints;
	
	boolean divided = false;
	
	QuadTree nw;
	QuadTree ne;
	QuadTree sw;
	QuadTree se;
	
	List<Point> points;

	public QuadTree(double x, double y, double w, double h, int maxPoints){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		rect = new Rect(x, y, w, h);
		
		this.maxPoints = maxPoints;
		
		points = new ArrayList<>(maxPoints);
	}
	
	public void insert(Point p) {
		//System.out.println("Inserting "+p.x+", "+p.y);
		if(p.x < x || p.x > x+w || p.y < y || p.y > y+h) {
			//System.out.println("p.x = "+p.x+", p.y = "+p.y+", x = "+x+", y = "+y+", w = "+w+", h = "+h);
			System.out.println("Point does not belong in this SubTree");
		} else {
			if (divided) {
				addPointToSubtree(p);
			} else {
				if(points.size() == maxPoints) {
					divide();
					
					points.add(p);
					for(int i = 0; i < points.size(); i++) {
						addPointToSubtree(points.get(i));
					}
					points.clear();
				} else {
					points.add(p);
				}
			}
		}
	}
	
	public void divideByLevels(int level) {
		if(levels == level) {
			levels -= 1;
			return;
		}
		
		divided = true;
		
		if(nw == null) nw = new QuadTree(      x,        y, w*0.5, h*0.5, maxPoints);
		levels += 1;
		nw.divideByLevels(level);
		
		if(ne == null) ne = new QuadTree(x+w*0.5,        y, w*0.5, h*0.5, maxPoints);
		levels += 1;
		ne.divideByLevels(level);
		
		if(sw == null) sw = new QuadTree(      x,  y+h*0.5, w*0.5, h*0.5, maxPoints);
		levels += 1;
		sw.divideByLevels(level);
		
		if(se == null) se = new QuadTree(x+w*0.5,  y+h*0.5, w*0.5, h*0.5, maxPoints);
		levels += 1;
		se.divideByLevels(level);
		
		levels -= 1;
	}
	
	private void divide() {
		if(nw == null) nw = new QuadTree(      x,        y, w*0.5, h*0.5, maxPoints);
		if(ne == null) ne = new QuadTree(x+w*0.5,        y, w*0.5, h*0.5, maxPoints);
		if(sw == null) sw = new QuadTree(      x,  y+h*0.5, w*0.5, h*0.5, maxPoints);
		if(se == null) se = new QuadTree(x+w*0.5,  y+h*0.5, w*0.5, h*0.5, maxPoints);
	
		divided = true;
	}

	private void addPointToSubtree(Point p) {
		if(p.x <= x+w*0.5) {
			if(p.y <= y+h*0.5) {
				nw.insert(p);
			} else {
				sw.insert(p);
			}
		} else {
			if(p.y <= y+h*0.5) {
				ne.insert(p);
			} else {
				se.insert(p);
			}
		}
	}
	
	public void clear() {
		nw = null;
		ne = null;
		sw = null;
		se = null;
		
		divided = false;
		
		points.clear();
	}
	
	public void checkIntersects(Car car) {
		if(!car.overlaps(rect)) return;
		else {
			if(points.size() > 0){
				Line L = points.get(0).lineFrom;
				if(car.overlaps(L)) {
					car.handleOverlaps(L);
				}
			}
		}
		if(divided) {
			nw.checkIntersects(car);
			ne.checkIntersects(car);
			sw.checkIntersects(car);
			se.checkIntersects(car);
		}
	}
	
	public void checkCarCollision(Car car1, Car car2) {
		if(!car1.overlaps(rect)) return;
		else {
			if(points.size() > 0) {
				for(int i = 0; i < points.size(); i++) {
					if(points.get(i).carFrom == car2) {
						if (!car1.stunned && !car2.stunned) car1.checkCarCollision(car2);
					}
				}
			}
		}
		if(divided) {
			nw.checkCarCollision(car1, car2);
			ne.checkCarCollision(car1, car2);
			sw.checkCarCollision(car1, car2);
			se.checkCarCollision(car1, car2);
		}
		
	}
	
	public void highlightPoints(Graphics g, Color col) {
		g.setColor(col);
		for(int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			g.drawLine(p.x, p.y, p.x, p.y);
		}
		g.setColor(Color.black);
	}
	
	public void draw(Graphics g, boolean drawPoints) {
		rect.draw(g);
		if(nw != null) nw.draw(g, drawPoints);
		if(ne != null) ne.draw(g, drawPoints);
		if(sw != null) sw.draw(g, drawPoints);
		if(se != null) se.draw(g, drawPoints);
		if(drawPoints) for(int i = 0; i < points.size(); i++) points.get(i).draw(g);
	}

}
