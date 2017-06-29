package DataStructure;

/**
 * Created by bachp on 1/31/2017.
 */
class Tree {
    protected TreeNode root;

    /**
     * empty constructor
     */
    public Tree() {
        root = null;
    }

    /**
     * @param c
     */
    public void add(Trie.TrieNode c) {
        this.root = insertInSubtree(this.root, c);
    }

    public TreeNode contains(char c) {
        return isInSubtree(this.root, c);
    }

    /**
     * @param root
     * @param node
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
     * @param root
     * @param c
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

    class TreeNode {
        protected Trie.TrieNode content;
        protected TreeNode left;
        protected TreeNode right;

        /**
         * @param c
         */
        public TreeNode(Trie.TrieNode c) {
            content = c;
            left = null;
            right = null;
        }
    }
}