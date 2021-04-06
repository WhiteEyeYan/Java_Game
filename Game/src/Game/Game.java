package Game;
import javax.swing.*;
import java.awt.*;

public class Game extends JFrame{
	public JLayeredPane panel;
	private Img background;
	private Img background_death;
	private Img[] floor= new Img[3];
	private Img[] floatFloor = new Img[3];
	
	private Img[] floorD= new Img[2];
	private Img[] floatFloorD = new Img[4];
	
	private Img[] soil = new Img[2];
	private Img finishImg;
	
	private int floorLength;
	private int floorHeight;
	private int floatFloorLength;
	private int floatFloorHeight;
	private static int mapNum = 0;
	private int lifeCount;
	
	public static Game game;
	private Player player;
	private Img monster;
	
	private boolean beenFinish = false;
	private static boolean finish = false;
	private static boolean death = false;
	
	private Message deathMsg;
	private Message lifeNum;
	private Message warning;
	private Message OB;

	public Game() 
	{
		super("Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setResizable(false);
		setSize(1280, 800);
		
		setVisible(true); 
		
		panel = new JLayeredPane();
		setContentPane(panel);		
		
		loading();
		
	}//Game
//=========================================載入檔案===================================================	
	private void loading() 
	{
		lifeCount = 0;
		
		background = new Img("background", 6);
		background_death = new Img("background_death");
		player = new Player("player" , 2 , this);
		for(int i = 0 ; i < 3 ; i++)
			floor[i] = new Img("floor");
		for(int i = 0 ; i < 2 ; i++)
			floorD[i] = new Img("火地1");
		finishImg = new Img("氣球");
		for(int i = 0 ; i < 4 ; i++)
			floatFloorD[i] = new Img("岩漿塊");
		for(int i = 0 ; i < 3 ; i++)
			floatFloor[i] = new Img("磚");
		for(int i = 0 ; i < 2 ; i++)
			soil[i] = new Img("soil");
		monster = new Img("monster");
		deathMsg = new Message("哎呀 你怎麼死了？", true, "標楷體", Font.BOLD, 72, Color.decode("#ffffff"), null);	
		warning = new Message("警告:系統異常！！", true, "標楷體", Font.BOLD, 72, Color.decode("#ff0000"), null);
		OB = new Message("*注意：角色超出視窗邊界", true, "標楷體", Font.BOLD, 15, Color.decode("#ff7200"), null);
	}//loading
	
	private void loadLifeNum() 
	{
		lifeNum = new Message(("已死亡: " + lifeCount + " 次") , true, "新細明體", Font.BOLD, 36, Color.decode("#ffffff"), null);
		if(lifeCount > 1)
			deathMsg = new Message("你怎麼又死了？", true, "標楷體", Font.BOLD, 72, Color.decode("#ffffff"), null);
	}//loadLifeNum
	
	private void loadFinishMessage() 
	{
		if(lifeCount == 0)
		lifeNum = new Message(("厲害！！您使用 " + lifeCount + " 條命就成功破關了！！！") , true, "新細明體", Font.BOLD, 36, Color.decode("#ffffff"), null);
		else
			lifeNum = new Message(("您使用了 " + lifeCount + " 條命才成功破關") , true, "新細明體", Font.BOLD, 36, Color.decode("#ffffff"), null);
		deathMsg = new Message("恭喜你成功到達終點了", true, "標楷體", Font.BOLD, 72, Color.decode("#ffffff"), null);
	}//loadFinishMessage	
//====================================================================================================	

	
//=========================================場景變換===================================================	
	public void changeMap() {
		try {
			panel.remove(OB);
			game.outOfBoundary();
			
		} catch (outOfBoundaryException e) {
			OB.setLocation(1085,10);
			addObj(OB , 10);
			
			refresh();
		}
		
		if(player.getImgY()+35 >= 800)
			death = true;
		//往前走
		if(player.getImgX() >= 1230 && mapNum != 2 && mapNum != 4 && mapNum < 5) {
			mapNum++;
			panel.removeAll();
			background.setImg(mapNum);
			addObj(background , 0);
			switch(mapNum)
			{
				case 1:
					player.setLocation(-39, player.getImgY());
					floor[2].setLocation(400, 800-floor[0].getPreferredSize().height);
					floor[0].setLocation(1200, 800-floor[0].getPreferredSize().height+1);
					monster.setLocation(600, 800-floor[0].getPreferredSize().height-60);
					
					addObj(player , 1);
					addObj(floor[2] , 2);
					addObj(floor[0] , 3);
					addObj(monster , 1);
					break;
					
				case 2:
					player.setLocation(-39, player.getImgY());
					floor[0].setLocation(800, 800-floor[0].getPreferredSize().height);
					floor[1].setLocation(-800, 800-floor[0].getPreferredSize().height);
					floatFloor[0].setLocation(500,400);
					floatFloor[1].setLocation(800,250);
					floatFloor[2].setLocation(1100,100);
					
					addObj(player , 1);
					for(int i = 0 ; i < 2 ; i++)
						addObj(floor[i] , 2);
					for(int i = 0 ; i < 3 ; i++)
						addObj(floatFloor[i] , 2);
					break;
					
				case 4:
					player.setLocation(-39, player.getImgY());
					floorD[0].setLocation(800, 800-floorD[0].getPreferredSize().height);
					floorD[1].setLocation(-800, 800-floorD[0].getPreferredSize().height);
					floatFloorD[0].setLocation(500,400);
					floatFloorD[1].setLocation(800,250);
					floatFloorD[2].setLocation(1100,100);
					
					addObj(player , 1);
					for(int i = 0 ; i < 2 ; i++)
						addObj(floorD[i] , 2);
					for(int i = 0 ; i < 3 ; i++)
						addObj(floatFloorD[i] , 2);
					break;

			}//switch
			refresh();
		} else if (mapNum == 1) {
			
			if(player.getImgY() <= 500 && player.getImgX() >= 410)
			{
				Thread floorFalling = new Thread(new Runnable(){ 
					public void run() { 
						
						for(int i = 0 ; i < 50 ; i++)
						{
							if(mapNum != 1)
								break;
							try {Thread.sleep(200);} catch (InterruptedException e) {}	
							floor[2].setLocation(400, floor[2].getImgY()+i);
							monster.setLocation(600, floor[2].getImgY()-60);
						}
					} 
				});
				floorFalling.start();
			}
			
			
		} else if (mapNum == 2) {
			
			//map 2 to 3
			if(player.getImgY() > 780)
			{
				mapNum++;
				panel.removeAll();
				
				background.setImg(mapNum);
				addObj(background , 0);
				
				warning.setLocation(640-(warning.getPreferredSize().width/2),100);
				addObj(warning , 3);
				Thread warn = new Thread(new Runnable(){ 
					public void run() { 
						while(true)
						{ 
							if(mapNum != 3)
								break;
							try {Thread.sleep(300);} catch (InterruptedException e) {}
							warning.setVisible(false);
							try { Thread.sleep(300); } catch (InterruptedException e) {}
							warning.setVisible(true);
						} 
					} 
				});
				warn.start();
				
				player.setLocation(player.getImgX(), -100);
				floorD[0].setLocation(825, 800-floorD[0].getPreferredSize().height);
				soil[0].setLocation(800, -400);
				soil[1].setLocation(-800, -400);	
				
				addObj(floorD[0] , 2);
				for(int i = 0 ; i < 2 ; i++)
					addObj(soil[i] , 2);

				try {Thread.sleep(2000);} catch (InterruptedException e) {}
				addObj(player , 1);
			}//if
			
			//map 2 to 0
			if(player.getImgY()+70 <= floatFloor[2].getImgY() && player.getImgX() >= 1230)
			{
				mapNum = 0;
				panel.removeAll();
				background.setImg(mapNum);
				addObj(background , 0);
				
				player.setLocation(-39, player.getImgY());
				floor[0].setLocation(0 , 800-floor[0].getPreferredSize().height);
				floatFloor[0].setLocation(400 , 450);
				
				addObj(player, 1);
				addObj(floor[0], 2);
				addObj(floatFloor[0], 2);
			}//if
			
			//map 2 to 1
			if(player.getImgX() <= -40)
			{
				mapNum = 1;
				panel.removeAll();
				background.setImg(mapNum);
				addObj(background , 0);
				
				player.setLocation(1210, player.getImgY());
				floor[2].setLocation(400, 800-floor[0].getPreferredSize().height);
				floor[0].setLocation(1200, 800-floor[0].getPreferredSize().height+1);
				
				addObj(player , 1);
				addObj(floor[2] , 2);
				addObj(floor[0] , 3);
			}//if
			refresh();
			
		} else if (mapNum == 4) {
			//map 4 to 0
			if(player.getImgY()+70 <= floatFloor[2].getImgY() && player.getImgX() >= 1230)
			{
				mapNum = 0;
				panel.removeAll();
				background.setImg(mapNum);
				addObj(background , 0);
				
				player.setLocation(-39, player.getImgY());
				floor[0].setLocation(0 , 800-floor[0].getPreferredSize().height);
				floatFloor[0].setLocation(400 , 450);
				
				addObj(player, 1);
				addObj(floor[0], 2);
				addObj(floatFloor[0], 2);
				
				
			} else if(player.getImgX() >= 1230) {
				//map 4 to 5 left
				beenFinish = true;
				mapNum++;
				panel.removeAll();
				background.setImg(mapNum);
				addObj(background , 0);
				
				player.setLocation(-39, player.getImgY());
				floorD[0].setLocation(1080, 800-floorD[0].getPreferredSize().height);
				floorD[1].setLocation(-1100, 800-floorD[0].getPreferredSize().height);
				finishImg.setLocation(1100,floorD[1].getImgY()-100);
				
				addObj(player , 1);
				for(int i = 0 ; i < 2 ; i++)
					addObj(floorD[i] , 2);
				addObj(finishImg, 2);
				
				
			} else if(player.getImgY()+70 <= floatFloorD[3].getImgY() && player.getImgX() <= -40) {
				//map 4 to 5 right
				
				mapNum++;
				panel.removeAll();
				background.setImg(mapNum);
				addObj(background , 0);
				
				player.setLocation(1210, player.getImgY());
				floorD[0].setLocation(1080, 800-floorD[0].getPreferredSize().height);
				floorD[1].setLocation(-1100, 800-floorD[0].getPreferredSize().height);
				finishImg.setLocation(1100,floorD[1].getImgY()-100);
				
				addObj(player , 1);
				for(int i = 0 ; i < 2 ; i++)
					addObj(floorD[i] , 2);
				addObj(finishImg, 2);		
				
			} else if(player.getImgX() <= -40) {
				//map 4 to 3 right
				mapNum--;
				
				panel.removeAll();
				
				background.setImg(mapNum);
				addObj(background , 0);
				
				warning.setLocation(640-(warning.getPreferredSize().width/2),100);
				addObj(warning , 3);
				Thread warn = new Thread(new Runnable(){ 
					public void run() { 
						while(true)
						{ 
							if(mapNum != 3)
								break;
							try {Thread.sleep(300);} catch (InterruptedException e) {}
							warning.setVisible(false);
							try { Thread.sleep(300); } catch (InterruptedException e) {}
							warning.setVisible(true);
						} 
					} 
				});
				warn.start();
				
				player.setLocation(1210, player.getImgY());
				floorD[0].setLocation(825, 800-floorD[0].getPreferredSize().height);
				
				soil[0].setLocation(800, -400);
				soil[1].setLocation(-800, -400);
				
				addObj(player , 1);
				addObj(floorD[0] , 2);
				for(int i = 0 ; i < 2 ; i++)
					addObj(soil[i] , 2);
				
			}//if-else
	
			refresh();
			
		// 往回走
		}//if-else-if
		if (player.getImgX() <= -40 && mapNum > 0 && mapNum != 3 && mapNum != 4){
			mapNum--;
			panel.removeAll();
			background.setImg(mapNum);
			addObj(background , 0);
			switch(mapNum)
			{
				case 0:
					player.setLocation(1210, player.getImgY());
					floor[0].setLocation(-500 , 800-floor[0].getPreferredSize().height);
					floatFloor[0].setLocation(400 , 450);
					
					addObj(player, 1);
					addObj(floor[0], 2);
					addObj(floatFloor[0], 2);
					break;
					
				case 1:
					player.setLocation(1210, player.getImgY());
					floor[0].setLocation(400, 800-floor[0].getPreferredSize().height);
					
					addObj(player, 1);
					addObj(floor[0], 2);
					break;
					
				case 4:
					player.setLocation(1210, player.getImgY());
					floorD[0].setLocation(800, 800-floorD[0].getPreferredSize().height);
					floorD[1].setLocation(-800, 800-floorD[0].getPreferredSize().height);
					floatFloorD[0].setLocation(500,400);
					floatFloorD[1].setLocation(800,250);
					floatFloorD[2].setLocation(1100,100);
					
					addObj(player , 1);
					for(int i = 0 ; i < 2 ; i++)
						addObj(floorD[i] , 2);
					for(int i = 0 ; i < 3 ; i++)
						addObj(floatFloorD[i] , 2);	
					
					if(beenFinish)
					{
						floatFloorD[3].setLocation(0,150);
						addObj(floatFloorD[3] , 2);
					}//if
					break;
			}//switch
			refresh();

		}//else-if
		
		player.mapNum(mapNum);
	}//changeMap
//====================================================================================================	
	
		
//====================================碰觸地板頂部(Top)===============================================
	public boolean touchFloorT() {
		switch(mapNum)
		{
			case 0:
				if(game.checkTouchFloorT(floor[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorT(floatFloor[0], 225, 48))
					return true;
				
				else
					return false;
				
			case 1:
				if(game.checkTouchFloorT(floor[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorT(floor[2], 1300, 800))
					return true;

				else
					return false;
				
			case 2:
				if(game.checkTouchFloorT(floor[0], 1300, 800))
					return true;

				if(game.checkTouchFloorT(floor[1], 1300, 800))
					return true;
				
				if(game.checkTouchFloorT(floatFloor[0], 225, 48))
					return true;
				
				if(game.checkTouchFloorT(floatFloor[1], 225, 48))
					return true;
				
				if(game.checkTouchFloorT(floatFloor[2], 225, 48))
					return true;

				else
					return false;
				
			case 3:
				if(game.checkTouchFloorT(floorD[0], 1300, 800))
					return true;

				else
					return false;
				
			case 4:
				if(game.checkTouchFloorT(floorD[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorT(floorD[1], 1300, 800))
					return true;
				
				if(game.checkTouchFloorT(floatFloorD[0], 225, 48))
					return true;
				
				if(game.checkTouchFloorT(floatFloorD[1], 225, 48))
					return true;
				
				if(game.checkTouchFloorT(floatFloorD[2], 225, 48))
					return true;
				
				if(beenFinish)
					if(game.checkTouchFloorT(floatFloorD[3], 225, 48))
						return true;
				
				else
					return false;
				
			case 5:
				if(game.checkTouchFloorT(floorD[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorT(floorD[1], 1300, 800))
					return true;

				else
					return false;
				
			default:
				return false;
		}//switch
			
		
		
	}//touchFloorT
	
	public boolean checkTouchFloorT(Img floorName , int floorNameLengthSize , int floorNameHeightSize) {
		floorLength = floorName.getImgX() + floorNameLengthSize;
		floorHeight = floorName.getImgY() + floorNameHeightSize;
		if(player.getImgX()+45 >= floorName.getImgX() &&
				player.getImgX()+45 <= floorLength+20 &&
					player.getImgY()+72 >= floorName.getImgY() &&
						player.getImgY()+72 <= floorHeight)
		{
			player.floorX(floorName.getImgX() , floorLength);
			player.floorY(floorName.getImgY() , floorHeight);
			return true;
		}//if

		else
			return false;
	}//checkTouchFloorT
//====================================================================================================
	
	
//===================================碰觸地板底部(Bottom)=============================================
	public boolean touchFloorB() {
		switch(mapNum)
		{
			case 0:
				if(game.checkTouchFloorB(floor[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorB(floatFloor[0], 225, 48))
					return true;
				
				else
					return false;
				
			case 1:
				if(game.checkTouchFloorB(floor[0], 1300, 800))
					return true;

				else
					return false;
				
			case 2:
				if(game.checkTouchFloorB(floor[0], 1300, 800))
					return true;

				if(game.checkTouchFloorB(floor[1], 1300, 800))
					return true;
				
				if(game.checkTouchFloorB(floatFloor[0], 225, 48))
					return true;
				
				if(game.checkTouchFloorB(floatFloor[1], 225, 48))
					return true;
				
				if(game.checkTouchFloorB(floatFloor[2], 225, 48))
					return true;

				else
					return false;
				
			case 3:
				if(game.checkTouchFloorB(floorD[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorB(soil[0], 1280, 800))
					return true;
				
				if(game.checkTouchFloorB(soil[1], 1280, 800))
					return true;
				
				else
					return false;
				
			case 4:
				if(game.checkTouchFloorB(floorD[0], 1300, 800))
					return true;

				if(game.checkTouchFloorB(floorD[1], 1300, 800))
					return true;
				
				if(game.checkTouchFloorB(floatFloorD[0], 225, 48))
					return true;

				if(game.checkTouchFloorB(floatFloorD[1], 225, 48))
					return true;
		
				if(game.checkTouchFloorB(floatFloorD[2], 225, 48))
					return true;
				
				if(beenFinish)
					if(game.checkTouchFloorB(floatFloorD[3], 225, 48))
						return true;
				
				else
					return false;
				
			case 5:
				if(game.checkTouchFloorB(floorD[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorB(floorD[1], 1300, 800))
					return true;

				else
					return false;
				
			default:
				return false;
		}//switch
	}//touchFloorB
	
	public boolean checkTouchFloorB(Img floorName , int floorNameLengthSize , int floorNameHeightSize) {
		floorLength = floorName.getImgX() + floorNameLengthSize;
		floorHeight = floorName.getImgY() + floorNameHeightSize;
		if(player.getImgX()+45 >= floorName.getImgX() &&
				player.getImgX()+45 <= floorLength+30 &&
					player.getImgY()-10 <= floorHeight &&
						player.getImgY()+70 >= floorHeight)
		{ 
			
			player.floorX(floorName.getImgX() , floorLength);
			player.floorY(floorName.getImgY() , floorHeight);
			return true;
		}//if
		else
			return false;
		
	}//checkTouchFloorB
//====================================================================================================	
	
	
//=====================================碰觸地板左(Left)===============================================
	public boolean touchFloorL() {
		switch(mapNum)
		{
			case 0:
				if(game.checkTouchFloorL(floor[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorL(floatFloor[0], 225, 48))
					return true;
				
				else
					return false;
				
			case 1:
				if(game.checkTouchFloorL(floor[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorL(floor[2], 1300, 800))
					return true;
				
				else
					return false;
				
			case 2:
				if(game.checkTouchFloorL(floor[0], 1300, 800))
					return true;

				if(game.checkTouchFloorL(floor[1], 1300, 800))
					return true;
				
				if(game.checkTouchFloorL(floatFloor[0], 225, 48))
					return true;
				
				if(game.checkTouchFloorL(floatFloor[1], 225, 48))
					return true;
				
				if(game.checkTouchFloorL(floatFloor[2], 225, 48))
					return true;
				
				else
					return false;
				
			case 3:
				if(game.checkTouchFloorL(floorD[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorL(soil[0], 1280, 800))
					return true;
				
				if(game.checkTouchFloorL(soil[1], 1280, 800))
					return true;
				
				else
					return false;
				
			case 4:
				if(game.checkTouchFloorL(floorD[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorL(floorD[1], 1300, 800))
					return true;
				
				if(game.checkTouchFloorL(floatFloorD[0], 225, 48))
					return true;
				
				if(game.checkTouchFloorL(floatFloorD[1], 225, 48))
					return true;
				
				if(game.checkTouchFloorL(floatFloorD[2], 225, 48))
					return true;

				if(beenFinish)
					if(game.checkTouchFloorL(floatFloorD[3], 225, 48))
						return true;
				
				else
					return false;
				
			case 5:
				if(game.checkTouchFloorL(floorD[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorL(floorD[1], 1300, 800))
					return true;
				
				else
					return false;
				
			default:
				return false;
		}//switch
	}//touchFloorL
	
	public boolean checkTouchFloorL(Img floorName , int floorNameLengthSize , int floorNameHeightSize) {
		floorLength = floorName.getImgX() + floorNameLengthSize;
		floorHeight = floorName.getImgY() + floorNameHeightSize;
		if(player.getImgX()+45 >= floorName.getImgX()-10 &&
				player.getImgX()+45 <= floorName.getImgX() &&
					player.getImgY()+70 >= floorName.getImgY() &&
						player.getImgY() <= floorHeight)
		{ 
			
			player.floorX(floorName.getImgX() , floorLength);
			player.floorY(floorName.getImgY() , floorHeight);
			return true;
		}//if
		else
			return false;
		
	}//checkTouchFloorL
//====================================================================================================	
	

//====================================碰觸地板右(Right)===============================================	
	public boolean touchFloorR() {
		
		switch(mapNum)
		{
			case 0:
				if(game.checkTouchFloorR(floor[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorR(floatFloor[0], 225, 48))
					return true;

				else
					return false;
				
			case 1:
				if(game.checkTouchFloorR(floor[0], 1300, 800))
					return true;
				
				else
					return false;
				
			case 2:
				if(game.checkTouchFloorR(floor[0], 1300, 800))
					return true;

				if(game.checkTouchFloorR(floor[1], 1300, 800))
					return true;
				
				if(game.checkTouchFloorR(floatFloor[0], 225, 48))
					return true;
				
				if(game.checkTouchFloorR(floatFloor[1], 225, 48))
					return true;
				
				if(game.checkTouchFloorR(floatFloor[2], 225, 48))
					return true;

				else
					return false;
				
			case 3:
				if(game.checkTouchFloorR(floorD[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorR(soil[0], 1280, 800))
					return true;
				
				if(game.checkTouchFloorR(soil[1], 1280, 800))
					return true;
				
				else
					return false;
				
			case 4:
				if(game.checkTouchFloorR(floorD[0], 1300, 800))
					return true;

				if(game.checkTouchFloorR(floorD[1], 1300, 800))
					return true;
				
				if(game.checkTouchFloorR(floatFloorD[0], 225, 48))
					return true;
				
				if(game.checkTouchFloorR(floatFloorD[1], 225, 48))
					return true;
				
				if(game.checkTouchFloorR(floatFloorD[2], 225, 48))
					return true;
				
				if(beenFinish)
					if(game.checkTouchFloorR(floatFloorD[3], 225, 48))
						return true;
				
				else
					return false;
				
			case 5:
				if(game.checkTouchFloorR(floorD[0], 1300, 800))
					return true;
				
				if(game.checkTouchFloorR(floorD[1], 1300, 800))
					return true;
				
				else
					return false;
				
			default:
				return false;
		}//switch
	}//touchFloorR
	
	
	public boolean checkTouchFloorR(Img floorName , int floorNameLengthSize , int floorNameHeightSize) {
		floorLength = floorName.getImgX() + floorNameLengthSize;
		floorHeight = floorName.getImgY() + floorNameHeightSize;
		if(player.getImgX() <= floorLength &&
				player.getImgX() >= floorLength-5 &&
					player.getImgY()+70 >= floorName.getImgY() &&
						player.getImgY() <= floorHeight)
		{ 
			player.floorX(floorName.getImgX() , floorLength);
			player.floorY(floorName.getImgY() , floorHeight);
			return true;
		}//if
		else
			return false;
		
	}//checkTouchFloorL
//====================================================================================================	
	
	
//=======================================碰觸結束的氣球===============================================
	
	public boolean touchFinishImg() {
		if(player.getImgX() >= finishImg.getImgX()+38 &&
				player.getImgY()+30 >= finishImg.getImgY() &&
					mapNum == 5)
		{
			finish = true;
			return true;
		}
			
		else
			return false;
	}//touchFinishImg
//====================================================================================================
	
	
//=========================================重新開始===================================================
	public void start() {
		//1273x770
		background.setLocation(0 , 0);
		background.setImg(0); 
		floor[0].setLocation(0 , 800-floor[0].getPreferredSize().height);
		floatFloor[0].setLocation(400 , 450);
		//monster.setLocation(500 , 700-floor.getPreferredSize().height);
		
		player.setLocation(0 , 550);//55x70 //647	
		
		addObj(background, 0);
		addObj(player, 1);
		//addObj(monster, 1);
		addObj(floor[0], 2);
		addObj(floatFloor[0], 2);
		refresh();
		
		player.control(7, 10);
	}//start
//====================================================================================================
	
	
	public static void main (String[] args) {
		game = new Game();
		game.start();
		while(death)
		{
			
			game.cleanPanel();
			
			if(finish)
				break;
			//播放死亡音樂
			
			death = false;
			game.deathUI();
			mapNum = 0;
			
			try { Thread.sleep(3000); } catch (InterruptedException e) {}
			game.cleanPanel();
			game.start();
			
		}//while
		game.finishUI();
		
	}//main

//=======================================死亡與結束UI================================================
	private void deathUI() {
		lifeCount++;
		loadLifeNum();
		background_death.setLocation(0, 0);
		//background_death.setImg(0);
		player.setLocation(490,400);
		
		addObj(background_death, 0);
		deathMsg.setLocation(640-(deathMsg.getPreferredSize().width/2),250);
		//deathMsg.setLocation(400,250);
		lifeNum.setLocation(560,410);
		addObj(player, 1);
		addObj(deathMsg, 2);
		addObj(lifeNum, 2);
		refresh();
	}//deathUI
	
	private void finishUI() {
		loadFinishMessage();
		background_death.setLocation(0, 0);
		
		addObj(background_death, 0);
		deathMsg.setLocation(640-(deathMsg.getPreferredSize().width/2),250);
		lifeNum.setLocation(640-(lifeNum.getPreferredSize().width/2),410);
		player.setLocation(640-(lifeNum.getPreferredSize().width/2)-70,400);
		
		addObj(player, 1);
		addObj(deathMsg, 2);
		addObj(lifeNum, 2);
		refresh();
	}//deathUI
//===================================================================================================	

	
//=========================================工具函式==================================================
	public void addObj(Img img, int layer) {
		panel.add(img);
		panel.setLayer(img, layer);
	}//addObj
	
	private void cleanPanel() {
		panel.removeAll();
		refresh();
	}//cleanPanel
	
	public void refresh() {
		panel.revalidate();
		panel.repaint();
	}//refresh
	
//====================================================================================================	

//=========================================例外處理==================================================
	public void outOfBoundary() throws outOfBoundaryException{
		if(player.getX() > 1280 || 
				player.getX()+54 < 0 ||
					player.getY()+70 < 0 ||
						player.getY() > 800)
			throw new outOfBoundaryException();
	}//outOfBoundary()
//====================================================================================================	
}//Game
