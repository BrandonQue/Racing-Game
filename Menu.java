import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	
	String[][] menuOptions;
	String[] currMenu;
	Rect[] menuRect;
	int rowAt = 0;
	
	int width;
	int height;
	
	public Menu(String[][] menu) {
		width = 1919;
		height = 964;
		
		menuOptions = menu;
		currMenu = menuOptions[0];
		menuRect = new Rect[currMenu.length];
		
		for(int i = 0; i < menuRect.length; i++) {
			double w = width / 2.0;
			double h = height / (menuRect.length + 1);
			
			double x = width / 2 - w / 2;
			double y = h / menuRect.length * (i + 1) + h * i;
			
			menuRect[i] = new Rect(x, y, w, h);
		}
	}
	
	public void MoveInMenu(int mx, int my) {
		for(int i = 0; i < menuRect.length; i++) {
			if(menuRect[i].contains(mx, my)) {
				if(rowAt == 0) rowAt = i+1;
								
				for(int j = 0; j < menuOptions[rowAt].length; j++) {
					currMenu[j] = menuOptions[rowAt][j];
				}
			}
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 2000, 1000);
		
		for(int i = 0; i < currMenu.length; i++) {
			g.setColor(Color.black);
			
			Rect r = menuRect[i];
			g.drawRect(r.x, r.y, r.w, r.h);
			
			g.setFont(new Font(Font.SERIF, Font.PLAIN, menuRect[i].h - 10));
			
			int w = g.getFontMetrics().stringWidth(currMenu[i]);
			g.drawString(currMenu[i], r.x+((r.w - w)/2), r.y+r.h - 20);
		}
	}

}
