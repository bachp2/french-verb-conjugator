import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bach Phan
 * @version 01/31/2017
 */
public class Conjugation {
    //todo : revise the code
    public String[][] _listConjugation;
    private NodeList nVerbs;
    private NodeList nConj;
    private List <List <String>> rads_vs = new ArrayList <>();
    private List <List <String>> conjugatedList = new ArrayList <>();

    public Conjugation() {
        try {//read verbs-fr.xml file
            File vFile = new File("./data/verbs-fr.xml");
            //read conjugation-fr.xml file
            File conFile = new File("./data/conjugation-fr.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            //parse verbs-fr file
            Document docVerbs = dBuilder.parse(vFile);
            this.nVerbs = docVerbs.getElementsByTagName("v");
            Document docConj = dBuilder.parse(conFile);
            this.nConj = docConj.getElementsByTagName("template");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public String[][] listOfPrefix(String templateName,
                                   int modeIndex,
                                   int tenseIndex) {
        String[][] p = new String[6][];
        for (int i = 0; i < nConj.getLength(); i++) {
            Node temp = nConj.item(i);
            Element tmp = (Element) temp;
            String other = temp.getAttributes().getNamedItem("name")
                    .getNodeValue();
            if (templateName.equals(other)) {
                Element md = (Element) tmp.getChildNodes().item(modeIndex);
                Element ten = (Element) md.getChildNodes().item(tenseIndex);
                NodeList listP = ten.getElementsByTagName("p");
                for (int j = 0; j < listP.getLength(); j++) {
                    boolean isType = (listP.item(j).getNodeType() == Node.ELEMENT_NODE);
                    if (isType) {
                        Element person = (Element) listP.item(j);
                        if (person.hasChildNodes()) {
                            NodeList listI = person
                                    .getElementsByTagName("i");
                            p[j] = new String[listI.getLength()];
                            for (int k = 0; k < listI.getLength(); k++) {
                                p[j][k] = listI.item(k).getTextContent();
                            }
                        }
                    }
                }
                break;
            }
        }
        return p;
    }

    public void display() {
        String[] pronouns = {"je", "tu", "il", "nous", "vous", "ils"};
        for (int i = 0; i < _listConjugation.length; i++) {
            if (_listConjugation[i] != null) {
                System.out.print(pronouns[i] + " ");
                for (int j = 0; j < _listConjugation[i].length; j++) {
                    System.out.print(_listConjugation[i][j]);
                    if (j < _listConjugation[i].length - 1
                            && _listConjugation[i].length > 1)
                        System.out.print("/");
                }
                System.out.println();
            } else
                System.out.println();
        }
    }

    //get NodeList of verbs-fr and conjugation-fr

    //list of verbs and its radical, use for deconjugation...
    //todo : create inheritance class of Conjugation to search for conjugasted verb
    //todo : strip accent for input.
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
}