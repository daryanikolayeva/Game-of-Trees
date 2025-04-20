

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class DaryaNikolayeva {
    private static Player player1;
    private static Player player2;
    private static Random random = new Random();
    private static Scanner scanner = new Scanner(System.in); // Global scanner for input
    /*
     * Asks for 2 files as input
     * Won't proceed with one or zero
     */

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("2 files needed to proceed");
            return;
        }

        initializePlayers(args[0], args[1]);
        playGame();
    }

    /*
     * Initializing players with given files
     */
    private static void initializePlayers(String file1, String file2) {
        player1 = new Player(initializeTree(file1), "player1");
        player2 = new Player(initializeTree(file2), "player2");
    }

    /*
     * Reads file to see type of tree and upload words from them
     */
    private static GameTree initializeTree(String filename) {
        GameTree tree = null;
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            String treeType = fileScanner.nextLine().toLowerCase();
            if (treeType.compareTo("bst") == 0) {
                tree = new BST();
            } else {
                tree = new TwoThreeTree();
            }

            while (fileScanner.hasNext()) {
                String word = fileScanner.next().toLowerCase();
                tree.addWord(word);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return tree;
    }

    /*
     * Main game
     * Decides randomly on numebr of rounds
     * Decides randomly who is going to start
     * Iteratively runs rounds
     */
    private static void playGame() {
        int rounds = random.nextInt(5 - 2) + 2; // Random rounds between 2 and 5
        boolean first = random.nextBoolean();

        System.out.println("Rounds: " + rounds);
        if (first) {
            System.out.println("Player 1 is first");
        } else {
            System.out.println("Player 2 is first");
        }

        for (int round = 1; round <= rounds; round++) {
            System.out.println("Round number: " + round);
            playRound(first);
            printRoundSummary();
        }

        finish();
        scanner.close();
    }

    /*
     * Depending on who is playing first, plays rounds, changes turns
     */
    private static void playRound(boolean player1First) {
        Player firstPlayer;
        Player secondPlayer;
        if (player1First) {
            firstPlayer = player1;
            secondPlayer = player2;
        } else {
            firstPlayer = player2;
            secondPlayer = player1;
        }

        playTurn(firstPlayer, secondPlayer);
        playTurn(secondPlayer, firstPlayer);
    }

    /*
     * Asking users(players) for input to decide on action, then runs needed action
     */
    private static void playTurn(Player firstPlayer, Player secondPlayer) {
        System.out.println("Player: " + firstPlayer.getName() + "'s turn");
        System.out.println("Your tree looks like this now:");
        firstPlayer.getTree().printTree();

        System.out.print("Choose an action: attack, defend or swap: ");
        String action = scanner.nextLine();

        if (action.compareToIgnoreCase("attack") == 0) {
            attack(firstPlayer, secondPlayer);
        } else if (action.compareToIgnoreCase("defend") == 0) {
            defend(firstPlayer);
        } else if (action.compareToIgnoreCase("swap") == 0) {
            swap(firstPlayer);
        } else {
            System.out.println("Please enter one of the valid options");
        }
    }

    /*
     * If player chose attack
     */
    private static void attack(Player firstPlayer, Player secondPlayer) {
        System.out.print("Select a word from your tree to attack with: ");
        String word = scanner.nextLine().toLowerCase();

        if (!firstPlayer.getTree().containsWord(word) || firstPlayer.isWordUsed(word)) {
            System.out.println("Invalid word. Attack failed.");
            return;
        }

        int firstFrequency = firstPlayer.getTree().getFrequency(word);
        int secondFrequency = secondPlayer.getTree().getFrequency(word);

        if (firstFrequency > secondFrequency) {
            firstPlayer.addScore(firstFrequency);
            System.out.println("Attack successful! Player " + (firstPlayer == player1 ? "1" : "2") + " scores " +
                    firstFrequency + " points.");
        } else {
            System.out.println("Attack failed. No points scored.");
        }
        firstPlayer.useWord(word);
    }

    /*
     * If player chose defend
     */
    private static void defend(Player player) {
        System.out.print("Enter a word to defend: ");
        String word = scanner.nextLine().toLowerCase();

        if (!player.getTree().containsWord(word) || player.isWordUsed(word)) {
            System.out.println("You can't use this word. It's either used already or not in your tree.");
            return;
        }
        player.getTree().doubleFrequency(word);
        System.out.println("Defended. New frequency is: " + player.getTree().getFrequency(word));
    }

    /*
     * If player chose swap
     */
    private static void swap(Player player) {
        System.out.print("Enter first word to swap: ");
        String word1 = scanner.nextLine().toLowerCase();

        System.out.print("Enter second word to swap: ");
        String word2 = scanner.nextLine().toLowerCase();

        if (!player.getTree().containsWord(word1) || !player.getTree().containsWord(word2) ||
                player.isWordUsed(word1) || player.isWordUsed(word2) || word1.equals(word2)) {
            System.out.println("You can't use these words. Words are either used already or not in your tree.");
            return;
        }

        int freq1 = player.getTree().getFrequency(word1);
        int freq2 = player.getTree().getFrequency(word2);
        player.getTree().swapFrequencies(word1, word2);
        System.out.println("Swapped. New Frequency of '" + word1 + "' is now " + freq2 +
                ", and new frequency of '" + word2 + "' is now: " + freq1);
    }

    /*
     * Prints out roundsummary with sores and printing out trees
     */
    private static void printRoundSummary() {
        System.out.println("Scores after this round are:");
        System.out.println(player1.getName() + ": " + player1.getScore());
        System.out.println(player2.getName() + ": " + player2.getScore());

        System.out.println("Player 1's Tree:");
        player1.getTree().printTree();

        System.out.println("Player 2's Tree:");
        player2.getTree().printTree();
    }

    /*
     * Enf of the game
     * Who scored more, won
     */
    private static void finish() {
        System.out.println("Game Over");
        if (player1.getScore() > player2.getScore()) {
            System.out.println(player1.getName() + " wins");
        } else if (player2.getScore() > player1.getScore()) {
            System.out.println(player2.getName() + " wins");
        } else {
            System.out.println("Scores are equal!");
        }
    }
}
