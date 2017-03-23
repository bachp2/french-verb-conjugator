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
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Bach Phan
 * @version 01/31/2017
 */
public class Conjugation {
    private final String path_to_verbs_fr = "./data/verbs-fr.xml";
    private final String path_to_conjugation_fr = "./data/conjugation-fr.xml";
    private String[][] _listConjugation; //conjugated version of verb for each pronoun
    private NodeList nVerbs; //list of french infinitive verbs
    private NodeList nConj;  //list of french template name in the form of 'rad:prefix'

    private List <List <String>> rads_vs = new ArrayList <>();
    //list of verbs, radicals and template names

    /**
     * empty constructor, when initiated will be use for the entire operation
     */
    public Conjugation() {
        File vFile, conFile;
        DocumentBuilder dBuilder;
        _listConjugation = null;
        try {
            //read verbs-fr.xml file
            vFile = new File(path_to_verbs_fr);
            //read conjugation-fr.xml file
            conFile = new File(path_to_conjugation_fr);
            dBuilder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();
            this.nVerbs = dBuilder.parse(vFile).getElementsByTagName("v");
            this.nConj = dBuilder.parse(conFile).getElementsByTagName("template");
            //clean up resource
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            vFile = null;
            conFile = null;
            dBuilder = null;
        }
    }

    /**
     * @param temp
     * @param v
     * @return
     */
    public static String trim(String temp, String v) {
        int index = 0;
        for (int i = 0; i < temp.length(); i++) {
            char c = temp.charAt(i);
            if (c == ':') {
                index = temp.length() - 1 - i;
            }
        }
        return v.substring(0, v.length() - index);
    }

    /**
     * @param radical
     * @param listP
     * @return
     */
    public static String[][] append(String radical, String[][] listP) {
        // already trim
        for (int i = 0; i < listP.length; i++) {
            if (listP[i] == null)
                break;
            for (int j = 0; j < listP[i].length; j++) {
                listP[i][j] = radical + listP[i][j];
            }
        }
        return listP;
    }

    /**
     * search for
     *
     * @param v
     * @return
     */
    public String search(String v) {
        String templateName = "";
        for (int i = 0; i < nVerbs.getLength(); i++) {
            Node verb = nVerbs.item(i);
            if (verb.getNodeType() == Node.ELEMENT_NODE) {
                Element eVerb = (Element) verb;
                if (v.equals(eVerb.getElementsByTagName("i").item(0)
                        .getTextContent())) {
                    templateName = eVerb.getElementsByTagName("t").item(0)
                            .getTextContent();
                    break;
                }
            }
        }
        return templateName;
    }

