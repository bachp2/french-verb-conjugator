import DataStructure.Mode;
import DataStructure.Tense;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Bach Phan
 * @version 01/31/2017
 */
public class Program {
    private static final String path_to_verbs_fr = "./data/verbs-fr.xml";
    private static final String path_to_conjugation_fr = "./data/conjugation-fr.xml";
    //private Program instance;
    //todo: implement static build and replace old constructor build method
    private static Random rand = new Random();

    static {
        try {
            //read verbs-fr.xml file
            File vFile = new File(path_to_verbs_fr);
            //read conjugate-fr.xml file
            File conFile = new File(path_to_conjugation_fr);
            //build NodeLists from sources
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            NodeList nVerbs = dBuilder.parse(vFile).getElementsByTagName("v");
            NodeList nConj = dBuilder.parse(conFile).getElementsByTagName("template");
            //initialize
            initVerbs(nVerbs);
            initConjugation(nConj);
            //clean up resource
            vFile = null;
            conFile = null;
            dBuilder = null;
            nVerbs = null;
            nConj = null;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Program() {
    }

    public static Program create() {
        return new Program();
    }

    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Program p = new Program();
        // Deconjugation.similarRadical();
//        String tn = p.conjugate.searchVerb("décapeler").template_name;
//        SuffixesGroup temp = p.conjugate.searchSuffixesGroup(tn);
//        ArrayList <String> tmp = temp.getSuffixes(DataStructure.Mode.indicative, DataStructure.Mode.Tense.past);
//        StringBuilder sb = new StringBuilder();
//        for (String a : tmp) {
//            sb.append(a + "\n");
//        }
//        System.out.print(sb.toString());
//        ArrayList<String> mylist = SuffixesGroup.append(Verb.radical(tn, "placer"), tmp);
//        StringBuilder sb1 = new StringBuilder();
//        for (String a : mylist) {
//            sb1.append(a + "\n");
//        }
//        System.out.println(sb1);
//        System.out.println(tn);
        //String s1 = "handir";
        //System.out.println(Deconjugation.isConjugated(s1));
        stopwatch.stop();
        System.out.println("Elapsed time in milliseconds ==> " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public static OutputWriter conjugate() {
        return null;
    }

    public static OutputWriter conjugate(String verb, Mode mode, Tense tense) {
        return null;
    }

    public static OutputWriter conjugate(String verb) {
        return null;
    }

    public static String deconjugate(String verb) {
        return null;
    }

    /**
     * check if the verb is already conjugated
     *
     * @param verb
     * @return
     */
    public static boolean isConjugated(String verb) {
        return !(verb.endsWith("er") || verb.endsWith("ir") || verb.endsWith("re"));
    }

    /**
     * return a boolean value if the input's verb is already conjugated
     *
     * @param s String
     * @return boolean
     */
    public static boolean isNotConjugated(String s) {
        return isConjugated(s);
    }

    /**
     * search for part radical of input's verb for deconjugation
     *
     * @param verb
     * @return
     */
    public String searchRadical(String verb) {
        //searchVerb for radical that matchRadical the conjugated verb
        return Verb.trie.search(verb);
    }

    /**
     * matchRadical conjugated verb with a probable radical then return verb and template name
     *
     * @param radical String
     * @return String[] -> 2 entries::verb & templateName
     */
    public Verb matchesRadical(String radical, String verb) {
        // verb is already conjugated
        String suffix = verb.substring(radical.length());
        //todo return mode and tense
        if (radical.equals("")) radical = "null";
        if (SimilarRadsDict.contains(radical)) {
            for (String s : SimilarRadsDict.list(radical)) {
                Verb v = searchVerb(s);
                if (v.containsSuffix(suffix)) return v;
            }
        } else {
            for (Verb v2 : Verb.getVerbsList()) {
                if (v2.matchesRadical(radical)) return v2;
            }
        }
        return null;
    }
    //PRIVATE METHODS

    /**
     * private helper method to sort NodeList of conjugation-fr.xml to ArrayList
     *
     * @param nConj NodeList
     */
    private static void initConjugation(NodeList nConj) {
        int length = nConj.getLength();
        for (int i = 0; i < length; i++) {
            Node temp = nConj.item(i);
            Element tmp = (Element) temp;
            String t_n = temp.getAttributes().getNamedItem("name")
                    .getNodeValue();
            Table <Mode, Tense, List <String>> newTable = HashBasedTable.create();
            for (Mode mode : Mode.values()) {
                for (Tense tense : mode.getTenses()) {
                    List <String> p = new ArrayList <>();
                    Element md = (Element) tmp.getElementsByTagName(mode.toString()).item(0);
                    Element ten = (Element) md.getElementsByTagName(tense.toString(mode)).item(0);
                    NodeList listP = ten.getElementsByTagName("p");
                    NodeList listI;
                    int le = listP.getLength();
                    for (int j = 0; j < le; j++) {
                        boolean isType = (listP.item(j).getNodeType() == Node.ELEMENT_NODE);
                        if (isType) {
                            Element person = (Element) listP.item(j);
                            listI = person
                                    .getElementsByTagName("i");
                            int listILength = listI.getLength();
                            if (listILength > 1)
                                p.add(Joiner.on("/").join(NodeList2Array(listI)));
                            else if (listILength == 1)
                                p.add(listI.item(0).getTextContent());
                            else p.add("null");
                        }
                    }
                    newTable.put(mode, tense, p);
                }
            }
            Verb.setTable(t_n, newTable);
        }
    }

    /**
     * helper method for initConjugation
     * convert NodeList to array
     *
     * @param A NodeList<String>
     * @return
     */
    private static String[] NodeList2Array(NodeList A) {
        int len = A.getLength();
        String[] temp = new String[len];
        for (int i = 0; i < len; i++) {
            temp[i] = A.item(i).getTextContent();
        }
        return temp;
    }
    /**
     * searchVerb for template name with a given verb
     * <p>
     * verb has to be of infinitive form
     * </p>
     *
     * @param v :: verb:String
     * @return String[][]
     */
    private static Verb searchVerb(String v) {
        int index = Collections.binarySearch(Verb.getVerbsList(), Verb.create(v));
        if (index >= 0)
            return Verb.getVerbsList().get(index);
        else return null;
    }
    /**
     * private helper method to sort NodeList of verbs-fr.xml to ArrayList
     *
     * @param nVerbs NodeList
     */

    private static void initVerbs(NodeList nVerbs) {
        int length1 = nVerbs.getLength();
        for (int i = 0; i < length1; i++) {
            Element node = (Element) nVerbs.item(i);
            String verb = node.getElementsByTagName("i").item(0)
                    .getTextContent();
            String template_name = node.getElementsByTagName("t").item(0)
                    .getTextContent();
            Verb.create(verb, template_name);
        }
        Collections.sort(Verb.getVerbsList(), (o1, o2) -> o1.getInfinitiveForm().compareTo(o2.getInfinitiveForm()));
    }

    //FOR TESTING PURPOSES

    /**
     * return a random verb from verbsGroup
     *
     * @return String
     */
    public static Verb getRandomVerb() {
        int index = rand.nextInt(Verb.getVerbsList().size());
        return Verb.getVerbsList().get(index);
    }

    public static Mode getRandomMode() {
        int index = rand.nextInt(Mode.values().length);
        return Mode.values()[index];//performance insensitive
    }

    public static Tense getRandomTense() {
        int index = rand.nextInt(Tense.values().length);
        return Tense.values()[index];//performance insensitive
    }

    public static String getRandomConjugatedVerb() {
        Verb v = getRandomVerb();
        List <String> s = v.getSuffixes(getRandomMode(), getRandomTense());
        return SuffixesGroup.appendString(v.radical(), s.get(rand.nextInt(s.size())));
    }
}