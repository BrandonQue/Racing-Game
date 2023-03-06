import java.awt.Graphics;
import java.util.Arrays;
import java.util.Random;

public class TileGrid {
	
	static int rows = 4;
	static int cols = 8;
	
	int tileWidth;
	int tileHeight;
	
	Tile[][] tile;
	Tile startTile;
	int startDirection;
		public static final int N = 0;
		public static final int W = 1;
		public static final int S = 2;
		public static final int E = 3;
	int[][] path;
	Rect[] rect = new Rect[48];
	int pathCount;
	int nextRow;
	int nextCol;
	
	boolean isLoop;
	boolean isGenerating;
	
	public static final int GG = 0;
	public static final int WE = 1;
	public static final int NS = 2;
	public static final int WS = 3;
	public static final int ES = 4;
	public static final int NE = 5;
	public static final int NW = 6;
	
	Random rnd = new Random();
	
	public TileGrid(int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		tile = new Tile[rows][cols];
		path = new int[rows][cols];
	}
	
	public TileGrid(int tileWidth, int tileHeight, int[][] tileTypes) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		tile = new Tile[rows][cols];
		path = new int[rows][cols];
		
		populateGrid(tileTypes);
	}
	
	public void populateGrid(int[][] tileTypes) {
		if(tileTypes.length == rows && tileTypes[0].length == cols) {
			for(int i = 0; i < rows; i++){
				for(int j = 0; j < cols; j++) {
					tile[i][j] = new Tile(
							tileWidth * j, 
							tileHeight * i, 
							tileWidth, 
							tileHeight, 
							tileTypes[i][j]
					);
					
					if(path[i][j] > 0) rect[path[i][j]] = new Rect(tile[i][j].x, tile[i][j].y, tile[i][j].w, tile[i][j].h);
					
					if(path[i][j] == 1) startTile = tile[i][j];
				}
			}
		}
	}
	
	public void randomize() {
		isLoop = false;
		pathCount = 1;
		nextRow = -1;
		nextCol = -1;
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				tile[i][j] = null;
				path[i][j] = 0;
			}
		}
		int[][] randomTiles = new int[rows][cols];
		randomTiles = randomStart(randomTiles);
		
		while(!isLoop) {
			randomTiles = randomWalk(randomTiles);
		}
		
		populateGrid(randomTiles);
		isGenerating = false;
	}
	
	private int[][] randomStart(int[][] randomTiles) {
		
		int nwse = rnd.nextInt(4);
		startDirection = nwse;
		
		if(nwse == N) {
			int col = rnd.nextInt(cols-2) + 1;
			randomTiles[0][col] = Tile.StartN;
			path[0][col] = 1;
			
			
			nextRow = 0;
			nextCol = col - 1;
		} else if(nwse == W) {
			int row = rnd.nextInt(rows-2) + 1;
			randomTiles[row][0] = Tile.StartW;
			path[row][0] = 1;
			nextRow = row + 1;
			nextCol = 0;
		} else if(nwse == S) {
			int col = rnd.nextInt(cols-2) + 1;
			randomTiles[rows-1][col] = Tile.StartS;
			path[rows-1][col] = 1;
			nextRow = rows - 1;
			nextCol = col + 1;
		} else if(nwse == E) {
			int row = rnd.nextInt(rows-2) + 1;
			randomTiles[row][cols-1] = Tile.StartE;
			path[row][cols-1] = 1;
			nextRow = row - 1;
			nextCol = cols - 1;
		}
		pathCount++;
		
		return randomTiles;
	}
	
	private int[][] randomWalk(int[][] randomTiles){
		boolean nEdge = nextRow == 0;
		boolean wEdge = nextCol == 0;
		boolean sEdge = nextRow == rows - 1;
		boolean eEdge = nextCol == cols - 1;
		
		// Check if next to starting tile
		if(pathCount > 1) {
			if(nEdge) {
				if(!wEdge) {
					if(path[nextRow][nextCol-1] == 1) {
						if(path[nextRow+1][nextCol] == pathCount-1) {
							randomTiles[nextRow][nextCol] = Tile.WS;
							path[nextRow][nextCol] = pathCount;
							
							isLoop = true;
						} else if(!eEdge && path[nextRow][nextCol+1] == pathCount-1) {
							randomTiles[nextRow][nextCol] = Tile.WE;
							path[nextRow][nextCol] = pathCount;
							
							isLoop = true;
						}
					}
				}
			}
			if(wEdge) {
				if(!sEdge) {
					if(path[nextRow+1][nextCol] == 1) {
						if(path[nextRow][nextCol+1] == pathCount-1) {
							randomTiles[nextRow][nextCol] = Tile.SE;
							path[nextRow][nextCol] = pathCount;
							
							isLoop = true;
						} else if(!nEdge && path[nextRow-1][nextCol] == pathCount-1) {
							randomTiles[nextRow][nextCol] = Tile.NS;
							path[nextRow][nextCol] = pathCount;
							
							isLoop = true;
						}
					}
				}
			}
			if(sEdge) {
				if(!eEdge) {
					if(path[nextRow][nextCol+1] == 1) {
						if(path[nextRow-1][nextCol] == pathCount-1) {
							randomTiles[nextRow][nextCol] = Tile.NE;
							path[nextRow][nextCol] = pathCount;
							
							isLoop = true;
						} else if(!wEdge && path[nextRow][nextCol-1] == pathCount-1) {
							randomTiles[nextRow][nextCol] = Tile.WE;
							path[nextRow][nextCol] = pathCount;
							
							isLoop = true;
						}
					}
				}
			}
			if(eEdge) {
				if(!nEdge) {
					if(path[nextRow-1][nextCol] == 1) {
						if(path[nextRow][nextCol-1] == pathCount-1) {
							randomTiles[nextRow][nextCol] = Tile.NW;
							path[nextRow][nextCol] = pathCount;
							
							isLoop = true;
						} else if(!sEdge && path[nextRow+1][nextCol] == pathCount-1) {
							randomTiles[nextRow][nextCol] = Tile.NS;
							path[nextRow][nextCol] = pathCount;
							
							isLoop = true;
						}
					}
				}
			}
			// End of check 
			
			if(!isLoop) {
				boolean trapped = (nEdge || path[nextRow-1][nextCol] > 0) && (wEdge || path[nextRow][nextCol-1] > 0) && 
						          (sEdge || path[nextRow+1][nextCol] > 0) && (eEdge || path[nextRow][nextCol+1] > 0);
				
				if(trapped) {
					for(int i = 0; i < rows; i++) {
						for(int j = 0; j < cols; j++) {
							if (path[i][j] > 1) {
								path[i][j] = 0;
								randomTiles[i][j] = 0;
							}
							else if(path[i][j] == 1) {
								nextRow = i;
								nextCol = j;
								
								nEdge = nextRow == 0;
								wEdge = nextCol == 0;
								sEdge = nextRow == rows - 1;
								eEdge = nextCol == cols - 1;
								
								if(nEdge) nextCol--;
								else if(wEdge) nextRow++;
								else if(sEdge) nextCol++;
								else if(eEdge) nextRow--;
								
								pathCount = 2;
							}
						}
					}
				} else {
					
					boolean[] rndType = new boolean[7];
					for(int i = 1; i < rndType.length; i++) {
						rndType[i] = true;
					}
					
					boolean fromN = !nEdge && path[nextRow-1][nextCol] == pathCount-1;
					boolean fromW = !wEdge && path[nextRow][nextCol-1] == pathCount-1;
					boolean fromS = !sEdge && path[nextRow+1][nextCol] == pathCount-1;
					boolean fromE = !eEdge && path[nextRow][nextCol+1] == pathCount-1;
					
					if(fromN) {
						rndType[Tile.WS] = false;
						rndType[Tile.WE] = false;
						rndType[Tile.SE] = false;
					}
					else if(fromW) {
						rndType[Tile.NS] = false;
						rndType[Tile.NE] = false;
						rndType[Tile.SE] = false;
					}
					else if(fromS) {
						rndType[Tile.NW] = false;
						rndType[Tile.NE] = false;
						rndType[Tile.WE] = false;
					}
					else if(fromE) {
						rndType[Tile.NW] = false;
						rndType[Tile.NS] = false;
						rndType[Tile.WS] = false;
					}
					
					if(!fromN && (nEdge || path[nextRow-1][nextCol] > 0)) {
						rndType[Tile.NW] = false;
						rndType[Tile.NE] = false;
						rndType[Tile.NS] = false;
					}
					if(!fromW && (wEdge || path[nextRow][nextCol-1] > 0)) {
						rndType[Tile.NW] = false;
						rndType[Tile.WS] = false;
						rndType[Tile.WE] = false;
					}
					if(!fromS && (sEdge || path[nextRow+1][nextCol] > 0)) {
						rndType[Tile.NS] = false;
						rndType[Tile.WS] = false;
						rndType[Tile.SE] = false;
					}
					if(!fromE && (eEdge || path[nextRow][nextCol+1] > 0)) {
						rndType[Tile.NE] = false;
						rndType[Tile.WE] = false;
						rndType[Tile.SE] = false;
					}
					
					int type = rnd.nextInt(rndType.length);
					
					while(!rndType[type]) {
						type = rnd.nextInt(rndType.length);
					}
					
					randomTiles[nextRow][nextCol] = type;
					path[nextRow][nextCol] = pathCount;
					
					switch(type) {
					
					case Tile.NW:
						
						if(fromN) nextCol--;
						else if(fromW) nextRow--;
						
						break;
					case Tile.NS:
						
						if(fromN) nextRow++;
						else if(fromS) nextRow--;
						
						break;
					case Tile.NE:

						if(fromN) nextCol++;
						else if(fromE) nextRow--;
						
						break;
					case Tile.WS:

						if(fromW) nextRow++;
						else if(fromS) nextCol--;
						
						break;
					case Tile.WE:

						if(fromW) nextCol++;
						else if(fromE) nextCol--;
						
						break;
					case Tile.SE:

						if(fromS) nextCol++;
						else if(fromE) nextRow++;
						
						break;
						
					}
					
					pathCount++;
				}
			} else if(pathCount < rows*cols/2){
				for(int i = 0; i < rows; i++) {
					for(int j = 0; j < cols; j++) {
						if (path[i][j] > 1) {
							path[i][j] = 0;
							randomTiles[i][j] = 0;
						}
						else if(path[i][j] == 1) {
							nextRow = i;
							nextCol = j;
							
							nEdge = nextRow == 0;
							wEdge = nextCol == 0;
							sEdge = nextRow == rows - 1;
							eEdge = nextCol == cols - 1;
							
							if(nEdge) nextCol--;
							else if(wEdge) nextRow++;
							else if(sEdge) nextCol++;
							else if(eEdge) nextRow--;
							
							pathCount = 2;
							
							isLoop = false;
						}
					}
				}
			}
		}
		
		return randomTiles;
		
	}
	
	public void addLinesToQuadTree(QuadTree qT) {
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++) {
				tile[i][j].addLinesToQuadTree(qT);
			}
		}
	}
	
	public void moveCarsToStart(Car[] c) {
		for(int i = 0; i < c.length; i++) {
			c[i].x = 10 + startTile.x + startTile.w / 2;
			c[i].y = 10 + startTile.y + startTile.h / 2;
			c[i].angle = (360 - 90*(startDirection+2))% 360;
			if(startDirection == N) {
				c[i].y -= (i+1)*tileHeight/5 - tileHeight/2;
			} else if(startDirection == W) {
				c[i].x -= (i+1)*tileWidth/5 - tileWidth/2;
			} else if(startDirection == S) {
				c[i].y += (i+1)*tileHeight/5 - tileHeight/2;
			} else if(startDirection == E) {
				c[i].x += (i+1)*tileWidth/5 - tileWidth/2;
			}
		}
	}
	
	public void addRectsToQuadTree(QuadTree qT) {
		for(int i = 1; i <= pathCount; i++) {
			rect[i].addPointsToQuadTree(qT);
		}
	}
	
	public void checkIfFinished(Car[] cars) {
		
		for(int i = 0; i < cars.length; i++) {
			Car c = cars[i];
			if(c.overlaps(rect[1]) && c.overlaps(rect[2])) {
				if(!c.isCrossing) {
					c.isCrossing = true;
					c.lap++;
				}
			} else if(c.overlaps(rect[2]) && c.overlaps(rect[3])) {
				c.isCrossing = false;
			}
			if(c.lap >= 4) c.finished = true;
		}
		
	}
	
	public void drawRects(Graphics g) {
		for(int i = 1; i <= pathCount; i++) {
			if(rect[i] != null){
				Rect r = rect[i];
				g.drawRect(r.x, r.y, r.w, r.h);
				g.drawString(Integer.toString(i), r.x+r.w/2, r.y+r.h/2);
			}
		}
	}

	public void draw(Graphics g) {
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(tile[i][j] != null) tile[i][j].draw(g);
			}
		}
		
	}
	
}
