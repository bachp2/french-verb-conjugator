package DataStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        System.out.println(trie.matchesWithInfVerbsInTrie("subsideride"));
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
     * verb in infinitive form
     * @param word
     * @return
     */
    public boolean isVerbInTrie(String word){
        TrieNode current = root;
        String infVerb = "";
        StringBuilder temp = new StringBuilder();
        for(char ch : word.toCharArray()){
            TrieNode trie_node = current.subNode(ch);
            if(trie_node != null){
                temp.append(ch);
                if(trie_node.isEnd){
                    infVerb = temp.toString();
                }
                current = trie_node;
            }
        }
        return infVerb.equals(word);
    }
    public boolean isRadInTrie(String radical){
        TrieNode current = root;
        String rad = "";
        StringBuilder temp = new StringBuilder();
        for(char ch : radical.toCharArray()){
            TrieNode trie_node = current.subNode(ch);
            if(trie_node != null){
                temp.append(ch);
                if(trie_node.isRadical){
                    rad = temp.toString();
                }
                current = trie_node;
            }
        }
        return rad.equals(radical);
    }
    /**
     * in the case of verb is conjugated
     * @param word
     * @return
     */
    public List<String> matchesWithInfVerbsInTrie(String word) {
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
        List<String> list = recursiveSearch(node.childList.root, new StringBuilder(), new ArrayList <String>());
        return list.size() == 0 ? Collections.emptyList() : list;
    }
    private List<String> recursiveSearch(Tree.TreeNode node, StringBuilder sb, List<String> l){
        if(node.left != null) recursiveSearch(node.left, sb, l);
        if(node.content != null){
            sb.append(node.content.aChar);
            if(node.content.hasBranch()) recursiveSearch(node.content.childList.root, sb, l);
            if(sb.length() != 0) l.add(sb.toString());
            sb.setLength(0);
        }
        if(node.right != null) recursiveSearch(node.right, sb, l);
        return l;
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
        public boolean hasBranch(){
            return !childList.isNull();
        }
        /**
         * searchVerbList for sub-node in the current trie node
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
