//Isaac Wen
//2017-03-09
public class Player {
	//Each player has a hand(a personal deck), name, and number of letters
	//BatWord and BAT aren't hard coded, so that you can set your own words in the future
	//Variables
	public Deck hand;
	public String name;
	public int wins;
	public Player(){	//Default cnstructor
		name = "default";
		hand = new Deck(0);
		wins = 0;
	}
	public Player(String s){	//Constructor with settable name
		name = s;
		hand = new Deck(0);
		wins = 0;
	}
	
	public String showHand(){		//Nicely formats the cards in their hand(Deck)
		String s = "Enter the number of the card you want to choose:\n";
		for(int i = 0; i < hand.cards.size(); i++){
			s += (i+1) + ". " + hand.cards.get(i).formatter();
		}
		return s;
	}
}
