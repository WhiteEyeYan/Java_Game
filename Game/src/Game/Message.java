package Game;
import java.awt.*;

public class Message extends Img {

	public Message(String text, boolean trans, String fontName, int style, int fontSize, Color fontColor, Color bg) {
		super(null, 0);
		setText(text);
		setBackground(bg);
		setOpaque(!trans);
		setForeground(fontColor);
		setFont(new Font(fontName, style, fontSize));
	}//MESSAGE
	
	protected void loadImg(String filename, int count) {
		//empty
	}//loadImg

}//MessageBox
