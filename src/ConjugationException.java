/**
 * Created by bachp on 3/24/2017.
 */
public class ConjugationException extends Exception {
    public ConjugationException(){
        super("can't not conjugate this verb");
    }
    public ConjugationException(String msg){
        super(msg);
    }
}
