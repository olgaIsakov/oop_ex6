package oop.ex6.methods;

import oop.ex6.main.TypeCheck;

import java.util.ArrayList;
import java.util.List;

import static oop.ex6.variables.VariableAnalyzer.listVariables;

/**
 * A class to check the method parameters
 */
public class ParamCheck {

    /*Constants*/
    private static final String START_PARAMETERS = "(";
    private static final String END_PARAMETERS = ")";
    private static final String NULL_MARK = "null";
    private static final String COMMA = ",";
    private static final String EMPTY_SPACE = " ";
    private static final int FIRST = 0;

    /*List of valid types*/
    final static List<String> typeOptions = List.of("int", "String", "double", "boolean", "char");

    /**
     * This method checks parameters called by another method
     *
     * @param methodLine the line with the call for a method
     * @param parameters method parameters
     * @return is the method call legal
     */
    public static boolean checkCalledParams(String methodLine, List<String> parameters) {
        int paramsStart = methodLine.indexOf(START_PARAMETERS);
        int paramsEnd = methodLine.indexOf(END_PARAMETERS);
        String params = methodLine.substring(paramsStart + 1, paramsEnd);
        if (params.isEmpty())
            return parameters.get(FIRST).equals(NULL_MARK);
        List<String> types = new ArrayList<>();
        for (String type : parameters) {
            type = type.trim();
            type = type.substring(FIRST, type.indexOf(EMPTY_SPACE));
            if (typeOptions.contains(type))
                types.add(type);
        }
        if (!params.contains(COMMA)) {
            if (listVariables.containsKey(params)) {
                return listVariables.get(params).equals(types.get(FIRST));
            } else return TypeCheck.checkType(types.get(FIRST), params);
        } else {
            String[] allParams = params.split(COMMA);
            if (allParams.length != types.size())
                return false;
            else {
                for (int i = 0; i < types.size(); i++) {
                    if (listVariables.containsKey(allParams[i])) {
                        if (!listVariables.get(allParams[i]).equals(types.get(i))) return false;
                    } else if (!TypeCheck.checkType(types.get(i), allParams[i]))
                        return false;
                }
                return true;
            }
        }

    }

}
