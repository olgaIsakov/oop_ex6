package oop.ex6.variables;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Condition {
    private static final String AND ="&&";
    private static final String OR ="||";
    private static final String OPEN_PAR ="(";
    private static final String CLOSE_PAR =")";

    public static final Pattern CONDITION_PATTERN = Pattern.compile("true|false|(?:-?(?:\\d+(?:\\.\\d*)?)\\s*|(?:\\.\\d+)\\s*])");


    public static boolean analyzeCondition(String line){

        String[] conditions = splitOperator(line);

        for (String condition : conditions){
            if (!isCondition(condition.trim())){
                return false;
            }

        }
        return counterOperators(line, conditions.length);

    }

    public static boolean counterOperators(String line, int numConditions){
        String line1 = replacedAnd(line);
        String line2 = replacedAnd(line1);
        int counter = 0 ;
        for(int i = 0; i<line2.length(); i++) {
            if (line2.charAt(i)== '*') {
                counter++;
            }


        } return  (counter+1 == numConditions);
    }

    public static String replacedAnd(String line){
       return line.replace(AND, "*");
    }
    public  String replacedOr(String line){
        return line.replace(OR, "*");
    }


    public static String[] splitOperator(String line){
        if (line.contains(AND) || line.contains(OR)){
            return line.split(AND+ OR);
        }
        return new String[]{line};
    }
    public static boolean isCondition(String condition){
        Matcher matcher = CONDITION_PATTERN.matcher(condition);
        return matcher.matches();
    }



}
