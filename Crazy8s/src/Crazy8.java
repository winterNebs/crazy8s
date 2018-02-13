//Isaac Wen
/*Crazy 8s:
1. Played card must be either same suit or number as top card on table
2. Can draw as many cards until willing/able to play a valid card
3. After drawing a card, player can skip their turn
4. Winner is first to have no cards

Special Cards:
2, any suit: Next person picks up 2 cards and their turn is skipped, unless they can play a 2 or Queen of spades
7, any suit: Skips next person's turn
8, any suit: Becomes declared suit
Jack, any suit: Reverse table order
Queen, spades: Pickup 5, same rules as 2
 */

/* Player Name:
 * Hand:
 * Pickup/skip
 * Next Players:
 * */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/*TODO:
 */
import java.util.Collections;
public class Crazy8 extends JFrame{
	//Declaring some constants
	private final static int HAND_SIZE = 8;
	private final static int PLAYER_COUNT = 3;
	private final static int DECK_SIZE = 52;
	//Declaring pickup counter, and the deck, cards on table, and players
	private int pickup = 0;
	private Deck deck1;
	private Deck table;
	private ArrayList<Player> players = new ArrayList<Player>();
	private JPanel cardArea;
	private JPanel hands;
	private JPanel buttons;
	private JPanel currentCard;
	private JPanel playerList;
	private JPanel title;
	private ArrayList<JPanel> panels = new ArrayList<JPanel>();
	private boolean drew = false;
	private BorderLayout layout = new BorderLayout();
	public static void main(String args[]){
		//System.out.println(deck1.Output());
		Crazy8 game = new Crazy8();
	}
	private Crazy8() {
		super("Crazy 8s");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(1280,720);
		for(int i = 0; i < PLAYER_COUNT; i++){
			players.add(new Player("Player " + i));		//initializing players
		}
		GridLayout vertGrid = new GridLayout();
		vertGrid.setColumns(1);
		vertGrid.setRows(0);
		this.getContentPane().setLayout(vertGrid);
		panels.add(cardArea = new JPanel(new FlowLayout()));
		panels.add(currentCard = new JPanel(new FlowLayout()));
		GridLayout fourGrid = new GridLayout();
		fourGrid.setColumns(8);
		fourGrid.setRows(1);
		fourGrid.setVgap(8);
		fourGrid.setHgap(8);
		hands = new JPanel();
		hands.setLayout(fourGrid);
		panels.add(hands);
		panels.add(buttons = new JPanel(new FlowLayout()));
		panels.add(title = new JPanel(new FlowLayout()));
		cardArea.add(title);
		cardArea.add(hands);
		cardArea.add(buttons);
		this.add(cardArea);
		this.add(currentCard);
		this.pack();
		this.setVisible(true);
		this.setSize(1280,720);
		newRound();		//Resets table
		Card.init();
		update();
	}
	public void paint(Graphics g) {
		super.paint(g);	
	}
	private void update() {	
		System.out.println("Table size: " + table.getSize());
		System.out.println(currentPlayer().showHand());
		System.out.println("Pick up:" + pickup);
		for(int i = 0; i < currentPlayer().hand.getSize(); i++) {
			currentPlayer().hand.getCard(i).playable = checkCard(currentPlayer().hand.getCard(i));
		}
		title.removeAll();
		title.add(new JLabel(currentPlayer().name +"'s hand: "));
		currentCard.removeAll();
		currentCard.add(new JLabel("Current Card:"));
		currentCard.add(new JLabel(new ImageIcon(table.last().cardImage())));
		buttons.removeAll();
		hands.removeAll();
		boolean hasCards = false;
		for(int i = 0; i < currentPlayer().hand.getSize(); i++) {
			JLabel card = new JLabel(new ImageIcon(currentPlayer().hand.getCard(i).cardImage()));
			hasCards = hasCards || currentPlayer().hand.getCard(i).playable; //See if has any playable cards
			card.setName(i+"");
			if(currentPlayer().hand.getCard(i).playable) {
				card.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(Integer.parseInt(((JLabel)e.getSource()).getName()) >= 0 || Integer.parseInt(((JLabel)e.getSource()).getName()) < currentPlayer().hand.getSize()) {
							playCard(Integer.parseInt(((JLabel)e.getSource()).getName()));				
							update();
						}
						System.out.println(((JLabel)e.getSource()).getName());       
					}
				});			
			}
			hands.add(card);
		}
		System.out.println("Has card: " + hasCards);
		JLabel pickupCard = new JLabel("Pick up card");
		pickupCard.setBackground(new Color(100,100,100,100));
		pickupCard.setOpaque(true);
		pickupCard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				drew = true;
				/// FORCE DRAW SKIP TURN
				if(pickup > 0) {
					for(int i = 0; i < pickup; i++) {
						currentPlayer().hand.cards.add(deck1.deal()); 	
					}
					nextTurn();
					update();
				}
				currentPlayer().hand.cards.add(deck1.deal()); 				
				update();
			}
		});
		buttons.add(pickupCard);
		if(drew) {
			JLabel skipTurn = new JLabel("Skip Turn");
			skipTurn.setBackground(new Color(100,100,100,100));
			skipTurn.setOpaque(true);
			skipTurn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					nextTurn();
					update();
				}
			});
			buttons.add(skipTurn);
		}
		for(JPanel i: panels) {
			i.revalidate();
			//i.getLayout().
			i.repaint();
		}		
		revalidate();
		repaint();
	}

	private void newRound(){		//Resets the table
		deck1 = new Deck(DECK_SIZE);
		deck1.shuffle();							//Creates a new shuffled deck, throws out the old cards on the table.
		table = new Deck(0);
		pickup = 0;									//Resets points
		for(int i = 0; i < players.size(); i++){	//Clears the players' hands
			players.get(i).hand.cards.clear();
			for(int j = 0; j < HAND_SIZE; j++) {
				players.get(i).hand.cards.add(deck1.deal());
			}
		}
		table.cards.add(deck1.deal());
	}
	private void playCard(int i) {
		boolean skip = false;
		switch(currentPlayer().hand.getCard(i).number+1){
		case 2:
			pickup += 2;
			break;
		case 7:
			skip = true;
			break;
		case 8:
			Object[] options = {"Spade", "Clubs", "Hearts", "Diamonds"};
			int response = JOptionPane.showOptionDialog(null, "Message", "Title",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, options, options[0]);
			System.out.println(response);
			if(response != 0) {
				currentPlayer().hand.getCard(i).suit = response;
			}
		case 11:
			Collections.reverse(players);
			break;
		case 12:
			if(currentPlayer().hand.getCard(i).suit == 0) {
				pickup += 5;
			}
			break;
		}
		table.cards.add(currentPlayer().hand.cards.remove(i));
		nextTurn();
		if(skip) {
			nextTurn();
		}
	}
	private boolean checkCard(Card a) {
		if(pickup > 0) {
			if(table.last().number == 2 && a.number == 2) {
				return true;
			}
			else if(table.last().number == 12 && a.number == 12 && a.suit == 0) {
				return true;
			}
			else {
				return false;
			}
		}
		if(a.number == 8) {
			return true;
		}
		return (table.last().number == a.number || table.last().suit == a.suit);
	}
	private Player currentPlayer() {
		return players.get(0);
	}
	private boolean roundOver(){
		for(Player p : players) {
			if(p.hand.getSize() == 0) {
				JOptionPane.showMessageDialog(null, p.name);
				newRound();
			}
		}
		return false;
	}
	private void tableSwap() {
		if(deck1.getSize() == 0) {
			for(int i = 0; i < table.getSize()-1; i++) {
				deck1.cards.add(table.deal());
				deck1.shuffle();
			}
		}
	}
	private void nextTurn(){
		drew = false;
		players.add(currentPlayer());		//Rotates the players around the table. 
		players.remove(0);
	}
}
