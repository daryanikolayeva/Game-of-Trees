public interface GameTree {
    /**
     * Adds a word to the tree, updating its frequency if it already exists.
     * Treats all words as case-insensitive.
     * Assume the files are already stripped of punctuation.
     * 
     * @param word The word to add.
     */
    void addWord(String word);

    /**
     * Checks if the tree contains the specified word.
     * 
     * @param word The word to check for.
     * @return true if the word is found in the tree, false otherwise.
     */
    boolean containsWord(String word);

    /**
     * Gets the frequency of a given word in the tree.
     * 
     * @param word The word whose frequency is to be retrieved.
     * @return The frequency of the word, or 0 if the word is not found.
     */
    int getFrequency(String word);

    /**
     * Prints the contents of the tree in lexicographic order.
     */
    void print();

    /**
     * Calculates the height of the tree as the number of edges on the longest
     * branch.
     * 
     * @return The height of the tree.
     */
    int height();

    /**
     * Compares the current tree with another tree, listing unique and common words.
     * If the other tree is not a compatible type, print a message indicating an
     * invalid comparison.
     * 
     * @param otherTree The other tree to compare against.
     */
    void compare(GameTree otherTree);

    /**
     * Prints a visual representation of the tree structure.
     * This can be used to display the tree with indentation to show hierarchy.
     */
    void printTree();

    /*
     * Doubles frequency of a given word by 2
     */
    void doubleFrequency(String word);

    /*
     * Swaps frequencies of two given word
     */
    void swapFrequencies(String word1, String word2);
}
