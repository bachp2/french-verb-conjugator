import DataStructure.Mode;
import DataStructure.Tense;
import DataStructure.Trie;
import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Bach Phan
 * @version 01/31/2017
 */
public class Program {
    //private Program instance;
    //todo: implement static build and replace old constructor build method
    private Conjugation conjugate;
    private Deconjugation deconjugate;

    /**
     * empty constructor
     */
    public Program() {
        //will subject to change
        init();
    }

    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Program p = new Program();
        // Deconjugation.similarRadical();
//        String tn = p.conjugate.searchVerb("décapeler").template_name;
//        SuffixesGroup temp = p.conjugate.searchSuffixesGroup(tn);
//        ArrayList <String> tmp = temp.getPrefixes(DataStructure.Mode.indicative, DataStructure.Mode.Tense.past);
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

    /**
     * helper method to initialize Program constructor
     */
    private void init() {
        conjugate = Conjugation.getInstance();
        deconjugate = Deconjugation.getInstance();
    }

}

class Conjugation {
    private static final String path_to_verbs_fr = "./data/verbs-fr.xml";
    private static final String path_to_conjugation_fr = "./data/conjugation-fr.xml";
    protected static ArrayList <Verb> verbsGroup; //collection of the infinitive verbs from verbs-fr.xml
    private static Conjugation INSTANCE = new Conjugation();
    private static ArrayList <SuffixesGroup> suffixesGroups; //collection of suffixes group from conjugation-fr.xml
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

    /**
     * empty constructor, when initiated will be use for the entire operation
     */
    private Conjugation() {
    }

    /**
     * initialization factory
     *
     * @return
     */
    public static Conjugation getInstance() {
        return INSTANCE;
    }

    /**
     * private helper method to sort NodeList of verbs-fr.xml to ArrayList
     *
     * @param nVerbs NodeList
     */
    private static void initVerbs(NodeList nVerbs) {
        int length1 = nVerbs.getLength();
        verbsGroup = new ArrayList <>();
        for (int i = 0; i < length1; i++) {
            Element node = (Element) nVerbs.item(i);
            String verb = node.getElementsByTagName("i").item(0)
                    .getTextContent();
            String template_name = node.getElementsByTagName("t").item(0)
                    .getTextContent();
            Verb temp = new Verb(verb, template_name);
            verbsGroup.add(temp);
        }
        Collections.sort(verbsGroup, (o1, o2) -> o1.infinitive_form.compareTo(o2.infinitive_form));
    }

    /**
     * private helper method to sort NodeList of conjugation-fr.xml to ArrayList
     *
     * @param nConj NodeList
     */
    private static void initConjugation(NodeList nConj) {
        suffixesGroups = new ArrayList <>(1000);
        int length = nConj.getLength();
        for (int i = 0; i < length; i++) {
            Node temp = nConj.item(i);
            Element tmp = (Element) temp;
            String t_n = temp.getAttributes().getNamedItem("name")
                    .getNodeValue();
            SuffixesGroup suffixesGroup = new SuffixesGroup(t_n);
            for (Mode mode : Mode.values()) {
                for (Tense tense : mode.getTenses()) {
                    ArrayList <String> p = new ArrayList <>();
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
                    suffixesGroup.append(mode, tense, p);
                }
                suffixesGroups.add(suffixesGroup);
            }
        }
        Collections.sort(suffixesGroups, (o1, o2) -> o1.getTemplateName().compareTo(o2.getTemplateName()));
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
    //todo : strip accent for input.

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
        int index = Collections.binarySearch(verbsGroup, new Verb(v));
        if (index >= 0)
            return Verb.newInstance(verbsGroup.get(index));
        else return null;
    }

    private static SuffixesGroup searchSuffixesGroup(String template_name) {
        int index = Collections.binarySearch(suffixesGroups, new SuffixesGroup(template_name));
        if (index >= 0)
            return suffixesGroups.get(index);//privacy leak
        else throw new ConjugationException("Can't find matching group with that template name ");
    }

    /**
     * return a boolean value if the input's verb is already conjugated
     *
     * @param s String
     * @return boolean
     */
    public static boolean isNotConjugated(String s) {
        return !Deconjugation.isConjugated(s);
    }


    //FOR TESTING PURPOSES

    /**
     * return a random verb from verbsGroup
     *
     * @return String
     */
    public static String getRandomInfVerb() {
        int s = rand.nextInt(verbsGroup.size());
        return verbsGroup.get(s).getInfinitive_form();
    }

    public static Mode getRandomMode() {
        int index = rand.nextInt(Mode.values().length);
        return Mode.values()[index];//performance insensitive
    }

    public static Tense getRandomTense() {
        int index = rand.nextInt(Tense.values().length);
        return Tense.values()[index];//performance insensitive
    }

    public static
}

class Deconjugation {
    private static final ArrayList <Verb> verbsGroup;
    private static final Trie verb_trie;
    private static Deconjugation INSTANCE = new Deconjugation();

    static {
        verbsGroup = Conjugation.verbsGroup;
        verb_trie = new Trie();
        for (Verb v : verbsGroup) {
            verb_trie.insert(v.radical());
        }
    }

    /**
     * empty constructor
     */
    private Deconjugation() {
    }

    public static Deconjugation getInstance() {
        return INSTANCE;
    }

    /**
     * this method is used to generate code to input verbs with similar radicals for the ease of look up.
     * Refer to SimilarRadDict.
     */
    private static void similarRadical() {
        ListMultimap <String, String> multimap = ArrayListMultimap.create();
        for (Verb v : verbsGroup) {
            multimap.put(v.radical(), v.infinitive_form);
        }
        Map <String, Collection <String>> m = multimap.asMap();
        for (Map.Entry <String, Collection <String>> entry : m.entrySet()) {
            String key = entry.getKey();
            Collection <String> values = entry.getValue();
            if (values.size() > 1) {
                System.out.print("map.put(\"" + key + "\",new String[]{");
                System.out.println(Joiner.on(',').join(stringWrapper(values)) + "});");
            }
            // ...
        }
    }

    /**
     * helper method for similarRadical method
     *
     * @param s
     * @return Collection <String>
     */
    private static Collection <String> stringWrapper(Collection <String> s) {
        Collection <String> temp = new ArrayList <>();
        for (String a : s) {
            String b = "\"" + a + "\"";
            temp.add(b);
        }
        return temp;
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
     * search for part radical of input's verb for deconjugation
     *
     * @param verb
     * @return
     */
    public String searchRadical(String verb) {
        //searchVerb for radical that matchRadical the conjugated verb
        return verb_trie.search(verb);
    }

    /**
     * matchRadical conjugated verb with a probable radical then return verb and template name
     *
     * @param radical String
     * @return String[] -> 2 entries::verb & templateName
     */
    public Verb matchRadical(String radical, String verb) {
        // verb is already conjugated
        String suffix = verb.substring(radical.length());
        //todo return mode and tense
        if (radical.equals("")) radical = "null";
        if (SimilarRadsDict.contains(radical)) {
            for (String s : SimilarRadsDict.getVerbsString(radical)) {
                Verb v = Conjugation.searchVerb(s);
                SuffixesGroup suffixesGroup = Conjugation.searchSuffixesGroup(v.getTemplate_name());
                if (suffixesGroup.containsSuffix(suffix)) return v;
            }
        } else {
            Verb verb2 = Conjugation.searchVerb(verb);
            if (verb2 != null) {
                return verb2;
            }
        }
        return null;
    }
}