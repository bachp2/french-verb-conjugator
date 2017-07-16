package structure;

import java.text.Collator;
import java.util.Stack;

/**
 * Created by bachp on 1/31/2017.
 */
class Tree {
    private TreeNode root;
    private static final Collator collator = Collator.getInstance();
    static {
        // this'll ignore the accents
        collator.setStrength(Collator.NO_DECOMPOSITION);
    }
    /**
     * empty constructor
     */
    public Tree() {
        root = null;
    }

    /**
     * @param c char
     */
    public void add(Trie.TrieNode c) {
        this.root = insertInSubtree(this.root, c);
    }

    public TreeNode contains(char c) {
        return isInSubtree(this.root, c);
    }
    public Stack<Trie.TrieNode> containsStack(char c){
        Stack<Trie.TrieNode> stack = new Stack <>();
        return stackIsInSubtree(stack, this.root, c);
    }
    /**
     * @param root TreeNode
     * @param node Trie.TrieNode
     * @return
     */
    private TreeNode insertInSubtree(TreeNode root, Trie.TrieNode node) {
        if (root == null) {
            root = new TreeNode(node);
        } else {
            if (node.aChar < root.content.aChar)
                root.left = insertInSubtree(root.left, node);
            else if (node.aChar > root.content.aChar)
                root.right = insertInSubtree(root.right, node);
        }
        return root;
    }
    public boolean isNull(){
        return root == null;
    }
    /**
     * check if a char is in a tree node
     * @param root TreeNode
     * @param c char
     * @return
     */
    private TreeNode isInSubtree(TreeNode root, char c) {
        if (root != null) {
            if (c < root.content.aChar)
                return isInSubtree(root.left, c);
            else if (c > root.content.aChar)
                return isInSubtree(root.right, c);
            else return root;
        }
        return null;
    }

    /**
     *
     * @param stack
     * @param root
     * @param c
     * @return
     */
    private Stack<Trie.TrieNode> stackIsInSubtree(Stack<Trie.TrieNode> stack, TreeNode root, char c){
        if (root.left != null)
            stackIsInSubtree(stack, root.left, c);

        if(collator.compare(Character.toString(c), Character.toString(root.content.aChar)) == 0)
            stack.add(root.content);

        if (root.right != null)
            stackIsInSubtree(stack, root.right, c);
        return stack;
    }

    class TreeNode {
        final Trie.TrieNode content;
        TreeNode left;
        TreeNode right;

        /**
         * constructor
         * @param c char
         */
        public TreeNode(Trie.TrieNode c) {
            content = c;
            left = null;
            right = null;
        }
    }
}