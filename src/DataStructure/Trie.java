package DataStructure;

import java.util.ArrayList;
import java.util.List;

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
    private static final List<String> EMPTY_LIST = new ArrayList <>(0);
    /**
     *
     */
    public Trie() {
        root = new TrieNode(' ');
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("subside", 3);
        trie.insert("sa", 3);
        trie.insert("sb", 3);
        trie.insert("sc", 3);
        trie.insert("sub", 3);
        trie.insert("creak", 3);
        trie.insert("larron", 3);
        trie.insert("depuis", 3);
        trie.insert("ascession", 3);
        trie.insert("s", 3);
        trie.insert("su", 3);
        System.out.println(trie.search("subsideride"));
    }

    /**
     * @param verb
     */
    public void insert(String verb, int radIndex) {
        int i = 0;
        TrieNode current = root;
        for (char ch : verb.toCharArray()) {
            TrieNode child = current.subNode(ch);
            if (child != null)
                current = child;
            else {
                current.childList.add(new TrieNode(ch));
                current = current.subNode(ch);
            }
            if(i == radIndex) current.isRadical = true;
            i++;
        }
        current.isEnd = true;
    }

    /**
     * @param word
     * @return
     */
    public List<String> search(String word) {
        TrieNode current = root;
        TrieNode radicalNode = null;
        List<String> listOfAllPossibleSuffixes = null;
        String radical = "";
        StringBuilder temp = new StringBuilder();
        for (char ch : word.toCharArray()) {
            TrieNode trie_node = current.subNode(ch);
            if (trie_node != null) {
                temp.append(ch);
                if (trie_node.isRadical){
                    radical = temp.toString();
                    radicalNode = trie_node;
                }
                current = trie_node;
            }
        }
        if(radicalNode != null) {
            listOfAllPossibleSuffixes = searchAtRadicalPosition(radicalNode);
            for (int i = 0; i < listOfAllPossibleSuffixes.size(); i++){
                temp.setLength(0);
                listOfAllPossibleSuffixes.set(i,
                        temp.append(radical).append(listOfAllPossibleSuffixes.get(i)).toString());
            }
        }
        return listOfAllPossibleSuffixes;
    }
    private List<String> searchAtRadicalPosition(TrieNode node){
        return recursiveSearch(node.childList.root, new StringBuilder(), new ArrayList <String>());
    }
    private List<String> recursiveSearch(Tree.TreeNode node, StringBuilder sb, List<String> l){
        if(node.left != null) recursiveSearch(node.left, sb, l);
        if(node.content != null){
            sb.append(node.content);
            if(node.content.childList.root != null) recursiveSearch(node.content.childList.root, sb, l);
            l.add(sb.toString());
            if(node.content.isEnd) return l;
            sb.setLength(0);
        }
        if(node.right != null) recursiveSearch(node.right, sb, l);
        return EMPTY_LIST;//empty list
    }
    /**
     * this class is a subclass used for DataStructure.Trie
     * use for the arrangement of childList
     */

    public class TrieNode {
        protected char aChar;
        private boolean isEnd;
        private boolean isRadical;
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
            isRadical = false;
            childList = new Tree();
        }
        public Tree getChildList(){
            return childList;
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
