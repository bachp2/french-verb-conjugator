package test;

public class testInputValidation {
    public static void main(String[] args){
        final String regex = "[a-zA-Z àâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ]+";
        String[] valid = {"etre", "éêëî", "éAfew"};
        String[] invalid = {"1234", "/><)((((", "123eter", "/>>??>ahsda"};
        for(String s : valid){
            System.out.println(s.matches(regex));
        }
        for(String s : invalid){
            System.out.println(s.matches(regex));
        }
    }
}
