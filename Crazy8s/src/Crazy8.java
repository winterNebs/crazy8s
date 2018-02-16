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
			players.add(new Player("Player " + players.size()));	//Add selected 
		}
		else {
			players.add(new Player(input));					//If empty/cancel set to a default
		}
	}
	private void initMenu() {						//Initializes all menu items
		menuBar = new JMenuBar();					//Make menu bar
		menu = new JMenu("Menu");					//Makes menu with name
		addPlayer = new JMenuItem("Add Player");	//Adds a menu item to add player
		help = new JMenuItem("Help");				//Adds a menu item to display help menu
		resetRound = new JMenuItem("Restart Round");//Adds a menu item to restart the round
		this.add(menuBar);							//Adds the menu bar to the JFrame
		menuBar.add(menu);							//Adds the menu to the menu bar
		menu.add(help);								//Adds the help option to the menu
		menu.add(addPlayer);						//Adds the add player option to the menu
		menu.add(resetRound);						//Adds the reset round option to the menu
		help.addActionListener(new ActionListener() {	//Button click event for the help button
			public void actionPerformed(ActionEvent e) {//If clicked display this text
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
		addPlayer.addActionListener(new ActionListener() {	//Button click event for the add player button
			public void actionPerformed(ActionEvent e) {	//Adds a new player and resets the round
				addPlayer();							
				newRound();
				update();
			}
		});
		resetRound.addActionListener(new ActionListener() {	//Button click event for the reset round button
			public void actionPerformed(ActionEvent e) {	//Resets round
				newRound();
				update();
			}
		});
	}
	private void initGUI() {									//Initializes GUI
		this.getContentPane().setLayout(new FlowLayout());		//Sets the layout to flow for the main frame
		playerList = new JPanel(new GridLayout(0,1,GAP,GAP));	//Initializes the player list with a vertical grid
		hands = new JPanel(new GridLayout(0,4,GAP,GAP));		//Initializes the hand with a 4 wide grid
		currentCard = new JPanel(new FlowLayout());				//Initializes current card
		buttons = new JPanel(new FlowLayout());					//Initialize buttons
		title = new JPanel(new FlowLayout());					//Initialize title
		cardArea = new JPanel(new GridLayout(0,1,GAP,GAP));		//Initialize the card area vertical grid
		pickupList = new JPanel(new FlowLayout());				//Initialize the list 
	}
	private void clearGUI() {		//Resets the GUI
		title.removeAll();			//Removes everything
		currentCard.removeAll();
		buttons.removeAll();
		hands.removeAll();
		playerList.removeAll();
		pickupList.removeAll();
	}
	private void update() {			//Updates the GUI and waits for input
		System.out.println("Table size: " + table.getSize());
		System.out.println(currentPlayer().showHand());
		System.out.println("Pick up:" + pickup);
		clearGUI();											//Resets GUI
		pickupList.add(new JLabel("Pickup: " + pickup));	//Shows how many card need to be picked up
		playerList.add(new JLabel("Next:"));				//Shows who is next
		for(int i = 1; i < players.size(); i++) {			//Out puts the who's turn is next, how many cards they have and number of wins
			playerList.add(new JLabel(players.get(i).name + ", Cards: " + players.get(i).hand.getSize() + ", Wins: " + players.get(i).wins));
		}													//Adds the current player to the end of this list aswell
		playerList.add(new JLabel(currentPlayer().name + ", Cards: " + currentPlayer().hand.getSize() + ", Wins: " + currentPlayer().wins));
		for(int i = 0; i < currentPlayer().hand.getSize(); i++) {		//Check if the player has playable cards
			currentPlayer().hand.getCard(i).playable = checkCard(currentPlayer().hand.getCard(i));	
		}
		title.add(new JLabel(currentPlayer().name +"'s hand: "));		//Shows who is currently playing
		currentCard.add(new JLabel("Current Card:"));					//Label to show current card
		currentCard.add(new JLabel(new ImageIcon(table.last().cardImage())));		//Image of the current card
		boolean hasCards = false;										//Whether the player can play or has to draw
		for(int i = 0; i < currentPlayer().hand.getSize(); i++) {		//Displays players hand
			JLabel card = new JLabel(new ImageIcon(currentPlayer().hand.getCard(i).cardImage()));
			hasCards = hasCards || currentPlayer().hand.getCard(i).playable; //See if has any playable cards (If has a card it stays true)
			card.setName(i+"");											//Sets the name of the image to the index in the hand
			if(currentPlayer().hand.getCard(i).playable) {				//If the card is playable add a click listener
				card.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {			//Play the clicked card
						if(Integer.parseInt(((JLabel)e.getSource()).getName()) >= 0 || Integer.parseInt(((JLabel)e.getSource()).getName()) < currentPlayer().hand.getSize()) {
							playCard(Integer.parseInt(((JLabel)e.getSource()).getName()));			
							roundOver();								//Check if winner
							update();
						}
						System.out.println(((JLabel)e.getSource()).getName());       
					}
				});			
			}
			hands.add(card);	//Add the image to the hand to display
		}
		System.out.println("Has card: " + hasCards);
		JLabel pickupCard = new JLabel("Pick up card");			//Initializes a button to allow players to pickup cards
		pickupCard.setBackground(new Color(100,100,100,100));	//Sets the background of said button to be darker that way it's more intuitive
		pickupCard.setOpaque(true);								//Sets the card to be opaque that way the background appears
		pickupCard.addMouseListener(new MouseAdapter() {		//Click listener for the button
			@Override
			public void mouseClicked(MouseEvent e) {
				drew = true;									//If they draw a card that means they can skip their turn (ie not/can't play a card)
				if(pickup == 0) {								//If there is a pickup counter (ie 2 or Q spade has been played) force pick up cards
					pickup = 1;
				}
				for(int i = 0; i < pickup; i++) {			
					tableSwap();							//Check if there are enough cards in the deck
					currentPlayer().hand.cards.add(deck1.deal()); 	//Deal the card to the player
				}
				if(pickup > 1) {
					nextTurn();									//Next turn if a 2 or Q Spade was previously played
				}
				pickup = 0;									//Resets the pickup counter
				update();									//Updates GUI
			}
		});
		buttons.add(pickupCard);								//Adds a button to allow players to pickup cards
		if(drew) {												//If the current player has drawn a card allow them to skip their turn
			JLabel skipTurn = new JLabel("Skip Turn");			//Initializes said button	
			skipTurn.setBackground(new Color(100,100,100,100));	//Sets background color
			skipTurn.setOpaque(true);							//Makes it opaque
			skipTurn.addMouseListener(new MouseAdapter() {		//Adds a click listener
				@Override
				public void mouseClicked(MouseEvent e) {		//Cycles to next turn, updates GUI
					nextTurn();
					update();
				}
			});
			buttons.add(skipTurn);								//Adds the button to the buttons
		}
		resetGUI();												//Resets GUI
		
	}
	private void resetGUI() {									//Adds everything to where they should be
		cardArea.add(hands);
		cardArea.add(buttons);
		this.add(title);
		this.add(cardArea);
		this.add(currentCard);
		this.add(playerList);	
		this.add(pickupList);
		this.revalidate();
		this.repaint();
		this.pack();											//Packs/revalidate Frame so it is the right size
		this.setVisible(true);
	}
	private void newRound(){						//Resets the table
		deck1 = new Deck(DECK_SIZE);
		deck1.shuffle();							//Creates a new shuffled deck, throws out the old cards on the table.
		table = new Deck(0);
		drew = false;								//Resets whether they have drawn
		pickup = 0;									//Resets points
		for(int i = 0; i < players.size(); i++){	//Clears the players' hands
			players.get(i).hand.cards.clear();
			for(int j = 0; j < HAND_SIZE; j++) {
				players.get(i).hand.cards.add(deck1.deal());	//Deals new cards
			}
		}
		table.cards.add(deck1.deal());				//Deals the first card
	}
	private void playCard(int i) {					//Allows player to play a card
		boolean skip = false;						//Skip next players turn
		System.out.println("Played card: " + currentPlayer().hand.getCard(i).formatter());
		System.out.println("Table card: " + table.last().formatter());
		switch(currentPlayer().hand.getCard(i).number+1){	//Switch the current card value
		case 2:				//2, any suit = pickup 2
			pickup += 2;
			break;
		case 7:				//7, any suit = skip next player's turn
			skip = true;
			break;
		case 8:				//8, any suit = let player choose what suit it is
			Object[] options = {"Spade", "Clubs", "Hearts", "Diamonds"};		//Options for the panel
			int response = JOptionPane.showOptionDialog(game, "Pick Which Suit", "WARNING! THIS EIGHT IS VERY CRAZY",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, options, options[0]);	//Gets user input
			System.out.println(response);
			if(response >= 0 && response <= 3) {
				currentPlayer().hand.getCard(i).suit = response;
				System.out.println("Eight Suit: " + currentPlayer().hand.getCard(i));
			}
			else {
				return;						//If they cancel don't do anything
			}
			break;
		case 11:							//Jack, any suit = reverse players
			Collections.reverse(players);
			break;
		case 12:							//Queen, spades = pickup 5
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
			currentPlayer().hand.cards.get(i).playable = true;			//Reset playability of card
			table.cards.add(currentPlayer().hand.cards.remove(i));		//Remove card from hand and add it to the table;
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println(e);										//At some point this threw an error for no reason, I believe that the size would get out of sync or something, but it hasn't thrown anything since I put the try catch
		e.printStackTrace();
		}
		nextTurn();				//Cycles next turn
		if(skip) {
			nextTurn();			//If the next player is skipped skip twice
		}
	}
	private boolean checkCard(Card a) {				//Check whether a card is currently playable
		int tablenum = table.last().number + 1;		//Ace is 0 so +1 to make ace = 1
		int cardnum = a.number + 1;					//Same here
		if(pickup > 0) {							//If a 2 or Queen spades, only allow 2s or queens for stacking
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
		if(cardnum == 8) {							//An eight is always fine unless there has been a 2 or Qspade
			return true;
		}											//Otherwise check if same suit or same number
		return (table.last().number == a.number || table.last().suit == a.suit);
	}
	private Player currentPlayer() {
		return players.get(0);				//Returns current player, easier to type
	}
	private boolean roundOver(){			//Check if a player has 0 cards
		for(Player p : players) {
			if(p.hand.getSize() == 0) {
				JOptionPane.showMessageDialog(game, "Dinner Dinner Chicken Winner " +p.name);
				p.wins++;					//Give them the win and start a new round
				newRound();
				update();
			}
		}
		return false;
	}
	private void tableSwap() {				//Check if there are still cards left in the deck. If there aren't take all but the current card from the table and add them to the deck and shuffle
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
