
public class TwoThreeTree implements GameTree {

    private Node root;

    /*
     * Constructor for 23 Trees
     */
    public TwoThreeTree() {
        root = null;
    }

    private class Node {
        String contents[] = new String[2];
        Node left;
        Node right;
        Node middle;
        int numWords;
        int[] frequency = new int[2];

        /*
         * Constructor for Nodes of tree
         * Input parameter - String word that will be saved in this node
         */
        public Node(String word1) {
            contents[0] = word1;
            numWords = 1;
            frequency[0] = 1;
            left = middle = right = null;
        }
    }

    /*
     * Adds new word to the tree
     * Input parameter:String word
     * If it's empty, makes new word a root of the tree
     * If not runs recursive function to find right place to insert new word
     */
    public void addWord(String word) {
        if (root == null) {
            root = new Node(word.toLowerCase());
        } else {
            insertWord(root, word.toLowerCase());
        }
    }

    /*
     * Recursive function looking for right place to insert new word
     * Check if it's leaf, if so, runs function specificaly made for insertion into
     * leafs
     * If not looks for right place, does splits if need based on capacity of node
     */
    private void insertWord(Node curr, String word) {
        if (curr == null) {
            return;
        }

        if (curr.left == null && curr.right == null && curr.middle == null) {
            insertLeaf(curr, word);
        } else {
            if (curr.numWords == 1) {
                if (word.compareTo(curr.contents[0]) == 0) {
                    curr.frequency[0]++;
                } else if (word.compareTo(curr.contents[0]) < 0) {
                    insertWord(curr.left, word);
                } else {
                    insertWord(curr.right, word);
                }
            } else if (curr.numWords == 2) {
                if (word.compareTo(curr.contents[0]) == 0) {
                    curr.frequency[0]++;
                } else if (word.compareTo(curr.contents[1]) == 0) {
                    curr.frequency[1]++;
                } else {
                    split(curr, word);
                }
            }
        }
    }

    /*
     * Insert new word into leaf nodes
     * Input parameters: Node curr(current node we're working with), String
     * word(word we want to insert)
     */
    private void insertLeaf(Node curr, String word) {
        if (curr.numWords == 1) {
            if (word.compareTo(curr.contents[0]) < 0) {
                curr.contents[1] = curr.contents[0];
                curr.contents[0] = word;
                curr.frequency[1] = curr.frequency[0];
                curr.frequency[0] = 1;
            } else if (word.compareTo(curr.contents[0]) == 0) {
                curr.frequency[0]++;
            } else {
                curr.contents[1] = word;
                curr.frequency[1] = 1;
            }
            curr.numWords = 2;
        } else if (curr.numWords == 2) {
            split(curr, word);
        }
    }

    /*
     * If node is full( has 2 values already), split and rearrange based on
     * lexicographical size
     * Input parameters: Node curr(current node we're working with), String word(
     * word we want to insert)
     */
    private void split(Node curr, String word) {
        String left = curr.contents[0];
        String right = curr.contents[1];
        String middle;

        Node newNode = new Node("");

        if (word.compareTo(left) < 0) {
            middle = left;
            newNode.contents[0] = right;
            newNode.frequency[0] = curr.frequency[1];
        } else if (word.compareTo(right) > 0) {
            middle = right;
            newNode.contents[0] = word;
            newNode.frequency[0] = 1;
        } else {
            middle = word;
            newNode.contents[0] = right;
            newNode.frequency[0] = curr.frequency[1];
        }
        Node middleNode = new Node(middle);
        middleNode.left = curr.left;
        middleNode.right = curr.right;
        curr.left = curr.middle;
        curr.middle = null;

        if (curr == root) {
            Node newRoot = new Node(middle);
            newRoot.left = curr;
            newRoot.right = newNode;
            newRoot.numWords = 1;
            root = newRoot;
        } else {
            insertIntoParent(curr, middle, newNode);
        }
    }

