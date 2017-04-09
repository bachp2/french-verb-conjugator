import org.w3c.dom.DOMException;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * @author Bach Phan
 * @version 01/31/2017
 */
public class Program {

    protected NodeList nConj;
    protected Trie verb_trie;
    private Conjugation conj;
    private Deconjugation deconj;

    /**
     * empty constructor
     */
    public Program() {
        init();
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        Program p = new Program();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration);
    }

    /**
     * helper method to initialize Program constructor
     */
    private void init() {
        conj = new Conjugation();
        this.nConj = conj.nConj;
        deconj = new Deconjugation(conj.v_tn_rad_Vector);
        this.verb_trie = deconj.verb_trie;
    }

    /**
     * @param verb
     */
    public void conjugate(String verb, Mode mode, Mode.Tense tense) {
        try {
            Verb v = conj.search(verb);
            String[][] listOfPrefixes = conj.listOfPrefixes(v.template_name, mode, tense);
            String[][] conjugatedVerbs = conj.append(v.template_name, listOfPrefixes);
            ConjugatedForm conjugatedForm = new ConjugatedForm(verb, mode, tense, conjugatedVerbs);
            System.out.println(conjugatedForm);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String matching_radical = deconj.search(verb);
        String remaining_prefix = trimPrefix(verb, matching_radical);
        ArrayList <Verb> listOfPossibleVerbs = deconj.match(matching_radical);
        for (Verb v : listOfPossibleVerbs) {
            //todo: trim prefix
            String template_name = v.template_name;
            String inf_verb = v.infinitive_form;
            String[][] listOfPrefixes = conj.listOfPrefixes(template_name, mode, tense);
            if (!isMatchingPrefix(listOfPrefixes, remaining_prefix)) continue;
            String[][] conjugatedVerbs = conj.append(template_name, listOfPrefixes);
            ConjugatedForm conjugatedForm = new ConjugatedForm(inf_verb, mode, tense, conjugatedVerbs);
            System.out.println(conjugatedForm);
        }
    }

    private String trimPrefix(String verb, String radical) {
        return verb.substring(radical.length(), verb.length());
    }

    private boolean isMatchingPrefix(String[][] listOfPrefixes, String prefix) {
        for (String[] e : listOfPrefixes) {
            if (Arrays.asList(e).contains(prefix))
                return true;
        }
        return false;
    }
}

