
public class BST implements GameTree {
    private Node root;

    private class Node {
        String contents;
        Node left;
        Node right;
        int frequency;

        /*
         * Constructor for Nodes of tree
         * Input parameter - String word that will be saved in this node
         */
        public Node(String contents) {
            this.contents = contents;
            this.frequency = 1;
            left = right = null;
        }
    }

    /*
     * Constructor for bst
     */
    public BST() {
        root = null;
    }

    /*
     * Adds new word to the tree
     * Input parameter:String word
     * If it's empty, makes new word a root of the tree
     * If not runs recursive function to find right place to insert new word
     */
    public void addWord(String word) {
        if (root == null) {
            root = new Node(word);
        } else {
            insertWord(root, word.toLowerCase());
        }
    }

    /*
     * REcursive function for inserting new word
     * Will constatly check given word and word inside current node for
     * lexicographical comaprison
     * When find right spot, insert(create New Node with that word inside)
     * If word is already there, it will increase it's frequency
     */
    private void insertWord(Node curr, String word) {
        if (word.compareToIgnoreCase(curr.contents) < 0) {
            if (curr.left == null) {
                curr.left = new Node(word);
            } else {
                insertWord(curr.left, word);
            }
        } else if (word.compareToIgnoreCase(curr.contents) > 0) {
            if (curr.right == null) {
                curr.right = new Node(word);
            } else {
                insertWord(curr.right, word);
            }
        } else if (word.compareToIgnoreCase(curr.contents) == 0) {
            curr.frequency++;
        }
    }

    /*
     * Checls if given word is in the tree
     * If tree is empty returns false
     * if not runs recursive function to find word
     * Input parameter:String word( word we're looking for)
     */
    public boolean containsWord(String word) {
        if (root == null) {
            return false;
        }
        return findWord(root, word.toLowerCase());
    }

    /*
     * Recursive function looking for word
     * Looks for word based on lexicographical comaprison of words in tree (curr
     * node) and word we're looking for
     */
    private boolean findWord(Node curr, String word) {
        if (curr == null) {
            return false;
        }
        if (word.compareTo(curr.contents) == 0) {
            return true;
        } else if (word.compareTo(curr.contents) < 0) {
            return findWord(curr.left, word);
        } else if (word.compareTo(curr.contents) > 0) {
            return findWord(curr.right, word);
        } else {
            return false;
        }
    }

    /*
     * Gets frequency of word
     * if root==null: Tree doesnt exist, return 0
     * else call recursive method for getting frequency
     */
    public int getFrequency(String word) {
        if (root == null) {
            return 0;
        }
        return getCount(root, word.toLowerCase());
    }

    /*
     * Recursive function that looks for word whose frequency we want to know by
     * constant comparison with curr node
     * When found return frequency
     * If not found returns 0
     * Input parameters: Node curr( nodes we're working with, comparing with word),
     * String word( word whose frequency we're looking for )
     * Output parameter: int frequency
     */
    private int getCount(Node curr, String word) {
        if (curr == null) {
            return 0;
        }
        if (word.compareTo(curr.contents) == 0) {
            return curr.frequency;
        } else if (word.compareTo(curr.contents) < 0) {
            return getCount(curr.left, word);
        } else {
            return getCount(curr.right, word);
        }
    }

    /*
     * Prints trees contents in order from min to max
     * Uses recursive function to start from min and finish at max
     */
    public void print() {
        if (root == null) {
            System.out.println("Tree is empty");
        } else {
            inOrderTraversal(root);
        }
    }

    /*
     * Recursive function for in order printing
     * First goes to furtherst left node which is smallest and then start returning
     * up to root
     * And then to the right till reaches furthest right which is max
     */
    private void inOrderTraversal(Node curr) {
        if (curr == null) {
            return;
        }
        inOrderTraversal(curr.left);
        System.out.printf("[" + curr.contents + "(" + curr.frequency + ")]");
        inOrderTraversal(curr.right);
    }

    /*
     * Caluclates height of tree
     * If root=null, tree is empty
     * Else run recursive function to find longest branch and its height
     */
    public int height() {
        if (root == null) {
            return -1;
        }
        return calculateHeight(root);
    }

