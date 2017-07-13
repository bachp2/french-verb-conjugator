package app;

import structure.Mode;
import structure.Tense;
import structure.Verb;
import test.ProgramTestSuite;
import com.google.common.base.Joiner;
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
import java.util.List;
import java.util.Random;

/**
 * @author Bach Phan
 * @version 01/31/2017
 */
public class Program {
    private static final String PATH_TO_VERBS_FR = "./data/verbs-fr.xml";
    private static final String PATH_TO_CONJUGATION_FR = "./data/conjugation-fr.xml";
    //private app.Program instance;
    protected static Random rand = new Random();

    static {
        try {
            //read verbs-fr.xml file
            File vFile = new File(PATH_TO_VERBS_FR);
            //read conjugateInfinitiveVerb-fr.xml file
            File conFile = new File(PATH_TO_CONJUGATION_FR);
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
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * empty constructor to be extended to a testing suite
     */
    public Program() {
    }

    /**
     * conjugate a random verb with random mode and tense
     * @return OutputWriter
     */

    protected static OutputWriter conjugateInfinitiveVerb() {
        Verb v = ProgramTestSuite.getRandomVerb();
        Mode mode = Mode.INFINITIVE;
        while(mode == Mode.INFINITIVE)
            mode = ProgramTestSuite.getRandomMode();
        Tense tense = ProgramTestSuite.getRandomTenseFromMode(mode);
        return new OutputWriter.Builder(v.getSuffixes(mode,tense))
                .templateName(v.getTemplateName())
                .verb(v.getInfinitiveForm()).infVerb(v.getInfinitiveForm())
                .mode(mode).tense(tense).build();
    }

    /**
     * conjugate a verb with given mode and tense
     * @param verb
     * @param mode
     * @param tense
     * @return
     */
    public static OutputWriter[] conjugateVerb(String verb, Mode mode, Tense tense) {
        Verb[] lv;
        OutputWriter[] owVectors;
        if(!mode.isTenseInMode(tense))
            throw new IllegalArgumentException("the tense is not compatible with the mode input");
        //array of Verb elements that matched in trie
        lv = Verb.searchVerb(verb);
        if(lv.length == 0) return null;
        owVectors = new OutputWriter[lv.length];
        int index = 0;
        for(Verb v : lv){
            OutputWriter ow = new OutputWriter.Builder(v.getSuffixes(mode,tense))
                    .templateName(v.getTemplateName())
                    .verb(verb).infVerb(v.getInfinitiveForm())
                    .mode(mode).tense(tense).build();
            owVectors[index++] = ow;
        }
        return owVectors;
    }

    /**
     * check if the verb is already conjugated
     *
     * @param verb String
     * @return boolean
     */
    private static boolean isConjugated(String verb) {
        return !(verb.endsWith("er") || verb.endsWith("ir") || verb.endsWith("re"));
    }

    /**
     * return a boolean value if the input's verb is already conjugated
     *
     * @param verb String
     * @return boolean
     */
    private static boolean isNotConjugated(String verb) {
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
                    Element md = (Element) tmp.getElementsByTagName(mode.toString().toLowerCase()).item(0);
                    Element ten = (Element) md.getElementsByTagName(tense.toString(mode).toLowerCase()).item(0);
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
        Verb.setTrie();
    }

    /**
     * private helper method to sort NodeList of verbs-fr.xml into table
     * and then put into verbs of the same template names
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
}