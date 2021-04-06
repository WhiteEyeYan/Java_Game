package Game;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class Img extends abstractImg implements interfaceImg{
	protected int x;
	protected int y;
	private ImageIcon img[];
	protected String name;
	/**
	 *	Img(String filename, int count)<br>
	 * 	設定多個檔案時可使用 <br>
	 *	另有載入單個檔案的 Img(String filename)
	 * @param filename 欲載入檔案名稱
	 * @param count 欲s載入數量
	 */
	public Img(String filename, int count) {
		this.name = filename;
		loadImg(filename, count);
	}//Img
	/**
	 *	Img(String filename)<br>
	 * 	設定單個檔案時可使用 <br>
	 *	另有載入多個檔案的 Img(String filename, int count)
	 * @param filename 欲載入檔案名稱
	 */
	public Img(String filename) {
		this.name = filename;
		loadImg(filename);
	}//Img
	
	public int getImgX() {
		return x;
	}//getObjX
	
	public int getImgY() {
		return y;
	}//getObjY
	/**
	 * setLocation(x,y)<br>
	 * 	設定一個Lable標籤的位置 <br>
	 *	輸入的xy為想要的位置<br>
	 *	該xy會設定Lable左上角的那點至該xy位置<br>
	 * @param x 欲設定的x座標
	 * @param y 欲設定的y座標
	 */
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		setBounds(x, y, getPreferredSize().width, getPreferredSize().height);
	}// setLocation
	
	protected void loadImg(String filename, int count) {
		img = new ImageIcon[count];
		try {
			for (int i = 0; i < count; i++) {
				img[i] = new ImageIcon(ImageIO.read(Game.class.getResource("/picture/" + filename + i + ".png")));
			} // for
			setIcon(img[0]);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} // try-catch
	}//loadImg
	
	protected void loadImg(String filename) {
		img = new ImageIcon[1];
		try {
			img[0] = new ImageIcon(ImageIO.read(Game.class.getResource("/picture/" + filename + ".png")));
			
			setIcon(img[0]);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} // try-catch
	}//loadImg
	
	public void setImg(int number) {
		setIcon(img[number]);
	}//setImg
	
	
}//Img
