package summativetask1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// This class is the main class of the application.
public class Mechanics {
    // These are the lists that will be used to store the players and dice.
    static Player[] playerList = new Player[3];
    static Dice[] diceList = new Dice[8];

    ArrayList<Integer> diceValues = new ArrayList<Integer>();
    // This is the method that will be used to initialise the game.
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
        // This is the main game loop.
        boolean running = true;
        while (running) {
            if (throwCounter > 0) {
                // This is the loop that will be used to ask the user if they want to keep any throwing dice.
                System.out.println("Finish Turn or continue?");
                System.out.println("Enter 'f' to finish turn or 'c' to continue");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                while (!input.equals("f") && !input.equals("c")) {
                    System.out.println("Invalid input, please enter 'f' or 'c'");
                    input = scanner.nextLine();
                }
                if (input.equals("f")) {
                    running = finishTurn();
                }
                else if (input.equals("c")) {
                    if (gameOn()) {
                        turnScore = 0;
                        running = finishTurn();
                    }
                }
            }
            else {
                gameOn();
            }
        }
        System.out.println("Game Over");
        int highestScore = 0;
        int winner = 0;
        for (int i = 0; i < 3; i++) {
            if (playerList[i].score[3] > highestScore) {
                highestScore = playerList[i].score[3];
                winner = i;
            }
        }
        System.out.println("The winner is " + playerList[winner].playerName + " with a score of " + highestScore);

    }

    private static boolean finishTurn() {
        playerList[playerNumber].updateScore(turnNumber, turnScore);
        playerNumber++;
        diceKeptThisTurn.clear();
        rolledDiceValuesThisTurn.clear();
        removedDice = 0;
        throwCounter = 0;
        turnScore = 0;
        if (playerNumber > 2) {
            scoreDisplay();
            playerNumber = 0;
            turnNumber++;
            if (turnNumber > 2) {
                return false;
            }
        }
        return true;
    }

    //display scores for each turn and total score
    public static void scoreDisplay() {
        for (int i = 0; i < 3; i++) {
            System.out.println("Turn " + (i + 1) + " scores:");
            for (int j = 0; j < 3; j++) {
                System.out.println(playerList[j].playerName + ": " + playerList[j].score[i]);
            }
            System.out.println();
        }
        System.out.println("Total scores:");
        for (int i = 0; i < 3; i++) {
            System.out.println(playerList[i].playerName + ": " + playerList[i].score[3]);
        }
        System.out.println();
    }

    public static boolean gameOn() {
        boolean busted = false;
        throwCounter++;
        System.out.println("Turn " + (turnNumber + 1) + " of 3");
        System.out.println("Player " + (playerNumber + 1) + " of 3");
        System.out.println("Press 't' to roll the dice");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        //while input is not t, keep asking for input
        while (!input.equals("t")) {
            System.out.println("Press 't' to roll the dice");
            input = scanner.nextLine();
        }
        rollDiceList(removedDice);
        ArrayList<Integer> storableDVs = storableDiceValues(Mechanics.rolledDiceValuesThisTurn, diceKeptThisTurn);
        if (storableDVs.isEmpty()) {
            System.out.println("No dice to store");
            System.out.println("Your score for this turn is 0!");
            System.out.println("BUSTED!!! \n\n");
            busted = true;
        }
        else {
            List<Integer> listWithoutDuplicates = storableDVs.stream().distinct().collect(Collectors.toList());
            System.out.println("Storable Dice Values: \n" + listWithoutDuplicates);
            System.out.println("Select a die value to set aside: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number");
                scanner.next();
            }
            if (scanner.hasNextInt()) {
                int dieValue = scanner.nextInt();
                while (!storableDVs.contains(dieValue)) {
                    System.out.println("Invalid input, please try again");
                    dieValue = scanner.nextInt();
                }

                int instances = 0;
                for (int i = 0; i < rolledDiceValuesThisTurn.size(); i++) {
                    if (rolledDiceValuesThisTurn.get(i) == dieValue) {
                        instances++;
                    }
                }
                System.out.println("There are " + instances + " " + dieValue + "'s");
                System.out.println("Choose how many dice with this value to keep: ");
                int numberOfInstances = scanner.nextInt();
                while (numberOfInstances > instances) {
                    System.out.println("Invalid input, please try again");
                    numberOfInstances = scanner.nextInt();
                }
                for (int i = 0; i < numberOfInstances; i++) {
                    diceKeptThisTurn.add(dieValue);
                }
                removedDice = removedDice + numberOfInstances;
                System.out.println("You kept " + numberOfInstances + " " + dieValue + "'s");
                System.out.println("for a total of: " + (dieValue * numberOfInstances));
                System.out.println("You're keeping so far: " + formatting(diceKeptThisTurn));
                turnScore = turnScore + dieValue * numberOfInstances;
                System.out.println("Score so far: " + turnScore);
                System.out.println("You have " + (8 - removedDice) + " dice left");
            }
        }
        return busted;
    }

    public static void rollDiceList(int removedDice) {
        rolledDiceValuesThisTurn.clear();
        for (int i = 0; i < diceList.length - removedDice; i++) {
            diceList[i].rollDice();
            Mechanics.rolledDiceValuesThisTurn.add(diceList[i].getDiceValue());
        }
        System.out.println("Throw :");
        for (int i = 0; i < Mechanics.rolledDiceValuesThisTurn.size(); i++) {
            System.out.print("[ " + Mechanics.rolledDiceValuesThisTurn.get(i) + " ] ");
        }
        System.out.println();
        System.out.println("Sorted :\n" + formatting(sortDice(Mechanics.rolledDiceValuesThisTurn)));
    }
    private static ArrayList<Integer> sortDice(ArrayList<Integer> rolledDiceValuesThisTurn) {
        int[] diceValues = new int[8];
        for (int i = 0; i < diceValues.length - removedDice; i++) {
            diceValues[i] = rolledDiceValuesThisTurn.get(i);
        }
        int temp;
        for (int i = 0; i < diceValues.length - removedDice; i++) {
            for (int j = i + 1; j < diceValues.length - removedDice; j++) {
                if (diceValues[i] > diceValues[j]) {
                    temp = diceValues[i];
                    diceValues[i] = diceValues[j];
                    diceValues[j] = temp;
                }
            }
        }
        ArrayList<Integer> sortedDice = new ArrayList<Integer>();
        for (int i = 0; i < diceValues.length - removedDice; i++) {
            sortedDice.add(diceValues[i]);
        }
        return sortedDice;
    }



    public void addDiceValue(int diceValue) {
        diceValues.add(diceValue);
    }

    public static ArrayList<Integer> storableDiceValues(ArrayList<Integer> diceValues, ArrayList<Integer> keptDiceThisTurn) {
        ArrayList<Integer> storableDiceValues = new ArrayList<Integer>();
        for (int i = 0; i < diceValues.size(); i++) {
            if (!keptDiceThisTurn.contains(diceValues.get(i))) {
                storableDiceValues.add(diceValues.get(i));
            }
        }
        return storableDiceValues;
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
