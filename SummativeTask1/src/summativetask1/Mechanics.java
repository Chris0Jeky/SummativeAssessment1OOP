package summativetask1;

import java.util.ArrayList;
import java.util.Scanner;

public class Mechanics {
    static Player[] playerList = new Player[3];
    static Dice[] diceList = new Dice[8];

    ArrayList<Integer> diceValues = new ArrayList<Integer>();
    static ArrayList<Integer> rolledDiceValuesThisTurn = new ArrayList<Integer>();
    static ArrayList<Integer> diceKeptThisTurn = new ArrayList<Integer>();
    static int[][] scoresPerTurn = new int[3][3];

    public static void initialise() {
        Player.addPlayerToList();
        Dice.addDiceToList();
        runGame();
    }
    static int turnNumber = 0;
    static int playerNumber = 0;
    static int turnScore = 0;
    static int throwCounter = 0;
    static int removedDice = 0;

    public static void runGame() {
        boolean running = true;
        while (running) {
            System.out.println("Turn " + (turnNumber + 1) + " of 3");
            System.out.println("Player " + (playerNumber + 1) + " of 3");

            if (throwCounter > 0) {
                System.out.println("Finish Turn or continue?");
                System.out.println("Enter 'f' to finish turn or 'c' to continue");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (input.equals("f")) {
                    playerList[playerNumber].updateScore(turnNumber, turnScore);
                    playerNumber++;
                    if (playerNumber > 2) {
                        playerNumber = 0;
                        turnNumber++;
                        throwCounter = 0;
                        turnScore = 0;
                        diceKeptThisTurn.clear();
                        rolledDiceValuesThisTurn.clear();
                        if (turnNumber > 2) {
                            running = false;
                        }
                    }
                    else {
                        throwCounter = 0;
                        turnScore = 0;
                        diceKeptThisTurn.clear();
                        rolledDiceValuesThisTurn.clear();
                    }
                }
                else if (input.equals("c")) {
                    continue;
                }
            }
            gameOn();
        }
    }

    public static void gameOn() {
        System.out.println("Press 't' to roll the dice");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("t")) {
            rollDiceList(removedDice);
            throwCounter++;
        }
        System.out.println("Select a die value to set aside: ");
        int dieValue = scanner.nextInt();
        int instances = 0;
        System.out.println(rolledDiceValuesThisTurn.toString());
        for (int i = 0; i < rolledDiceValuesThisTurn.size(); i++) {
            if (rolledDiceValuesThisTurn.get(i) == dieValue) {
                instances++;
            }
        }
        System.out.println("There are " + instances + " " + dieValue + "'s");
        System.out.println("Choose how many dice with this value to keep: ");
        int numberOfInstances = scanner.nextInt();
        if (numberOfInstances > instances) {
            System.out.println("You can't keep more dice than there are of that value");
        } else {
            for (int i = 0; i < numberOfInstances; i++) {
                diceKeptThisTurn.add(dieValue);
            }
            removedDice = numberOfInstances;
            System.out.println("You kept " + numberOfInstances + " " + dieValue + "'s");
            System.out.println("You're keeping so far: " + formatting(diceKeptThisTurn));
            turnScore = turnScore + dieValue * numberOfInstances;
            System.out.println("Score so far: " + turnScore);
        }
    }

    public static void rollDiceList(int removedDice) {
        for (int i = 0; i < diceList.length - removedDice; i++) {
            diceList[i].rollDice();
            Mechanics.rolledDiceValuesThisTurn.add(diceList[i].getDiceValue());
            System.out.println("Dice " + (diceList[i].getDiceId()) + " rolled a [" + diceList[i].getDiceValue() + "]");
        }
        System.out.println("Throw :");
        for (int i = 0; i < Mechanics.rolledDiceValuesThisTurn.size(); i++) {
            System.out.print("[ " + Mechanics.rolledDiceValuesThisTurn.get(i) + " ] ");
        }
        System.out.println();
        System.out.println("Sorted :\n" + sortDice(Mechanics.rolledDiceValuesThisTurn));
    }
    private static String sortDice(ArrayList<Integer> rolledDiceValuesThisTurn) {
        int[] diceValues = new int[8];
        for (int i = 0; i < diceValues.length; i++) {
            diceValues[i] = rolledDiceValuesThisTurn.get(i);
        }
        int temp;
        for (int i = 0; i < diceValues.length; i++) {
            for (int j = i + 1; j < diceValues.length; j++) {
                if (diceValues[i] > diceValues[j]) {
                    temp = diceValues[i];
                    diceValues[i] = diceValues[j];
                    diceValues[j] = temp;
                }
            }
        }
        String sortedDice = "";
        for (int i = 0; i < diceValues.length; i++) {
            sortedDice += "[ " + diceValues[i] + " ] ";
        }
        return sortedDice;
    }

    public void addDiceValue(int diceValue) {
        diceValues.add(diceValue);
    }

    public ArrayList<Integer> storableDiceValues(ArrayList<Integer> diceValues, ArrayList<Integer> rolledDiceThisTurn) {
        ArrayList<Integer> storableDiceValues = new ArrayList<Integer>();
        for (int i = 0; i < diceValues.size(); i++) {
            if (!rolledDiceThisTurn.contains(diceValues.get(i))) {
                storableDiceValues.add(diceValues.get(i));
            }
        }
        return storableDiceValues;
    }
    public static void rollDice(Dice dice1) {
        Dice.rollDice();
        int diceResult = Dice.diceValue;
        System.out.println("Dice " + dice1.diceId +" rolled a " + diceResult);
        rolledDiceValuesThisTurn.add(diceResult);
    }

    public void addToScore(int scoredAmount, int playerId, int turnNumber) {
        scoresPerTurn[playerId - 1][turnNumber] = scoredAmount;
    }

    public static String formatting(ArrayList<Integer> toFormat) {
        String formatted = "";
        for (int i = 0; i < toFormat.size(); i++) {
            formatted += "[ " + toFormat.get(i) + " ] ";
        }
        return formatted;
    }
}
