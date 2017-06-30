package DataStructure;

import java.util.*;

public class Trie {
    private TrieNode root;
    /**
     *
     */
    public Trie() {
        root = new TrieNode(' ');
    }

    /**
     * @param
     */
    public void insert(Verb v, Collection<List<String>> lists, int radIndex) {
        int i = 0;
        TrieNode current = root;

        for (char c : v.getInfinitiveForm().toCharArray()) {
            TrieNode child = current.subNode(c);
            if (child != null)
                current = child;
            else {
                current.childList.add(new TrieNode(c));
                current = current.subNode(c);
            }
            if(i == radIndex){
                current.isRadical = true;
                TrieNode radical = current;
                for (List<String> list : lists) {
                    for (String e : list) {
                        if (!e.equals("null")) {
                            current = radical;
                            for (char ch : e.toCharArray()) {
                                child = current.subNode(ch);
                                if (child != null)
                                    current = child;
                                else {
                                    current.childList.add(new TrieNode(ch));
                                    current = current.subNode(ch);
                                }
                            }
                            current.verb = v;
                        }
                    }
                }
                break;
            }
            i++;
        }
        current.isEnd = true;
        current.verb = v;
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
    public List<Verb> matchesWithInfVerbsInTrie(String word) {
        TrieNode current = root;
        TrieNode radicalNode = null;
        List<Verb> listOfAllPossibleSuffixes = null;
        StringBuilder temp = new StringBuilder();
        for (char ch : word.toCharArray()) {
            TrieNode trie_node = current.subNode(ch);
            if (trie_node != null) {
                temp.append(ch);
                if (trie_node.isRadical){
                    radicalNode = trie_node;
                }
                current = trie_node;
            }
        }
        if(radicalNode != null) {
            listOfAllPossibleSuffixes = searchAtRadicalPosition(radicalNode);
        }
        //for testing
        //System.out.println(listOfAllPossibleSuffixes);
        return listOfAllPossibleSuffixes;
    }
    private List<Verb> searchAtRadicalPosition(TrieNode node){
        List<Verb> list = recursiveSearch(node.childList.root, new ArrayList <Verb>());
        return list.size() == 0 ? Collections.emptyList() : list;
    }
    private List<Verb> recursiveSearch(Tree.TreeNode node, List<Verb> l){
        if(node.left != null) recursiveSearch(node.left, l);
        if(node.content != null){
            if(node.content.isRadical) return l;
            if(node.content.hasBranch()) recursiveSearch(node.content.childList.root, l);
            if(node.content.verb != null) l.add(node.content.verb);
        }
        if(node.right != null) recursiveSearch(node.right, l);
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
        private Verb verb;
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
            verb = null;
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
