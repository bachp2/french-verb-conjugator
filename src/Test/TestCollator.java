package Test;

import java.text.Collator;

/**
 * Created by bachp on 7/10/2017.
 */
public class TestCollator {
    public static void main(String[] args){
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.NO_DECOMPOSITION);
        String a = Character.toString('a');
        String b = Character.toString('â');
        String c = Character.toString('d');
        System.out.println(collator.compare(c,a));
    }
}
