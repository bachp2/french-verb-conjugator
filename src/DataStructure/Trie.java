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
            TrieNode child;
            if (radIndex != -1) {
                child = current.subNode(c);
                if (child != null)
                    current = child;
                else {
                    current.childList.add(new TrieNode(c));
                    current = current.subNode(c);
                }
            }
            if(i == radIndex || radIndex == -1){
                current.isRadical = true;
                TrieNode radical = current;
                for (List<String> list : lists) {
                    for (String e : list) {
                        //in the form of {asseoir<ass:eoir>}:assiéras/eyeras/oiras>{assoir<ass:oir>}
                        if(e.contains("/"))
                            for(String l : e.split("/")){
                                current = radical;
                                for (char ch : l.toCharArray()) {
                                    child = current.subNode(ch);
                                    if (child != null)
                                        current = child;
                                    else {
                                        current.childList.add(new TrieNode(ch));
                                        current = current.subNode(ch);
                                    }
                                }
                                current.isEnd = true;
                                if(current.verb != null){
                                    current.setOfVerbs = new HashSet <>();
                                    current.setOfVerbs.add(current.verb);
                                    current.setOfVerbs.add(v);
                                }
                                current.verb = v;
                            }
                        // regular verb with only onw conjugation
                        if (!e.equals("null") || e != null) {
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
                            if(current.verb != null){
                                current.setOfVerbs = new HashSet <>();
                                current.setOfVerbs.add(current.verb);
                                current.setOfVerbs.add(v);
                            }
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
     * verb in INFINITIVE form
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

    public Verb[] searchVerb(String verb){
        //first search through for exact match
        TrieNode current = root;
        Verb[] temp = new Verb[1];
        for(char ch : verb.toCharArray()){
            TrieNode trieNode = current.subNode(ch);
            if(trieNode != null){
                current = trieNode;
            }
            else break;
        }
        if(current.isEnd && current.setOfVerbs == null && current.verb != null){
            temp[0] = current.verb;
            return temp;
        }
        else if (current.isEnd && current.setOfVerbs != null)
            return current.setOfVerbs.toArray(new Verb[current.setOfVerbs.size()]);
        else{
            //second search through, ignore accents
            return searchVerbIgnoreAccents(verb);
        }
    }

    private boolean isVowel(char c){
        //with exception of char 'c'
        final char[] vowels = {'a','e','i','u','c','o'};
        for(char a : vowels){
            if(a==c) return true;
        }
        return false;
    }
    private Verb[] searchVerbIgnoreAccents(String verb){
        String stripVerb = verb.replaceAll("\\p{M}", "");
        List<Verb> list = new ArrayList <>();
        searchVerbIgnoreAccentsHelper(this.root, stripVerb, 0 , stripVerb.length(), list);
        return (Verb[]) list.toArray();
    }
    private void searchVerbIgnoreAccentsHelper(TrieNode root, String stripVerb, int count, int length, List<Verb> temp){
        for(char ch : stripVerb.toCharArray()){
            if(isVowel(ch)){
                Stack<TrieNode> charsWithAccent = root.stackSubNode(ch);
                while(!charsWithAccent.empty()){
                    searchVerbIgnoreAccentsHelper(charsWithAccent.pop(),
                            stripVerb.substring(count+1,stripVerb.length()), count+1, length, temp);
                }
            }
            else{
                TrieNode trieNode = root.subNode(ch);
                if(trieNode != null){
                    root = trieNode;
                }
                else break;
                //count for index of char 'ch'
                count++;
            }
        }
        if (count == length) {
            if (root.isEnd && root.setOfVerbs == null) {
                temp.add(root.verb);
            } else if (root.isEnd){
                for (Verb b : root.setOfVerbs) {
                    temp.add(b);
                }
            }
        }
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
        private Set<Verb> setOfVerbs;
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
            setOfVerbs = null;
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
        private Stack<TrieNode> stackSubNode(char c){
            Stack<Trie.TrieNode> trieNodeStack = childList.containsStack(c);
            if(trieNodeStack.empty()) return null;
            return trieNodeStack;
        }
    }
}
