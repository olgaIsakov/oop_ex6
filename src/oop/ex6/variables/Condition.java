package oop.ex6.variables;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static oop.ex6.variables.Analyze.*;

public class Condition {
    private static final String AND ="&&";
    private static final String OR ="||";
    private static final String OPEN_PAR ="(";
    private static final String CLOSE_PAR =")";
    final static String INT = "int";
    final static String DOUBLE = "double";
    final static String BOOLEAN = "boolean";
    final static int INITIALIZE_COUNTER = 0;
    /*public static final Pattern CONDITION_PATTERN = Pattern.compile("true|false|(?:-?(?:\\d+(?:\\.\\d*)?)\\s*|(?:\\.\\d+)\\s*])");*/


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
        line = replacedAnd(line);
        line = replacedAnd(line);
        line = replacedOr(line);
        line = replacedOr(line);
        int counter = INITIALIZE_COUNTER ;
        for(int i = 0; i<line.length(); i++) {
            if (line.charAt(i)== '*') {
                counter++;
            }


        } return  (counter+1 == numConditions);
    }

    public static String replacedAnd(String line){
       return line.replace(AND, "*");
    }
    public static String replacedOr(String line){
        return line.replace(OR, "*");
    }


    public static String[] splitOperator(String line){
        return line.split("[&&||]+");
    }
    public static boolean isCondition(String condition){
        if (listVariables.containsKey(condition) ){
             if (listVariables.get(condition).equals(INT)
                    || listVariables.get(condition).equals(DOUBLE) || listVariables.get(condition).equals(BOOLEAN)) {
                 return listInit.contains(condition);
             }return false;
        }else {
            Matcher matcher = VariablesPattern.CONDITION_PATTERN.matcher(condition);
            return matcher.matches();
        }
    }



}
