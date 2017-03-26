/**
 * Created by bachp on 3/26/2017.
 */
public class Token {
    final String infinitive_form;
    final String template_name;
    public Token(String infinitive_form, String template_name){
        this.infinitive_form = infinitive_form;
        this.template_name =template_name;
    }
    public Token(Token copy){
        this.infinitive_form = copy.infinitive_form;
        this.template_name = copy.template_name;
    }
}
