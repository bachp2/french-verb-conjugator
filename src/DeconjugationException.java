/**
 * Created by bachp on 3/24/2017.
 */
public class DeconjugationException extends Exception {
    public DeconjugationException(){
        super("can not deconjugate this verb");
    }
    public DeconjugationException(String msg){
        super(msg);
    }
}
