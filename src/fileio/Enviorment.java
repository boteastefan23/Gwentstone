package fileio;

import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

public class Enviorment extends Card {

    public Enviorment(Enviorment copieSpell) {
        this.setColors(copieSpell.getColors());
        this.setName(copieSpell.getName());
        this.setDescription(copieSpell.getDescription());
        this.setMana(copieSpell.getMana());
    }


    public Enviorment(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }

    public void Firestorm(ArrayList<ArrayList<Minion>> board, int row)
    {
        for(int i = 0; i < board.get(row).size(); i++)
        {
            board.get(row).get(i).setHealth(board.get(row).get(i).getHealth() - 1);
        }

        // we check if any minion died
        for(int i = 0; i < board.get(row).size(); i++)
        {
            if(board.get(row).get(i).getHealth() == 0)
            {
                // we delete the minion
                board.get(row).remove(board.get(row).get(i));

                // we decrement i so we don't miss any card
                i--;
            }
        }
    }

    public void Winterfell(ArrayList<ArrayList<Minion>> board, int row)
    {
        for(int i = 0; i < board.get(row).size(); i++)
        {
            board.get(row).get(i).freezed = 1;
        }
    }

    public void HeartHound(ArrayList<ArrayList<Minion>> board, int row, ArrayNode output, int cardIndex, Player juc, ArrayList<Card> hand)
    {
        int error = 0;
        int opositeRow = 0;
        int temp = 0;
        int indexMostHealth = 0;
        for(int i = 0; i < board.get(row).size(); i++)
        {
            if(board.get(row).get(i).getHealth() > temp)
            {
                temp = board.get(row).get(i).getHealth();
                indexMostHealth = i;
            }
        }

        switch (row)
        {
            case 0:
            {
                opositeRow = 3;
                break;
            }
            case 1:
            {
                opositeRow = 2;
                break;
            }
            case 2:
            {
                opositeRow = 1;
                break;
            }
            case 3:
            {
                opositeRow = 0;
                break;
            }
        }

            // we check if we have space on the row
            if(board.get(opositeRow).size() == 5) {

                output.addObject().put("command", "useEnvironmentCard").put("handIdx", cardIndex).put("affectedRow",row).put("error", "Cannot steal enemy card since the player's row is full.");
            } else {
                // adaug cartea pe randul opus
                board.get(opositeRow).add(board.get(row).get(indexMostHealth));

                // scot cartea dupa ce o schimb
                board.get(row).remove(indexMostHealth);

                juc.setMana(juc.getMana() - hand.get(cardIndex).getMana());

                // we delete the card used from hand
                hand.remove(cardIndex);
            }

    }
}
