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
                            current.isEnd = true;
                            current.verb = v;
                        }
                    }
                }
                break;
            }
            i++;
        }
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
    public Verb searchVerb(String verb){
        TrieNode current = root;
        for(char ch : verb.toCharArray()){
            TrieNode trieNode = current.subNode(ch);
            if(trieNode != null){
                current = trieNode;
            }
            else return null;
        }
        if(current.isEnd) return current.verb;
        else return null;
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
