import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GoFishForTwo {
    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private char whoseTurn = 'P';
    private final Player player1 = new Player();
    private final Player player2 = new Player();
    private List<Card> deck;
    private final Scanner in = new Scanner(System.in);

    public void play() {
            // play the game until someone wins
        shuffleAndDeal();

            while (true) {
                if (whoseTurn == 'P') {
                    whoseTurn = takeTurn(false);

                    if (player1.findAndRemoveBooks()) {
                        System.out.println("PLAYER 1: Oh, that's a book!");
                        showBooks(false);
                    }
                } else if (whoseTurn == 'C') {
                    whoseTurn = takeTurn(true);

                    if (player2.findAndRemoveBooks()) {
                        System.out.println("PLAYER 2: Oh, that's a book!");
                        showBooks(true);
                    }
                }

                // the games doesn't end until all 13 books are completed. the player with the
                // most books at the end of the game wins.

                if (player1.getBooks().size() + player2.getBooks().size() == 13) {
                    if (player1.getBooks().size() > player2.getBooks().size()) {
                        System.out.println("\nCongratulations PLAYER 1, you win!");
                    } else {
                        System.out.println("\nCongratulations PLAYER 2, you win!");
                    }
                    break;
                }
            }
    }


    public void shuffleAndDeal() {
        if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck);  // shuffles the deck

        while (player1.getHand().size() < 7) {
            player1.takeCard(deck.remove(0));    // deal 7 cards to the
            player2.takeCard(deck.remove(0));  // player1 and the player2
        }
    }

    ////////// PRIVATE METHODS /////////////////////////////////////////////////////

    private void initializeDeck() {
        deck = new ArrayList<>(52);

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));     // adds 52 cards to the deck (13 ranks, 4 suits)
            }
        }
    }

    private char takeTurn(boolean cpu) {
        showHand(cpu);
        showBooks(cpu);

        // if requestCard returns null, then the hand was empty and new card was drawn.
        // this restarts the turn, ensuring the updated hand is printed to the console.

        Card card = requestCard(cpu);
        if (card == null) {
            return cpu ? 'C' : 'P';     // restart this turn with updated hand
        }

        // check if your opponent has the card you requested. it will be automatically
        // relinquished if you do. otherwise, draw from the deck. return the character
        // code for whose turn it should be next.

        if (!cpu) {
            if (player2.hasCard(card)) {
                System.out.println("PLAYER 2: Yup, here you go!");
                player2.relinquishCard(player1, card);

                return 'P';
            } else {
                System.out.println("PLAYER 2: Nope, go fish!");
                player1.takeCard(deck.remove(0));

                return 'C';
            }
        } else {
            if (player1.hasCard(card)) {
                System.out.println("PLAYER 1: Yup, here you go!");
                player1.relinquishCard(player2, card);

                return 'C';
            } else {
                System.out.println("PLAYER 1: Nope, go fish!");
                player2.takeCard(deck.remove(0));

                return 'P';
            }
        }
    }

    private Card requestCard(boolean cpu) {
        Card card = null;

        // request a card from your opponent, ensuring that the request is valid.
        // if your hand is empty, we return null to signal the calling method to
        // restart the turn. otherwise, we return the requested card.

        while (card == null) {
            if (!cpu) {
                if (player1.getHand().size() == 0) {
                    player1.takeCard(deck.remove(0));

                    return null;
                } else {
                    System.out.print("PLAYER 1: Got any... ");
                    String rank = in.nextLine().trim().toUpperCase();
                    card = Card.getCardByRank(rank);
                }
            } else {
                if (player2.getHand().size() == 0) {
                    player2.takeCard(deck.remove(0));

                    return null;
                } else {
                    System.out.println("PLAYER 2: Got any... ");
                    String rank = in.nextLine().trim().toUpperCase();
                    card = Card.getCardByRank(rank);
                }
            }
        }

        return card;
    }

    private void showHand(boolean cpu) {
        if (!cpu) {
            System.out.println("\nPLAYER 1 hand: " + player1.getHand());   // only show player's hand
        } else {
            System.out.println("\nPLAYER 2 hand: " + player2.getHand());   // only show player's hand
        }
    }

    private void showBooks(boolean cpu) {
        if (!cpu) {
            System.out.println("PLAYER 1 books: " + player1.getBooks());   // shows the player's books
        } else {
            System.out.println("PLAYER 2 books: " + player2.getBooks());  // shows the computer's books
        }
    }

}