    /*
     * Recursively looks for longest branch in the tree
     * Output parameter: int, length of longest branch
     */
    private int calculateHeight(Node curr) {
        if (curr == null) {
            return -1;
        }
        int left = calculateHeight(curr.left);
        int right = calculateHeight(curr.right);
        int height = Math.max(left, right);
        return height + 1;
    }

    /*
     * Compares current tree with otherTree
     * Prints out words common to both, unique to current and unique to other
     */
    public void compare(GameTree otherTree) {
        if (!(otherTree instanceof BST)) {
            System.out.println("Invalid comparison");
            return;
        }
        BST otherTreeBST = (BST) otherTree;
        System.out.println("Common words between trees are:");
        printCommon(root, otherTreeBST);
        System.out.println("Unique words to main tree are:");
        printUnique(root, otherTreeBST);
        System.out.println("Unique words to other tree are:");
        printUnique(otherTreeBST.root, this);
    }

    /*
     * Recursive function
     * Traverse through the current tree, and for every node, check if its contents
     * exist in the other tree. If they do, print the common words.
     */
    private void printCommon(Node curr, GameTree otherTree) {
        if (curr == null) {
            return;
        }
        printCommon(curr.left, otherTree);
        if (otherTree.containsWord(curr.contents)) {
            System.out.println(curr.contents);
        }
        printCommon(curr.right, otherTree);
    }

    /*
     * Traverse through the current tree, and for every node, check if its contents
     * exist in the other tree. If they don't, print the unique words.
     */
    private void printUnique(Node curr, GameTree otherTree) {
        if (curr == null) {
            return;
        }
        printCommon(curr.left, otherTree);
        if (!otherTree.containsWord(curr.contents)) {
            System.out.println(curr.contents);
        }
        printCommon(curr.right, otherTree);
    }

    /*
     * Prints visual hierarchy of a tree
     */
    public void printTree() {
        if (root == null) {
            System.out.println("Tree is empty");
        } else {
            printHierarchy(root, 0);
        }
    }

    /*
     * Recursive function, goes through levels of tree
     * Depeneding on level prints needed amount of spaces/indentation to show
     * hierarchy
     */
    private void printHierarchy(Node curr, int depth) {
        if (curr == null) {
            printIndent(depth);
            System.out.println("[TREE GAP]");
            return;
        }
        printIndent(depth);
        System.out.println("[" + curr.contents + "(" + curr.frequency + ")]");
        printHierarchy(curr.left, depth + 1);
        printHierarchy(curr.right, depth + 1);
    }

    private void printIndent(int depth) {
        for (int i = 0; i < depth * 4; i++) {
            System.out.print(" ");
        }
    }

    /*
     * Doubles frequency of given word
     */
    public void doubleFrequency(String word) {
        doubleRecursive(root, word.toLowerCase());
    }

    /*
     * Recursively traverse through tree till finds a word whose frequency we want
     * to double
     */
    private void doubleRecursive(Node curr, String word) {
        if (curr == null) {
            return;
        }

        if (word.compareTo(curr.contents) == 0) {
            curr.frequency *= 2;
        } else if (word.compareTo(curr.contents) < 0) {
            doubleRecursive(curr.left, word);
        } else {
            doubleRecursive(curr.right, word);
        }
    }

    /*
     * Swap frequencies of 2 given words
     */
    public void swapFrequencies(String word1, String word2) {
        Node node1 = findNode(root, word1.toLowerCase());
        Node node2 = findNode(root, word2.toLowerCase());

        if (node1 != null && node2 != null) {
            int temp = node1.frequency;
            node1.frequency = node2.frequency;
            node2.frequency = temp;
        }
    }

    /*
     * Recursively traverse through tree to find both words and then swap their
     * frequencies
     */
    private Node findNode(Node curr, String word) {
        if (curr == null) {
            return null;
        }

        if (word.compareTo(curr.contents) == 0) {
            return curr;
        } else if (word.compareTo(curr.contents) < 0) {
            return findNode(curr.left, word);
        } else {
            return findNode(curr.right, word);
        }
    }
}
