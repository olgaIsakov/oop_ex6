package oop.ex6.methods;

import oop.ex6.main.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class paramCheck {
    private static final String START_PARAMETERS = "(";
    private static final String END_PARAMETERS = ")";
    private static final String NULL_MARK = "null";
    private static final String COMMA = ",";
    private static final int EMPTY_PARAMS = 0;
    private static final int FIRST_PARAM = 0;
    private final static String ONE_PARAMETER = "\\s*final*\\s+[a-zA-Z]+\\s*";
    final static Pattern ONE_PARAMETER_PATTERN = Pattern.compile(ONE_PARAMETER);
    final static List<String> typeOptions = List.of("int", "String", "double", "boolean", "char");

    /**
     * This method checks parameters called by another method
     * @param methodLine the line with the call for a method
     * @param parameters method parameters
     * @return is the method call legal
     */
    public  static boolean  checkCalledParams(String methodLine, List<String> parameters) {
        int paramsStart = methodLine.indexOf(START_PARAMETERS);
        int paramsEnd = methodLine.indexOf(END_PARAMETERS);
        String params = methodLine.substring(paramsStart+1, paramsEnd) ;
        if (params.length() == EMPTY_PARAMS)
            return parameters.get(FIRST_PARAM).equals(NULL_MARK);
        List<String> types = new ArrayList<>();
        for (String type: parameters){
            if (typeOptions.contains(type))
                types.add(type);
        }
        Matcher oneParam = ONE_PARAMETER_PATTERN.matcher(params);
        if (oneParam.matches())
            return Tools.checkType(types.get(FIRST_PARAM), params);
        else{
            String[] allParams = params.split(COMMA);
            if (allParams.length != types.size())
                return false;
            else{
                for (int i=0; i < types.size() ; i++){
                    if (!Tools.checkType(types.get(i) , allParams[i]))
                        return false;
                }
                return true;
            }
        }

    }

}