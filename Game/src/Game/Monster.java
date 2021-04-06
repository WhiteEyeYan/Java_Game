package Game;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Monster extends Img {
	
	private ImageIcon playerRStand;
	private ImageIcon playerLStand;
	private ImageIcon RMove[];
	private ImageIcon LMove[];
	private ImageIcon monster;
	private Game game;
	
	private boolean touchFloorT;
	private boolean touchFloorLR;
	private boolean touchFloorB;
	private int moveX;
	private int moveY;
	private int speed;
	
	
	private int floorTop;
	private int floorBottom;
	private int floorLeft;
	private int floorRight;
	
	public Monster(String filename, int count, Game game) {
		super(filename, count);
		this.game = game;
		touchFloorT = false;
		moveX = 0;
	}//cons.

	protected void loadImg(String filename, int count) {
		try {
			monster = new ImageIcon(ImageIO.read(Game.class.getResource("/picture/" + filename + ".png")));
			setIcon(monster);
		} catch (IOException e) {
			System.out.println("找不到檔案");
		} // try-catch
	}//loadImg
	public void floorY(int floorTopLocation , int floorBottomLocation)
	{

			floorTop = floorTopLocation;
			floorBottom = floorBottomLocation;
	}
	
	public void floorX(int floorLeftLocation , int floorRightLocation)
	{
		
			floorLeft = floorLeftLocation;
			floorRight = floorRightLocation;
	}
	
	
	public void listenCmd(int speed, int changeCount) {
		this.speed = Math.abs(speed);
		touchFloorT = true;
		touchFloorLR = false;
		int moveTime = 20;
		int frameCount = 0;
		int actCount = 0;
		int ychange = 0;
		int count = 0;
		boolean death = false;
		while(!death) {
			if(y+35>=800)
				death = true;
			setLocation(x + moveX, y + moveY);
			
			try { Thread.sleep(moveTime); } catch (InterruptedException e) {}
			if(frameCount % changeCount == 0) {
				if(moveX > 0) {
					setIcon(RMove[actCount]);
				} else if(moveX < 0) {
					setIcon(LMove[actCount]);
				}//else-if
				actCount = (actCount+1) % 2;
			}//if
			frameCount = (frameCount + 1) % 7;
			
			touchFloorT = game.touchFloorT();
			//touchFloorLR = game.touchFloorLR();
			//touchFloorB = game.touchFloorB();
			//floorLeft
			if(moveX < 0 && touchFloorLR)
			{
				moveX = 0;
				
			} else if(moveX > 0 && touchFloorLR) {
					
					moveX = 0;
					
			} else if(moveX < 0 && x <= -50) {
				
				moveX = 0;
				setIcon(playerRStand);
			} else if(moveX > 0 && x >= 1250) {
				moveX = 0;
			}//else-if
			
			
			game.changeMap();
		}//while
		
	}// listenCmd
	
	public void stopCmd() {
		touchFloorT = false;
		setIcon(playerRStand);
		moveX = 0;
	}//stopCmd
	
}//Role2
