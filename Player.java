

public class Player {
    private String name;
    private int score;
    private GameTree tree;
    private String[] usedWords = new String[100];
    private int count;

    /*
     * Constructor for player
     */
    public Player(GameTree tree, String name) {
        this.name = name;
        this.score = 0;
        this.tree = tree;
        count = 0;

    }

    /*
     * Name getter
     */
    public String getName() {
        return name;
    }

    /*
     * Score getter
     */
    public int getScore() {
        return score;
    }

    /*
     * Adding score
     */
    public void addScore(int points) {
        score += points;
    }

    /*
     * Checking if given word from player's tree is already used
     */
    public boolean isWordUsed(String word) {
        if (usedWords == null) {
            return false;
        }
        for (int i = 0; i < usedWords.length; i++) {
            if (usedWords[i] != null && usedWords[i].compareTo(word.toLowerCase()) == 0) {
                return true;
            }
        }
        return false;
    }

    /*
     * Makrs given word as used
     */
    public void useWord(String word) {
        if (count < usedWords.length) {
            usedWords[count] = word.toLowerCase();
            count++;
        }
    }

    /*
     * Gets tree of player
     */
    public GameTree getTree() {
        return tree;
    }
}