class Conjugation {
    private static final String path_to_verbs_fr = "./data/verbs-fr.xml";
    private static final String path_to_conjugation_fr = "./data/conjugation-fr.xml";
    protected NodeList nConj;  //list of french template name in the form of 'rad:prefix'
    protected ArrayList <Verb> v_tn_rad_Vector;
//    static{
//        //read verbs-fr.xml file
//        File vFile = new File(path_to_verbs_fr);
//        //read conjugation-fr.xml file
//        File conFile = new File(path_to_conjugation_fr);
//
//        try {
//            DocumentBuilder dBuilder = DocumentBuilderFactory
//                    .newInstance().newDocumentBuilder();
//            NodeList nVerbs = dBuilder.parse(vFile).getElementsByTagName("v");
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * empty constructor, when initiated will be use for the entire operation
     */
    public Conjugation() {
        File vFile, conFile;
        NodeList nVerbs;
        DocumentBuilder dBuilder;
        try {
            //read verbs-fr.xml file
            vFile = new File(path_to_verbs_fr);
            //read conjugation-fr.xml file
            conFile = new File(path_to_conjugation_fr);
            dBuilder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();
            nVerbs = dBuilder.parse(vFile).getElementsByTagName("v");
            int len = nVerbs.getLength();
            //todo: implement single circular linkedlist to linked 2 separate list together so that when sort the first
            //todo: collumn the second column won't be disturb
            v_tn_rad_Vector = new ArrayList <>();
            for (int i = 0; i < len; i++) {
                Element node = (Element) nVerbs.item(i);
                String verb = node.getElementsByTagName("i").item(0)
                        .getTextContent();
                String template_name = node.getElementsByTagName("t").item(0)
                        .getTextContent();
                Verb temp = new Verb(verb, template_name);
                v_tn_rad_Vector.add(temp);
            }
            Collections.sort(v_tn_rad_Vector, (o1, o2) -> o1.infinitive_form.compareTo(o2.infinitive_form));

            this.nConj = dBuilder.parse(conFile).getElementsByTagName("template");
            ArrayList <PrefixesGroup> frefixes_Vector = new ArrayList <>(1000);
            len = nConj.getLength();
            for (int i = 0; i < len; i++) {
                Node temp = nConj.item(i);
                Element tmp = (Element) temp;
                String t_n = temp.getAttributes().getNamedItem("name")
                        .getNodeValue();
                ArrayList <String> p = new ArrayList <>();
                PrefixesGroup frefixesGroup = new PrefixesGroup(t_n);
                for (Mode mode : Mode.values()) {
                    for (Mode.Tense tense : mode.getTenses()) {
                        Element md = (Element) tmp.getElementsByTagName(mode.toString()).item(0);
                        Element ten = (Element) md.getElementsByTagName(tense.toString(mode)).item(0);
                        NodeList listP = (NodeList) ten.getElementsByTagName("p");
                        NodeList listI = null;
                        int le = listP.getLength();
                        for (int j = 0; j < le; j++) {
                            boolean isType = (listP.item(j).getNodeType() == Node.ELEMENT_NODE);
                            if (isType) {
                                Element person = (Element) listP.item(j);
                                listI = person
                                        .getElementsByTagName("i");
                                int length = listI.getLength();
                                StringBuilder in = new StringBuilder();
                                for (int k = 0; k < length; k++) {
                                    if (k > 0) in.append("/");
                                    in.append(listI.item(k).getTextContent());
                                }
                                p.add(in.toString());
                            }
                        }
                        frefixesGroup.append(mode, tense, p);
                    }
                    frefixes_Vector.add(frefixesGroup);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //clean up resource
            vFile = null;
            conFile = null;
            dBuilder = null;
            nVerbs = null;
            nConj = null;
        }
    }

    /**
     * append radical with list of prefixes when conjugated
     *
     * @param radical::String
     * @param listOfPrefixes::String[][]
     * @return String[][]
     */
    public static String[][] append(String radical, String[][] listOfPrefixes) {
        // already trim
        for (int i = 0; i < listOfPrefixes.length; i++) {
            if (listOfPrefixes[i] == null)
                continue;
            for (int j = 0; j < listOfPrefixes[i].length; j++) {
                listOfPrefixes[i][j] = radical + listOfPrefixes[i][j];
            }
        }
        return listOfPrefixes;
    }

    /**
     * get list of prefixes
     *
     * @param templateName String
     * @param mode         String
     * @param tense        String
     * @return
     */
    public String[][] listOfPrefixes(String templateName,
                                     Mode mode,
                                     Mode.Tense tense) {
        Node temp;
        Element tmp, md, ten, person;
        NodeList listP, listI;
        String other;
        try {
            String[][] p = new String[6][];
            int len = nConj.getLength();
            for (int i = 0; i < len; i++) {
                temp = nConj.item(i);
                tmp = (Element) temp;
                other = temp.getAttributes().getNamedItem("name")
                        .getNodeValue();
                if (templateName.equals(other)) {
                    md = (Element) tmp.getElementsByTagName(mode.toString()).item(0);
                    ten = (Element) md.getElementsByTagName(tense.toString()).item(0);
                    listP = (NodeList) ten.getElementsByTagName("p");
                    listI = null;
                    int le = listP.getLength();
                    for (int j = 0; j < le; j++) {
                        boolean isType = (listP.item(j).getNodeType() == Node.ELEMENT_NODE);
                        if (isType) {
                            person = (Element) listP.item(j);
                            if (person.hasChildNodes()) {
                                listI = person
                                        .getElementsByTagName("i");
                            }
                            p[j] = new String[listI.getLength()];
                            int l = listI.getLength();
                            for (int k = 0; k < l; k++) {
                                p[j][k] = listI.item(k).getTextContent();
                            }
                        }
                    }
                    return p;
                }
            }
            throw new NoSuchElementException();
        } catch (DOMException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        } finally {
            temp = null;
            tmp = null;
            md = null;
            ten = null;
            person = null;
            listP = null;
            listI = null;
            other = null;
        }
    }
    //todo : strip accent for input.

    /**
     * search for template name with a given verb
     * <p>
     * verb has to be of infinitive form
     * </p>
     *
     * @param v :: verb:String
     * @return String[][]
     */
    public Verb search(String v) {
        int index = Collections.binarySearch(v_tn_rad_Vector, new Verb(v));
        if (index >= 0)
            return v_tn_rad_Vector.get(index);
        throw new ConjugationException("No verbs match the input%nLooking to deconjugate...");
    }
}

class Deconjugation {
    private final ArrayList <Verb> v_tn_rad_Vector;
    protected Trie verb_trie;

    /**
     * empty constructor
     */
    public Deconjugation(ArrayList <Verb> v_tn_rad_Vector) {
        this.v_tn_rad_Vector = v_tn_rad_Vector;
        verb_trie = new Trie();
        for (Verb v : v_tn_rad_Vector) {
            verb_trie.insert(v.radical);
        }
    }

    public String search(String verb) {
        //search for radical that match the conjugated verb
        return verb_trie.search(verb);
    }

    /**
     * match conjugated verb with a probable radical then return verb and template name
     *
     * @param radical String
     * @return String[] -> 2 entries::verb & templateName
     */
    public ArrayList <Verb> match(String radical) {
        // verb is already conjugated
        //todo: implement search base on radical or search based on prefixe compare the performance of both cases
        ArrayList <Verb> listOfPossibleInfVerbs = new ArrayList <>();
        for (Verb v : v_tn_rad_Vector) {
            if (v.radical.equals(radical)) {
                //this will be the indexes of possible verbs that can be refer back from v_tn_rad_Vector
                listOfPossibleInfVerbs.add(v);
            }
        }
        return listOfPossibleInfVerbs;
    }
}