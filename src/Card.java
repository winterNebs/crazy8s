import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

//Isaac Wen
//2018-02-15
//Card class, with suit and number, and image
public class Card{
	private static final int SPRITE_SIZE = 200; 	//Consts for the sprite sheet
	private static final int SPRITE_DISPLAY_SIZE = 30;
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
	public static void init(){			//Initializes the sprite sheet
		try {
			spriteSheet = ImageIO.read(Card.class.getResourceAsStream("/img/CardSprite.png"));		//Tries to find the sprite sheet
			for (int i = 0; i < suits.length; i++) {					//Divides the first 4 images into separate images
		        suits[i] = spriteSheet.getSubimage(i * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
			}
			for (int i = 0; i < nums.length; i++) {						//Same but for all the numbers
		        nums[i] = spriteSheet.getSubimage((i+suits.length) * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE);
			}
		}
		catch (IOException e){
			System.out.println("imgerror");
		}
	}
	private BufferedImage nums() {
		return nums[number];		//Returns the array of images that are numbers
	}
	private BufferedImage suits() {
		return suits[suit];			//Returns the array of images that are suits
	}
	public BufferedImage cardImage() {//Creates a card from the sprites
        AffineTransform at = new AffineTransform();		//THis is for the mirrored cards
        at.rotate(Math.PI, CARD_WIDTH/2, CARD_HEIGHT/2);
        at.scale((double)SPRITE_DISPLAY_SIZE/SPRITE_SIZE,(double)SPRITE_DISPLAY_SIZE/SPRITE_SIZE);
		BufferedImage fullCard = new BufferedImage(CARD_WIDTH,CARD_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) fullCard.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
		g.drawImage(nums(),0,0,SPRITE_DISPLAY_SIZE,SPRITE_DISPLAY_SIZE,null);
		g.drawImage(suits(),0,SPRITE_DISPLAY_SIZE,SPRITE_DISPLAY_SIZE,SPRITE_DISPLAY_SIZE,null);
		g.drawImage(nums(),at,null);
		at.translate(0, SPRITE_SIZE);
		g.drawImage(suits(),at,null);
		if(!playable) {							//Adds grayed out color if unplayable
			g.setColor(new Color(0,0,0,100));
			g.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
		}
		return fullCard;
	}
}
