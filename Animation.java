
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animation{
	private BufferedImage[] image;
	
	private double current = 0;
	
	int delay;
	int duration; 
	
	public Animation(String name, String color, String pose, int count, String ext, int duration){
		image = new BufferedImage[count];
		
		for(int i = 1; i <= count; i++)
			try {
				image[i-1] = ImageIO.read(new File("Images/"+ name +"_"+ color +"_"+ pose +"_"+ i + ext));
			} catch (IOException e) {
				e.printStackTrace();
			}
	
		this.duration = duration;
		delay = duration;
	}
	
	
	public BufferedImage getImage(double speed){
		if(delay == 0){
			
		   current += speed;
		
	   		if(current >= image.length)  current = 0;
	   		if(current < 0) current += image.length;
	   	
	   		delay = duration;
		}
		
		delay--;
		
	   return image[(int)current];
	}
	
	
	public Image loadImage(String filename){
		return Toolkit.getDefaultToolkit().getImage(filename);
	}

}