    /*
     * Pushing up middle values into parent/rearranging
     */
    private void insertIntoParent(Node curr, String middleValue, Node newNode) {
        Node parent = findParent(root, curr);
        if (parent != null) {
            if (parent.numWords == 1) {
                if (middleValue.compareTo(parent.contents[0]) == 0) {
                    parent.frequency[0]++;
                } else if (middleValue.compareTo(parent.contents[0]) < 0) {
                    parent.contents[1] = parent.contents[0];
                    parent.contents[0] = middleValue;
                    parent.frequency[1] = parent.frequency[0];
                    parent.frequency[0] = 1;
                    parent.right = newNode;
                } else {
                    parent.contents[1] = middleValue;
                    parent.frequency[1] = 1;
                    parent.right = newNode;
                }
                parent.numWords++;
            } else if (parent.numWords == 2) {
                split(parent, middleValue);
                insertIntoParent(parent, middleValue, newNode);
            }
        }
    }

    /*
     * Finds parent of a given node
     * Input parameters: Node curr(node we're working with), Node child( of which
     * node's parent are we looking for)
     */
    private Node findParent(Node curr, Node child) {
        if (curr == null)
            return null;

        if (curr.left == child || curr.middle == child || curr.right == child) {
            return curr;
        }

        Node parent = null;
        if (curr.left != null) {
            parent = findParent(curr.left, child);
        }
        if (parent == null && curr.middle != null) {
            parent = findParent(curr.middle, child);
        }
        if (parent == null && curr.right != null) {
            parent = findParent(curr.right, child);
        }

        return parent;
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

        if (curr.contents[0] != null && word.compareTo(curr.contents[0]) == 0) {
            return true;
        }
        if (curr.contents[1] != null && word.compareTo(curr.contents[1]) == 0) {
            return true;
        }

        if (curr.numWords == 1 && curr.contents[0] != null) {
            if (word.compareTo(curr.contents[0]) < 0) {
                return findWord(curr.left, word);
            } else if (word.compareTo(curr.contents[0]) > 0) {
                return findWord(curr.right, word);
            }

        } else if (curr.numWords == 2 && curr.contents[0] != null && curr.contents[1] != null) {
            if (word.compareTo(curr.contents[0]) < 0) {
                return findWord(curr.left, word);
            } else if (word.compareTo(curr.contents[0]) > 0 && word.compareTo(curr.contents[1]) < 0) {
                return findWord(curr.middle, word);
            } else {
                return findWord(curr.right, word);
            }
        }
        return false;
    }

