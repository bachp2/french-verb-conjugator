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

public class Trie {
    private TrieNode root;

    /**
     *
     */
    public Trie() {
        root = new TrieNode(' ');
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("subside");
        trie.insert("sa");
        trie.insert("sb");
        trie.insert("sc");
        trie.insert("sub");
        trie.insert("creak");
        trie.insert("larron");
        trie.insert("depuis");
        trie.insert("ascession");
        trie.insert("s");
        trie.insert("su");
        System.out.println(trie.search("subsideride"));
    }

    /**
     * @param rad
     */
    public void insert(String rad) {
        TrieNode current = root;
        for (char ch : rad.toCharArray()) {
            TrieNode child = current.subNode(ch);
            if (child != null)
                current = child;
            else {
                current.childList.add(new TrieNode(ch));
                current = current.subNode(ch);
            }
        }
        current.isEnd = true;
    }

    /**
     * @param word
     * @return
     */
    public String search(String word) {
        TrieNode current = root;
        String temp = "", rad = "";
        for (char ch : word.toCharArray()) {
            TrieNode trie_node = current.subNode(ch);
            if (trie_node != null) {
                temp += ch;
                if (trie_node.isEnd)
                    rad = temp;
                current = trie_node;
            }
        }
        return rad;
    }

    /**
     * this class is a subclass used for DataStructure.Trie
     * use for the arrangement of childList
     */

    public class TrieNode {
        protected char aChar;
        private boolean isEnd;
        private Tree childList;

        /**
         * constructor for TrieNode
         * store sub-nodes in binary tree
         *
         * @param c char
         */
        public TrieNode(char c) {
            aChar = c;
            isEnd = false;
            childList = new Tree();
        }

        /**
         * searchVerb for sub-node in the current trie node
         *
         * @param c char
         * @return TrieNode
         */
        public TrieNode subNode(char c) {
            Tree.TreeNode n;
            if ((n = childList.contains(c)) != null) return n.content;
            return null;
        }
    }
}
