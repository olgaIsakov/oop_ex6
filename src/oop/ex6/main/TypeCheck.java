package oop.ex6.main;

import oop.ex6.variables.VariablesPattern;

import java.util.regex.Matcher;

/**
 * A class to check the different types
 */
public class TypeCheck {

    /*Magic strings for  types*/
    private final static String INT = "int";
    private final static String STRING = "String";
    private final static String DOUBLE = "double";
    private final static String BOOLEAN = "boolean";
    private final static String CHAR = "char";

    /**
     * this method checks if the given parameter is of the wanted type
     * @param wantedType the expected type
     * @param calledParam the given parameter
     * @return true if the type matches, false otherwise
     */

    public static boolean checkType(String wantedType, String calledParam) {
        Matcher intMach = VariablesPattern.INT_TYPE_PATTERN.matcher(calledParam);
        Matcher stringMach = VariablesPattern.STRING_TYPE_PATTERN.matcher(calledParam);
        Matcher doubleMach = VariablesPattern.DOUBLE_TYPE_PATTERN.matcher(calledParam);
        Matcher charMach = VariablesPattern.CHAR_TYPE_PATTERN.matcher(calledParam);
        Matcher boolMach = VariablesPattern.BOOL_TYPE_PATTERN.matcher(calledParam);
        switch (wantedType){
            case INT:
                return intMach.matches();
            case STRING:
                return stringMach.matches();
            case BOOLEAN:
                return (intMach.matches() || doubleMach.matches() || boolMach.matches());
            case CHAR:
                return charMach.matches();
            case DOUBLE:
                return (intMach.matches() || doubleMach.matches());
        }
        return false;
    }
}
