//Isaac Wen
/*Crazy 8s:
1. Played card must be either same suit or number as top card on table
2. Can draw as many cards until willing/able to play a valid card
3. After drawing a card, player can skip their turn
4. Winner is first to have no cards

Special Cards:
2, any suit: Next person picks up 2 cards, unless they can play a 2 or Queen of spades
7, any suit: Skips next person's turn
8, any suit: Becomes declared suit
Jack, any suit: Reverse table order
Queen, spades: Pickup 5, same rules as 2
 */

/* Player Name: Hand:
 * Pickup/skip
 * Next Players:
 * */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/*TODO:
 */
import java.util.Collections;
public class Crazy8 extends JFrame{
	private final static int HAND_SIZE = 8;		//Constants
	private final static int PLAYER_COUNT = 3;	//Used to start a game off with a number of players
	private final static int DECK_SIZE = 52;
	private final static int GAP = 4;
	private int pickup = 0;		//Amount of cards to be picked up (when 2 or Q of spade is played)
	private Deck deck1;			//The deck that cards would be dealt from
	private Deck table;			//The table where cards are played to
	private boolean drew;		//Whether the player has drawn a card (affects whether they can skip turn)
	private ArrayList<Player> players = new ArrayList<Player>();	//List of players
	private JPanel cardArea;	//*******GUI STUFF START********//
	private JPanel hands;		//All of these panels are used	//
	private JPanel buttons;		//for organization. Sadly the	//
	private JPanel currentCard;	//layouts are awkward to use.	//
	private JPanel playerList;	//Which can cause alignment		//
	private JPanel title;		//issues						//
	private JPanel pickupList;	//********GUI STUFF END*********//
	private JMenuBar menuBar;		//********MENU STUFF START**********//
	private JMenu menu;				//Since the menu is an item on the	//
	private JMenuItem help;			//JFrame, it suffers from the same	//
	private JMenuItem addPlayer;	//layout manager/alignment issues	//
	private JMenuItem resetRound;	//********MENU STUFF END************//
	static Crazy8 game;			//Static instance of our game
	public static void main(String args[]){
		game = new Crazy8();	//Initializes game
	}
	private Crazy8() {		//Default constructor
		super("Crazy 8s");	//Calls super constructor & sets title
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Default close operation
		/*for(int i = 0; i < PLAYER_COUNT; i++){
			players.add(new Player("Player " + i));		//initializing set amount of players (no longer needed)
		}*/
		addPlayer();	//Adds first player
		initGUI();		//Initializes GUI
		initMenu();		//Initializes Menus
		newRound();		//Resets table
		Card.init();	//Initializes card sprites
		update();		//Updates GUI/game
	}
	private void addPlayer() {
		String input = JOptionPane.showInputDialog(		//Asks for first player name
				game, "Enter new player's name", "New Player", JOptionPane.WARNING_MESSAGE);
		if(input.isEmpty() || input == null) {
			players.add(new Player("Player "+players.size()));
		}
		else {
			players.add(new Player(input));
		}
	}
	private void initMenu() {
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		addPlayer = new JMenuItem("Add Player");
		help = new JMenuItem("Help");
		resetRound = new JMenuItem("Restart Round");
		this.add(menuBar);
		menuBar.add(menu);
		menu.add(help);
		menu.add(addPlayer);
		menu.add(resetRound);
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(game,
						"Crazy 8s:\r\n" + 
								"1. Played card must be either same suit or number as top card on table\r\n" + 
								"2. Can draw as many cards until willing/able to play a valid card\r\n" + 
								"3. After drawing a card, player can skip their turn\r\n" + 
								"4. Winner is first to have no cards\r\n" + 
								"\r\n" + 
								"Special Cards:\r\n" + 
								"2, any suit: Next person picks up 2 cards and loses their turn, unless they can play a 2 or Queen of spades\r\n" + 
								"7, any suit: Skips next person's turn\r\n" + 
								"8, any suit: Becomes declared suit\r\n" + 
								"Jack, any suit: Reverse table order\r\n" + 
								"Queen, spades: Pickup 5, same rules as 2",
								"Help",
								JOptionPane.QUESTION_MESSAGE);
			}
		});
		addPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPlayer();
				newRound();
				update();
			}
		});
		resetRound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newRound();
				update();
			}
		});
	}
	private void initGUI() {
		this.getContentPane().setLayout(new FlowLayout());
		playerList = new JPanel(new GridLayout(0,1,GAP,GAP));
		hands = new JPanel(new GridLayout(0,4,GAP,GAP));
		currentCard = new JPanel(new FlowLayout());
		buttons = new JPanel(new FlowLayout());
		title = new JPanel(new FlowLayout());
		cardArea = new JPanel(new GridLayout(0,1,GAP,GAP));
		pickupList = new JPanel(new FlowLayout());
	}
	private void clearGUI() {
		title.removeAll();
		currentCard.removeAll();
		buttons.removeAll();
		hands.removeAll();
		playerList.removeAll();
		pickupList.removeAll();
	}
	private void update() {	
		System.out.println("Table size: " + table.getSize());
		System.out.println(currentPlayer().showHand());
		System.out.println("Pick up:" + pickup);
		clearGUI();
		pickupList.add(new JLabel("Pickup: " + pickup));
		playerList.add(new JLabel("Next:"));
		for(int i = 1; i < players.size(); i++) {
			playerList.add(new JLabel(players.get(i).name + ", Cards: " + players.get(i).hand.getSize() + ", Wins: " + players.get(i).wins));
		}
		playerList.add(new JLabel(currentPlayer().name + ", Cards: " + currentPlayer().hand.getSize() + ", Wins: " + currentPlayer().wins));
		for(int i = 0; i < currentPlayer().hand.getSize(); i++) {
			currentPlayer().hand.getCard(i).playable = checkCard(currentPlayer().hand.getCard(i));
		}
		title.add(new JLabel(currentPlayer().name +"'s hand: "));
		currentCard.add(new JLabel("Current Card:"));
		currentCard.add(new JLabel(new ImageIcon(table.last().cardImage())));
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
				if(pickup > 0) {
					for(int i = 0; i < pickup; i++) {
						tableSwap();
						//currentPlayer().hand.cards.add(deck1.deal()); 	
						players.get(0).hand.cards.add(deck1.deal()); 
					}
					pickup = 0;
					nextTurn();
					update();
				}
				tableSwap();
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
		resetGUI();
		roundOver();
	}
	private void resetGUI() {
		//cardArea.add(title);
		cardArea.add(hands);
		cardArea.add(buttons);
		this.add(title);
		this.add(cardArea);
		this.add(currentCard);
		this.add(playerList);	
		this.add(pickupList);
		this.revalidate();
		this.repaint();
		this.pack();
		this.setVisible(true);
	}
	private void newRound(){		//Resets the table
		deck1 = new Deck(DECK_SIZE);
		deck1.shuffle();							//Creates a new shuffled deck, throws out the old cards on the table.
		table = new Deck(0);
		drew = false;
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
		System.out.println("Played card: " + currentPlayer().hand.getCard(i).formatter());
		System.out.println("Table card: " + table.last().formatter());
		switch(currentPlayer().hand.getCard(i).number+1){
		case 2:
			pickup += 2;
			break;
		case 7:
			skip = true;
			break;
		case 8:
			Object[] options = {"Spade", "Clubs", "Hearts", "Diamonds"};
			int response = JOptionPane.showOptionDialog(game, "Pick Which Suit", "WARNING! THIS EIGHT IS VERY CRAZY",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, options, options[0]);
			System.out.println(response);
			if(response >= 0 && response <= 3) {
				currentPlayer().hand.getCard(i).suit = response;
				System.out.println("Eight Suit: " + currentPlayer().hand.getCard(i));
			}
			else {
				return;
			}
			break;
		case 11:
			Collections.reverse(players);
			break;
		case 12:
			if(currentPlayer().hand.getCard(i).suit == 0) {
				pickup += 5;
			}
			break;
		}
		try {
			System.out.println("Picked: " + i);
			System.out.println("Hand size: " + currentPlayer().hand.getSize() + " " + currentPlayer().name);
			System.out.println("Played: " + currentPlayer().hand.cards.get(i).formatter());
			System.out.println(currentPlayer().hand.output());
			currentPlayer().hand.cards.get(i).playable = true;
			table.cards.add(currentPlayer().hand.cards.remove(i));
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println(e);
		}
		nextTurn();
		if(skip) {
			nextTurn();
		}
	}
	private boolean checkCard(Card a) {
		int tablenum = table.last().number + 1;
		int cardnum = a.number + 1;
		if(pickup > 0) {
			if(tablenum == 2 && cardnum == 2) {
				return true;
			}
			else if(tablenum == 12 && cardnum == 12 && a.suit == 0) {
				return true;
			}
			else {
				return false;
			}
		}
		if(cardnum == 8) {
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
				JOptionPane.showMessageDialog(game, "Dinner Dinner Chicken Winner " +p.name);
				p.wins++;
				newRound();
				update();
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