    /**
     * @param templateName
     * @param mode
     * @param tense
     * @return
     */
    public String[][] listOfPrefix(String templateName,
                                   String mode,
                                   String tense) {
        Node temp;
        Element tmp, md, ten, person;
        NodeList listP, listI;
        String other;
        try {
            String[][] p = new String[6][];
            for (int i = 0; i < nConj.getLength(); i++) {
                temp = nConj.item(i);
                tmp = (Element) temp;
                other = temp.getAttributes().getNamedItem("name")
                        .getNodeValue();
                if (templateName.equals(other)) {
                    md = (Element) tmp.getElementsByTagName(mode).item(0);
                    ten = (Element) md.getElementsByTagName(tense).item(0);
                    listP = (NodeList) ten.getElementsByTagName("p");
                    listI = null;
                    for (int j = 0; j < listP.getLength(); j++) {
                        boolean isType = (listP.item(j).getNodeType() == Node.ELEMENT_NODE);
                        if (isType) {
                            person = (Element) listP.item(j);
                            if (person.hasChildNodes()) {
                                listI = person
                                        .getElementsByTagName("i");
                            }
                            p[j] = new String[listI.getLength()];
                            for (int k = 0; k < listI.getLength(); k++) {
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

    /**
     *
     */
    public String toString() {
        if(_listConjugation == null) return "";
        String out = "";
        String[] pronouns = {"je", "tu", "il", "nous", "vous", "ils"};
        for (int i = 0; i < _listConjugation.length; i++) {
            if (_listConjugation[i] != null) {
                out = pronouns[i] + " ";
                for (int j = 0; j < _listConjugation[i].length; j++) {
                    out += _listConjugation[i][j];
                    if (j < _listConjugation[i].length - 1
                            && _listConjugation[i].length > 1)
                        out += "/";
                }
                out += "%n";
            } else
                out += "%n";
        }
        return out;
    }
    //todo : strip accent for input.

    /**
     * @return
     */
    public List <List <String>> setListRad_and_Verbs() {
        List <String> list = new ArrayList <>();
        for (int i = 0; i < nVerbs.getLength(); i++) {
            Node verb = nVerbs.item(i);
            if (verb.getNodeType() == Node.ELEMENT_NODE) {
                Element eVerb = (Element) verb;
                String v = eVerb.getElementsByTagName("i").item(0)
                        .getTextContent();
                String tempN = eVerb.getElementsByTagName("t").item(0)
                        .getTextContent();
                String rad = trim(tempN, v);
                list.add(0, v);
                list.add(1, rad);
                list.add(2, tempN);
                rads_vs.add(i, list);
            }
        }
        return this.rads_vs;
    }

    //get NodeList of verbs-fr and conjugation-fr

    enum Mode {
        infinitive("infinitive-present"),
        indicative("present", "imperfect", "future", "simple-past"),
        conditional("present"),
        subjunctive("present", "imperfect"),
        imperative("imperative-present"),
        participle("present-participle", "past-participle");
        private String[] tenses;

        Mode(String... tenses) {
            this.tenses = tenses;
        }

        /**
         * get index of a given mode
         *
         * @param mode
         * @return
         */
        public static int indexOf(Object mode) {
            if (mode instanceof Mode) {
                int i = 0;
                for (Mode m : Mode.values()) {
                    if (m == mode) return i;
                    i++;
                }
            }
            return -1;
        }

        public static Mode getMode(String mode) {
            try {
                for (Mode m : Mode.values()) {
                    if (mode.equals(m.toString()))
                        return m;
                }
                throw new NoSuchElementException();
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                return null;
            }
        }

        public String getTense(int index) {
            if(index < this.length() && index >= 0){
                return tenses[index];
            }
            else throw new NoSuchElementException();
        }

        public int length() {
            return tenses.length;
        }
    }

    public class Deconjugation {
        private Trie verb_trie;

        public Deconjugation() {
            verb_trie = new Trie();
            for (List <String> rad_v : rads_vs) {
                verb_trie.insert(rad_v.get(1));
            }
        }

        public String[] match(String verb) {
            String[] verb_templateName = new String[2];
            String temp = verb_trie.search(verb);
            for (List <String> rad_v : rads_vs) {
                if (rad_v.contains(temp)) {
                    verb_templateName[0] = rad_v.get(0);
                    verb_templateName[1] = rad_v.get(2);
                    return verb_templateName;
                }
            }
            return null;
        }
    }
    /**
     * testing code
     *
     * @param args
     */
    public static void main(String[] args) {
        Conjugation conj = new Conjugation();
        String tn = conj.search("abjurer");
        String[][] p = conj.listOfPrefix(tn, "indicative", "future");
        for (int i = 0; i < p.length; i++) {
            if (p[i] != null) {
                for (int j = 0; j < p[i].length; j++) {
                    System.out.println(p[i][j]);
                }
            }
        }
        String rad = trim(tn, "abjurer");
        System.out.println(rad);
        String[][] f = conj.append(rad, p);
        for (int i = 0; i < f.length; i++) {
            if (f[i] != null) {
                for (int j = 0; j < f[i].length; j++) {
                    System.out.println(p[i][j]);
                }
            }
        }
    }
}