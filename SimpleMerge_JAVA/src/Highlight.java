import java.awt.Color;

public class Highlight {
	public static final Color GRAY = Color.LIGHT_GRAY;
	public static final Color YELLOW = Color.YELLOW;
	public static final Color PINK = Color.PINK;
	public static final Color GRAY_SELECT = Color.GRAY ;
	public static final Color YELLOW_SELECT = Color.ORANGE;
	public static final Color PINK_SELECT = Color.MAGENTA;
	private Color color;
	private int lineNum;
	private boolean isEntireLine;
	private int start;
	private int end;
	
	public Highlight(Color color, int lineNum){
		this.color = color;
		this.lineNum = lineNum;
		this.isEntireLine = true;
		this.start = -1;
		this.end = -1;
	}
	
	public Highlight(Color color, int start, int end){
		this.color = color;
		this.lineNum = -1;
		this.isEntireLine = false;
		this.start = start;
		this.end = end;
	}
	
	public Color getColor(){
		return color;
	}
	
	public int getLineNum(){
		return lineNum;
	}
	
	public boolean getIsEntireLine(){
		return isEntireLine;
	}
	
	public int getStart(){
		return start;
	}
	
	public int getEnd(){
		return end;
	}
	
	public void changeToSelect(){
		if(color == this.GRAY){
			color = this.GRAY_SELECT;
		} else if(color == this.YELLOW){
			color = this.YELLOW_SELECT;
		} else if(color == this.PINK){
			color = this.PINK_SELECT;
		}
	}
	
	public void changeToNormal(){
		if(color == this.GRAY_SELECT){
			color = this.GRAY;
		} else if(color == this.YELLOW_SELECT){
			color = this.YELLOW;
		} else if(color == this.PINK_SELECT){
			color = this.PINK;
		}
	}
	
}
