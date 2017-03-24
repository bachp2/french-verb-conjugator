import java.util.List;

/**
 * Created by bachp on 3/23/2017.
 */
public class Driver {
    /**
     * testing code
     *
     * @param args
     */
    public static void main(String[] args) {
//        Conjugation conj = new Conjugation();
//        String tn = conj.search("abjurer");
//        String[][] p = conj.listOfPrefix(tn, "indicative", "future");
//        for (int i = 0; i < p.length; i++) {
//            if (p[i] != null) {
//                for (int j = 0; j < p[i].length; j++) {
//                    System.out.println(p[i][j]);
//                }
//            }
//        }
//        String rad = Conjugation.trim(tn, "abjurer");
//        System.out.println(rad);
//        String[][] f = conj.append(rad, p);
//
//        for (int i = 0; i < f.length; i++) {
//            if (f[i] != null) {
//                for (int j = 0; j < f[i].length; j++) {
//                    System.out.println(p[i][j]);
//                }
//            }
//        }
        Conjugation.Deconjugation deconj = new Conjugation().new Deconjugation();
        //System.out.println(deconj.match("places"));
        for(List<String> l : deconj.getList()){
            if(l.get(0).length() == 1)
                System.out.println( l.get(0).concat(l.get(1).substring(l.get(1).indexOf(':')+1)) + " " + l.get(1) );
        }
        System.out.println("===============================");
//        int i = 1;
//        for(List<String> l : deconj.getList()) {
//            if (deconj.match(l.get(0)).spli.equals(l.get(0)))
//                System.out.print(".");
//            else System.out.println(deconj.match(l.get(0))+" "+l.get(0));
//            if (i%30 == 0)
//                System.out.println(i+" "+i/30);
//            i++;
//        }
    }
}
