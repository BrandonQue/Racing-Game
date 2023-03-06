import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile {
	
	int x;
	int y;
	
	int w;
	int h;
	
	int tileType;
	
	public static final int GG = 0;
	public static final int NW = 1;
	public static final int NS = 2;
	public static final int NE = 3;
	public static final int WS = 4;
	public static final int WE = 5;
	public static final int SE = 6;
	public static final int StartN = 7;
	public static final int StartW = 8;
	public static final int StartS = 9;
	public static final int StartE = 10;
	
	Line L1;
	Line L2;
	Line L3;
	Line L4;
	
	Image image;
	
	public Tile(int x, int y, int w, int h, int tileType) {
		this.x = x;
		this.y = y;
		
		this.w = w;
		this.h = h;
		
		this.tileType = tileType;
		
		try {
			setImageAndLinesByTileType();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Tile(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		
		this.w = w;
		this.h = h;
	}
	
	public void setTileType(int tileType) {
		this.tileType = tileType;
		
		try {
			setImageAndLinesByTileType();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setImageAndLinesByTileType() throws IOException {
		int os = (int)(w * 0.08);
		
		switch(tileType) {
		
			case GG:
				image = ImageIO.read(new File("Images/GG.jpg"));

				L1 = null;
				L2 = null;

				L3 = null;
				L4 = null;
				
				break;
			case WE:
				
				image = ImageIO.read(new File("Images/WE.jpg"));
				L1 = new Line(  x,   y+os, x+w,   y+os);
				L2 = new Line(x+w, y+h-os,   x, y+h-os);

				L3 = null;
				L4 = null;
				
				break;
			case NS:
				
				image = ImageIO.read(new File("Images/NS.jpg"));
				L1 = new Line(x+w-os,   y, x+w-os, y+h);
				L2 = new Line(  x+os, y+h,   x+os,   y);

				L3 = null;
				L4 = null;
				
				break;
			case WS:
				
				image = ImageIO.read(new File("Images/WS.jpg"));
				L1 = new Line(     x, y+os, x+w-1-os, y+os);
				L2 = new Line(x+w-os, y+os,   x+w-os,  y+h);
				
				L3 = new Line(x+os,    y+h, x+os, y+h-os+1);
				L4 = new Line(x+os, y+h-os,    x,   y+h-os);
				
				break;
			case SE:
				
				image = ImageIO.read(new File("Images/ES.jpg"));
				L1 = new Line(x+os, y+os,  x+w,   y+os);
				L2 = new Line(x+os,  y+h, x+os, y+1+os);
				
				L3 = new Line(   x+w, y+h-os, x+w-os+1, y+h-os);
				L4 = new Line(x+w-os, y+h-os,   x+w-os,    y+h);
				
				break;
			case NE:
				
				image = ImageIO.read(new File("Images/NE.jpg"));
				L1 = new Line( x+w, y+h-os, x+1+os, y+h-os);
				L2 = new Line(x+os, y+h-os,   x+os,      y);

				L3 = new Line(x+w-os,    y, x+w-os, y+os-1);
				L4 = new Line(x+w-os, y+os,    x+w,   y+os);
				
				break;
			case NW:
				
				image = ImageIO.read(new File("Images/NW.jpg"));
				L1 = new Line(x+w-os,      y, x+w-os, y+h-1-os);
				L2 = new Line(x+w-os, y+h-os,      x,   y+h-os);

				L3 = new Line(   x, y+os, x+os-1, y+os);
				L4 = new Line(x+os, y+os,   x+os,    y);
				
				break;
			case StartN:

				image = ImageIO.read(new File("Images/StartN.png"));
				L1 = new Line(  x,   y+os, x+w,   y+os);
				L2 = new Line(x+w, y+h-os,   x, y+h-os);

				L3 = null;
				L4 = null;
				
					break;
			case StartW:

				image = ImageIO.read(new File("Images/StartW.png"));
				L1 = new Line(x+w-os,   y, x+w-os, y+h);
				L2 = new Line(  x+os, y+h,   x+os,   y);

				L3 = null;
				L4 = null;
				
					break;
			case StartS:

				image = ImageIO.read(new File("Images/StartS.png"));
				L1 = new Line(  x,   y+os, x+w,   y+os);
				L2 = new Line(x+w, y+h-os,   x, y+h-os);

				L3 = null;
				L4 = null;
				
					break;
			case StartE:

				image = ImageIO.read(new File("Images/StartE.png"));
				L1 = new Line(x+w-os,   y, x+w-os, y+h);
				L2 = new Line(  x+os, y+h,   x+os,   y);

				L3 = null;
				L4 = null;
				
					break;
		}
	}
	
	public void addLinesToQuadTree(QuadTree qT) {
		if(L1 != null) L1.addPointsToQuadTree(qT);
		if(L2 != null) L2.addPointsToQuadTree(qT);
		if(L3 != null) L3.addPointsToQuadTree(qT);
		if(L4 != null) L4.addPointsToQuadTree(qT);
	}

	public void draw(Graphics g) {
		
		g.drawImage(image, x, y, w, h, null);
	
	}
	
}
