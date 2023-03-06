
import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite
{
	
	Animation[] animation;
	
	int pose = 0;
	
	final static int base = 0;
	final static int left = 1;
	final static int right = 2;
	final static int idle = 3;
	final static int stun = 4;
	
	
	public Sprite(String name, String color, String[] pose, int count, String ext, int duration)
	{
		animation = new Animation[pose.length];
		
		for(int i = 0; i < pose.length; i++)
		{
			animation[i] = new Animation(name, color, pose[i], count, ext, duration);
		}
	}

	public BufferedImage getImage(double speed) {
		if(pose > 2) {
			return animation[pose].getImage(1);
		} else {
			return animation[pose].getImage(speed);
		}
		
	}
	
	public void goStraight() {	
		pose = base;
	}
	
	public void turnLeft() {
		pose = left;
	}
	
	public void turnRight() {
		pose = right;
	}

	public void idle() {
		pose = idle;
	}

	public void stunned() {
		pose = stun;
	}
	
}