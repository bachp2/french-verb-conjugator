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

    protected NodeList nConj;
    protected Trie verb_trie;
    protected List <String> radVector; //list of radicals and template names
    private Conjugation conj;
    private Deconjugation deconj;

    /**
     * empty constructor
     */
    public Program() {
        init();
    }

    /**
     * helper method to initialize Program constructor
     */
    private void init() {
        conj = new Conjugation();
        this.nConj = conj.nConj;
        deconj = new Deconjugation(conj.v_tn_Vector);
        this.verb_trie = deconj.verb_trie;
        this.radVector = deconj.radVector;
    }
    public static void main(String[] args){
        Conjugation conjugation = new Conjugation();
        long startTime = System.nanoTime();
        System.out.println(conjugation.search("placer"));
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration);
    }
}

class Conjugation {
    private final String path_to_verbs_fr = "./data/verbs-fr.xml";
    private final String path_to_conjugation_fr = "./data/conjugation-fr.xml";
    protected NodeList nConj;  //list of french template name in the form of 'rad:prefix'
    protected ArrayList<ArrayList<String>> v_tn_Vector;
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
            nVerbs =  dBuilder.parse(vFile).getElementsByTagName("v");
            int len = nVerbs.getLength();
            v_tn_Vector = new ArrayList <>();
            for (int i = 0; i < len; i++) {
                ArrayList<String> temp = new ArrayList <>();
                Element node =(Element) nVerbs.item(i);
                temp.add(node.getElementsByTagName("i").item(0)
                        .getTextContent());
                temp.add(node.getElementsByTagName("t").item(0)
                        .getTextContent());
                v_tn_Vector.add(temp);
            }
            Collections.sort(v_tn_Vector, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    return o1.get(0).compareTo(o2.get(0));
                }
            });

            this.nConj = dBuilder.parse(conFile).getElementsByTagName("template");
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
        }
    }

    /**
     * append radical with list of prefixes when conjugated
     *
     * @param radical::String
     * @param listP::String[][]
     * @return String[][]
     */
    public static String[][] append(StringBuilder radical, String[][] listP) {
        // already trim
        for (int i = 0; i < listP.length; i++) {
            if (listP[i] == null)
                break;
            for (int j = 0; j < listP[i].length; j++) {
                listP[i][j] = radical.append(listP[i][j]).toString();
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
            int len = nConj.getLength();
            for (int i = 0; i < len; i++) {
                temp = nConj.item(i);
                tmp = (Element) temp;
                other = temp.getAttributes().getNamedItem("name")
                        .getNodeValue();
                if (templateName.equals(other)) {
                    md = (Element) tmp.getElementsByTagName(mode).item(0);
                    ten = (Element) md.getElementsByTagName(tense).item(0);
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


    //get NodeList of verbs-fr and conjugation-fr

    /**
     * helper method to trim a verb into remaining radical (discard prefix)
     *
     * @param temp::String
     * @param v::String
     * @return
     */
    public StringBuilder trim(String temp, String v) {
        int index = 0;
        for (int i = 0; i < temp.length(); i++) {
            char c = temp.charAt(i);
            if (c == ':') {
                index = temp.length() - 1 - i;
            }
        }
        return new StringBuilder(v.substring(0, v.length() - index));
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
        for(int i = 0; i < v_tn_Vector.size(); i++){
            ArrayList<String> temp = v_tn_Vector.get(i);
            if(temp.contains(v)) return temp.get(1);
        }
        throw new NoSuchElementException();
    }
}

class Deconjugation {
    private final ArrayList<ArrayList<String>> v_tn_Vector;
    protected Trie verb_trie;
    protected final List <String> radVector; //list of radicals and template names

    /**
     * empty constructor
     */
    public Deconjugation(ArrayList<ArrayList<String>> v_tn_Vector) {
        radVector = new ArrayList <>();
        this.v_tn_Vector = v_tn_Vector;
        setListRad_and_TNs();
        verb_trie = new Trie();
        for (String rad : radVector) {
            verb_trie.insert(rad);
        }
    }

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
        for(int i = 0; i < v_tn_Vector.size(); i++){
            ArrayList<String> temp = v_tn_Vector.get(i);
            if(temp.contains(v)) return temp.get(1);
        }
        throw new NoSuchElementException();
    }

    private void setListRad_and_TNs() {
        StringBuilder rad = new StringBuilder();
        for(ArrayList<String> temp : v_tn_Vector){
            radVector.add( trim(temp.get(1), temp.get(0)) );
        }
    }

    /**
     * init an array list of radicals, verbs and template names
     *
     * @return
     */
    public List <String> getList() {
        return radVector;
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
        String radical = verb_trie.search(verb);
        for (int i = 0; i < radVector.size(); i++) {
            if (radVector.get(i).equals(radical)) {
                //appending verb in the infinitive form
                String template_name = v_tn_Vector.get(i).get(1);
                String[] list = {appendVerb(radical, template_name), template_name};
                listOfPossibleVerbs_and_TNs.add(list);
            }
        }
        return listOfPossibleVerbs_and_TNs;
    }

    public int getIndex(ArrayList <String[]> list) {
        int i = 0;
        for (String[] l : list) {
            if (search(l[0]).equals(l[1]))
                return i;
            i++;
        }
        return -1;
    }

    private String appendVerb(String rad, String tn) {
        return rad.concat(tn.substring(tn.indexOf(':') + 1));
    }
}