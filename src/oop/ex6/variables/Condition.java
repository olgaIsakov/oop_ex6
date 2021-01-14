package oop.ex6.variables;

import java.util.regex.Matcher;

import static oop.ex6.variables.VariableAnalyzer.*;

public class Condition {

    /*Magic strings and numbers*/
    private static final String AND ="&&";
    private static final String OR ="||";
    private final static String INT = "int";
    private final static String DOUBLE = "double";
    private final static String BOOLEAN = "boolean";
    private final static int INITIALIZE_COUNTER = 0;
    private final static String DEFAULT = "*";



    /**
     * This method analyzes a line of condition
     * @param line line of condition
     * @return true if the condition is valid
     */
    public static boolean analyzeCondition(String line){
        String[] conditions = splitOperator(line);
        for (String condition : conditions){
            if (!isCondition(condition.trim())){
                return false;
            }
        }
        return counterOperators(line, conditions.length);
    }

    /**
     * This method count the operators in the line for testing valid of the condition
     * @param line line of condition
     * @param numConditions number of conditions
     * @return true if line is valid
     */
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

    /**
     * This method replace in the line the operator and
     * @param  line
     * @return new line
     */
    public static String replacedAnd(String line){
       return line.replace(AND, DEFAULT);
    }
    /**
     * This method replace in the line the operator or
     * @param  line
     * @return new line
     */
    public static String replacedOr(String line){
        return line.replace(OR, DEFAULT);
    }
    /**
     * This method splits the line by the operators
     * @param  line
     * @return new line
     */
    public static String[] splitOperator(String line){
        return line.split(VariablesPattern.OPERATORS);
    }

    /**
     * This method returns true if the condition is valid
     * @param  condition
     * @return true or false
     */
    public static boolean isCondition(String condition){
        //  if the condition is a variable we already declared
        //  , we check if is type suitable for condition type and if it was initialized with a value
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
