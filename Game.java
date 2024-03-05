import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.SecureRandom;
import java.util.*;

public class Game extends GameBase {
	
	Car[] racer = {
			new Car(Car.GOLD),
			new CarAI(),
			new CarAI(),
			new CarAI(),
	};
	
	CarAI[] ai = {
			(CarAI) racer[1],
			(CarAI) racer[2],
			(CarAI) racer[3],
	};
	
	Car[] places = new Car[4];
	int place = 0;
	String[] placeString = {"1st", "2nd", "3rd", "4th"};
	boolean[] placed = new boolean[4];
	
	boolean raceOngoing;
	boolean raceStarting;
	int second = 60;
	int timer = 0;
	int secondTimer = 0;
	String countdown;
	
	//CarAI ai = new CarAI(0, 0, 22, 37, 0);
	
//	String[][] menuStrings = {
//			{"Test 0 0", "Test 0 1", "Test 0 2", "Test 0 3"},
//			{"Test 1 0", "Test 1 1", "Test 1 2", "Test 1 3"},
//			{"Test 2 0", "Test 2 1", "Test 2 2", "Test 2 3"},
//			{"Test 3 0", "Test 3 1", "Test 3 2", "Test 3 3"},
//			{"Test 4 0", "Test 4 1", "Test 4 2", "Test 4 3"},
//	};
	
	//Menu menu = new Menu(menuStrings);
	
	int w = 1919;
	int h = 964;
	
	QuadTree barrierQTree = new QuadTree(0, 0, w, h, 4);
	QuadTree carQTree = new QuadTree(0, 0, w, h, 2);
	
	TileGrid tileGrid;
	
	Image image;
	
	int mx = -1000;
	int my = -1000;
	
	Random rnd = new SecureRandom();
	
	private Image doubleBuffer;
	
	public void startRace(){
		secondTimer = 3;
		timer = 0;
		raceOngoing = false;
		raceStarting = true;
		
		for(int i = 0; i < racer.length; i++) {
			racer[i].startRace();
		}
		
		places = new Car[4];
		placed = new boolean[4];
		place = 0;
	}
	
	public void raceStarting() {
		countdown = "GO!";
		
		if(timer == second) {
			timer = 0;
			secondTimer--;
		} else {
			timer++;
		}
		
		if(secondTimer == 0) {
			raceOngoing = true;
		} else if(secondTimer == -1) {
			raceStarting = false;
		} else {
			countdown = Integer.toString(secondTimer);
		}
		
	}
	
	public void mouseClicked(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		
		//menu.MoveInMenu(mx, my);
	}
	
	public void initialize() {
		setFocusable(true);
		setSize(w, h);
		
		tileGrid = new TileGrid((w+1)/TileGrid.cols, h/TileGrid.rows);
		int[][] tiles = new int[TileGrid.rows][TileGrid.cols];
		tileGrid.populateGrid(tiles);
		tileGrid.addLinesToQuadTree(barrierQTree);
		
		
		tileGrid.isGenerating = true;
		repaint();
		tileGrid.randomize();
		barrierQTree.clear();
		tileGrid.addLinesToQuadTree(barrierQTree);
		tileGrid.moveCarsToStart(racer);
		startRace();
		
	}

	
	public void inTheGameLoop()
	{
		if(raceStarting) {
			raceStarting();
		}
		
		if(raceOngoing) {
			
			// Take inputs and move things on screen
			if(!racer[0].stunned) {
				if(!pressed[UP] && !pressed[DN]) {
					racer[0].decelerate();
				} else {
					if (pressed[UP]) racer[0].accelerate();
					if (pressed[DN]) racer[0].reverse();
				}
				
				racer[0].rotateIfMoving(pressed[LT], pressed[RT], 3);
				
			} else {
				racer[0].handleStun();
			}
			
			for(int i = 0; i < ai.length; i++) {
				if(!ai[i].stunned) {
					ai[i].followPath(tileGrid);
					ai[i].avoidWallsAndCars(racer);
				} else {
					ai[i].handleStun();
				}
			}
			//ai.followPath(tileGrid);
			
			if(pressed[SPACE]) {
				tileGrid.isGenerating = true;
				repaint();
				tileGrid.randomize();
				barrierQTree.clear();
				tileGrid.addLinesToQuadTree(barrierQTree);
				tileGrid.moveCarsToStart(racer);
				startRace();
				//tileGrid.moveCarsToStart(ai);
			}
			
			// Handle collision
			carQTree.clear();
			for(int i = 0; i < racer.length; i++) carQTree.insert(new Point((int)racer[i].x, (int)racer[i].y, racer[i]));
			
			for(int i = 0; i < racer.length-1; i++) {
				for(int j = i+1; j < racer.length; j++) {
					if(!racer[i].finished && !racer[j].finished) carQTree.checkCarCollision(racer[i], racer[j]);
				}
			}
			
			//barrierQTree.checkIntersects(racer[0]);
			for(int i = 0; i < racer.length; i++) {
				barrierQTree.checkIntersects(racer[i]);
			}
			//barrierQTree.checkIntersects(ai);
			
			// Check places
			if(!(racer[0].finished && racer[1].finished && racer[2].finished && racer[3].finished)) {
				tileGrid.checkIfFinished(racer);
			}
			
			for(int i = 0; i < racer.length; i++) {
				if (!placed[i] && racer[i].finished) {
					places[place] = racer[i];
					racer[i].place = placeString[place++];
					placed[i] = true;
				}
			}
			
		}
		
	}
	
	public void update(Graphics g) {
		Dimension size = getSize();
		
		if (doubleBuffer == null || doubleBuffer.getWidth(this) != size.width || doubleBuffer.getHeight(this) != size.height) {
			doubleBuffer = createImage(size.width, size.height);
		}
		
		if (doubleBuffer != null) {
			Graphics g2 = doubleBuffer.getGraphics();
			paint(g2);
			g2.dispose();
			
			g.drawImage(doubleBuffer, 0, 0, null);
		} else {
			paint(g);
		}
	}
	
	public void paint(Graphics g)
	{
		
		if(tileGrid.isGenerating) {
			g.setColor(Color.black);
			g.fillRect(0, 0, 2000, 2000);
			g.setColor(Color.white);
			g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 96));
			String s = "Generating...";
			g.drawString(s, w/2 - g.getFontMetrics().stringWidth(s)/2, h/2);
		} else {
			//g.setColor(Color.white);
			//g.fillRect(0, 0, 2000, 2000);
			g.setColor(Color.yellow);
			
			tileGrid.draw(g);
			//tileGrid.drawRects(g);
			
			//carQTree.draw(g, false);
			//tileGrid.drawRects(g);
			
			//racer[0].draw(g);
			for(int i = 0; i < racer.length; i++) {
				racer[i].draw(g);
				//racer[i].drawHitbox(g);
				if(racer[i].finished ) g.drawString("Finished "+racer[i].place, (int)racer[i].x - 20, (int)racer[i].y - 20);
			}
			//ai.draw(g);
			//car.drawHitbox(g);
			
			//menu.draw(g);
			//barrierQTree.draw(g, false);
			
			if(raceStarting) {
				g.setColor(Color.black);
				g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 192));
				g.drawString(countdown, w/2 - g.getFontMetrics().stringWidth(countdown)/2, h/2);

				g.setColor(Color.white);
				g.drawString(countdown, (w/2 - g.getFontMetrics().stringWidth(countdown)/2) + 4, (h/2) - 4);
			}
		}
		
	}
	
}
