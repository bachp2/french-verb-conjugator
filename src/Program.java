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
import java.util.*;

/**
 * @author Bach Phan
 * @version 01/31/2017
 */
public class Program {
    protected NodeList nVerbs;
    protected NodeList nConj;
    private Conjugation conj;
    private Deconjugation deconj;
    static boolean flag = true;
    private Scanner s;
    Stack<String> verbsTobeConjugated;
    Stack<Conjugation.Mode> modes;
    ArrayList<Stack<String>> tenses;

    public Program() {
        init();
    }
    private void init(){
        s = new Scanner(System.in);
        conj = new Conjugation();
        this.nVerbs = conj.nVerbs;
        this.nConj = conj.nConj;
        deconj = new Deconjugation();
        verbsTobeConjugated = new Stack <>();
        modes = new Stack <>();
        tenses = new ArrayList <>();
    }
    public void start(Scanner s){
        String[] argBlocks = s.nextLine().split(" -");
        for(String argBlock : argBlocks){
            String[] args = argBlock.split(" ");
        }
    }

    public void argParser(String[] args){
        String arg = args[0];
        switch(arg){
            case "-help":
            case "h":
                //todo: write a help message here
                System.out.println("");
                break;
            case "-verb":
            case "v":
                for(int i = 1; i < args.length; i++){
                    verbsTobeConjugated.push(args[i]);
                }
                break;
            case "-mode":
            case "m":
                for(int i = 1; i < args.length; i++){
                    if(Mode.isMode(args[i])){
                        modes.push( Mode.getMode(args[i]) );
                    }
                    else throw new NoSuchElementException();
                }
                break;
            case "-tense":
            case "t":
                //todo: clone Stack
                Stack<Conjugation.Mode> temp = (Stack<Conjugation.Mode>) modes.clone();
                for(int i = 0; i < args.length; i++){
                    Mode.
                }

        }
    }
    public String trim(String temp, String v){
        return conj.trim(temp, v);
    }
    public String search(String v){
        return conj.search(v);
    }
    //todo arguments parser
    //
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
        public static boolean isMode(String mode){
            for (Mode m : Mode.values()) {
                if (mode.equals(m.toString()))
                    return true;
            }
            return false;
        }
        public boolean isTense(String tense){
            for(String t : tenses){
                if(t.equals(tense)){
                    return true;
                }
            }
            return false;
        }
        public String getTense(int index) {
            if (index < this.length() && index >= 0) {
                return tenses[index];
            } else throw new NoSuchElementException();
        }

        public int length() {
            return tenses.length;
        }
    }
    public static void main(String[] args){

    }
}

class Conjugation extends Program {
    private final String path_to_verbs_fr = "./data/verbs-fr.xml";
    private final String path_to_conjugation_fr = "./data/conjugation-fr.xml";
    protected NodeList nVerbs; //list of french infinitive verbs
    protected NodeList nConj;  //list of french template name in the form of 'rad:prefix'

    /**
     * empty constructor, when initiated will be use for the entire operation
     */
    public Conjugation() {
        File vFile, conFile;
        DocumentBuilder dBuilder;
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
     * append radical with list of prefixes when conjugated
     *
     * @param radical::String
     * @param listP::String[][]
     * @return String[][]
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
     * toString method
     *
     * @param f String[][]
     * @return String
     */
    public static String toString(String[][] f) {
        if (f == null) return "";
        String out = "";
        String[] pronouns = {"je", "tu", "il", "nous", "vous", "ils"};
        for (int i = 0; i < f.length; i++) {
            if (f[i] != null) {
                out = pronouns[i] + " ";
                for (int j = 0; j < f[i].length; j++) {
                    out += f[i][j];
                    if (j < f[i].length - 1
                            && f[i].length > 1)
                        out += "/";
                }
                out += "%n";
            } else
                out += "%n";
        }
        return out;
    }

    /**
     * get list of prefixes
     *
     * @param templateName String
     * @param mode         String
     * @param tense        String
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
    //todo : strip accent for input.


    //get NodeList of verbs-fr and conjugation-fr
    /**
     * helper method to trim a verb into remaining radical (discard prefix)
     *
     * @param temp::String
     * @param v::String
     * @return
     */
    public String trim(String temp, String v) {
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
     * search for template name with a given verb
     * <p>
     * verb has to be of infinitive form
     * </p>
     *
     * @param v :: verb:String
     * @return String[][]
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
        if (templateName.isEmpty())
            throw new NoSuchElementException();
        return templateName;
    }

}

class Deconjugation extends Program {
    protected Trie verb_trie;
    protected List <List <String>> rads_vs; //list of radicals and template names

    /**
     * empty constructor
     */
    public Deconjugation() {
        rads_vs = new ArrayList <>();
        setListRad_and_Verbs();
        verb_trie = new Trie();
        for (List <String> rad_v : rads_vs) {
            verb_trie.insert(rad_v.get(0));
        }
    }

    private void setListRad_and_Verbs() {
        for (int i = 0; i < nVerbs.getLength(); i++) {
            Node verb = nVerbs.item(i);
            List <String> list = new ArrayList <>();
            if (verb.getNodeType() == Node.ELEMENT_NODE) {
                Element eVerb = (Element) verb;
                String v = eVerb.getElementsByTagName("i").item(0)
                        .getTextContent();
                String tempN = eVerb.getElementsByTagName("t").item(0)
                        .getTextContent();
                String rad = trim(tempN, v);
                list.add(0, rad);
                list.add(1, tempN);
                rads_vs.add(i, list);
            }
        }
    }

    /**
     * init an array list of radicals, verbs and template names
     *
     * @return
     */
    public List <List <String>> getList() {
        return rads_vs;
    }

    /**
     * match conjugated verb with a probable radical then return verb and template name
     *
     * @param verb String
     * @return String[] -> 2 entries::verb & templateName
     */
    public ArrayList <String[]> match(String verb) {
        // verb is already conjugated
        //todo: implement search base on radical or search based on prefixe compare the performance of both cases
        ArrayList <String[]> listOfPossibleVerbs_and_TNs = new ArrayList <>();
        String temp = verb_trie.search(verb);
        for (List <String> rad_v : rads_vs) {
            if (rad_v.contains(temp)) {
                //appending verb in the infinitive form
                String[] list = {appendVerb(temp, rad_v.get(1)), rad_v.get(1)};
                listOfPossibleVerbs_and_TNs.add(list);
            }
        }
        return listOfPossibleVerbs_and_TNs;
    }

    public int getIndex(ArrayList <String[]> list){
        int i = 0;
        for(String[] l : list){
            if(search(l[0]).equals(l[1]))
                return i;
            i++;
        }
        return -1;
    }
    private String appendVerb(String rad, String tn) {
        return rad.concat(tn.substring(tn.indexOf(':') + 1));
    }
}