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
import java.util.concurrent.TimeUnit;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
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
        String tn = p.conjugate.searchVerb("placer").template_name;
        SuffixesGroup temp = p.conjugate.searchSuffixesGroup(tn);
        ArrayList <String> tmp = temp.getPrefixes(Mode.indicative, Mode.Tense.past);
        StringBuilder sb = new StringBuilder();
        for (String a : tmp) {
            sb.append(a + "\n");
        }
        System.out.print(sb.toString());
        ArrayList<String> mylist = SuffixesGroup.append(Verb.radical(tn, "placer"), tmp);
        StringBuilder sb1 = new StringBuilder();
        for (String a : mylist) {
            sb1.append(a + "\n");
        }
        System.out.println(sb1);
        System.out.println(tn);
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
    private static Conjugation INSTANCE = new Conjugation();
    private static final String path_to_verbs_fr = "./data/verbs-fr.xml";
    private static final String path_to_conjugation_fr = "./data/conjugation-fr.xml";
    private static ArrayList <SuffixesGroup> suffixesGroups;
    protected static ArrayList <Verb> verbsGroup;
    static{
        try {
            //read verbs-fr.xml file
            File vFile = new File(path_to_verbs_fr);
            //read conjugate-fr.xml file
            File conFile = new File(path_to_conjugation_fr);
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            NodeList nVerbs = dBuilder.parse(vFile).getElementsByTagName("v");
            NodeList nConj = dBuilder.parse(conFile).getElementsByTagName("template");
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
    private Conjugation() {}
    public static Conjugation getInstance(){
        return INSTANCE;
    }
    private static void initVerbs(NodeList nVerbs){
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

    private static void initConjugation(NodeList nConj){
        suffixesGroups = new ArrayList <>(1000);
        int length = nConj.getLength();
        for (int i = 0; i < length; i++) {
            Node temp = nConj.item(i);
            Element tmp = (Element) temp;
            String t_n = temp.getAttributes().getNamedItem("name")
                    .getNodeValue();
            SuffixesGroup frefixesGroup = new SuffixesGroup(t_n);
            for (Mode mode : Mode.values()) {
                for (Mode.Tense tense : mode.getTenses()) {
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
                            if(listILength > 1)
                                p.add(Joiner.on("/").join(NodeList2Array(listI)));
                            else if(listILength == 1)
                                p.add(listI.item(0).getTextContent());
                        }
                    }
                    frefixesGroup.append(mode, tense, p);
                }
                suffixesGroups.add(frefixesGroup);
            }
        }
        Collections.sort(suffixesGroups, (o1, o2) -> o1.template_name.compareTo(o2.template_name));
    }
    private static String[] NodeList2Array(NodeList A){
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
    public Verb searchVerb(String v) {
        int index = Collections.binarySearch(verbsGroup, new Verb(v));
        if (index >= 0)
            return verbsGroup.get(index);//privacy leak
            //todo implement clone
        else throw new ConjugationException("No verb match the string input%nLooking to deconjugate...");
    }

    public SuffixesGroup searchSuffixesGroup(String template_name) {
        int index = Collections.binarySearch(suffixesGroups, new SuffixesGroup(template_name));
        if (index >= 0)
            return suffixesGroups.get(index);//privacy leak
        else throw new ConjugationException("Can't find matching group with that template name ");
    }
}

class Deconjugation {
    private static final ArrayList <Verb> verbsGroup;
    private static final Trie verb_trie;
    private static Deconjugation INSTANCE = new Deconjugation();
    static{
        verbsGroup = Conjugation.verbsGroup;
        verb_trie = new Trie();
        for (Verb v : verbsGroup) {
            verb_trie.insert(v.radical());
        }
    }
    public static Deconjugation getInstance(){
        return INSTANCE;
    }
    /**
     * empty constructor
     */
    private Deconjugation() {}

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
    public ArrayList <Verb> matchRadical(String radical) {
        // verb is already conjugated
        ArrayList <Verb> listOfPossibleInfVerbs = new ArrayList <>();
        for (Verb v : verbsGroup) {
            if (v.isRadical(radical)) {
                //this will be the indexes of possible verbs that can be refer back from verbsGroup
                listOfPossibleInfVerbs.add(v);
            }
        }
        return listOfPossibleInfVerbs;
    }
    public static boolean isMatchWithOneCandidate(ArrayList <Verb> list){
        return list.size() == 1;
    }
    public static boolean isConjugated(String verb){
        //todo regex suffix
        return verb.matches("^.*[ir|er|re]$");
    }
}