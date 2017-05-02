import DataStructure.Mode;
import DataStructure.Tense;

/**
 * Created by bachp on 4/24/2017.
 */
public class MatchTuple {
    private Verb verb;
    private SuffixesGroup suffixesGroup;
    private Mode mode;
    private Tense tense;

    private final MatchTuple instance = new MatchTuple();

    private MatchTuple(){}
    public void verb(Verb verb){this.verb = verb;}
    public void suffixesGroup(SuffixesGroup suffixesGroup){this.suffixesGroup = suffixesGroup;}
    public void mode(Mode mode){this.mode = mode;}
    public void tense(Tense tense){this.tense = tense;}
    public MatchTuple build(){
        return this.instance;
    }

    /**
     *
     * @return
     */
    public Verb getVerb(){
        try{
            return (Verb) verb.clone();
        }
        catch (CloneNotSupportedException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return
     */
    public SuffixesGroup getSuffixesGroup(){
        try{
            return (SuffixesGroup) suffixesGroup.clone();
        }
        catch (CloneNotSupportedException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return
     */
    public Mode getMode(){
        return mode;
    }

    /**
     *
     * @return
     */
    public Tense getTense(){
        return tense;
    }
}
