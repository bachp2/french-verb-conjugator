package Main;

import DataStructure.Mode;
import DataStructure.Tense;
import DataStructure.Verb;
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
import java.io.*;
import java.util.ArrayList;
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
    //private Main.Program instance;
    //todo: implement static build and replace old constructor build method
    private static Random rand = new Random();

    static {
        try {
            //read verbs-fr.xml file
            File vFile = new File(path_to_verbs_fr);
            //read conjugateInfinitiveVerb-fr.xml file
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
     *
     */
    protected Program() {
    }

    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Program p = new Program();
        stopwatch.stop();
        System.out.println("Elapsed time in milliseconds ==> " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
    public static OutputWriter conjugate(String verb, Mode mode, Tense tense){
        if(!mode.isTenseInMode(tense))
            throw new IllegalArgumentException("the tense is not compatible with the mode input");
        if(isNotConjugated(verb)){
            return conjugateInfinitiveVerb(verb, mode, tense);
        }
        Verb v = deconjugate(verb);
        if(v != null)
            return new OutputWriter(v.getInfinitiveForm(), mode, tense, v.getSuffixes(mode, tense));
        return null;
    }

    /**
     *
     * @return
     */

    public static OutputWriter conjugateInfinitiveVerb() {
        Verb v = getRandomVerb();
        Mode m = getRandomMode();
        Tense t = getRandomTenseFromMode(m);
        return new OutputWriter(v.getInfinitiveForm(), m, t, v.getSuffixes(m,t));
    }

    /**
     *
     * @param verb
     * @param mode
     * @param tense
     * @return
     */
    private static OutputWriter conjugateInfinitiveVerb(String verb, Mode mode, Tense tense) {
        if(!mode.isTenseInMode(tense)) throw new IllegalArgumentException("the tense is not compatible with the mode input");
        Verb v = Verb.searchVerbList(verb);
        return new OutputWriter(v.getInfinitiveForm(), mode, tense, v.getSuffixes(mode,tense));
    }
    //todo check the validity of tense that belongs to a specific mode
    /**
     *
     * @param verb
     * @return
     */
    private static OutputWriter conjugateInfinitiveVerb(String verb) {
        Verb v = Verb.searchVerbList(verb);
        Mode m = getRandomMode();
        Tense t = getRandomTenseFromMode(m);
        return new OutputWriter(v.getInfinitiveForm(), m, t, v.getSuffixes(m,t));
    }

    /**
     *
     * @param verb
     * @return
     */
    public static Verb deconjugate(String verb) {
        for(Verb infVerb : Verb.matchesWithVerbs(verb)){
            String suffix = Verb.suffix(infVerb.getInfinitiveForm(), infVerb.getTemplateName());
            if(infVerb.containsConjugatedSuffix(suffix))
                return infVerb;
        }
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
     * @param verb String
     * @return boolean
     */
    public static boolean isNotConjugated(String verb) {
        return !isConjugated(verb);
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
        Verb.sortList();
        Verb.setTrie();
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

    //FOR TESTING PURPOSES

    /**
     * return a random verb from verbsGroup
     *
     * @return String
     */
    public static Verb getRandomVerb() {
        int index = rand.nextInt(Verb.getListSize());
        return Verb.getListElement(index);
    }

    /**
     *
     * @return
     */
    public static String getRandomVerbString(){
        int index = rand.nextInt(Verb.getListSize());
        return Verb.getListElement(index).getInfinitiveForm();
    }

    /**
     *
     * @return
     */
    public static Mode getRandomMode() {
        int index = rand.nextInt(Mode.values().length);
        return Mode.values()[index];//performance insensitive
    }

    /**
     *
     * @return
     */
    public static Tense getRandomTenseFromMode(Mode mode) {
        int index = rand.nextInt(mode.getTenses().length);
        return mode.getTenses()[index];//performance insensitive
    }

    /**
     *
     * @return
     */
    public static String getRandomConjugatedVerb() {
        Verb v = getRandomVerb();
        Mode m = getRandomMode();
        List <String> s = v.getSuffixes(m, getRandomTenseFromMode(m));
        return Verb.appendString(v.radical(), s.get(rand.nextInt(s.size())));
    }
}