package fileio;

import java.util.ArrayList;

public class Hero extends Card{

    private int health;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Hero(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
        this.health = 30;
    }
}
