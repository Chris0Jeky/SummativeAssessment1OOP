package summativetask1;

public class Player {
    int playerId;
    String playerName;

    int[] score = {0, 0, 0, 0};

    public void updateScore(int turnNumber, int turnScore) {
        score[turnNumber] = turnScore;
        score[3] = score[0] + score[1] + score[2];
    }

    public Player(int playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public static void addPlayerToList() {
        Mechanics.playerList[0] = new Player(1, "Player 1");
        Mechanics.playerList[1] = new Player(2, "Player 2");
        Mechanics.playerList[2] = new Player(3, "Player 3");
    }
}
