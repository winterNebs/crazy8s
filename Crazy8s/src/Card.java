import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Isaac Wen
//2017-03-09
//Card class, with suit and number
public class Card{
	//Variables
	private static final int SPRITE_SIZE = 32;
	private static final int COLS = 17;
	private static final int CARD_WIDTH = 128;
	private static final int CARD_HEIGHT = 192;
	
	private static BufferedImage spriteSheet;
	private static BufferedImage[] suits = new BufferedImage[Deck.MAX_SUIT_VAL];
	private static BufferedImage[] nums = new BufferedImage[Deck.MAX_CARD_VAL];
	public int number;
	public int suit;
	public boolean playable;
	public Card(){	//Default constructor
		number = 0;
		suit = 0;
		playable = true;
	}
	public Card(int n, int s){	//Overloaded constructor (lets user set the card number & suit)
		number = n;
		suit = s;
		playable = true;
	}
	public String formatter(){	//Formats the suit and number nicely
		String out = "";
		int tempNum = number + 1;
		switch(tempNum){
		case 1: out += "Ace"; break;
		case 11: out += "Jack"; break;
		case 12: out += "Queen"; break;
		case 13: out += "King"; break;
		default: out += tempNum;break;
		}
		switch(suit){
		case 0: out += " of " + "Spades " +  "\n"; break;
		case 1: out += " of " + "Clubs " +  "\n"; break;
		case 2: out += " of " + "Hearts " + "\n"; break;
		case 3: out += " of " + "Diamonds " + "\n"; break;
		}
		return out;
	}
	public static void init(){
		try {
			spriteSheet = ImageIO.read(new File("CardSprite.png"));
			for (int i = 0; i < suits.length; i++) {
		        suits[i] = spriteSheet.getSubimage(i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
			}
			for (int i = 0; i < nums.length; i++) {
		        nums[i] = spriteSheet.getSubimage((i+suits.length) * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
			}
		}
		catch (IOException e){
			System.out.println("imgerror");
		}
	}
	public BufferedImage nums() {
		return nums[number];
	}
	public BufferedImage suits() {
		return suits[suit];
	}
	public BufferedImage cardImage() {
		BufferedImage fullCard = new BufferedImage(CARD_WIDTH,CARD_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		Graphics g = fullCard.getGraphics();
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
		g.drawImage(nums(),0,0,null);
		g.drawImage(suits(),0,SPRITE_SIZE,null);
		if(!playable) {
			g.setColor(new Color(0,0,0,170));
			g.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
		}
		return fullCard;
	}
}
