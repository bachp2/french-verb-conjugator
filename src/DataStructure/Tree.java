package DataStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by bachp on 1/31/2017.
 */
class Tree {
    protected TreeNode root;
    private static Collator collator = Collator.getInstance();
    static {
        // This strategy mean it'll ignore the accents
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
    public Stack<TreeNode> containsStack(char c){
        Stack<TreeNode> stack = new Stack <>();
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
    private Stack<TreeNode> stackIsInSubtree(Stack<TreeNode> stack, TreeNode root, char c){
        if (root != null) {
            if (collator.compare(c,root.content.aChar) < 0)
                return stackIsInSubtree(stack, root.left, c);
            else if (collator.compare(c,root.content.aChar) > 0)
                return stackIsInSubtree(stack, root.right, c);
            else{
                stack.add(root);
                return stack;
            }
        }
        return null;
    }
    class TreeNode {
        protected Trie.TrieNode content;
        protected TreeNode left;
        protected TreeNode right;

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