package summativetask1;

public class Dice {
    static int diceValue;
    int diceId;

    public Dice(int diceId) {
        this.diceId = diceId;
    }

    public static void rollDice() {
        diceValue = (int) (Math.random() * 6) + 1;
    }

    public static int getDiceValue() {
        return diceValue;
    }

    public static void addDiceToList() {
        Mechanics.diceList[0] = new Dice(1);
        Mechanics.diceList[1] = new Dice(2);
        Mechanics.diceList[2] = new Dice(3);
        Mechanics.diceList[3] = new Dice(4);
        Mechanics.diceList[4] = new Dice(5);
        Mechanics.diceList[5] = new Dice(6);
        Mechanics.diceList[6] = new Dice(7);
        Mechanics.diceList[7] = new Dice(8);
    }

    public int getDiceId() {
        return diceId;
    }
}

