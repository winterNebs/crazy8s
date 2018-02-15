//Isaac Wen
//2017-03-09
import java.util.ArrayList;

public class Deck{	//Deck is an array of cards
	//Variables
	public static final int MAX_CARD_VAL = 13;
	public static final int MAX_SUIT_VAL = 4;
	private int size;
	public ArrayList<Card> cards = new ArrayList<Card>();
	public Deck(){	//Default constructor with typical deck size.
		size=52;
		New();
	}
	public Deck(int s){	//Overloaded with deck size
		size = s;
		New();
	}
	public void New(){	//Initializes cards in the deck.
		for(int i = 0; i < size; i++){
			cards.add(new Card(i%MAX_CARD_VAL,i%MAX_SUIT_VAL));			//Does some fancy math (counts 1-13 for number,and 1-4 for suits)
		}
	}
	public void empty(){		//Empty deck
		cards.clear();
	}
	public String output(){		//Outputs all the cards nicely, using the card format for each card
		String out = ""; 
		for(int i = 0; i < cards.size(); i++){
			out += cards.get(i).formatter();
		}
		return out;
	}
	public void shuffle(){		//Grabs a random card from the old deck, and then adds it to the new deck
		ArrayList<Card> old = new ArrayList<Card>();		//Repeat until out of cards.
		for(int i = 0; i < cards.size(); i++){
			old.add(new Card(cards.get(i).number,cards.get(i).suit));
		}
		empty();
		for(int i = old.size(); i > 0; i--){
			int random = (int) (Math.random() * i);
			cards.add(new Card(old.get(random).number,old.get(random).suit));
			old.remove(random);
		
		}
	}
	public Card getCard(int i) {
		return cards.get(i);
	}
	public int getSize() {
		return cards.size();
	}
	public Card deal() {
		return cards.remove(0);
	}
	public Card first() {
		return getCard(0);
	}
	public Card last() {
		return getCard(getSize()-1);
	}
}