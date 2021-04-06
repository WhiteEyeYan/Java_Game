package Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Player extends Img { 
	
	private ImageIcon playerRStand;
	private ImageIcon playerLStand;
	private ImageIcon RMove[];
	private ImageIcon LMove[];
	
	private Player player;
	private Game game;
	
	private boolean touchFloorT;
	private boolean touchFloorL;
	private boolean touchFloorR;
	private boolean touchFloorB;
	
	private int moveX;
	private int moveY;
	private int speed;
	private int floorTop;
	private int floorBottom;
	private int floorLeft;
	private int floorRight;
	private int mapNum;
	
	private boolean jump = false;
	private boolean jumpFinish = false;
		
	public Player(String filename, int count, Game game) {
		super(filename, count);
		this.game = game;
		addKeyController();
		touchFloorT = false;
		moveX = 0;
	}//cons.

	protected void loadImg(String filename, int count) {
		RMove = new ImageIcon[count];
		LMove = new ImageIcon[count];
		try {
			playerRStand = new ImageIcon(ImageIO.read(Game.class.getResource("/picture/player/" + filename + "_RStand.png")));
			playerLStand = new ImageIcon(ImageIO.read(Game.class.getResource("/picture/player/" + filename + "_LStand.png")));
			for (int i = 0; i < count; i++) {
				RMove[i] = new ImageIcon(ImageIO.read(Game.class.getResource("/picture/player/" + filename + "_RMove" + i + ".png")));
				LMove[i] = new ImageIcon(ImageIO.read(Game.class.getResource("/picture/player/" + filename + "_LMove" + i + ".png")));
			
			} // for
			setIcon(playerRStand);
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
	
	public void mapNum(int mapNum)
	{
		
		this.mapNum = mapNum;
	}
	
	public void control(int speed, int changeCount) {
		this.speed = Math.abs(speed);
		touchFloorT = true;
		touchFloorL = false;
		touchFloorR = false;
		int moveTime = 20;
		int frameCount = 0;
		int actCount = 0;
		int ychange = 0;
		int count = 0;
		boolean death = false;
		while(!death) {
			if(y+35>=800 && mapNum!=2)
				death = true;
			if(game.touchFinishImg() && mapNum ==5)
				break;
			game.changeMap();
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
			touchFloorL = game.touchFloorL();
			touchFloorR = game.touchFloorR();
			touchFloorB = game.touchFloorB();
			
			//floorLeft
			if(moveX > 0 && touchFloorL)
			{
				moveX = 0;
				
			} else if(moveX < 0 && touchFloorR) {
					
					moveX = 0;
					
			} else if(moveX < 0 && x <= -50) {
				moveX = 0;
			} else if(moveX > 0 && x >= 1250) {
				moveX = 0;
			}//else-if
			
			
//====================Y軸判斷======================
			
			
			if(jump == true)
			{
				count++;
				if(count%4 == 0)
				{
					ychange+=2;
				}
					moveY = -16 + ychange;
				if(moveY >= 0)
				{
					jump = false;
					count = 0;
					ychange = 0;
				}

			}
			
			if(jump == false)
			{
				
				if(touchFloorT == false)
				{
					count++;
					if(count%4 == 0)
					{
						ychange+=2;
					}//if
						moveY = ychange;
				} else if(moveY < 0 && y <= 0) {

					moveY = 0;
				} else if(moveY > 0 && y+70 >= floorTop) { //落下位置
					moveY = 0;
				}//if-else-if
				
				if(touchFloorT)
				{
					count = 0;
					ychange = 0;
					jumpFinish = true;
				}//if
					
			}
			
			if(touchFloorB)
			{
				//moveY = 0;
				jump = false;
			}//if
//=======================================================
			if(x >= floorLeft && (x+54) <= floorRight && y+72 >= floorTop && y <= floorBottom-10 && touchFloorT)
			{
				setLocation(x , floorTop-70);
				
			}
		}//while
		
	}//control
	
	/*public void stopCmd() {
		touchFloorT = false;
		setIcon(playerRStand);
		moveX = 0;
	}//stopCmd*/
	
	
	protected void addKeyController() {
		game.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				
				System.out.println(e);
				switch(e.getKeyCode()) {
				
					case KeyEvent.VK_LEFT:
						if(mapNum == 3)
							moveX = speed;
						else
							moveX = -speed;
						break;
						
					case KeyEvent.VK_RIGHT:
						if(mapNum == 3)
							moveX = -speed;
						else
							moveX = speed;
						break;
					
					case KeyEvent.VK_UP:
						if(jumpFinish && touchFloorT)
						{
							jump = true;
							jumpFinish = false;
							break;
						}
						
					
					case KeyEvent.VK_DOWN:
						moveY = speed;
						break;
						
				}//switch
				


			}//keyPressed
			
			

			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println(e);
				switch(e.getKeyCode()) {
				
					case KeyEvent.VK_LEFT:
						moveX = 0;
						if(mapNum == 3)
							setIcon(playerRStand);
						else
							setIcon(playerLStand);
						
						break;
						
					case KeyEvent.VK_RIGHT:
						moveX = 0;
						if(mapNum == 3)
							setIcon(playerLStand);
						else
							setIcon(playerRStand);

						break;
					
				}//switch
			}//keyReleased
		});//addKeyListener
	}//addKeyController
}//Role2
