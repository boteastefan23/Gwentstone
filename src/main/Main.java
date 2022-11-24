package main;

import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;

import javax.print.attribute.HashAttributeSet;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        //TODO add here the entry point to your implementation

        /* Inceput pregatire joc */

        // Creez jucatorii
        Player juc1 = new Player();
        Player juc2 = new Player();

        // copiez pachetele cu care se joaca
        ArrayList<Card> CopyDeckPlayerOne = new ArrayList<>();

        //indexul pachetului cu care se joaca
        int index1 = inputData.getGames().get(0).getStartGame().getPlayerOneDeckIdx();

        for (int i = 0; i < inputData.getPlayerOneDecks().getNrCardsInDeck(); i++) {
            if (inputData.getPlayerOneDecks().getDecks().get(index1).get(i).getHealth() == 0) {
                // carti enviorment
                CardInput carte = inputData.getPlayerOneDecks().getDecks().get(index1).get(i);
                Enviorment env = new Enviorment(carte.getMana(), carte.getDescription(), carte.getColors(), carte.getName());
                CopyDeckPlayerOne.add(env);
            } else {
                // carti minioni
                CardInput carte = inputData.getPlayerOneDecks().getDecks().get(index1).get(i);
                Minion minion = new Minion(carte.getMana(), carte.getAttackDamage(), carte.getHealth(), carte.getDescription(), carte.getColors(), carte.getName());
                CopyDeckPlayerOne.add(minion);
            }
        }

        ArrayList<Card> CopyDeckPlayerTwo = new ArrayList<>();
        int index2 = inputData.getGames().get(0).getStartGame().getPlayerTwoDeckIdx();
        for (int i = 0; i < inputData.getPlayerTwoDecks().getNrCardsInDeck(); i++) {
            if (inputData.getPlayerTwoDecks().getDecks().get(index2).get(i).getHealth() == 0) {
                // carti enviorment
                CardInput carte = inputData.getPlayerTwoDecks().getDecks().get(index2).get(i);
                Enviorment env = new Enviorment(carte.getMana(), carte.getDescription(), carte.getColors(), carte.getName());
                CopyDeckPlayerTwo.add(env);
            } else {
                // carti minioni
                CardInput carte = inputData.getPlayerTwoDecks().getDecks().get(index2).get(i);
                Minion minion = new Minion(carte.getMana(), carte.getAttackDamage(), carte.getHealth(), carte.getDescription(), carte.getColors(), carte.getName());
                CopyDeckPlayerTwo.add(minion);
            }
        }

        // amestec pachetele
        Random rand;
        int seed = inputData.getGames().get(0).getStartGame().getShuffleSeed();
        rand = new Random(seed);


        Collections.shuffle(CopyDeckPlayerOne, rand);

        rand = new Random(seed);
        Collections.shuffle(CopyDeckPlayerTwo, rand);

        // creez si setez eroii
        CardInput erou1 = inputData.getGames().get(0).getStartGame().getPlayerOneHero();
        Hero heroOne = new Hero(erou1.getMana(), erou1.getDescription(), erou1.getColors(), erou1.getName());

        CardInput erou2 = inputData.getGames().get(0).getStartGame().getPlayerTwoHero();
        Hero heroTwo = new Hero(erou2.getMana(), erou2.getDescription(), erou2.getColors(), erou2.getName());

        // setez cine incepe

        int start = inputData.getGames().get(0).getStartGame().getStartingPlayer();
        int currentTurn = start;

        // setez pachetele in clasa
        juc1.setCopyDeckPlayer(CopyDeckPlayerOne);
        juc2.setCopyDeckPlayer(CopyDeckPlayerTwo);

        // setez mana
        ArrayList<Card> hand1 = new ArrayList<>();
        ArrayList<Card> hand2 = new ArrayList<>();

        // adaugam prima carte din pachet in mana
        hand1.add(juc1.getCopyDeckPlayer().get(0));
        hand2.add(juc2.getCopyDeckPlayer().get(0));

        juc1.getCopyDeckPlayer().remove(0);
        juc2.getCopyDeckPlayer().remove(0);

        /* Sfarsit pregatire joc */

        // Pregatire tabla de joc

        ArrayList<ArrayList<Minion>> board = new ArrayList<>();

        // imi adaug cele 4 randuri
        board.add(new ArrayList<>());
        board.add(new ArrayList<>());
        board.add(new ArrayList<>());
        board.add(new ArrayList<>());


        // parsare pt comenzi si afisare

        //setez runda si mana de inceput
        int tura = 0;


        for (int i = 0; i < inputData.getGames().get(0).getActions().size(); i++) {
            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("getPlayerDeck")) {
                if (inputData.getGames().get(0).getActions().get(i).getPlayerIdx() == 1) {
                    output.addObject().put("command", "getPlayerDeck").put("playerIdx", 1).putPOJO("output", juc1.getCopyDeckPlayer());
                }

                if (inputData.getGames().get(0).getActions().get(i).getPlayerIdx() == 2) {
                    output.addObject().put("command", "getPlayerDeck").put("playerIdx", 2).putPOJO("output", juc2.getCopyDeckPlayer());
                }
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("getPlayerHero")) {
                if (inputData.getGames().get(0).getActions().get(i).getPlayerIdx() == 1) {
                    output.addObject().put("command", "getPlayerHero").put("playerIdx", 1).putPOJO("output", heroOne);
                }

                if (inputData.getGames().get(0).getActions().get(i).getPlayerIdx() == 2) {
                    output.addObject().put("command", "getPlayerHero").put("playerIdx", 2).putPOJO("output", heroTwo);
                }
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("getPlayerTurn")) {
                output.addObject().put("command", "getPlayerTurn").put("output", currentTurn);
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("endPlayerTurn")) {

                // unfreeze the minions
                if (currentTurn == 1) {
                    for (int j = 0; j < board.get(2).size(); j++) {
                        board.get(2).get(j).setFreezed(0);
                        board.get(2).get(j).setAttacked(0);
                    }

                    for (int j = 0; j < board.get(3).size(); j++) {
                        board.get(3).get(j).setFreezed(0);
                        board.get(3).get(j).setAttacked(0);
                    }
                } else if (currentTurn == 2) {
                    for (int j = 0; j < board.get(1).size(); j++) {
                        board.get(1).get(j).setFreezed(0);
                        board.get(1).get(j).setAttacked(0);
                    }

                    for (int j = 0; j < board.get(0).size(); j++) {
                        board.get(0).get(j).setFreezed(0);
                        board.get(0).get(j).setAttacked(0);
                    }
                }

                if (currentTurn == 1) {
                    currentTurn = 2;
                } else {
                    if (currentTurn == 2) {
                        currentTurn = 1;
                    }
                }


                tura++;

                if (tura % 2 == 0) {
                    // cresc mana jucatorilor
                    juc1.setMana(juc1.getMana() + (tura / 2) + 1);
                    juc2.setMana(juc2.getMana() + (tura / 2) + 1);


                    // draw card


                    if (juc1.getCopyDeckPlayer().size() != 0) {

                        hand1.add(juc1.getCopyDeckPlayer().get(0));

                        juc1.getCopyDeckPlayer().remove(0);

                    }

                    if (juc2.getCopyDeckPlayer().size() != 0) {

                        hand2.add(juc2.getCopyDeckPlayer().get(0));

                        juc2.getCopyDeckPlayer().remove(0);
                    }
                }
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("placeCard")) {

                int cardIdx = inputData.getGames().get(0).getActions().get(i).getHandIdx();
                // we check what player's turn is it
                if (currentTurn == 1) {
                    // we test if the current card is enviorment type
                    if (hand1.get(cardIdx) instanceof Enviorment) {
                        output.addObject().put("command", "placeCard").put("handIdx", cardIdx).put("error", "Cannot place environment card on table.");
                    } else {
                        // we test if the player has enough mana to play the card
                        if (hand1.get(cardIdx).getMana() > juc1.getMana()) {
                            output.addObject().put("command", "placeCard").put("handIdx", cardIdx).put("error", "Not enough mana to place card on table.");
                        } else {


                            // we place cards for player 1 if there is space
                            if ((((Minion) hand1.get(cardIdx)).rowCard() == 1)) {

                                // we test is the row is full
                                if (board.get(2).size() >= 5) {
                                    output.addObject().put("command", "placeCard").put("handIdx", cardIdx).put("error", "Cannot place card on table since row is full.");
                                } else {
                                    // place card on first row
                                    board.get(2).add((Minion) hand1.get(cardIdx));

                                    // decrease mana
                                    juc1.setMana(juc1.getMana() - hand1.get(cardIdx).getMana());

                                    // remove card from hand
                                    hand1.remove(cardIdx);
                                }


                            } else {
                                if ((((Minion) hand1.get(cardIdx)).rowCard() == 0)) {
                                    // we test is the row is full
                                    if (board.get(3).size() >= 5) {
                                        output.addObject().put("command", "placeCard").put("handIdx", cardIdx).put("error", "Cannot place card on table since row is full.");
                                    } else {
                                        // place card on second row
                                        board.get(3).add((Minion) hand1.get(cardIdx));

                                        // decrease mana
                                        juc1.setMana(juc1.getMana() - hand1.get(cardIdx).getMana());

                                        // remove card from hand
                                        hand1.remove(cardIdx);
                                    }
                                }
                            }
                        }


                    }
                }

                if (currentTurn == 2) {
                    // we test if the current card is enviorment type
                    if (hand2.get(cardIdx) instanceof Enviorment) {
                        output.addObject().put("command", "placeCard").put("handIdx", cardIdx).put("error", "Cannot place environment card on table.");
                    } else {
                        // we test if the player has enough mana to play the card
                        if (hand2.get(cardIdx).getMana() > juc2.getMana()) {
                            output.addObject().put("command", "placeCard").put("handIdx", cardIdx).put("error", "Not enough mana to place card on table.");
                        } else {
                            // we place cards for player 2
                            if (((Minion) hand2.get(cardIdx)).rowCard() == 1) {
                                // we test is the row is full
                                if (board.get(1).size() >= 5) {
                                    output.addObject().put("command", "placeCard").put("handIdx", cardIdx).put("error", "Cannot place card on table since row is full.");
                                } else {
                                    // place card on first row
                                    board.get(1).add((Minion) hand2.get(cardIdx));

                                    // decrease mana
                                    juc2.setMana(juc2.getMana() - hand2.get(cardIdx).getMana());

                                    // remove card from hand
                                    hand2.remove(cardIdx);
                                }
                            } else {
                                if (((Minion) hand2.get(cardIdx)).rowCard() == 0) {
                                    // we test is the row is full
                                    if (board.get(0).size() >= 5) {
                                        output.addObject().put("command", "placeCard").put("handIdx", cardIdx).put("error", "Cannot place card on table since row is full.");
                                    } else {
                                        // place card on second row
                                        board.get(0).add((Minion) hand2.get(cardIdx));

                                        // decrease mana
                                        juc2.setMana(juc2.getMana() - hand2.get(cardIdx).getMana());

                                        // remove card from hand
                                        hand2.remove(cardIdx);
                                    }
                                }
                            }
                        }
                    }
                }

            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("getPlayerMana")) {
                if (inputData.getGames().get(0).getActions().get(i).getPlayerIdx() == 1) {
                    // player's one mana
                    output.addObject().put("command", "getPlayerMana").put("playerIdx", 1).put("output", juc1.getMana());
                }
                if (inputData.getGames().get(0).getActions().get(i).getPlayerIdx() == 2) {
                    // player's two mana
                    output.addObject().put("command", "getPlayerMana").put("playerIdx", 2).put("output", juc2.getMana());
                }
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("getCardsOnTable")) {
                // we create an array list where we will add al the cards in the order
                ArrayList<ArrayList<Minion>> afisare = new ArrayList<ArrayList<Minion>>();
                for (int j = 0; j < 4; j++) {
                    afisare.add(new ArrayList<Minion>());
                }
                for (int j = 0; j < 4; j++) {
                    for (int q = 0; q < board.get(j).size(); q++) {
                        afisare.get(j).add(new Minion((Minion) board.get(j).get(q)));
                    }
                }

                output.addObject().put("command", "getCardsOnTable").putPOJO("output", afisare);
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("getCardsInHand")) {
                ArrayList<Card> afisare = new ArrayList<>();

                if (inputData.getGames().get(0).getActions().get(i).getPlayerIdx() == 1) {
                    for (int j = 0; j < hand1.size(); j++) {
                        if (hand1.get(j) instanceof Enviorment)
                            afisare.add(new Enviorment((Enviorment) hand1.get(j)));
                        else
                            afisare.add(new Minion((Minion) hand1.get(j)));
                    }
                    output.addObject().put("command", "getCardsInHand").put("playerIdx", 1).putPOJO("output", afisare);
                }

                if (inputData.getGames().get(0).getActions().get(i).getPlayerIdx() == 2) {
                    for (int j = 0; j < hand2.size(); j++) {
                        if (hand2.get(j) instanceof Enviorment)
                            afisare.add(new Enviorment((Enviorment) hand2.get(j)));
                        else
                            afisare.add(new Minion((Minion) hand2.get(j)));
                    }
                    output.addObject().put("command", "getCardsInHand").put("playerIdx", 2).putPOJO("output", afisare);
                }

            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("useEnvironmentCard")) {
                int cardIndex = inputData.getGames().get(0).getActions().get(i).getHandIdx();
                int affRow = inputData.getGames().get(0).getActions().get(i).getAffectedRow();

                // we check if the card is environment type

                // for player 1
                if (currentTurn == 1) {
                    // we check if we have enough mana
                    if (hand1.get(cardIndex) instanceof Minion) {
                        output.addObject().put("command", "useEnvironmentCard").put("handIdx", cardIndex).put("affectedRow", affRow).put("error", "Chosen card is not of type environment.");
                    } else {

                        if (hand1.get(cardIndex).getMana() > juc1.getMana()) {
                            output.addObject().put("command", "useEnvironmentCard").put("handIdx", cardIndex).put("affectedRow", affRow).put("error", "Not enough mana to use environment card.");
                        } else {
                            // we check if the row belongs to the enemy
                            if (affRow == 2 || affRow == 3) {
                                output.addObject().put("command", "useEnvironmentCard").put("handIdx", cardIndex).put("affectedRow", affRow).put("error", "Chosen row does not belong to the enemy.");
                            } else {
                                int hearthoundException = 0;
                                if (hand1.get(cardIndex).getName().equals("Firestorm")) {
                                    ((Enviorment) hand1.get(cardIndex)).Firestorm(board, affRow);
                                } else if (hand1.get(cardIndex).getName().equals("Winterfell")) {
                                    ((Enviorment) hand1.get(cardIndex)).Winterfell(board, affRow);
                                } else if (hand1.get(cardIndex).getName().equals("Heart Hound")) {
                                    System.out.println("aaaaa");
                                    hearthoundException = 1;
                                    ((Enviorment) hand1.get(cardIndex)).HeartHound(board, affRow, output, cardIndex, juc1, hand1);
                                }

                                if (hearthoundException != 1) {
                                    // we consume the mana
                                    juc1.setMana(juc1.getMana() - hand1.get(cardIndex).getMana());

                                    // we delete the card used from hand
                                    hand1.remove(cardIndex);
                                }
                            }
                        }
                    }
                } else if (currentTurn == 2) {
                    // for player 2
                    // we check if we have enough mana
                    if (hand2.get(cardIndex) instanceof Minion) {
                        output.addObject().put("command", "useEnvironmentCard").put("handIdx", cardIndex).put("affectedRow", affRow).put("error", "Chosen card is not of type environment.");
                    } else {
                        if (hand2.get(cardIndex).getMana() > juc2.getMana()) {
                            output.addObject().put("command", "useEnvironmentCard").put("handIdx", cardIndex).put("affectedRow", affRow).put("error", "Not enough mana to use environment card.");
                        } else {
                            // we check if the row belongs to the enemy
                            if (affRow == 0 || affRow == 1) {
                                output.addObject().put("command", "useEnvironmentCard").put("handIdx", cardIndex).put("affectedRow", affRow).put("error", "Chosen row does not belong to the enemy.");
                            } else {
                                int hearthoundException = 0;
                                if (hand2.get(cardIndex).getName().equals("Firestorm")) {
                                    ((Enviorment) hand2.get(cardIndex)).Firestorm(board, affRow);
                                } else if (hand2.get(cardIndex).getName().equals("Winterfell")) {
                                    ((Enviorment) hand2.get(cardIndex)).Winterfell(board, affRow);
                                } else if (hand2.get(cardIndex).getName().equals("Heart Hound")) {
                                    // we check if there is enough space on the row
                                    hearthoundException = 1;
                                    ((Enviorment) hand2.get(cardIndex)).HeartHound(board, affRow, output, cardIndex, juc2, hand2);
                                }
                                if (hearthoundException != 1) {
                                    // we consume the mana
                                    juc2.setMana(juc2.getMana() - hand2.get(cardIndex).getMana());

                                    // we delete the card used from hand
                                    hand2.remove(cardIndex);
                                }
                            }
                        }
                    }
                }
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("getEnvironmentCardsInHand")) {
                ArrayList<Card> afisare = new ArrayList<>();
                if (inputData.getGames().get(0).getActions().get(i).getPlayerIdx() == 1) {
                    for (int j = 0; j < hand1.size(); j++) {
                        if (hand1.get(j) instanceof Enviorment) {
                            afisare.add(new Enviorment((Enviorment) hand1.get(j)));
                        }
                    }
                    output.addObject().put("command", "getEnvironmentCardsInHand").put("playerIdx", 1).putPOJO("output", afisare);
                }

                if (inputData.getGames().get(0).getActions().get(i).getPlayerIdx() == 2) {
                    for (int j = 0; j < hand2.size(); j++) {
                        if (hand2.get(j) instanceof Enviorment) {
                            afisare.add(new Enviorment((Enviorment) hand2.get(j)));
                        }
                    }
                    output.addObject().put("command", "getEnvironmentCardsInHand").put("playerIdx", 2).putPOJO("output", afisare);
                }
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("getFrozenCardsOnTable")) {
                ArrayList<String> color = new ArrayList<>();
                Minion mini = new Minion(1, 1, 1, "duumy", color, "blank");
                ArrayList<Card> afisare = new ArrayList<>();
                mini.getFrozen(afisare, board, output);
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("getCardAtPosition")) {

                int x = inputData.getGames().get(0).getActions().get(i).getX();
                int y = inputData.getGames().get(0).getActions().get(i).getY();

                if(board.get(x).size() < y)
                {
                    output.addObject().put("command", "getCardAtPosition").put("x", x).put("y", y).put("output", "No card available at that position.");
                }

                if (board.get(x).size() > y) {
                    Minion mini = new Minion(board.get(x).get(y));
                    output.addObject().put("command", "getCardAtPosition").put("x", x).put("y", y).putPOJO("output", mini);
                }
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("cardUsesAttack")) {

                Coordinates coordAttacker = new Coordinates();
                coordAttacker.setX(inputData.getGames().get(0).getActions().get(i).getCardAttacker().getX());
                coordAttacker.setY(inputData.getGames().get(0).getActions().get(i).getCardAttacker().getY());

                Coordinates coordAttacked = new Coordinates();
                coordAttacked.setX(inputData.getGames().get(0).getActions().get(i).getCardAttacked().getX());
                coordAttacked.setY(inputData.getGames().get(0).getActions().get(i).getCardAttacked().getY());

                int erroare = 0;
                // we check if the card belongs to the enemy
                if (currentTurn == 1) {
                    if (coordAttacked.getX() == 2 || coordAttacked.getX() == 3) {
                        erroare = 1;
                        output.addObject().put("command", "cardUsesAttack").putPOJO("cardAttacker", coordAttacker).putPOJO("cardAttacked", coordAttacked).put("error", "Attacked card does not belong to the enemy.");
                    }
                }
                if (currentTurn == 2) {
                    if (coordAttacked.getX() == 0 || coordAttacked.getX() == 1) {
                        erroare = 1;
                        output.addObject().put("command", "cardUsesAttack").putPOJO("cardAttacker", coordAttacker).putPOJO("cardAttacked", coordAttacked).put("error", "Attacked card does not belong to the enemy.");
                    }
                }
                // here is the attack

                // error if the minion already attacks
                if (board.get(coordAttacker.getX()).get(coordAttacker.getY()).getAttacked() == 1 && erroare == 0) {
                    output.addObject().put("command", "cardUsesAttack").putPOJO("cardAttacker", coordAttacker).putPOJO("cardAttacked", coordAttacked).put("error", "Attacker card has already attacked this turn.");
                    erroare = 1;
                }

                // error if freezed
                if(board.get(coordAttacker.getX()).get(coordAttacker.getY()).getFreezed() == 1 && erroare == 0)
                {
                    output.addObject().put("command", "cardUsesAttack").putPOJO("cardAttacker", coordAttacker).putPOJO("cardAttacked", coordAttacked).put("error", "Attacker card is frozen.");
                    erroare = 1;
                }

                // error if tank
                if(currentTurn == 1 && erroare == 0)
                {
                    for(int j = 0; j < board.get(1).size(); j++)
                    {
                        if(board.get(1).get(j).getName().equals("Goliath") || board.get(1).get(j).getName().equals("Warden"))
                        {
                            if(!(board.get(coordAttacked.getX()).get(coordAttacked.getY()).getName().equals("Goliath")) &&
                                    !(board.get(coordAttacked.getX()).get(coordAttacked.getY()).getName().equals("Warden")))
                            {
                                erroare = 1;
                                output.addObject().put("command", "cardUsesAttack").putPOJO("cardAttacker", coordAttacker).putPOJO("cardAttacked", coordAttacked).put("error", "Attacked card is not of type 'Tank'.");
                            }
                        }
                    }
                }

                if(currentTurn == 2 && erroare == 0)
                {
                    for(int j = 0; j < board.get(2).size(); j++)
                    {
                        if(board.get(2).get(j).getName().equals("Goliath") || board.get(2).get(j).getName().equals("Warden"))
                        {
                            if(!(board.get(coordAttacked.getX()).get(coordAttacked.getY()).getName().equals("Goliath"))) {
                                if (!(board.get(coordAttacked.getX()).get(coordAttacked.getY()).getName().equals("Warden"))) {
                                    erroare = 1;
                                    output.addObject().put("command", "cardUsesAttack").putPOJO("cardAttacker", coordAttacker).putPOJO("cardAttacked", coordAttacked).put("error", "Attacked card is not of type 'Tank'.");
                                }
                            }
                        }
                    }
                }


                // we set 'attacked' to true
                if(erroare == 0) {
                    board.get(coordAttacker.getX()).get(coordAttacker.getY()).setAttacked(1);

                    if (board.get(coordAttacked.getX()).get(coordAttacked.getY()).getHealth() <= board.get(coordAttacker.getX()).get(coordAttacker.getY()).getAttackDamage()) {
                        board.get(coordAttacked.getX()).remove(coordAttacked.getY());
                    } else {
                        board.get(coordAttacked.getX()).get(coordAttacked.getY()).setHealth(board.get(coordAttacked.getX()).get(coordAttacked.getY()).getHealth() - board.get(coordAttacker.getX()).get(coordAttacker.getY()).getAttackDamage());
                    }
                }
            }

            if (inputData.getGames().get(0).getActions().get(i).getCommand().equals("useAttackHero"))
            {
                Coordinates coordAttacker = new Coordinates();
                coordAttacker.setX(inputData.getGames().get(0).getActions().get(i).getCardAttacker().getX());
                coordAttacker.setY(inputData.getGames().get(0).getActions().get(i).getCardAttacker().getY());
            }
        }
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new

                File(filePath2), output);
    }
}
