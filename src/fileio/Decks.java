package fileio;
import java.util.ArrayList;
import fileio.CardInput;

public class Decks {
    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public ArrayList<ArrayList<Card>> getDecks() {
        return decks;
    }

    public void setNrCardsInDeck(int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public void setNrDecks(int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public void setDecks(ArrayList<ArrayList<Card>> decks) {
        this.decks = decks;
    }

    private int nrCardsInDeck;
    private int nrDecks;
    private ArrayList<ArrayList<Card>> decks;


    public Decks(int nrCardsInDeck, int nrDecks, ArrayList<ArrayList<Card>> decks) {
        this.nrCardsInDeck = nrCardsInDeck;
        this.nrDecks = nrDecks;
        this.decks = decks;
    }
}


