import com.google.common.base.Joiner;
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
import java.util.concurrent.TimeUnit;

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
        String tn = p.conjugate.searchTemplateName("placer").template_name;
        PrefixesGroup temp = p.conjugate.searchFrefixesGroup(tn);
        ArrayList <String> tmp = temp.getPrefixes(Mode.indicative, Mode.Tense.past);
        StringBuilder sb = new StringBuilder();
        for (String a : tmp) {
            sb.append(a + "\n");
        }
        System.out.print(sb.toString());
        ArrayList<String> mylist = PrefixesGroup.append(Verb.trimPrefix(tn, "placer"), tmp);
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
        conjugate = new Conjugation();
        deconjugate = new Deconjugation(conjugate.v_tn_rad_Vector);
    }

}

class Conjugation {
    private static final String path_to_verbs_fr = "./data/verbs-fr.xml";
    private static final String path_to_conjugation_fr = "./data/conjugation-fr.xml";
    private ArrayList <PrefixesGroup> frefixes_Vector;
    protected ArrayList <Verb> v_tn_rad_Vector;

    /**
     * empty constructor, when initiated will be use for the entire operation
     */
    public Conjugation() {
        File vFile, conFile;
        NodeList nVerbs, nConj;
        DocumentBuilder dBuilder;
        try {
            //read verbs-fr.xml file
            vFile = new File(path_to_verbs_fr);
            //read conjugate-fr.xml file
            conFile = new File(path_to_conjugation_fr);
            dBuilder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();
            nVerbs = dBuilder.parse(vFile).getElementsByTagName("v");
            nConj = dBuilder.parse(conFile).getElementsByTagName("template");
            initVerbs(nVerbs);
            initConjugation(nConj);

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
    private void initVerbs(NodeList nVerbs){
        int length1 = nVerbs.getLength();
        v_tn_rad_Vector = new ArrayList <>();
        for (int i = 0; i < length1; i++) {
            Element node = (Element) nVerbs.item(i);
            String verb = node.getElementsByTagName("i").item(0)
                    .getTextContent();
            String template_name = node.getElementsByTagName("t").item(0)
                    .getTextContent();
            Verb temp = new Verb(verb, template_name);
            v_tn_rad_Vector.add(temp);
        }
        Collections.sort(v_tn_rad_Vector, (o1, o2) -> o1.infinitive_form.compareTo(o2.infinitive_form));
    }

    private void initConjugation(NodeList nConj){
        frefixes_Vector = new ArrayList <>(1000);
        int length = nConj.getLength();
        for (int i = 0; i < length; i++) {
            Node temp = nConj.item(i);
            Element tmp = (Element) temp;
            String t_n = temp.getAttributes().getNamedItem("name")
                    .getNodeValue();
            PrefixesGroup frefixesGroup = new PrefixesGroup(t_n);
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
                            else throw new NullPointerException();
                        }
                    }
                    frefixesGroup.append(mode, tense, p);
                }
                frefixes_Vector.add(frefixesGroup);
            }
        }
        Collections.sort(frefixes_Vector, (o1, o2) -> o1.template_name.compareTo(o2.template_name));
    }
    private String[] NodeList2Array(NodeList A){
        int len = A.getLength();
        String[] temp = new String[len];
        for (int i = 0; i < len; i++) {
            temp[i] = A.item(i).getTextContent();
        }
        return temp;
    }
    //todo : strip accent for input.

    /**
     * searchTemplateName for template name with a given verb
     * <p>
     * verb has to be of infinitive form
     * </p>
     *
     * @param v :: verb:String
     * @return String[][]
     */
    public Verb searchTemplateName(String v) {
        int index = Collections.binarySearch(v_tn_rad_Vector, new Verb(v));
        if (index >= 0)
            return v_tn_rad_Vector.get(index);//privacy leak
            //todo implement clone
        else throw new ConjugationException("No verbs matchRadical the input%nLooking to deconjugate...");
    }

    public PrefixesGroup searchFrefixesGroup(String template_name) {
        int index = Collections.binarySearch(frefixes_Vector, new PrefixesGroup(template_name));
        if (index >= 0)
            return frefixes_Vector.get(index);//privacy leak
        else throw new ConjugationException("Can't find matching group with that template name ");
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

    public String searchRadical(String verb) {
        //searchTemplateName for radical that matchRadical the conjugated verb
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
        for (Verb v : v_tn_rad_Vector) {
            if (v.radical.equals(radical)) {
                //this will be the indexes of possible verbs that can be refer back from v_tn_rad_Vector
                listOfPossibleInfVerbs.add(v);
            }
        }
        return listOfPossibleInfVerbs;
    }
    public static boolean isOneMatch(ArrayList <Verb> list){
        return list.size() == 1;
    }
}