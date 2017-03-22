import java.util.LinkedList;

/**
 * Created by bachp on 1/31/2017.
 */
public class Trie {
    class TrieNode{
        private char content;
        boolean isEnd;
        int count;
        LinkedList<TrieNode> childList;

        public TrieNode(char c){
            content = c;
            isEnd = false;
            childList = new LinkedList <>();
            count = 0;
        }
    }
}
