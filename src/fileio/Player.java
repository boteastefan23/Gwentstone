package fileio;

import java.util.ArrayList;

public class Player {
    ArrayList<Card> CopyDeckPlayer= new ArrayList<>();

    int mana = 1;

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setCopyDeckPlayer(ArrayList<Card> copyDeckPlayer) {
        CopyDeckPlayer = copyDeckPlayer;
    }

    public ArrayList<Card> getCopyDeckPlayer() {
        return CopyDeckPlayer;
    }
}