    /*
     * Gets frequency of needed word
     * Input Parameter: String word(word whose frequency we want to know)
     */
    public int getFrequency(String word) {
        if (root == null) {
            return 0;
        }
        return findFrequency(root, word.toLowerCase());
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
    private int findFrequency(Node curr, String word) {
        if (curr == null) {
            return 0;
        }

        if (curr.numWords == 1) {
            if (word.compareTo(curr.contents[0]) == 0) {
                return curr.frequency[0];
            } else if (word.compareTo(curr.contents[0]) < 0) {
                return findFrequency(curr.left, word);
            } else {
                return findFrequency(curr.right, word);
            }
        } else if (curr.numWords == 2) {
            if (word.compareTo(curr.contents[0]) == 0) {
                return curr.frequency[0];
            } else if (word.compareTo(curr.contents[0]) < 0) {
                return findFrequency(curr.left, word);
            } else if (word.compareTo(curr.contents[1]) == 0) {
                return curr.frequency[1];
            } else if (word.compareTo(curr.contents[1]) < 0) {
                return findFrequency(curr.middle, word);
            } else {
                return findFrequency(curr.right, word);
            }
        }
        return 0;
    }

    /*
     * Prints trees contents in order from min to max
     * Uses recursive function to start from min and finish at max
     */
    public void print() {
        if (root == null) {
            return;
        }
        System.out.print("[");
        inOrderTraversal(root);
        System.out.println("]");
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
        if (curr.left != null) {
            inOrderTraversal(curr.left);
        }
        if (curr.numWords > 0) {
            for (int i = 0; i < curr.numWords; i++) {
                if (i > 0) {
                    System.out.print(",");
                }
                System.out.print(curr.contents[i] + "(" + curr.frequency[i] + ")");
            }
        }
        if (curr.right != null) {
            inOrderTraversal(curr.right);
        }
    }

    /*
     * Caluclates the height of a tree by longest branch
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

        int leftHeight = calculateHeight(curr.left);
        int middleHeight = calculateHeight(curr.middle);
        int rightHeight = calculateHeight(curr.right);
        int max1 = Math.max(leftHeight, middleHeight);
        int max2 = Math.max(max1, rightHeight);
        return 1 + max2;
    }

    /*
     * Compares current tree with otherTree
     * Prints out words common to both, unique to current and unique to other
     */
    public void compare(GameTree otherTree) {
        if (!(otherTree instanceof TwoThreeTree)) {
            System.out.println("Invalid comparison");
            return;
        }
        TwoThreeTree otherTree23 = (TwoThreeTree) otherTree;
        System.out.println("Common words between trees are:");
        printCommon(root, otherTree23);
        System.out.println("Unique words to main tree are:");
        printUnique(root, otherTree23);
        System.out.println("Unique words to other tree are:");
        printUnique(otherTree23.root, this);
    }

    /*
     * Recursive function
     * Traverse through the current tree, and for every node, check if its contents
     * exist in the other tree. If they do, print the common words.
     */
    private void printCommon(Node curr, TwoThreeTree otherTree) {
        if (curr == null) {
            return;
        }
        printCommon(curr.left, otherTree);
        for (int i = 0; i < curr.numWords; i++) {
            if (otherTree.containsWord(curr.contents[i])) {
                System.out.println(curr.contents[i]);
            }
        }

        printCommon(curr.middle, otherTree);
        printCommon(curr.right, otherTree);
    }

    /*
     * Traverse through the current tree, and for every node, check if its contents
     * exist in the other tree. If they don't, print the unique words.
     */
    private void printUnique(Node curr, TwoThreeTree otherTree) {
        if (curr == null) {
            return;
        }
        printUnique(curr.left, otherTree);
        for (int i = 0; i < curr.numWords; i++) {
            if (!otherTree.containsWord(curr.contents[i])) {
                System.out.println(curr.contents[i]);
            }
        }
        printUnique(curr.middle, otherTree);
        printUnique(curr.right, otherTree);
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
        for (int i = 0; i < curr.numWords; i++) {
            System.out.print("[" + curr.contents[i] + "(" + curr.frequency[i] + ")] ");
        }
        System.out.println();

        printHierarchy(curr.left, depth + 1);
        printHierarchy(curr.middle, depth + 1);
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

        if (curr.numWords == 1) {
            if (word.compareTo(curr.contents[0]) == 0) {
                curr.frequency[0] *= 2;
            } else if (word.compareTo(curr.contents[0]) < 0) {
                doubleRecursive(curr.left, word);
            } else {
                doubleRecursive(curr.right, word);
            }
        } else if (curr.numWords == 2) {
            if (word.compareTo(curr.contents[0]) == 0) {
                curr.frequency[0] = curr.frequency[0] * 2;
            } else if (word.compareTo(curr.contents[1]) == 0) {
                curr.frequency[1] = curr.frequency[1] * 2;
            } else if (word.compareTo(curr.contents[0]) < 0) {
                doubleRecursive(curr.left, word);
            } else if (word.compareTo(curr.contents[1]) < 0) {
                doubleRecursive(curr.middle, word);
            } else {
                doubleRecursive(curr.right, word);
            }
        }
    }

    /*
     * Swap frequencies of 2 given words
     */
    public void swapFrequencies(String word1, String word2) {
        if (root == null) {
            return;
        }
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();
        int freq1 = findFrequency(root, word1);
        int freq2 = findFrequency(root, word2);
        updateFrequency(root, word1, freq2);
        updateFrequency(root, word2, freq1);
    }

    /*
     * Recursively traverse through tree to find both words and then swap their
     * frequencies
     */
    private void updateFrequency(Node curr, String word, int newFrequency) {
        if (curr == null) {
            return;
        }

        for (int i = 0; i < curr.numWords; i++) {
            if (word.equals(curr.contents[i])) {
                curr.frequency[i] = newFrequency;
                return;
            }
        }

        if (curr.numWords == 1) {
            if (word.compareTo(curr.contents[0]) < 0) {
                updateFrequency(curr.left, word, newFrequency);
            } else {
                updateFrequency(curr.right, word, newFrequency);
            }
        } else {
            if (word.compareTo(curr.contents[0]) < 0) {
                updateFrequency(curr.left, word, newFrequency);
            } else if (word.compareTo(curr.contents[1]) < 0) {
                updateFrequency(curr.middle, word, newFrequency);
            } else {
                updateFrequency(curr.right, word, newFrequency);
            }
        }
    }
}
