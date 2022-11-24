package fileio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

public class Minion extends Card {
    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    private int attackDamage;
    private int health;

    int freezed;

    @JsonIgnore
    public int getFreezed() {
        return freezed;
    }

    int attacked;

    @JsonIgnore
    public void setAttacked(int attacked) {
        this.attacked = attacked;
    }

    public int getAttacked() {
        return attacked;
    }

    public void setFreezed(int freezed) {
        this.freezed = freezed;
    }

    //copy constructor
    public Minion(Minion copieMinion) {
        this.setMana(copieMinion.getMana());
        this.setAttackDamage(copieMinion.getAttackDamage());
        this.setHealth(copieMinion.getHealth());
        this.setDescription(copieMinion.getDescription());
        this.setColors(copieMinion.getColors());
        this.setName(copieMinion.getName());
        this.freezed = copieMinion.freezed;
        this.attacked = copieMinion.attacked;
    }

    public Minion(int mana, int attackDamage, int health, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);

        this.attackDamage = attackDamage;
        this.health = health;
        this.freezed = 0;
        this.attacked = 0;
    }



    /**
     * @return returns 1 if the card belongs to the front row and 0 if it belongs to the back row
     */
    public int rowCard() {
        if (this.getName().equals("The Ripper") || this.getName().equals("Miraj") ||
                this.getName().equals("Goliath") || this.getName().equals("Warden")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void getFrozen(ArrayList<Card> afisare, ArrayList<ArrayList<Minion>> board, ArrayNode output)
    {
        for (int j = 0; j < 4; j++) {
            for(int k = 0; k < board.get(j).size(); k++)
            {
                if(board.get(j).get(k).freezed == 1)
                {
                    afisare.add((board.get(j).get(k)));
                }
            }
        }

        output.addObject().put("command", "getFrozenCardsOnTable").putPOJO("output", afisare);
    }

    @Override
    public String toString() {
        return "Minion{" +
                "attackDamage=" + attackDamage +
                ", health=" + health +
                '}';
    }
}
